<template>
  <el-container class="shell">
    <el-aside width="220px">
      <div class="brand">校园活动管理</div>
      <el-menu :default-active="$route.path" router>
        <template v-for="item in menus" :key="item.path">
          <el-menu-item :index="item.path">{{ item.label }}</el-menu-item>
        </template>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <span>{{ auth.user?.displayName }} · {{ roleName }}</span>
        <el-button @click="logout">退出</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'

const router = useRouter()
const auth = useAuthStore()

const menuMap = {
  student: [
    { path: '/student/activities', label: '活动列表' },
    { path: '/student/registrations', label: '我的报名' },
    { path: '/student/checkin', label: '活动签到' },
    { path: '/student/rating', label: '评分反馈' },
    { path: '/student/recommendations', label: '推荐活动' }
  ],
  organizer: [
    { path: '/organizer/activities', label: '活动管理' },
    { path: '/organizer/activities/new', label: '发布活动' },
    { path: '/organizer/checkin', label: '签到管理' },
    { path: '/organizer/statistics', label: '活动统计' }
  ],
  admin: [
    { path: '/admin/users', label: '用户状态' },
    { path: '/admin/activities', label: '活动审核' }
  ]
}

const menus = computed(() => menuMap[auth.user?.role] || [])
const roleName = computed(() => ({ student: '学生', organizer: '组织者', admin: '管理员' }[auth.user?.role] || ''))

function logout() {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.shell {
  min-height: 100vh;
}

.el-aside {
  background: #fff;
  border-right: 1px solid #e5e7eb;
}

.brand {
  font-size: 18px;
  font-weight: 700;
  padding: 20px;
}

.el-header {
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
}

.el-main {
  background: #f6f7fb;
  padding: 0;
}
</style>
