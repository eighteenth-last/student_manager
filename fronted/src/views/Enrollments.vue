<template>
  <div class="card">
    <template v-if="isStudent">
      <h3 style="margin-bottom: 16px;">可选课程</h3>
      <el-table :data="availableCourses" size="small" style="margin-bottom: 24px;">
        <el-table-column prop="courseNo" label="课程号" width="120" />
        <el-table-column prop="courseName" label="课程名称" width="200" />
        <el-table-column prop="credits" label="学分" width="100" />
        <el-table-column prop="teacherName" label="任课教师" width="120" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button link size="small" type="primary" @click="onEnroll(row.id)" :disabled="isEnrolled(row.id)">{{ isEnrolled(row.id) ? '已选' : '选课' }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <h3 style="margin: 24px 0 16px 0;">我的课程</h3>
      <el-table :data="myEnrollments" size="small">
        <el-table-column prop="courseNo" label="课程号" width="120" />
        <el-table-column prop="courseName" label="课程名称" width="200" />
        <el-table-column prop="credits" label="学分" width="100" />
        <el-table-column prop="teacherName" label="任课教师" width="120" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button link size="small" type="danger" @click="onDrop(row.enrollmentId)">退选</el-button>
          </template>
        </el-table-column>
      </el-table>
    </template>

    <template v-else>
      <div class="toolbar">
        <el-select v-model="courseId" placeholder="选择课程" style="width: 280px; margin-right: 8px;" @change="load">
          <el-option label="全部课程" :value="0" />
          <el-option v-for="c in courses" :key="c.id" :label="`${c.courseNo} | ${c.courseName} (${c.semester})`" :value="Number(c.id)" />
        </el-select>
        <el-button type="primary" @click="load">查询选课</el-button>
      </div>

      <el-table :data="list" size="small">
        <el-table-column prop="id" label="选课ID" width="80" />
        <el-table-column label="学号" width="120">
          <template #default="{ row }">{{ studentNoMap[row.studentId] || '' }}</template>
        </el-table-column>
        <el-table-column label="学生" width="100">
          <template #default="{ row }">{{ studentMap[row.studentId] || row.studentId }}</template>
        </el-table-column>
        <el-table-column label="课程号" width="100">
          <template #default="{ row }">{{ getCourseInfo(row.courseOfferingId, 'courseNo') }}</template>
        </el-table-column>
        <el-table-column label="课程名称" width="180">
          <template #default="{ row }">{{ getCourseInfo(row.courseOfferingId, 'courseName') }}</template>
        </el-table-column>
        <el-table-column label="学分" width="80">
          <template #default="{ row }">{{ getCourseInfo(row.courseOfferingId, 'credit') }}</template>
        </el-table-column>
        <el-table-column label="教师" width="100">
          <template #default="{ row }">{{ getCourseInfo(row.courseOfferingId, 'teacherName') }}</template>
        </el-table-column>
        <el-table-column label="学期" width="90">
          <template #default="{ row }">{{ getCourseInfo(row.courseOfferingId, 'semester') }}</template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'ENROLLED'" type="success" size="small">已选课</el-tag>
            <el-tag v-else-if="row.status === 'DROPPED'" type="info" size="small">已退课</el-tag>
            <el-tag v-else type="warning" size="small">已完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="选课时间" width="160">
          <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
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
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { enrollCourse, fetchMyEnrollments, dropEnrollment, fetchEnrollmentsByCourse, fetchCourseOfferings, fetchOpenCourseOfferings, fetchMyTeachingOfferings, fetchStudents } from '@/api';
import { useAuthStore } from '@/stores/auth';

const auth = useAuthStore();
const isStudent = computed(() => auth.role === 'STUDENT');
const list = ref<any[]>([]);
const allEnrollments = ref<any[]>([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const courseId = ref<number | null>(null);
const courses = ref<any[]>([]);
const availableCourses = ref<any[]>([]);
const myEnrollments = ref<any[]>([]);
const enrolledCourseIds = ref<Set<number>>(new Set());
const studentMap = ref<Record<number, string>>({});
const studentNoMap = ref<Record<number, string>>({});
const courseMap = ref<Record<number, string>>({});
const courseOfferingMap = ref<Record<number, any>>({});

const load = async () => {
  try {
    if (isStudent.value) {
      const enrollments = await fetchMyEnrollments();
      // 过滤掉已退选的课程（防御性编程）
      const activeEnrollments = enrollments.filter((e: any) => e.status !== 'DROPPED');
      enrolledCourseIds.value = new Set(activeEnrollments.map((e: any) => e.courseOfferingId));
      
      myEnrollments.value = activeEnrollments.map((e: any) => {
        const offering = courses.value.find((c: any) => c.id === e.courseOfferingId);
        return {
          ...e,
          enrollmentId: e.id,
          courseNo: offering?.courseNo || '',
          courseName: offering?.courseName || '',
          credits: offering?.credit || 0,
          teacherName: offering?.teacherName || ''
        };
      });
      
      availableCourses.value = courses.value.map((c: any) => ({
        ...c,
        teacherName: c.teacherName || '',
        credits: c.credit || 0
      }));
    } else if (courseId.value !== null) {
      allEnrollments.value = await fetchEnrollmentsByCourse(courseId.value);
      total.value = allEnrollments.value.length;
      const start = (page.value - 1) * pageSize.value;
      const end = start + pageSize.value;
      list.value = allEnrollments.value.slice(start, end);
    } else {
      list.value = [];
      total.value = 0;
    }
  } catch (e: any) {
    if (isStudent.value) {
      myEnrollments.value = [];
      availableCourses.value = [];
    } else {
      list.value = [];
      total.value = 0;
    }
    ElMessage.error(e?.message || '加载失败');
  }
};

const loadCourses = async () => {
  try {
    if (auth.role === 'TEACHER') {
      courses.value = await fetchMyTeachingOfferings();
    } else if (auth.role === 'ADMIN') {
      // 管理员获取所有选课（包括已结课的）
      courses.value = await fetchCourseOfferings(1, 1000);
    } else {
      courses.value = await fetchOpenCourseOfferings('2025-1');
    }
    courses.value.forEach((c: any) => {
      courseMap.value[c.id] = c.courseName;
      courseOfferingMap.value[c.id] = c;
    });
    if (!isStudent.value && !courseId.value) {
      courseId.value = 0; // 默认显示全部
      await load();
    }
  } catch (e: any) {
    courses.value = [];
    ElMessage.error(e?.message || '加载选课失败');
  }
};

const loadStudents = async () => {
  if (!isStudent.value) {
    try {
      const res = await fetchStudents(1, 1000);
      res.forEach((s: any) => {
        studentMap.value[s.id] = s.name || s.studentNo || s.student_no;
        studentNoMap.value[s.id] = s.studentNo || s.student_no || '';
      });
    } catch (e: any) {
      ElMessage.error(e?.message || '加载学生失败');
    }
  }
};

const isEnrolled = (courseId: number) => {
  return enrolledCourseIds.value.has(courseId);
};

const getCourseInfo = (offeringId: number, field: string) => {
  const offering = courseOfferingMap.value[offeringId];
  return offering ? offering[field] || '' : offeringId;
};

const formatTime = (time: string) => {
  if (!time) return '';
  return time.replace('T', ' ').substring(0, 16);
};

const onEnroll = async (cId: number) => {
  try {
    await enrollCourse(cId);
    ElMessage.success('选课成功');
    load();
  } catch (e: any) {
    ElMessage.error(e?.message || '选课失败');
  }
};

const onDrop = async (enrollmentId: number) => {
  try {
    await dropEnrollment(enrollmentId);
    ElMessage.success('已退选');
    load();
  } catch (e: any) {
    ElMessage.error(e?.message || '退选失败');
  }
};

onMounted(async () => {
  await loadCourses();
  await loadStudents();
  await load();
});
</script>

<style scoped>
.card { background: #fff; padding: 16px; border-radius: 12px; }
.toolbar { margin-bottom: 12px; }
.pager { margin-top: 12px; justify-content: center; }
</style>
