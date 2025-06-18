<template>
  <el-dialog
    v-model="visible"
    width="600px"
    top="30px"
    :close-on-click-modal="false"
    :show-close="true"
    @close="members=[]"
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

        <el-table :data="members" style="margin-top: 20px" border>
          <el-table-column prop="id" label="用户ID" width="150" />
          <el-table-column prop="name" label="用户名" width="250" />
          <el-table-column label="操作" width="150">
            <template #default="scope">
            <div>
              <el-button
                v-if="scope.row.state === 200"
                size="small"
                type="primary"
                @click="invite(scope.row)"
              >
                邀请
              </el-button>
              <span
                v-else
                style="color: #aaa; font-style: italic; margin-left: 8px;"
              >
                已在团队中
              </span>
            </div>
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

const members = ref([])

const filteredMembers = ref([...members.value]) // 初始为全部

const teamId = ref(localStorage.getItem('teamId') || '') // 从 localStorage 获取团队ID

function handleSearch() {
  searchQuery.value = searchInput.value.trim()
  console.log(searchQuery.value)
  if (!teamId.value) {
    ElMessage.error('无法获取团队ID,请检查。')
    return
  }
  teamUtils.searchMember(teamId.value,searchInput.value)
  .then(({code,data,msg})=>{
      if(code==200||code==300){
        ElMessage.success("查询成功")
        members.value=data
      }else{
        ElMessage.error(msg)
      }
      console.log(members.value)
  }).catch(({code,data,msg})=>{
    ElMessage.error(msg)
  })
}

function invite(member) {
  // 确保 teamId 有值
  if (!teamId.value) {
    ElMessage.error('无法获取团队ID,请检查。')
    return
  }
  teamUtils.memberInvite(teamId.value,member.id)
  .then(({code,msg})=>{
    if(code==200){
      ElMessage.success("邀请成功，等待对方同意")
    }else if(code==300){
      ElMessage.info(msg)
    }else{
      ElMessage.error(msg)
    }
  }).catch(({code,msg})=>{
    ElMessage.error(msg)
  })
  console.log(`邀请用户: ${member.name} (ID: ${member.id}) 到团队: ${teamId.value}`);
}


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

