<template>
  <main class="login-page">
    <section class="intro">
      <div class="intro-badge">Campus Activity</div>
      <h1>校园活动管理系统</h1>
      <p>活动发布、报名、签到、评分与统计的一体化管理平台。</p>
      <div class="stats">
        <div>
          <strong>3</strong>
          <span>角色工作台</span>
        </div>
        <div>
          <strong>7</strong>
          <span>核心模块</span>
        </div>
        <div>
          <strong>1</strong>
          <span>完整闭环</span>
        </div>
      </div>
    </section>

    <section class="login-card">
      <div class="card-head">
        <div class="logo">CA</div>
        <div>
          <h2>欢迎登录</h2>
          <span>请选择身份并进入系统</span>
        </div>
      </div>

      <el-form :model="form" label-position="top" @submit.prevent="submit">
        <el-form-item label="角色">
          <el-segmented v-model="form.role" :options="roles" />
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="form.username" placeholder="学生 2026001 / 组织者 org001 / 管理员 admin" size="large" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" show-password size="large" />
        </el-form-item>
        <el-button type="primary" native-type="submit" :loading="loading" size="large">登录系统</el-button>
      </el-form>

      <div class="demo-account">
        <span>演示账号</span>
        <button type="button" @click="fill('student', '2026001')">学生</button>
        <button type="button" @click="fill('organizer', 'org001')">组织者</button>
        <button type="button" @click="fill('admin', 'admin')">管理员</button>
      </div>
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

function fill(role, username) {
  form.role = role
  form.username = username
  form.password = '123456'
}

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
  background:
    linear-gradient(135deg, rgba(15, 23, 42, 0.88), rgba(30, 41, 59, 0.68)),
    url("https://images.unsplash.com/photo-1523050854058-8df90110c9f1?auto=format&fit=crop&w=1800&q=80");
  background-position: center;
  background-size: cover;
  display: grid;
  gap: 48px;
  grid-template-columns: minmax(0, 1fr) 420px;
  min-height: 100vh;
  padding: 48px clamp(24px, 7vw, 96px);
}

.intro {
  color: #fff;
  max-width: 660px;
}

.intro-badge {
  background: rgba(255, 255, 255, 0.14);
  border: 1px solid rgba(255, 255, 255, 0.22);
  border-radius: 999px;
  display: inline-flex;
  font-size: 13px;
  margin-bottom: 18px;
  padding: 7px 12px;
}

.intro h1 {
  font-size: 46px;
  line-height: 1.12;
  margin: 0;
}

.intro p {
  color: #dbeafe;
  font-size: 17px;
  line-height: 1.8;
  margin: 18px 0 28px;
}

.stats {
  display: grid;
  gap: 12px;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  max-width: 500px;
}

.stats div {
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.16);
  border-radius: 8px;
  padding: 14px;
}

.stats strong,
.stats span {
  display: block;
}

.stats strong {
  font-size: 24px;
}

.stats span {
  color: #cbd5e1;
  font-size: 13px;
  margin-top: 4px;
}

.login-card {
  background: rgba(255, 255, 255, 0.96);
  border: 1px solid rgba(255, 255, 255, 0.72);
  border-radius: 8px;
  box-shadow: 0 24px 70px rgba(15, 23, 42, 0.26);
  padding: 30px;
}

.card-head {
  align-items: center;
  display: flex;
  gap: 12px;
  margin-bottom: 26px;
}

.logo {
  align-items: center;
  background: #2563eb;
  border-radius: 8px;
  color: #fff;
  display: flex;
  font-weight: 800;
  height: 42px;
  justify-content: center;
  width: 42px;
}

h2 {
  color: #0f172a;
  font-size: 22px;
  margin: 0;
}

.card-head span {
  color: #64748b;
  font-size: 13px;
}

.el-button {
  width: 100%;
}

.demo-account {
  align-items: center;
  border-top: 1px solid #e5e7eb;
  display: flex;
  gap: 8px;
  margin-top: 20px;
  padding-top: 16px;
}

.demo-account span {
  color: #64748b;
  font-size: 13px;
  margin-right: auto;
}

.demo-account button {
  background: #f1f5f9;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  color: #334155;
  cursor: pointer;
  padding: 7px 10px;
}

@media (max-width: 900px) {
  .login-page {
    grid-template-columns: 1fr;
    padding: 24px;
  }

  .intro h1 {
    font-size: 34px;
  }
}
</style>
