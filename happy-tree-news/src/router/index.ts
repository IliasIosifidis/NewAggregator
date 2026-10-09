import { createRouter, createWebHistory } from 'vue-router'
import NewsView from '@/views/NewsView.vue'
import PathsView from '@/views/PathsView.vue'

declare module 'vue-router' {
  interface RouteMeta {
    title: string
  }
}

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: NewsView, meta: { title: 'Forest News' } },
    { path: '/paths', component: PathsView, meta: { title: 'Forest Paths' } },
    // Unknown addresses used to show the news; keep it that way.
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
  // Back and forward return to where you were; a new page starts at the top.
  scrollBehavior: (_to, _from, savedPosition) => savedPosition ?? { top: 0 },
})

router.afterEach((to) => {
  document.title = to.meta.title
})

export default router
