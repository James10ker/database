<template>
  <section class="page">
    <div class="page-header">
      <h2>活动管理</h2>
      <el-button type="primary" @click="$router.push('/organizer/activities/new')">新建活动</el-button>
    </div>
    <div class="panel">
      <el-table :data="rows">
        <el-table-column prop="activityId" label="编号" width="80" />
        <el-table-column prop="activityTitle" label="活动名称" min-width="180" />
        <el-table-column prop="category" label="类别" width="100" />
        <el-table-column prop="activityStatus" label="状态" width="110" />
        <el-table-column prop="auditStatus" label="审核" width="110" />
        <el-table-column label="操作" width="360">
          <template #default="{ row }">
            <el-button size="small" @click="$router.push(`/organizer/activities/${row.activityId}/edit`)">编辑</el-button>
            <el-button size="small" type="success" @click="publish(row.activityId)">发布</el-button>
            <el-button size="small" type="warning" @click="cancel(row.activityId)">取消</el-button>
            <el-button size="small" @click="$router.push(`/organizer/activities/${row.activityId}/registrations`)">名单</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { listActivities, publishActivity, cancelActivity } from '../../api'
import { useAuthStore } from '../../store/auth'

const auth = useAuthStore()
const rows = ref([])

async function load() {
  rows.value = await listActivities({ organizerId: auth.user.id })
}
async function publish(id) {
  await publishActivity(id)
  ElMessage.success('已发布')
  load()
}
async function cancel(id) {
  await cancelActivity(id)
  ElMessage.success('已取消')
  load()
}
onMounted(load)
</script>
