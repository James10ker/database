<template>
  <section class="page">
    <div class="page-header">
      <h2>推荐活动</h2>
      <el-button @click="load">刷新</el-button>
    </div>
    <div class="panel">
      <el-table :data="rows">
        <el-table-column prop="activityTitle" label="活动名称" min-width="180" />
        <el-table-column prop="category" label="类别" width="100" />
        <el-table-column prop="location" label="地点" min-width="140" />
        <el-table-column prop="startTime" label="开始时间" min-width="180" />
      </el-table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { recommendations } from '../../api'

const rows = ref([])
async function load() {
  rows.value = await recommendations()
}
onMounted(load)
</script>
