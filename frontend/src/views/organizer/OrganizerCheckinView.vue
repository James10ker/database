<template>
  <section class="page">
    <div class="page-header">
      <h2>签到管理</h2>
    </div>
    <div class="panel">
      <el-form :model="form" label-width="120px" style="max-width: 560px">
        <el-form-item label="活动编号">
          <el-input-number v-model="form.activityId" :min="1" />
        </el-form-item>
        <el-button type="primary" @click="generate">生成签到码</el-button>
        <el-alert v-if="code" :title="`签到码：${code}`" type="success" style="margin: 14px 0" />
        <el-divider />
        <el-form-item label="报名编号">
          <el-input-number v-model="manual.registrationId" :min="1" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="manual.remark" />
        </el-form-item>
        <el-button @click="submitManual">人工补签</el-button>
      </el-form>
    </div>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { generateCheckinCode, manualCheckin } from '../../api'

const code = ref('')
const form = reactive({ activityId: 1 })
const manual = reactive({ registrationId: 1, remark: '' })

async function generate() {
  const result = await generateCheckinCode(form.activityId)
  code.value = result.checkinCode
}
async function submitManual() {
  await manualCheckin(manual)
  ElMessage.success('补签成功')
}
</script>
