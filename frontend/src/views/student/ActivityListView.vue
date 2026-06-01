<template>
  <section class="page">
    <div class="page-header">
      <h2>活动列表</h2>
    </div>
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.keyword" placeholder="搜索活动名称" clearable style="width: 220px" />
        <el-input v-model="query.category" placeholder="类别" clearable style="width: 160px" />
        <el-button type="primary" @click="load">查询</el-button>
      </div>
      <el-table :data="activities" v-loading="loading">
        <el-table-column prop="activityTitle" label="活动名称" min-width="180" />
        <el-table-column prop="category" label="类别" width="100" />
        <el-table-column prop="location" label="地点" min-width="140" />
        <el-table-column prop="startTime" label="开始时间" min-width="180" />
        <el-table-column prop="activityStatus" label="状态" width="110" />
        <el-table-column label="操作" width="190">
          <template #default="{ row }">
            <el-button size="small" @click="$router.push(`/student/activities/${row.activityId}`)">详情</el-button>
            <el-button size="small" type="primary" @click="register(row.activityId)">报名</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { listActivities, registerActivity } from '../../api'

const loading = ref(false)
const activities = ref([])
const query = reactive({ keyword: '', category: '' })

async function load() {
  loading.value = true
  try {
    activities.value = await listActivities(query)
  } finally {
    loading.value = false
  }
}

async function register(activityId) {
  await registerActivity(activityId)
  ElMessage.success('报名成功')
}

onMounted(load)
</script>
