<script setup>
import bar from "../components/bar.vue";
import CreateTeamDialog from "../components/MyTeam/CreateTeamDialog.vue";
import JoinTeamDialog from "../components/MyTeam/JoinTeamDialog.vue";
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';

// 全部团队数据
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

// 计算分页后的数据
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

    <main class="main-content">
      <div class="breadcrumb">首页&gt;我的团队</div>
      <h1 class="title">我的团队</h1>

      <div class="table-wrapper">
        <el-table :data="pagedTableData" style="width: 100%;" max-height="400">
          <el-table-column prop="name" label="团队名" width="180" />
          <el-table-column prop="number" label="团队编号" width="180" />
          <el-table-column prop="leader" label="团队组长" width="180" />
          <el-table-column label="操作">
            <template #default>
              <router-link to="/other">
                <el-button type="primary" size="small" class="action-btn">进入</el-button>
              </router-link>
              <el-button type="danger" size="small" class="action-btn">退出</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="actions">
        <el-button type="success" class="action-btn" @click="showCreateDialog = true">创建团队</el-button>
        <el-button type="info" class="action-btn" @click="showJoinDialog = true">申请加入团队</el-button>
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
  </div>
</template>

<style scoped>
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

.breadcrumb {
  font-size: 12px;
  color: #888;
  margin-bottom: 20px;
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

