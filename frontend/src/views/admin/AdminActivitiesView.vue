<template>
  <section class="page">
    <div class="page-header">
      <h2>活动审核</h2>
      <el-button @click="load">刷新</el-button>
    </div>
    <div class="panel">
      <el-table :data="rows">
        <el-table-column prop="activityId" label="编号" width="80" />
        <el-table-column prop="activityTitle" label="活动名称" min-width="190" />
        <el-table-column prop="organizerName" label="组织者" min-width="140" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }"><el-tag>{{ row.statusText }}</el-tag></template>
        </el-table-column>
        <el-table-column label="审核状态" width="120">
          <template #default="{ row }"><el-tag type="info">{{ row.auditText }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" type="success" @click="audit(row.activityId, 'approved')">通过</el-button>
            <el-button size="small" type="danger" @click="audit(row.activityId, 'rejected')">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { auditActivity, listActivities } from '../../api'

const rows = ref([])
async function load() {
  rows.value = await listActivities()
}
async function audit(id, status) {
  await auditActivity(id, status)
  ElMessage.success('审核状态已更新')
  load()
}
onMounted(load)
</script>
