<template>
  <section class="page">
    <div class="page-header">
      <h2>报名名单</h2>
      <el-button @click="$router.back()">返回</el-button>
    </div>
    <div class="panel">
      <el-table :data="rows">
        <el-table-column prop="registrationId" label="报名编号" width="110" />
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column prop="studentName" label="姓名" width="130" />
        <el-table-column prop="activityTitle" label="活动名称" min-width="220" />
        <el-table-column prop="registrationTime" label="报名时间" min-width="180" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="row.registrationStatus === 'registered' ? 'success' : 'info'">{{ row.statusText }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { activityRegistrations } from '../../api'

const route = useRoute()
const rows = ref([])
async function load() {
  rows.value = await activityRegistrations(route.params.id)
}
onMounted(load)
</script>
