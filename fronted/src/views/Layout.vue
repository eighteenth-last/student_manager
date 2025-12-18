<template>
  <el-container class="layout">
    <el-aside width="220px" class="sidebar">
      <div class="logo">学生管理</div>
      <el-menu :default-active="active" router class="menu" :collapse="false">

        <el-menu-item v-if="can('ADMIN')" index="/teachers">
          <span>教师管理</span>
        </el-menu-item>
        <el-menu-item v-if="can('ADMIN') || can('TEACHER')" index="/students">
          <span>学生管理</span>
        </el-menu-item>
        <el-menu-item v-if="can('ADMIN')" index="/classes">
          <span>班级管理</span>
        </el-menu-item>
        <el-menu-item v-if="can('ADMIN')" index="/majors">
          <span>专业管理</span>
        </el-menu-item>
        <el-menu-item index="/courses">
          <span>课程管理</span>
        </el-menu-item>
        <el-menu-item v-if="can('ADMIN') || can('STUDENT')" index="/enrollments">
          <span>选课记录</span>
        </el-menu-item>
        <el-menu-item index="/scores">
          <span>成绩管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="grow"></div>
        <span class="user">{{ auth.username }} ({{ auth.role || '未登录' }})</span>
        <el-button type="text" @click="onLogout">退出</el-button>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const route = useRoute();
const router = useRouter();
const auth = useAuthStore();
const active = computed(() => route.path);

const can = (role: string) => auth.role === role;

const onLogout = () => {
  auth.logout();
  router.push('/login');
};
</script>

<style scoped>
.layout {
  min-height: 100vh;
}
.sidebar {
  background: #1f2d3d;
  color: #fff;
  overflow-x: hidden;
}
.logo {
  height: 56px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  font-weight: 600;
  font-size: 18px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  color: #fff;
}
.menu {
  border-right: none;
  background: transparent !important;
}
.header {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
  background: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}
.main {
  background: #f5f6fa;
  padding: 20px;
}
.grow {
  flex: 1;
}
.user {
  color: #606266;
}

/* 菜单项样式 */
:deep(.el-menu-item) {
  color: #bfcbd9 !important;
  background-color: transparent !important;
}

:deep(.el-menu-item:hover) {
  background-color: rgba(255, 255, 255, 0.1) !important;
  color: #fff !important;
}

:deep(.el-menu-item.is-active) {
  background-color: #409eff !important;
  color: #fff !important;
}

:deep(.el-menu-item i) {
  color: inherit;
}
</style>
