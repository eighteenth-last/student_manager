<template>
  <div class="card">
    <div class="toolbar">
      <el-button type="primary" @click="openEdit()">新增班级</el-button>
    </div>
    <el-table :data="list" size="small">
      <el-table-column prop="classCode" label="班级编号" width="140" />
      <el-table-column prop="major" label="专业" />
      <el-table-column prop="headTeacher" label="班主任" />
      <el-table-column prop="entryYear" label="入学年份" width="120" />
      <el-table-column prop="studentCount" label="班级人数" width="120" />
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

    <el-dialog v-model="visible" :title="editing ? '编辑班级' : '新增班级'" width="520px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="编号">
          <el-input v-model="form.classCode" :disabled="!!editing" />
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="form.major" />
        </el-form-item>
        <el-form-item label="班主任">
          <el-input v-model="form.headTeacher" />
        </el-form-item>
        <el-form-item label="入学年份">
          <el-input-number v-model="form.entryYear" :min="2000" :max="2100" />
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
import { fetchClasses, createClass, updateClass, deleteClass } from '@/api';

const list = ref<any[]>([]);
const allClasses = ref<any[]>([]);
const page = ref(1);
const pageSize = ref(20);
const total = ref(0);
const visible = ref(false);
const editing = ref<any | null>(null);
const form = reactive<any>({ classCode: '', major: '', headTeacher: '', entryYear: new Date().getFullYear() });

const load = async () => {
  allClasses.value = await fetchClasses(1, 1000);
  total.value = allClasses.value.length;
  const start = (page.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  list.value = allClasses.value.slice(start, end);
};

const openEdit = (row?: any) => {
  editing.value = row || null;
  if (row) {
    Object.assign(form, row);
  } else {
    Object.assign(form, { classCode: '', major: '', headTeacher: '', entryYear: new Date().getFullYear() });
  }
  visible.value = true;
};

const onSubmit = async () => {
  try {
    if (editing.value) {
      await updateClass(editing.value.id, form);
      ElMessage.success('已更新');
    } else {
      await createClass(form);
      ElMessage.success('已创建');
    }
    visible.value = false;
    load();
  } catch (e: any) {
    ElMessage.error(e?.message || '操作失败');
  }
};

const onDelete = async (row: any) => {
  await ElMessageBox.confirm('确认删除该班级？', '提示');
  await deleteClass(row.id);
  ElMessage.success('已删除');
  load();
};

onMounted(load);
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
