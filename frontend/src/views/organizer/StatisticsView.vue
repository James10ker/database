<template>
  <section class="page">
    <div class="page-header">
      <h2>活动统计</h2>
    </div>
    <div class="panel">
      <div class="toolbar">
        <el-input-number v-model="activityId" :min="1" />
        <el-button type="primary" @click="load">查询</el-button>
      </div>
      <div v-if="data" class="metric-grid">
        <div class="metric">报名人数<strong>{{ data.registrationCount }}</strong></div>
        <div class="metric">签到人数<strong>{{ data.checkinCount }}</strong></div>
        <div class="metric">签到率<strong>{{ data.checkinRate }}%</strong></div>
        <div class="metric">评分人数<strong>{{ data.ratingCount }}</strong></div>
        <div class="metric">平均评分<strong>{{ data.averageScore }}</strong></div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import { activityStatistics } from '../../api'

const activityId = ref(5)
const data = ref(null)
async function load() {
  data.value = await activityStatistics(activityId.value)
}
</script>
