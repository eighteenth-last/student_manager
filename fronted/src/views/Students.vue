<template>
  <div class="card">
    <div class="toolbar">
      <el-button type="primary" @click="openEdit()">新增学生</el-button>
    </div>
    <el-table :data="list" style="width: 100%" size="small">
      <el-table-column prop="studentNo" label="学号" width="120" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="gender" label="性别" width="70" />
      <el-table-column prop="age" label="年龄" width="70" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="department" label="院系" width="130" />
      <el-table-column prop="major" label="专业" width="150" />
      <el-table-column label="班级" width="120">
        <template #default="{ row }">{{ classMap[row.classId] || row.classId }}</template>
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
      v-model:page-size="size"
      :page-sizes="[20, 50, 100, 200]"
      layout="total, sizes, prev, pager, next"
      :total="total"
      @size-change="load"
      @current-change="load"
      class="pager"
    />

    <el-dialog v-model="visible" :title="editing ? '编辑学生' : '新增学生'" width="520px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="学号">
          <el-input v-model="form.studentNo" :disabled="!!editing" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="form.age" :min="0" :max="150" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender" placeholder="请选择">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
            <el-option label="未知" value="未知" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="院系">
          <el-select v-model="form.department" placeholder="请选择院系" style="width: 100%;" filterable @change="onDepartmentChange">
            <el-option v-for="dept in departments" :key="dept.id" :label="dept.deptName" :value="dept.deptName" />
          </el-select>
        </el-form-item>
        <el-form-item label="专业">
          <el-select v-model="form.major" placeholder="请先选择院系" style="width: 100%;" filterable :disabled="!form.department">
            <el-option v-for="major in majorOptions" :key="major.id" :label="major.majorName" :value="major.majorName" />
          </el-select>
        </el-form-item>
        <el-form-item label="班级">
          <el-select v-model="form.classId" placeholder="请选择班级" style="width: 100%;" filterable clearable>
            <el-option v-for="cls in classes" :key="cls.id" :label="`${cls.classCode} - ${cls.major}`" :value="Number(cls.id)" />
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
import { fetchStudents, createStudent, updateStudent, deleteStudent, fetchClasses, fetchDepartments, fetchMajors } from '@/api';

const list = ref<any[]>([]);
const classes = ref<any[]>([]);
const classMap = ref<Record<number, string>>({});
const departments = ref<any[]>([]);
const allMajors = ref<any[]>([]);
const majorOptions = ref<any[]>([]);
const page = ref(1);
const size = ref(20);
const total = ref(0);
const visible = ref(false);
const editing = ref<any | null>(null);

const form = reactive<any>({
  studentNo: '',
  name: '',
  age: null,
  gender: '未知',
  phone: '',
  department: '',
  major: '',
  classId: null
});

// 监听院系变化，更新专业选项
const onDepartmentChange = () => {
  const selectedDept = departments.value.find(d => d.deptName === form.department);
  if (selectedDept) {
    majorOptions.value = allMajors.value.filter(m => m.departmentId === selectedDept.id);
  } else {
    majorOptions.value = [];
  }
  if (!majorOptions.value.find(m => m.majorName === form.major)) {
    form.major = '';
  }
};

const load = async () => {
  const data = await fetchStudents(page.value, size.value);
  list.value = data || [];
  total.value = (data || []).length >= size.value ? page.value * size.value + 1 : data.length;
};

const loadDepartments = async () => {
  try {
    departments.value = await fetchDepartments();
  } catch (e: any) {
    console.error('加载院系失败', e);
  }
};

const loadMajors = async () => {
  try {
    allMajors.value = await fetchMajors();
  } catch (e: any) {
    console.error('加载专业失败', e);
  }
};

const loadClasses = async () => {
  try {
    const res = await fetchClasses(1, 100);
    classes.value = res || [];
    res.forEach((c: any) => {
      classMap.value[c.id] = c.classCode;
    });
  } catch (e: any) {
    console.error('加载班级失败', e);
  }
};

const openEdit = (row?: any) => {
  editing.value = row || null;
  if (row) {
    Object.assign(form, row);
    // 设置专业选项
    onDepartmentChange();
  } else {
    Object.assign(form, { studentNo: '', name: '', age: null, gender: '未知', phone: '', department: '', major: '', classId: null });
    majorOptions.value = [];
  }
  visible.value = true;
};

const onSubmit = async () => {
  try {
    if (editing.value) {
      await updateStudent(editing.value.id, form);
      ElMessage.success('已更新');
    } else {
      await createStudent(form);
      ElMessage.success('已创建');
    }
    visible.value = false;
    load();
  } catch (e: any) {
    ElMessage.error(e?.message || '操作失败');
  }
};

const onDelete = async (row: any) => {
  await ElMessageBox.confirm('确认删除该学生？', '提示');
  await deleteStudent(row.id);
  ElMessage.success('已删除');
  load();
};

onMounted(async () => {
  await Promise.all([loadClasses(), loadDepartments(), loadMajors()]);
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
