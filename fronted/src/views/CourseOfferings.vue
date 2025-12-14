<template>
  <div class="card">
    <div class="toolbar">
      <el-button type="primary" @click="openEdit()">新增选课</el-button>
    </div>
    <el-table :data="list" size="small">
      <el-table-column prop="courseNo" label="课程号" width="120" />
      <el-table-column prop="courseName" label="课程名称" width="180" />
      <el-table-column prop="semester" label="学期" width="100" />
      <el-table-column prop="teacherName" label="授课教师" width="120" />
      <el-table-column prop="classTime" label="上课时间" width="150" />
      <el-table-column prop="classroom" label="教室" width="120" />
      <el-table-column label="选课人数" width="120">
        <template #default="{ row }">{{ row.currentStudents }} / {{ row.maxStudents }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.status === 'OPEN'" type="success">开放选课</el-tag>
          <el-tag v-else-if="row.status === 'CLOSED'" type="warning">关闭选课</el-tag>
          <el-tag v-else type="info">已结课</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
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

    <el-dialog v-model="visible" :title="editing ? '编辑选课' : '新增选课'" width="520px">
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
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { fetchCourseOfferings, createCourseOffering, updateCourseOffering, deleteCourseOffering, fetchTeachers } from '@/api';

const list = ref<any[]>([]);
const allOfferings = ref<any[]>([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const visible = ref(false);
const editing = ref<any | null>(null);
const teachers = ref<any[]>([]);
const form = reactive<any>({
  courseNo: '',
  courseName: '',
  credit: 0,
  semester: '2025-1',
  teacherId: null,
  classTime: '',
  classroom: '',
  maxStudents: 50,
  currentStudents: 0,
  status: 'OPEN'
});

const load = async () => {
  allOfferings.value = await fetchCourseOfferings(1, 1000);
  total.value = allOfferings.value.length;
  const start = (page.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  list.value = allOfferings.value.slice(start, end);
};

const loadTeachers = async () => {
  teachers.value = await fetchTeachers(1, 100);
};

const openEdit = (row?: any) => {
  if (row) {
    editing.value = row;
    Object.assign(form, {
      courseNo: row.courseNo,
      courseName: row.courseName,
      credit: row.credit,
      semester: row.semester,
      teacherId: row.teacherId,
      classTime: row.classTime,
      classroom: row.classroom,
      maxStudents: row.maxStudents,
      currentStudents: row.currentStudents,
      status: row.status
    });
  } else {
    editing.value = null;
    Object.assign(form, {
      courseNo: '',
      courseName: '',
      credit: 0,
      semester: '2025-1',
      teacherId: null,
      classTime: '',
      classroom: '',
      maxStudents: 50,
      currentStudents: 0,
      status: 'OPEN'
    });
  }
  visible.value = true;
};

const onSubmit = async () => {
  try {
    if (editing.value) {
      await updateCourseOffering(editing.value.id, form);
      ElMessage.success('已更新');
    } else {
      await createCourseOffering(form);
      ElMessage.success('已新增');
    }
    visible.value = false;
    load();
  } catch (e: any) {
    ElMessage.error(e?.message || '操作失败');
  }
};

const onDelete = async (row: any) => {
  await ElMessageBox.confirm('确认删除该选课记录？', '提示');
  await deleteCourseOffering(row.id);
  ElMessage.success('已删除');
  load();
};

onMounted(async () => {
  await loadTeachers();
  await load();
});
</script>

<style scoped>
.card {
  background: #fff;
  padding: 16px;
  border-radius: 12px;
}
.toolbar {
  margin-bottom: 12px;
}
.pager {
  margin-top: 12px;
  justify-content: center;
}
</style>
