<template>
  <section class="page">
    <div class="page-header">
      <h2>活动列表</h2>
    </div>
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.keyword" placeholder="搜索活动名称" clearable style="width: 220px" />
        <el-input v-model="query.category" placeholder="类别" clearable style="width: 160px" />
        <el-select v-model="query.status" placeholder="状态" clearable style="width: 160px">
          <el-option label="报名中" value="registering" />
          <el-option label="报名截止" value="registration_closed" />
          <el-option label="进行中" value="ongoing" />
          <el-option label="已结束" value="ended" />
        </el-select>
        <el-button type="primary" @click="load">查询</el-button>
      </div>
      <el-table :data="activities" v-loading="loading">
        <el-table-column prop="activityTitle" label="活动名称" min-width="190" />
        <el-table-column prop="category" label="类别" width="110" />
        <el-table-column prop="organizerName" label="组织者" min-width="140" />
        <el-table-column prop="location" label="地点" min-width="120" />
        <el-table-column prop="startTime" label="开始时间" min-width="180" />
        <el-table-column label="名额" width="100">
          <template #default="{ row }">{{ row.remainingCapacity }}/{{ row.capacity }}</template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="statusType(row.activityStatus)">{{ row.statusText }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="190" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="$router.push(`/student/activities/${row.activityId}`)">详情</el-button>
            <el-button size="small" type="primary" :disabled="row.activityStatus !== 'registering'" @click="register(row.activityId)">报名</el-button>
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
const query = reactive({ keyword: '', category: '', status: '' })

function statusType(status) {
  if (status === 'registering') return 'success'
  if (status === 'ongoing') return 'warning'
  if (status === 'ended' || status === 'cancelled') return 'info'
  return 'primary'
}

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
  load()
}

onMounted(load)
</script>
