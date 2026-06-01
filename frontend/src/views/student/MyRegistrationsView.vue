<template>
  <section class="page">
    <div class="page-header">
      <h2>我的报名</h2>
      <el-button @click="load">刷新</el-button>
    </div>
    <div class="panel">
      <el-table :data="rows">
        <el-table-column prop="registrationId" label="报名编号" width="110" />
        <el-table-column prop="activityId" label="活动编号" width="110" />
        <el-table-column prop="registrationTime" label="报名时间" min-width="180" />
        <el-table-column prop="registrationStatus" label="状态" width="120" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button size="small" type="danger" @click="cancel(row.registrationId)">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { cancelRegistration, myRegistrations } from '../../api'

const rows = ref([])
async function load() {
  rows.value = await myRegistrations()
}
async function cancel(id) {
  await cancelRegistration(id)
  ElMessage.success('已取消报名')
  load()
}
onMounted(load)
</script>
