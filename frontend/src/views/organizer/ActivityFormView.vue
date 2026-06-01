<template>
  <section class="page">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑活动' : '发布活动' }}</h2>
      <el-button @click="$router.back()">返回</el-button>
    </div>
    <div class="panel">
      <el-form :model="form" label-width="110px" style="max-width: 780px">
        <el-form-item label="活动名称"><el-input v-model="form.activityTitle" /></el-form-item>
        <el-form-item label="类别"><el-input v-model="form.category" /></el-form-item>
        <el-form-item label="地点"><el-input v-model="form.location" /></el-form-item>
        <el-form-item label="人数上限"><el-input-number v-model="form.capacity" :min="1" /></el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="form.startTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" /></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="form.endTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" /></el-form-item>
        <el-form-item label="报名开始"><el-date-picker v-model="form.registerStartTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" /></el-form-item>
        <el-form-item label="报名截止"><el-date-picker v-model="form.registerEndTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" /></el-form-item>
        <el-form-item label="活动简介"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-button type="primary" @click="save">保存</el-button>
      </el-form>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createActivity, getActivity, updateActivity } from '../../api'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => Boolean(route.params.id))
const form = reactive({
  activityTitle: '',
  category: '讲座',
  location: '',
  capacity: 50,
  startTime: '',
  endTime: '',
  registerStartTime: '',
  registerEndTime: '',
  description: ''
})

async function load() {
  if (!isEdit.value) return
  Object.assign(form, await getActivity(route.params.id))
}
async function save() {
  if (isEdit.value) {
    await updateActivity(route.params.id, form)
  } else {
    await createActivity(form)
  }
  ElMessage.success('保存成功')
  router.push('/organizer/activities')
}
onMounted(load)
</script>
