<template>
  <div class="card">
    <template v-if="isStudent">
      <el-button type="primary" @click="load">刷新我的成绩</el-button>
    </template>
    <template v-else>
      <div class="toolbar">
        <el-radio-group v-model="scoreType" @change="handleTypeChange" style="margin-right: 12px;">
          <el-radio-button label="OFFERING">选课成绩</el-radio-button>
          <el-radio-button label="ENROLLMENT">正课成绩</el-radio-button>
        </el-radio-group>
        <el-select v-model="courseId" placeholder="选择课程" style="width: 360px; margin-right: 8px;" @change="load">
          <el-option v-if="auth.role === 'ADMIN'" label="全部课程" :value="0" />
          <el-option v-for="c in currentCourses" :key="c.id" :label="`${c.courseNo} | ${c.courseName} | ${c.semester || ''} | ${c.teacherName || ''}`" :value="Number(c.id)" />
        </el-select>
        <el-button type="primary" @click="load">查询课程成绩</el-button>
        <el-button type="success" @click="openAdd" style="margin-left: 8px;">录入成绩</el-button>
      </div>
    </template>

    <el-table :data="list" size="small">
      <el-table-column label="类型" width="100">
        <template #default="{ row }">
          <el-tag :type="row.type === 'OFFERING' ? 'success' : 'primary'" size="small">
            {{ row.type === 'OFFERING' ? '选课成绩' : '正课成绩' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="学号" width="110">
        <template #default="{ row }">{{ row.studentNo || '' }}</template>
      </el-table-column>
      <el-table-column label="姓名" width="100">
        <template #default="{ row }">{{ row.studentName || '' }}</template>
      </el-table-column>
      <el-table-column label="课程号" width="100">
        <template #default="{ row }">{{ row.courseNo || '' }}</template>
      </el-table-column>
      <el-table-column label="课程名称" width="160">
        <template #default="{ row }">{{ row.courseName || '' }}</template>
      </el-table-column>
      <el-table-column label="学期" width="90">
        <template #default="{ row }">{{ row.semester || '' }}</template>
      </el-table-column>
      <el-table-column label="教师" width="100">
        <template #default="{ row }">{{ row.teacherName || '' }}</template>
      </el-table-column>
      <el-table-column prop="score" label="成绩" width="80" />
      <el-table-column label="更新时间" width="160">
        <template #default="{ row }">{{ formatTime(row.updatedAt) }}</template>
      </el-table-column>
      <el-table-column v-if="!isStudent" label="操作" width="100">
        <template #default="{ row }">
          <el-button link size="small" type="primary" @click="onEdit(row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-if="!isStudent"
      v-model:current-page="page"
      v-model:page-size="pageSize"
      :page-sizes="[20, 50, 100, 200]"
      layout="total, sizes, prev, pager, next"
      :total="total"
      @size-change="load"
      @current-change="load"
      class="pager"
    />

    <el-dialog v-model="visible" :title="`录入${scoreType === 'OFFERING' ? '选课' : '正课'}成绩`" width="520px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="成绩类型">
          <el-radio-group v-model="form.type" @change="handleFormTypeChange">
            <el-radio label="OFFERING">选课成绩</el-radio>
            <el-radio label="ENROLLMENT">正课成绩</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="课程">
          <el-select v-model="form.courseId" placeholder="请选择课程" style="width: 100%;" filterable @change="onCourseChange">
            <el-option v-for="c in (form.type === 'OFFERING' ? offeringCourses : enrollmentCourses)" :key="c.id" :label="`${c.courseNo} | ${c.courseName} | ${c.semester || ''}`" :value="Number(c.id)" />
          </el-select>
        </el-form-item>
        <el-form-item label="学生">
          <el-select v-model="form.studentId" placeholder="请选择学生" style="width: 100%;" filterable :disabled="!form.courseId">
            <el-option v-for="s in filteredStudents" :key="s.id" :label="`${s.studentNo} | ${s.name}`" :value="Number(s.id)" />
          </el-select>
        </el-form-item>
        <el-form-item label="成绩">
          <el-input-number v-model="form.score" :min="0" :max="100" :precision="2" style="width: 100%;" placeholder="请输入成0-100的数值" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { fetchMyScores, fetchScoresByCourse, recordScore, fetchCourseOfferings, fetchCourses, fetchStudents } from '@/api';
import { useAuthStore } from '@/stores/auth';
import http from '@/api/http';

const auth = useAuthStore();
const isStudent = computed(() => auth.role === 'STUDENT');
const list = ref<any[]>([]);
const allScores = ref<any[]>([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const courseId = ref<number | null>(null);
const scoreType = ref<string>('OFFERING'); // 当前查看的成绩类型
const offeringCourses = ref<any[]>([]); // 选课课程列表
const enrollmentCourses = ref<any[]>([]); // 正课列表
const currentCourses = computed(() => scoreType.value === 'OFFERING' ? offeringCourses.value : enrollmentCourses.value);
const students = ref<any[]>([]);
const filteredStudents = ref<any[]>([]); // 根据课程筛选的学生列表
const visible = ref(false);
const form = reactive<{ studentId: number | null; courseId: number | null; score: number | null; type: string }>({
  studentId: null,
  courseId: null,
  score: null,
  type: 'OFFERING'
});

const handleTypeChange = async () => {
  // 切换成绩类型时，重置课程选择
  if (auth.role === 'ADMIN') {
    courseId.value = 0;
  } else if (currentCourses.value && currentCourses.value.length > 0) {
    courseId.value = currentCourses.value[0].id;
  } else {
    courseId.value = null;
  }
  await load();
};

const handleFormTypeChange = () => {
  form.courseId = null;
  form.studentId = null;
  filteredStudents.value = [];
};

const onCourseChange = async () => {
  form.studentId = null;
  if (form.courseId) {
    try {
      const res = await http.get(`/scores/students-by-course/${form.courseId}?type=${form.type}`);
      filteredStudents.value = res || [];
    } catch (e: any) {
      ElMessage.error('加载学生列表失败');
      filteredStudents.value = [];
    }
  } else {
    filteredStudents.value = [];
  }
};

const load = async () => {
  if (isStudent.value) {
    list.value = await fetchMyScores();
  } else if (courseId.value !== null) {
    try {
      const res = await http.get(`/scores/by-course/${courseId.value}?type=${scoreType.value}`);
      allScores.value = res || [];
      total.value = allScores.value.length;
      const start = (page.value - 1) * pageSize.value;
      const end = start + pageSize.value;
      list.value = allScores.value.slice(start, end);
    } catch (e: any) {
      ElMessage.error(e?.message || '加载成绩失败');
      list.value = [];
      total.value = 0;
    }
  } else {
    list.value = [];
    total.value = 0;
  }
};

const openAdd = async () => {
  form.studentId = null;
  form.type = scoreType.value;
  form.courseId = courseId.value && courseId.value !== 0 ? courseId.value : (currentCourses.value.length > 0 ? currentCourses.value[0].id : null);
  form.score = null;
  filteredStudents.value = [];
  
  // 如果已选择课程，立即加载该课程的学生列表
  if (form.courseId) {
    await onCourseChange();
  }
  
  visible.value = true;
};

const onSubmit = async () => {
  if (!form.studentId || !form.courseId || form.score === null) {
    ElMessage.warning('请填写完整信息');
    return;
  }
  try {
    const endpoint = form.type === 'OFFERING' ? '/scores/offering' : '/scores/enrollment';
    await http.post(endpoint, { studentId: form.studentId, courseId: form.courseId, score: form.score });
    ElMessage.success('成绩已录入');
    visible.value = false;
    load();
  } catch (e: any) {
    ElMessage.error(e?.message || '录入失败');
  }
};

const formatTime = (time: string) => {
  if (!time) return '';
  return time.replace('T', ' ').substring(0, 16);
};

const onEdit = async (row: any) => {
  const { value: newScore } = await ElMessageBox.prompt('请输入新成绩', '修改成绩', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^\d+(\.\d{1,2})?$/,
    inputErrorMessage: '请输入有效的成绩',
    inputValue: row.score
  }).catch(() => ({ value: null }));
  
  if (newScore !== null) {
    try {
      const endpoint = row.type === 'OFFERING' ? '/scores/offering' : '/scores/enrollment';
      await http.post(endpoint, { studentId: row.studentId, courseId: row.courseId, score: parseFloat(newScore) });
      ElMessage.success('成绩已更新');
      load();
    } catch (e: any) {
      ElMessage.error(e?.message || '修改失败');
    }
  }
};

const loadOfferingCourses = async () => {
  try {
    if (auth.role === 'TEACHER') {
      // 教师：只加载自己的课程
      const res = await http.get('/scores/teacher-courses?type=OFFERING');
      offeringCourses.value = res.courses || [];
    } else {
      // 管理员：加载所有课程
      offeringCourses.value = await fetchCourseOfferings(1, 1000);
    }
  } catch (e: any) {
    console.error('加载选课课程失败', e);
  }
};

const loadEnrollmentCourses = async () => {
  try {
    if (auth.role === 'TEACHER') {
      // 教师：只加载自己的正课
      const res = await http.get('/scores/teacher-courses?type=ENROLLMENT');
      enrollmentCourses.value = res.courses || [];
    } else {
      // 管理员：加载所有正课
      enrollmentCourses.value = await fetchCourses(1, 1000);
    }
  } catch (e: any) {
    console.error('加载正课失败', e);
  }
};

const loadStudents = async () => {
  if (!isStudent.value) {
    try {
      const res = await fetchStudents(1, 1000);
      students.value = res || [];
    } catch (e: any) {
      console.error('加载学生失败', e);
    }
  }
};

const initCourseSelection = () => {
  if (!isStudent.value && !courseId.value) {
    if (auth.role === 'ADMIN') {
      courseId.value = 0;
    } else if (currentCourses.value && currentCourses.value.length > 0) {
      courseId.value = currentCourses.value[0].id;
    }
  }
};

onMounted(async () => {
  await Promise.all([loadOfferingCourses(), loadEnrollmentCourses(), loadStudents()]);
  initCourseSelection();
  await load();
});
</script>

<style scoped>
.card { background: #fff; padding: 16px; border-radius: 12px; }
.toolbar { margin-bottom: 12px; }
.pager { margin-top: 12px; justify-content: center; }
</style>
