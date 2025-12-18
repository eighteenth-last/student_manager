import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import Layout from '@/views/Layout.vue';
import Login from '@/views/Login.vue';

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { public: true }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/courses',
    children: [
      { path: 'students', name: 'Students', component: () => import('@/views/Students.vue'), meta: { roles: ['ADMIN', 'TEACHER'] } },
      { path: 'teachers', name: 'Teachers', component: () => import('@/views/Teachers.vue'), meta: { roles: ['ADMIN'] } },
      { path: 'classes', name: 'Classes', component: () => import('@/views/Classes.vue'), meta: { roles: ['ADMIN'] } },
      { path: 'majors', name: 'Majors', component: () => import('@/views/Majors.vue'), meta: { roles: ['ADMIN'] } },
      { path: 'courses', name: 'Courses', component: () => import('@/views/Courses.vue'), meta: { roles: ['ADMIN', 'TEACHER', 'STUDENT'] } },
      { path: 'enrollments', name: 'Enrollments', component: () => import('@/views/Enrollments.vue'), meta: { roles: ['ADMIN', 'TEACHER', 'STUDENT'] } },
      { path: 'scores', name: 'Scores', component: () => import('@/views/Scores.vue'), meta: { roles: ['ADMIN', 'TEACHER', 'STUDENT'] } }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, _from, next) => {
  const auth = useAuthStore();
  if (to.meta.public) {
    return next();
  }
  if (!auth.token) {
    return next({ path: '/login', query: { redirect: to.fullPath } });
  }
  const roles = to.meta.roles as string[] | undefined;
  if (roles && auth.role && !roles.includes(auth.role)) {
    return next('/students');
  }
  return next();
});

export default router;
