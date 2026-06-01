<template>
  <main class="login-page">
    <section class="login-panel">
      <h1>校园活动管理系统</h1>
      <el-form :model="form" label-position="top" @submit.prevent="submit">
        <el-form-item label="角色">
          <el-segmented v-model="form.role" :options="roles" />
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="form.username" placeholder="学生 2026001 / 组织者 org001 / 管理员 admin" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" show-password />
        </el-form-item>
        <el-button type="primary" native-type="submit" :loading="loading">登录</el-button>
      </el-form>
    </section>
  </main>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'
import { defaultPath } from '../router'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const roles = [
  { label: '学生', value: 'student' },
  { label: '组织者', value: 'organizer' },
  { label: '管理员', value: 'admin' }
]
const form = reactive({
  role: 'student',
  username: '2026001',
  password: '123456'
})

async function submit() {
  loading.value = true
  try {
    const user = await auth.login(form)
    router.push(defaultPath(user.role))
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  align-items: center;
  background: linear-gradient(135deg, #eef2ff 0%, #f8fafc 45%, #ecfeff 100%);
  display: flex;
  min-height: 100vh;
  justify-content: center;
}

.login-panel {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  box-shadow: 0 20px 60px rgba(31, 41, 55, 0.12);
  padding: 32px;
  width: min(420px, calc(100vw - 32px));
}

h1 {
  font-size: 26px;
  margin: 0 0 24px;
}

.el-button {
  width: 100%;
}
</style>
