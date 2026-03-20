import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Home.vue'),
    meta: { title: '平台数据仪表盘' }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫拦截
router.beforeEach((to, from, next) => {
  // 管理后台简单鉴权拦截逻辑
  // const token = localStorage.getItem('admin_token');
  // if (!token && to.path !== '/login') {
  //   next({ path: '/login' });
  // } else {
  //   document.title = (to.meta.title as string) || 'AI能力平台管理端';
  //   next();
  // }
  document.title = (to.meta.title as string) || 'AI能力平台管理后台';
  next();
});

export default router;
