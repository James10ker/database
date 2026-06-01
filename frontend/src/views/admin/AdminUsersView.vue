<template>
  <section class="page">
    <div class="page-header">
      <h2>用户状态管理</h2>
      <el-button @click="load">刷新</el-button>
    </div>
    <div class="panel">
      <el-tabs>
        <el-tab-pane label="学生">
          <user-table type="student" :rows="data.students || []" @changed="load" />
        </el-tab-pane>
        <el-tab-pane label="组织者">
          <user-table type="organizer" :rows="data.organizers || []" @changed="load" />
        </el-tab-pane>
        <el-tab-pane label="管理员">
          <user-table type="admin" :rows="data.admins || []" @changed="load" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </section>
</template>

<script setup>
import { defineComponent, h, onMounted, reactive } from 'vue'
import { ElButton, ElMessage, ElTable, ElTableColumn } from 'element-plus'
import { adminUsers, updateUserStatus } from '../../api'

const data = reactive({ students: [], organizers: [], admins: [] })

const UserTable = defineComponent({
  props: { rows: Array, type: String },
  emits: ['changed'],
  setup(props, { emit }) {
    async function setStatus(row, status) {
      const id = row.studentId || row.organizerId || row.adminId
      await updateUserStatus(props.type, id, status)
      ElMessage.success('状态已更新')
      emit('changed')
    }
    return () => h(ElTable, { data: props.rows }, () => [
      h(ElTableColumn, { prop: props.type === 'student' ? 'studentName' : props.type === 'organizer' ? 'organizerName' : 'adminName', label: '名称', minWidth: 180 }),
      h(ElTableColumn, { prop: 'accountStatus', label: '状态', width: 120 }),
      h(ElTableColumn, { label: '操作', width: 180 }, {
        default: ({ row }) => [
          h(ElButton, { size: 'small', type: 'success', onClick: () => setStatus(row, 'enabled') }, () => '启用'),
          h(ElButton, { size: 'small', type: 'danger', onClick: () => setStatus(row, 'disabled') }, () => '禁用')
        ]
      })
    ])
  }
})

async function load() {
  Object.assign(data, await adminUsers())
}
onMounted(load)
</script>
