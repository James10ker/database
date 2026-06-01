import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../store/auth'
import LoginView from '../views/LoginView.vue'
import AppLayout from '../views/AppLayout.vue'
import ActivityListView from '../views/student/ActivityListView.vue'
import ActivityDetailView from '../views/student/ActivityDetailView.vue'
import MyRegistrationsView from '../views/student/MyRegistrationsView.vue'
import StudentCheckinView from '../views/student/StudentCheckinView.vue'
import StudentRatingView from '../views/student/StudentRatingView.vue'
import RecommendationsView from '../views/student/RecommendationsView.vue'
import OrganizerActivitiesView from '../views/organizer/OrganizerActivitiesView.vue'
import ActivityFormView from '../views/organizer/ActivityFormView.vue'
import RegistrationListView from '../views/organizer/RegistrationListView.vue'
import OrganizerCheckinView from '../views/organizer/OrganizerCheckinView.vue'
import StatisticsView from '../views/organizer/StatisticsView.vue'
import AdminUsersView from '../views/admin/AdminUsersView.vue'
import AdminActivitiesView from '../views/admin/AdminActivitiesView.vue'

const routes = [
  { path: '/login', component: LoginView },
  {
    path: '/',
    component: AppLayout,
    meta: { requiresAuth: true },
    children: [
      { path: '', redirect: '/student/activities' },
      { path: 'student/activities', component: ActivityListView },
      { path: 'student/activities/:id', component: ActivityDetailView },
      { path: 'student/registrations', component: MyRegistrationsView },
      { path: 'student/checkin', component: StudentCheckinView },
      { path: 'student/rating', component: StudentRatingView },
      { path: 'student/recommendations', component: RecommendationsView },
      { path: 'organizer/activities', component: OrganizerActivitiesView },
      { path: 'organizer/activities/new', component: ActivityFormView },
      { path: 'organizer/activities/:id/edit', component: ActivityFormView },
      { path: 'organizer/activities/:id/registrations', component: RegistrationListView },
      { path: 'organizer/checkin', component: OrganizerCheckinView },
      { path: 'organizer/statistics', component: StatisticsView },
      { path: 'admin/users', component: AdminUsersView },
      { path: 'admin/activities', component: AdminActivitiesView }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.token) {
    return '/login'
  }
  if (to.path === '/login' && auth.token) {
    return defaultPath(auth.user?.role)
  }
})

export function defaultPath(role) {
  if (role === 'organizer') return '/organizer/activities'
  if (role === 'admin') return '/admin/users'
  return '/student/activities'
}

export default router
