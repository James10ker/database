<template>
  <el-container class="admin-shell">
    <el-aside class="sidebar" width="240px">
      <div class="brand">
        <div class="brand-mark">CA</div>
        <div>
          <strong>校园活动管理</strong>
          <span>Campus Activity</span>
        </div>
      </div>

      <el-menu class="side-menu" :default-active="$route.path" router>
        <el-menu-item v-for="item in menus" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.label }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="topbar">
        <div>
          <div class="eyebrow">{{ roleName }}工作台</div>
          <h1>{{ currentTitle }}</h1>
        </div>
        <div class="user-box">
          <el-avatar :size="36">{{ avatarText }}</el-avatar>
          <div class="user-meta">
            <strong>{{ auth.user?.displayName }}</strong>
            <span>{{ auth.user?.username }}</span>
          </div>
          <el-button plain @click="logout">退出</el-button>
        </div>
      </el-header>

      <el-main class="content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Calendar,
  Checked,
  Collection,
  DataAnalysis,
  EditPen,
  List,
  Medal,
  Star,
  Tickets,
  User,
  UserFilled
} from '@element-plus/icons-vue'
import { useAuthStore } from '../store/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const menuMap = {
  student: [
    { path: '/student/activities', label: '活动列表', icon: Calendar },
    { path: '/student/registrations', label: '我的报名', icon: Tickets },
    { path: '/student/checkin', label: '活动签到', icon: Checked },
    { path: '/student/rating', label: '评分反馈', icon: EditPen },
    { path: '/student/recommendations', label: '推荐活动', icon: Star }
  ],
  organizer: [
    { path: '/organizer/activities', label: '活动管理', icon: Collection },
    { path: '/organizer/activities/new', label: '发布活动', icon: EditPen },
    { path: '/organizer/checkin', label: '签到管理', icon: Checked },
    { path: '/organizer/statistics', label: '活动统计', icon: DataAnalysis }
  ],
  admin: [
    { path: '/admin/users', label: '用户状态', icon: User },
    { path: '/admin/activities', label: '活动审核', icon: List }
  ]
}

const menus = computed(() => menuMap[auth.user?.role] || [])
const roleName = computed(() => ({ student: '学生', organizer: '组织者', admin: '管理员' }[auth.user?.role] || ''))
const currentTitle = computed(() => menus.value.find((item) => route.path === item.path)?.label || '控制台')
const avatarText = computed(() => (auth.user?.displayName || auth.user?.username || 'U').slice(0, 1).toUpperCase())

function logout() {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-shell {
  min-height: 100vh;
  background: #f3f6fb;
}

.sidebar {
  background: #111827;
  color: #e5e7eb;
  overflow: hidden;
}

.brand {
  align-items: center;
  display: flex;
  gap: 12px;
  height: 72px;
  padding: 0 20px;
}

.brand-mark {
  align-items: center;
  background: #38bdf8;
  border-radius: 8px;
  color: #0f172a;
  display: flex;
  font-weight: 800;
  height: 38px;
  justify-content: center;
  width: 38px;
}

.brand strong,
.brand span {
  display: block;
}

.brand span {
  color: #94a3b8;
  font-size: 12px;
  margin-top: 2px;
}

.side-menu {
  background: transparent;
  border-right: 0;
  padding: 8px 12px;
}

.side-menu :deep(.el-menu-item) {
  border-radius: 8px;
  color: #cbd5e1;
  height: 44px;
  margin-bottom: 6px;
}

.side-menu :deep(.el-menu-item.is-active) {
  background: #2563eb;
  color: #fff;
}

.side-menu :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.08);
  color: #fff;
}

.topbar {
  align-items: center;
  background: rgba(255, 255, 255, 0.92);
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  height: 72px;
  justify-content: space-between;
  padding: 0 24px;
}

.eyebrow {
  color: #64748b;
  font-size: 12px;
  margin-bottom: 4px;
}

h1 {
  color: #0f172a;
  font-size: 20px;
  line-height: 1.2;
  margin: 0;
}

.user-box {
  align-items: center;
  display: flex;
  gap: 10px;
}

.user-meta strong,
.user-meta span {
  display: block;
}

.user-meta strong {
  color: #0f172a;
  font-size: 14px;
}

.user-meta span {
  color: #64748b;
  font-size: 12px;
}

.content {
  background:
    radial-gradient(circle at top right, rgba(56, 189, 248, 0.12), transparent 28rem),
    #f3f6fb;
  padding: 0;
}
</style>
