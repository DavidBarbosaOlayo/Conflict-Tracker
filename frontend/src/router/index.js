import { createRouter, createWebHistory } from 'vue-router'

import ConflictDetailView from '../views/ConflictDetailView.vue'
import DashboardView from '../views/DashboardView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'dashboard',
      component: DashboardView
    },
    {
      path: '/conflicts/:id',
      name: 'conflict-detail',
      component: ConflictDetailView
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/'
    }
  ],
  scrollBehavior() {
    return { top: 0 }
  }
})

export default router
