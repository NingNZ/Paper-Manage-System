<template>
  <el-dialog
    v-model="visible"
    width="600px"
    top="30px"
    :close-on-click-modal="false"
    :show-close="true"
  >
    <template #default>
      <div class="invite-container">
        <div class="search-box">
          <el-input v-model="searchQuery" placeholder="输入要邀请的成员名" clearable>
            <template #suffix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
        <el-table :data="filteredMembers" style="margin-top: 20px" border>
          <el-table-column prop="id" label="用户id" width="100" />
          <el-table-column prop="name" label="用户名" width="200" />
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button size="small" type="primary" @click="invite(scope.row)">邀请</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Search } from '@element-plus/icons-vue'

const visible = ref(false)
const searchQuery = ref('')

const members = ref([
  { id: '#01', name: 'Linda' },
  { id: '#06', name: 'Jily' },
  { id: '#07', name: 'Bob' },
  { id: '#08', name: 'Duck' },
  { id: '#09', name: 'Domb' },
])

const filteredMembers = computed(() =>
  members.value.filter((m) =>
    m.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
)

function invite(member) {
  console.log(`邀请：${member.name}`)
}

function openDialog() {
  visible.value = true
}

defineExpose({ openDialog })
</script>

<style scoped>
.invite-container {
  padding: 10px;
}

.search-box {
  display: flex;
  align-items: center;
}

:deep(.el-dialog__body) {
  padding-top: 10px;
}
</style>
