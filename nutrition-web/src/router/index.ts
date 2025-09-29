import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import Layout from '@/components/Layout/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 登录页面
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login/index.vue'),
      meta: {
        title: '登录',
        requiresAuth: false
      }
    },
    // 主应用布局
    {
      path: '/',
      component: Layout,
      redirect: '/',
      meta: {
        requiresAuth: true
      },
      children: [
        {
          path: '',
          name: 'Home',
          component: () => import('@/views/Home/index.vue'),
          meta: {
            title: '首页',
            requiresAuth: true
          }
        },
        {
          path: '/nutrition',
          name: 'Nutrition',
          component: () => import('@/views/Nutrition/index.vue'),
          meta: {
            title: '营养记录',
            requiresAuth: true
          }
        },
        {
          path: '/food',
          name: 'Food',
          component: () => import('@/views/Food/index.vue'),
          meta: {
            title: '食物库',
            requiresAuth: true
          }
        },
        {
          path: '/assessment',
          name: 'Assessment',
          component: () => import('@/views/NutritionAssessment/index.vue'),
          meta: {
            title: '营养评估',
            requiresAuth: true
          }
        },
        {
          path: '/analysis',
          name: 'Analysis',
          component: () => import('@/views/Analysis/index.vue'),
          meta: {
            title: '数据分析',
            requiresAuth: true
          }
        },
        {
          path: '/profile',
          name: 'Profile',
          component: () => import('@/views/Profile/index.vue'),
          meta: {
            title: '个人设置',
            requiresAuth: true
          }
        },
        {
          path: '/user-management',
          name: 'UserManagement',
          component: () => import('@/views/UserManagement/index.vue'),
          meta: {
            title: '用户管理',
            requiresAuth: true
          }
        },
        {
          path: '/food-management',
          name: 'FoodManagement',
          component: () => import('@/views/FoodManagement/index.vue'),
          meta: {
            title: '食物管理',
            requiresAuth: true
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
        title: '页面未找到',
        requiresAuth: false
      }
    }
  ],
})

// 检查用户是否已登录
const isAuthenticated = (): boolean => {
  const token = localStorage.getItem('token')
  return !!token
}

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta?.title) {
    document.title = `${to.meta.title} - 营养平台`
  } else {
    document.title = '营养平台'
  }

  // 检查路由是否需要认证
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)

  if (requiresAuth && !isAuthenticated()) {
    // 需要认证但未登录，跳转到登录页
    ElMessage.warning('请先登录')
    next('/login')
  } else if (to.path === '/login' && isAuthenticated()) {
    // 已登录用户访问登录页，跳转到首页
    next('/')
  } else {
    // 正常访问
    next()
  }
})

export default router
