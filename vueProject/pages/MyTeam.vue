<script setup>
import bar from "../components/bar.vue";
import CreateTeamDialog from "../components/MyTeam/CreateTeamDialog.vue";
import JoinTeamDialog from "../components/MyTeam/JoinTeamDialog.vue";
import LeaveConfirmDialog from "../components/MyTeam/LeaveConfirmDialog.vue";
import { ref, computed, onMounted, useId, watch } from 'vue';
import { ElMessage } from 'element-plus';
import teamUtils from "../scripts/team";

const setAllData = (data)=>{
  fullTableData.value = Array.from({length:data.length},(_,i)=>({
    name:data[i].name,
    id:data[i].id,
    leader:data[i].leaderName
  }));
  fullTableData.value=[...fullTableData.value]
}
const sendAndSet = (userId)=>{
  teamUtils.getMyTeamList(userId)
  .then(({code,data,msg})=>{
    if(code==200){
      if(data.length==0){
        ElMessage.info("你还没有加入任何团队")
      }else{
        ElMessage.success("刷新已加入的团队")
      }
    }else{
      ElMessage.error(msg)
    }
    setAllData(data)
  })
  .catch(({code,data,msg})=>{
    ElMessage.error(msg)
    setAllData(data)
  }) 
}
onMounted(()=>{
  const userId = localStorage.getItem("userId")
  sendAndSet(userId)
})
// 全部团队数据
/**
 * @type {Array<{name:string,id:string,leader:string}>}
 */
const fullTableData = ref([]);

const currentPage = ref(1);
const pageSize = ref(5);

const pagedTableData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return fullTableData.value.slice(start, end);
});
const handlePageChange = (page) => {
  currentPage.value = page;
};

const handleSizeChange = (size) => {
  pageSize.value = size;
  currentPage.value = 1;
};

const showCreateDialog = ref(false);
const showJoinDialog = ref(false);
const leaveDialogVisible = ref(false);
const teamToLeave = ref(null);

const confirmLeave = () => {
  // console.log(teamToLeave.value)
  const teamId = teamToLeave.value.id
  const uid = localStorage.getItem("userId");
  teamUtils.dropMember(teamId,uid)
  .then(({code,msg})=>{
    if(code==200){
      sendAndSet(uid);
      ElMessage.success("退出成功")
    }else if(code==390){
      ElMessage.error("组长不可以退出团队")
    }else{
      ElMessage.error(msg)
    }
  })
  .catch(({code,msg})=>{
    ElMessage.error(msg);
  })
};

const openLeaveDialog = (team) => {
  teamToLeave.value = team;
  leaveDialogVisible.value = true;
};

const handleCreateTeam = (name)=>{
  console.log(name)
  const leaderId = localStorage.getItem("userId")
  teamUtils.createTeam(name,leaderId)
  .then(({code,msg})=>{
    if(code==200){
      sendAndSet(leaderId)
      ElMessage.success(`团队"${name}"创建成功`)
    }else{
      ElMessage.error(msg)
    }
  })
  .catch(({code,msg})=>{
    ElMessage.error(msg)
  })
}
</script>

<template>
  <div class="container">
    <bar />
    <div class="breadcrumb"><a href="/">首页</a>&gt;<a href="/myteam">我的团队</a></div>
    <main class="main-content">
      
      <h1 class="title">我的团队</h1>

      <div class="table-wrapper">
        <el-table :data="pagedTableData" style="width: 100%;" max-height="400">
          <el-table-column prop="name" label="团队名" width="180" />
          <el-table-column prop="id" label="团队编号" width="180" />
          <el-table-column prop="leader" label="团队组长" width="180" />
          <el-table-column label="操作">
            <template #default="scope">
              <router-link to="/other">
                <el-button type="primary" size="small" class="action-btn">进入</el-button>
              </router-link>
              <el-button type="danger" size="small" class="action-btn" @click="openLeaveDialog(scope.row)">退出</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="actions">
        <el-button type="success" class="action-btn" @click="showCreateDialog = true">创建团队</el-button>
        <el-button type="primary" class="action-btn" @click="showJoinDialog = true">申请加入团队</el-button>
      </div>

      <div class="pagination">
        <el-pagination
          background
          layout="prev, pager, next, sizes, jumper"
          :total="fullTableData.length"
          :page-size="pageSize"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </main>

    <!-- 弹窗组件 -->
    <CreateTeamDialog v-model="showCreateDialog" @create="handleCreateTeam" />
    <JoinTeamDialog v-model="showJoinDialog" />
    <LeaveConfirmDialog
      v-model:visible="leaveDialogVisible"
      :team-name="teamToLeave?.name"
      @confirm="confirmLeave"
    />
  </div>
</template>

<style scoped>

thead {
  background-color: #f0f0f0;
}
.container {
  min-height: 100vh;
  background-color: #f4f4f4;
  display: flex;
  flex-direction: column;
}
.main-content {
  flex: 1;
  padding: 40px 80px;
}
.title {
  font-size: 32px;
  color: #333;
  margin-bottom: 30px;
}
.table-wrapper {
  overflow-y: auto;
  margin-bottom: 20px;
  border-radius: 8px;
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

/* 表格样式 */
.native-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
}
.native-table th,
.native-table td {
  padding: 12px 16px;
  border-bottom: 1px solid #e0e0e0;
}
.even-row {
  background-color: #ffffff;
}
.odd-row {
  background-color: #f9f9f9;
}

.native-table thead th {
  position: sticky;
  top: 0;
  background-color: #f0f0f0;
  z-index: 1;
}

.table-wrapper {
  max-height: 500px;
  overflow-y: auto;
  background: white;
  border: 1px solid #ccc;
  padding: 0;
  border-radius: 12px;
}

/* 按钮样式 */
.action-btn {
  transition: all 0.3s ease;
  margin: 0 5px;
}
.action-btn:hover {
  filter: brightness(1.1);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.actions {
  margin: 20px 0;
  display: flex;
  gap: 20px;
  justify-content: center;
}
.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
  padding-right: 20px;
}
:deep(.el-pagination) {
  align-self: flex-end;
  margin-top: 20px;
}
</style>
