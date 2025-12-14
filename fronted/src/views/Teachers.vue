<template>
  <div class="card">
    <div class="toolbar">
      <el-button type="primary" @click="openEdit()">新增教师</el-button>
    </div>
    <el-table :data="list" size="small">
      <el-table-column prop="teacherNo" label="工号" width="120" />
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column prop="department" label="院系" />
      <el-table-column prop="phone" label="电话" width="140" />
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

    <el-dialog v-model="visible" :title="editing ? '编辑教师' : '新增教师'" width="520px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="工号">
          <el-input v-model="form.teacherNo" :disabled="!!editing" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="院系">
          <el-select v-model="form.department" placeholder="请选择院系" style="width: 100%;" filterable>
            <el-option v-for="dept in departments" :key="dept.id" :label="dept.deptName" :value="dept.deptName" />
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
import { fetchTeachers, createTeacher, updateTeacher, deleteTeacher, fetchDepartments } from '@/api';

const list = ref<any[]>([]);
const allTeachers = ref<any[]>([]);
const departments = ref<any[]>([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const visible = ref(false);
const editing = ref<any | null>(null);
const form = reactive<any>({ teacherNo: '', name: '', phone: '', department: '' });

const load = async () => {
  allTeachers.value = await fetchTeachers(1, 1000);
  total.value = allTeachers.value.length;
  const start = (page.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  list.value = allTeachers.value.slice(start, end);
};

const openEdit = (row?: any) => {
  editing.value = row || null;
  if (row) {
    Object.assign(form, row);
  } else {
    Object.assign(form, { teacherNo: '', name: '', phone: '', department: '' });
  }
  visible.value = true;
};

const onSubmit = async () => {
  try {
    if (editing.value) {
      await updateTeacher(editing.value.id, form);
      ElMessage.success('已更新');
    } else {
      await createTeacher(form);
      ElMessage.success('已创建');
    }
    visible.value = false;
    load();
  } catch (e: any) {
    ElMessage.error(e?.message || '操作失败');
  }
};

const onDelete = async (row: any) => {
  await ElMessageBox.confirm('确认删除该教师？', '提示');
  await deleteTeacher(row.id);
  ElMessage.success('已删除');
  load();
};

const loadDepartments = async () => {
  try {
    departments.value = await fetchDepartments();
  } catch (e: any) {
    console.error('加载院系失败', e);
  }
};

onMounted(async () => {
  await loadDepartments();
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
