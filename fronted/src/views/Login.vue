<template>
  <div class="login-page">
    <div class="panel">
      <h2>学生管理系统</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" autocomplete="username" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" autocomplete="current-password" />
        </el-form-item>
        <el-button type="primary" style="width: 100%;" @click="onSubmit" :loading="loading">登录</el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage } from 'element-plus';

const router = useRouter();
const route = useRoute();
const auth = useAuthStore();
const loading = ref(false);
const formRef = ref<FormInstance>();
const form = reactive({ username: '', password: '' });

const rules = reactive<FormRules>({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
});

const onSubmit = () => {
  if (!formRef.value) return;
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      await auth.login(form.username, form.password);
      ElMessage.success('登录成功');
      const redirect = (route.query.redirect as string) || '/';
      router.replace(redirect);
    } catch (e: any) {
      ElMessage.error(e?.message || '登录失败');
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f7f8fc, #eef1f6);
}
.panel {
  width: 360px;
  padding: 32px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}
.panel h2 {
  margin-bottom: 24px;
  text-align: center;
}
</style>
