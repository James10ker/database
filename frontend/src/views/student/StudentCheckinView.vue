<template>
  <section class="page">
    <div class="page-header">
      <h2>活动签到</h2>
    </div>
    <div class="panel">
      <el-form :model="form" label-width="100px" style="max-width: 460px">
        <el-form-item label="活动编号">
          <el-input-number v-model="form.activityId" :min="1" />
        </el-form-item>
        <el-form-item label="签到码">
          <el-input v-model="form.checkinCode" />
        </el-form-item>
        <el-button type="primary" @click="submit">提交签到</el-button>
      </el-form>
    </div>
  </section>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { submitCheckin } from '../../api'

const form = reactive({ activityId: 1, checkinCode: '' })
async function submit() {
  await submitCheckin(form)
  ElMessage.success('签到成功')
}
</script>
