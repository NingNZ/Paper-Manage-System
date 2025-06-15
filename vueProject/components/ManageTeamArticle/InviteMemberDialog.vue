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
          <el-input
            v-model="searchInput"
            placeholder="输入要搜索的用户id"
            clearable
            class="search-input"
          >
            <template #suffix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-button type="primary" class="search-btn" @click="handleSearch">
            搜索
          </el-button>
        </div>

        <el-table :data="filteredMembers" style="margin-top: 20px" border>
          <el-table-column prop="id" label="用户ID" width="150" />
          <el-table-column prop="name" label="用户名" width="250" />
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button size="small" type="primary" @click="invite(scope.row)">
                邀请
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import teamUtils from '../../scripts/team'

const visible = ref(false)
const searchInput = ref('')
const searchQuery = ref('') // 实际用于筛选的关键词

const members = ref([
  { id: '#01', name: 'Linda' },
  { id: '#06', name: 'Jily' },
  { id: '#07', name: 'Bob' },
  { id: '#08', name: 'Duck' },
  { id: '#09', name: 'Domb' },
])

const filteredMembers = ref([...members.value]) // 初始为全部

const teamId = ref(localStorage.getItem('teamId') || '') // 从 localStorage 获取团队ID

function handleSearch() {
  searchQuery.value = searchInput.value.trim()
  if (!searchQuery.value) {
    filteredMembers.value = [...members.value]
  } else {
    const keyword = searchQuery.value.toLowerCase()
    filteredMembers.value = members.value.filter(
      (m) =>
        m.id.toLowerCase().includes(keyword) ||
        m.name.toLowerCase().includes(keyword)
    )
  }
}

async function invite(member) {
  // 确保 teamId 有值
  if (!teamId.value) {
    ElMessage.error('无法获取团队ID，请检查。')
    return
  }
  console.log(`邀请用户: ${member.name} (ID: ${member.id}) 到团队: ${teamId.value}`);
  try {
    const response = await teamUtils.searchMember(teamId.value, member.id);
    console.log('API Response:', response);

    if (response.code === 200) {
      ElMessage.success(`用户 ${member.name} 存在且未加入团队，可以邀请。`);
      // 在这里你可以继续执行邀请的后续操作，例如发送邀请请求
      emit('invite-sent', member);
    } else if (response.code === 300) {
      ElMessage.warning(`用户 ${member.name} 已经在此团队中。`);
    } else if (response.code === 400) {
      ElMessage.error(`未找到用户 ${member.name}。`);
    } else {
      ElMessage.error(`邀请失败: ${response.msg || '未知错误'}`);
    }
  } catch (error) {
    console.error('调用 searchMember 失败:', error);
    ElMessage.error(`服务不可用或网络错误: ${error.msg || '请稍后再试。'}`);
  }
}

const emit = defineEmits(['invite-sent'])

function openDialog() {
  visible.value = true
  searchInput.value = ''
  searchQuery.value = ''
  filteredMembers.value = [...members.value]
}

defineExpose({ openDialog })
</script>

<style scoped>
.invite-container {
  padding: 10px;
}

.search-box {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  flex: 1;
}

.search-btn {
  border-radius: 20px;
}

:deep(.el-table__empty-text) {
  color: #999;
  font-size: 14px;
}
</style>

