import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/components/Layout/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: Layout,
      redirect: '/',
      children: [
        {
          path: '',
          name: 'Home',
          component: () => import('@/views/Home/index.vue'),
          meta: {
            title: '首页'
          }
        },
        {
          path: '/nutrition',
          name: 'Nutrition',
          component: () => import('@/views/Nutrition/index.vue'),
          meta: {
            title: '营养记录'
          }
        },
        {
          path: '/food',
          name: 'Food',
          component: () => import('@/views/Food/index.vue'),
          meta: {
            title: '食物库'
          }
        },
        {
          path: '/analysis',
          name: 'Analysis',
          component: () => import('@/views/Analysis/index.vue'),
          meta: {
            title: '数据分析'
          }
        },
        {
          path: '/profile',
          name: 'Profile',
          component: () => import('@/views/Profile/index.vue'),
          meta: {
            title: '个人设置'
          }
        }
      ]
    },
    // 404页面
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/views/NotFound/index.vue'),
      meta: {
        title: '页面未找到'
      }
    }
  ],
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta?.title) {
    document.title = `${to.meta.title} - 营养平台`
  } else {
    document.title = '营养平台'
  }
  next()
})

export default router
