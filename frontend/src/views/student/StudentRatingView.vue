<template>
  <section class="page">
    <div class="page-header">
      <h2>评分反馈</h2>
    </div>
    <div class="panel">
      <el-form :model="form" label-width="100px" style="max-width: 560px">
        <el-form-item label="活动编号">
          <el-input-number v-model="form.activityId" :min="1" />
        </el-form-item>
        <el-form-item label="评分">
          <el-rate v-model="form.score" />
        </el-form-item>
        <el-form-item label="评价">
          <el-input v-model="form.comment" type="textarea" />
        </el-form-item>
        <el-button type="primary" @click="submit">提交评分</el-button>
      </el-form>
    </div>
  </section>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { submitRating } from '../../api'

const form = reactive({ activityId: 5, score: 5, comment: '' })
async function submit() {
  await submitRating(form)
  ElMessage.success('评分已提交')
}
</script>
