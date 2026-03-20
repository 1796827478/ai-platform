import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import BasicLayout from '../layout/BasicLayout.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: BasicLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Home.vue'),
        meta: { title: '平台数据仪表盘' }
      },
      {
        path: 'model-config',
        name: 'ModelConfig',
        component: () => import('../views/model/ModelConfig.vue'),
        meta: { title: '大模型网关配置' }
      }
      // 此处留空，方便日后增加 MCP挂载管理 和 应用授权 的子组件
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫拦截
router.beforeEach((to, _from, next) => {
  document.title = (to.meta.title as string) || '统一 AI 能力平台后台管理';
  next();
});

export default router;
