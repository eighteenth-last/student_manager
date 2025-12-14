<template>
  <div class="card">
    <div class="toolbar">
      <el-radio-group v-model="courseType" @change="load" style="margin-right: 16px;">
        <el-radio-button value="course">正课</el-radio-button>
        <el-radio-button value="offering">{{ isStudent ? '我的选课' : '选课' }}</el-radio-button>
      </el-radio-group>
      <el-button type="primary" @click="openEdit()" v-if="canManage">新增课程</el-button>
    </div>

    <!-- 正课表格 -->
    <el-table v-if="courseType === 'course'" :data="list" size="small">
      <el-table-column prop="courseNo" label="课程号" width="120" />
      <el-table-column prop="courseName" label="课程名称" width="200" />
      <el-table-column prop="credit" label="学分" width="100" />
      <el-table-column label="教师" width="120">
        <template #default="{ row }">{{ teacherMap[row.teacherId] || row.teacherId }}</template>
      </el-table-column>
      <el-table-column prop="classTime" label="上课时间" width="180" />
      <el-table-column v-if="canManage" label="操作" width="160">
        <template #default="{ row }">
          <el-button link size="small" @click="openEdit(row)">编辑</el-button>
          <el-button link size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 选课表格 -->
    <el-table v-else :data="list" size="small">
      <el-table-column prop="courseNo" label="课程号" width="120" />
      <el-table-column prop="courseName" label="课程名称" width="180" />
      <el-table-column prop="credit" label="学分" width="80" />
      <el-table-column prop="semester" label="学期" width="100" />
      <el-table-column prop="teacherName" label="教师" width="100" />
      <el-table-column prop="classTime" label="上课时间" width="150" />
      <el-table-column prop="classroom" label="教室" width="120" />
      <el-table-column label="选课人数" width="120">
        <template #default="{ row }">{{ row.currentStudents }} / {{ row.maxStudents }}</template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.status === 'OPEN'" type="success" size="small">开放选课</el-tag>
          <el-tag v-else-if="row.status === 'CLOSED'" type="warning" size="small">关闭选课</el-tag>
          <el-tag v-else type="info" size="small">已结课</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="canManage" label="操作" width="160">
        <template #default="{ row }">
          <el-button link size="small" @click="openEdit(row)">编辑</el-button>
          <el-button link size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="page"
      v-model:page-size="pageSize"
      :page-sizes="[20, 50, 100, 200]"
      layout="total, sizes, prev, pager, next"
      :total="total"
      @size-change="load"
      @current-change="load"
      class="pager"
    />

    <!-- 正课编辑对话框 -->
    <el-dialog v-model="visible" :title="editing ? '编辑课程' : '新增课程'" width="520px" v-if="courseType === 'course'">
      <el-form :model="form" label-width="90px">
        <el-form-item label="课程号">
          <el-input v-model="form.courseNo" placeholder="如：CS101" :disabled="!!editing" />
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="form.courseName" placeholder="如：数据结构" />
        </el-form-item>
        <el-form-item label="学分">
          <el-input-number v-model="form.credit" :min="0" :max="10" :precision="1" :step="0.5" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="授课教师">
          <el-select v-model="form.teacherId" placeholder="请选择教师" style="width: 100%;">
            <el-option v-for="t in teachers" :key="t.id" :label="`${t.teacherNo} | ${t.name}`" :value="Number(t.id)" />
          </el-select>
        </el-form-item>
        <el-form-item label="上课时间">
          <el-input v-model="form.classTime" placeholder="如：周一 08:00-10:00" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 选课编辑对话框 -->
    <el-dialog v-model="visible" :title="editing ? '编辑选课' : '新增选课'" width="520px" v-else>
      <el-form :model="form" label-width="90px">
        <el-form-item label="课程号">
          <el-input v-model="form.courseNo" placeholder="如：CS101" />
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="form.courseName" placeholder="如：数据结构" />
        </el-form-item>
        <el-form-item label="学分">
          <el-input-number v-model="form.credit" :min="0" :max="10" :precision="1" :step="0.5" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="学期">
          <el-input v-model="form.semester" placeholder="如：2025-1" />
        </el-form-item>
        <el-form-item label="授课教师">
          <el-select v-model="form.teacherId" placeholder="请选择教师" style="width: 100%;">
            <el-option v-for="t in teachers" :key="t.id" :label="`${t.teacherNo} | ${t.name}`" :value="Number(t.id)" />
          </el-select>
        </el-form-item>
        <el-form-item label="上课时间">
          <el-input v-model="form.classTime" placeholder="如：周一 08:00-10:00" />
        </el-form-item>
        <el-form-item label="教室">
          <el-input v-model="form.classroom" placeholder="如：教学楼A101" />
        </el-form-item>
        <el-form-item label="最大人数">
          <el-input-number v-model="form.maxStudents" :min="1" :max="500" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%;">
            <el-option label="开放选课" value="OPEN" />
            <el-option label="关闭选课" value="CLOSED" />
            <el-option label="已结课" value="FINISHED" />
          </el-select>
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
import { fetchCourses, createCourse, updateCourse, deleteCourse, fetchCourseOfferings, createCourseOffering, updateCourseOffering, deleteCourseOffering, fetchTeachers, fetchMyEnrollments } from '@/api';
import { useAuthStore } from '@/stores/auth';

const auth = useAuthStore();
const canManage = computed(() => auth.role === 'ADMIN');
const isStudent = computed(() => auth.role === 'STUDENT');

const courseType = ref<'course' | 'offering'>('offering'); // 默认显示选课
const list = ref<any[]>([]);
const allCourses = ref<any[]>([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const visible = ref(false);
const editing = ref<any | null>(null);
const teachers = ref<any[]>([]);
const teacherMap = ref<Record<number, string>>({});

// 正课表单
const courseForm = { courseNo: '', courseName: '', credit: 0, teacherId: null as number | null, classTime: '' };
// 选课表单
const offeringForm = { courseNo: '', courseName: '', credit: 0, semester: '2025-1', teacherId: null as number | null, classTime: '', classroom: '', maxStudents: 50, currentStudents: 0, status: 'OPEN' };

const form = reactive<any>({ ...offeringForm });

const load = async () => {
  try {
    if (isStudent.value) {
      // 学生根据类型查看不同课程
      if (courseType.value === 'course') {
        // 正课：所有学生都能看到
        allCourses.value = await fetchCourses(1, 1000);
      } else {
        // 我的选课：只显示自己选的选课
        const enrollments = await fetchMyEnrollments();
        const courseOfferingIds = enrollments.map((e: any) => e.courseOfferingId);
        const allOfferings = await fetchCourseOfferings(1, 1000);
        allCourses.value = allOfferings.filter((c: any) => courseOfferingIds.includes(c.id));
      }
    } else {
      // 管理员和教师可以看到所有课程
      if (courseType.value === 'course') {
        allCourses.value = await fetchCourses(1, 1000);
      } else {
        allCourses.value = await fetchCourseOfferings(1, 1000);
      }
    }
    total.value = allCourses.value.length;
    const start = (page.value - 1) * pageSize.value;
    const end = start + pageSize.value;
    list.value = allCourses.value.slice(start, end);
  } catch (e: any) {
    ElMessage.error(e?.message || '加载失败');
  }
};

const loadTeachers = async () => {
  try {
    teachers.value = await fetchTeachers(1, 100);
    teachers.value.forEach((t: any) => {
      teacherMap.value[t.id] = t.name;
    });
  } catch (e: any) {
    console.error('加载教师失败', e);
  }
};

const openEdit = (row?: any) => {
  if (!canManage.value) return;
  editing.value = row || null;
  if (row) {
    Object.assign(form, row);
  } else {
    if (courseType.value === 'course') {
      Object.assign(form, { ...courseForm });
    } else {
      Object.assign(form, { ...offeringForm });
    }
  }
  visible.value = true;
};

const onSubmit = async () => {
  try {
    if (courseType.value === 'course') {
      if (editing.value) {
        await updateCourse(editing.value.id, form);
        ElMessage.success('已更新');
      } else {
        await createCourse(form);
        ElMessage.success('已创建');
      }
    } else {
      if (editing.value) {
        await updateCourseOffering(editing.value.id, form);
        ElMessage.success('已更新');
      } else {
        await createCourseOffering(form);
        ElMessage.success('已创建');
      }
    }
    visible.value = false;
    load();
  } catch (e: any) {
    ElMessage.error(e?.message || '操作失败');
  }
};

const onDelete = async (row: any) => {
  await ElMessageBox.confirm(`确认删除该${courseType.value === 'course' ? '课程' : '选课'}？`, '提示');
  try {
    if (courseType.value === 'course') {
      await deleteCourse(row.id);
    } else {
      await deleteCourseOffering(row.id);
    }
    ElMessage.success('已删除');
    load();
  } catch (e: any) {
    ElMessage.error(e?.message || '删除失败');
  }
};

onMounted(async () => {
  await loadTeachers();
  await load();
});
</script>

<style scoped>
.card { background: #fff; padding: 16px; border-radius: 12px; }
.toolbar { margin-bottom: 12px; display: flex; align-items: center; }
.pager { margin-top: 12px; justify-content: center; }
</style>
