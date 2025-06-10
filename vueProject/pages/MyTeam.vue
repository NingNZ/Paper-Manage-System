<script setup>
import bar from "../components/bar.vue";
import CreateTeamDialog from "../components/MyTeam/CreateTeamDialog.vue";
import JoinTeamDialog from "../components/MyTeam/JoinTeamDialog.vue";
import LeaveConfirmDialog from "../components/MyTeam/LeaveConfirmDialog.vue";
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';

const fullTableData = ref([
  { name: 'AI科研小组', number: '#01', leader: 'Linda' },
  { name: 'CS科研小组', number: '#02', leader: 'Jason' },
  { name: 'Web小队', number: '#03', leader: 'Alice' },
  { name: '机器人研究', number: '#04', leader: 'Mock' },
  { name: '算法设计', number: '#05', leader: 'Jack' },
  { name: 'AI算法', number: '#06', leader: 'Jily' },
]);

const joinableTeams = ref([
  { name: 'AI科研小组', number: '#01', leader: 'Linda' },
  { name: 'AI算法', number: '#06', leader: 'Jily' },
  { name: 'AI避障', number: '#07', leader: 'Bob' },
  { name: 'AI机器人', number: '#08', leader: 'Duck' },
  { name: 'AI小助手', number: '#09', leader: 'Domb' },
]);

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
  const index = fullTableData.value.findIndex(t => t.name === teamToLeave.value.name);
  if (index !== -1) {
    fullTableData.value.splice(index, 1);
    ElMessage.success(`已退出团队 "${teamToLeave.value.name}"`);
  }
  leaveDialogVisible.value = false;
};

const openLeaveDialog = (team) => {
  teamToLeave.value = team;
  leaveDialogVisible.value = true;
};

function handleCreateTeam(name) {
  const newTeam = {
    name,
    number: `#${String(fullTableData.value.length + 1).padStart(2, '0')}`,
    leader: '未指定',
  };
  fullTableData.value.push(newTeam);
  ElMessage.success(`团队 "${name}" 创建成功`);
}
</script>

<template>
  <div class="container">
    <bar />
    <div class="breadcrumb"><a href="/">首页</a>&gt;<a href="/myteam">我的团队</a></div>
    <main class="main-content">
      
      <h1 class="title">我的团队</h1>

      <div class="table-wrapper">
        <table class="native-table">
          <thead>
            <tr>
              <th>团队名</th>
              <th>团队编号</th>
              <th>团队组长</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(item, index) in pagedTableData"
              :key="item.number"
              :class="index % 2 === 0 ? 'even-row' : 'odd-row'"
            >
              <td>{{ item.name }}</td>
              <td>{{ item.number }}</td>
              <td>{{ item.leader }}</td>
              <td>
                <router-link to="/other">
                  <el-button type="primary" size="small" class="action-btn">进入</el-button>
                </router-link>
                <el-button
                  type="danger"
                  size="small"
                  class="action-btn"
                  @click="openLeaveDialog(item)"
                >
                  退出
                </el-button>
              </td>
            </tr>
          </tbody>
        </table>
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
    <JoinTeamDialog v-model="showJoinDialog" :teams="joinableTeams" />
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
