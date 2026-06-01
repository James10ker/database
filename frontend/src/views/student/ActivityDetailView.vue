<template>
  <section class="page">
    <div class="page-header">
      <h2>{{ activity?.activityTitle }}</h2>
      <el-button @click="$router.back()">返回</el-button>
    </div>
    <div class="panel" v-if="activity">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="类别">{{ activity.category }}</el-descriptions-item>
        <el-descriptions-item label="地点">{{ activity.location }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ activity.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ activity.endTime }}</el-descriptions-item>
        <el-descriptions-item label="报名开始">{{ activity.registerStartTime }}</el-descriptions-item>
        <el-descriptions-item label="报名截止">{{ activity.registerEndTime }}</el-descriptions-item>
        <el-descriptions-item label="人数上限">{{ activity.capacity }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ activity.activityStatus }}</el-descriptions-item>
      </el-descriptions>
      <p>{{ activity.description }}</p>
      <el-button type="primary" @click="register">报名该活动</el-button>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getActivity, registerActivity } from '../../api'

const route = useRoute()
const activity = ref(null)

async function load() {
  activity.value = await getActivity(route.params.id)
}

async function register() {
  await registerActivity(activity.value.activityId)
  ElMessage.success('报名成功')
}

onMounted(load)
</script>
