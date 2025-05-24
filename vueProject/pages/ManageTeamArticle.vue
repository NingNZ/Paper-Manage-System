<template>
  <div class="container">
    <!-- 顶部导航栏 -->
    <bar></bar>

    <!-- 页面主体 -->
    <main class="main-wrapper">
      <!-- 左侧表格 -->
      <aside class="left-table">
        <h3>团队成员</h3>
        <table class="member-table">
          <thead>
            <tr>
              <th>用户ID</th>
              <th>用户名</th>
              <th>成员角色</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(member, index) in members" :key="index">
              <td>{{ member.id }}</td>
              <td>{{ member.name }}</td>
              <td>{{ member.role }}</td>
            </tr>
          </tbody>
        </table>
        <div class="buttons">
          <el-button type="primary" size="small" @click="openInviteDialog">邀请成员</el-button>
          <el-button type="info" size="small" @click="showCategoryManager = true">管理分类</el-button>
        </div>
      </aside>

      <!-- 右侧内容 -->
      <section class="content">
        <div class="breadcrumb">首页 &gt; 我的团队 &gt; 团队xxx</div>

        <div class="table-wrapper">
          <table class="custom-table">
            <thead>
              <tr>
                <th>序号</th>
                <th>参考论文标题</th>
                <th>类别</th>
                <th>上传者</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(paper, index) in pagedPapers" :key="index">
                <td>#{{ (currentPage - 1) * pageSize + index + 1 }}</td>
                <td><em>{{ paper.title }}</em></td>
                <td>{{ paper.category }}</td>
                <td>{{ paper.uploader }}</td>
                <td class="actions">
                  <img src="../assets/download.svg" alt="下载" class="action-icon" />
                  <img src="../assets/edit.svg" alt="编辑" class="action-icon" />
                  <img src="../assets/delete.svg" alt="删除" class="action-icon" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <el-pagination
          background
          layout="prev, pager, next, sizes, jumper"
          :total="papers.length"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20]"
          :current-page="currentPage"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
          class="pagination-right"
        />
      </section>
    </main>

    <!-- 管理分类弹窗 -->
    <el-dialog
      v-model="showCategoryManager"
      title="管理分类"
      width="600px"
      destroy-on-close
      :close-on-click-modal="false"
    >
      <CategoryManager />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCategoryManager = false">取消</el-button>
          <el-button type="primary" @click="showCategoryManager = false">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 邀请成员弹窗 -->
    <InviteMemberDialog ref="inviteDialogRef" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElButton, ElPagination, ElDialog } from 'element-plus'
import bar from '../components/bar.vue'
import CategoryManager from '../ManageTeamArticle/CategoryManagerDialog.vue'
import InviteMemberDialog from '../ManageTeamArticle/InviteMemberDialog.vue'

const members = [
  { id: 'uid01', name: 'Anda', role: '组长' },
  { id: 'uid02', name: 'Lily', role: '组员' },
  { id: 'uid03', name: 'Lily', role: '组员' },
  { id: 'uid04', name: 'Lily', role: '组员' },
  { id: 'uid05', name: 'Lily', role: '组员' },
  { id: 'uid06', name: 'Lily', role: '组员' }
]

const papers = Array.from({ length: 20 }, (_, i) => ({
  title: `Title of paper ${i + 1}`,
  category: 'AI',
  uploader: 'xxx'
}))

const currentPage = ref(1)
const pageSize = ref(5)
const showCategoryManager = ref(false)
const inviteDialogRef = ref(null)

const pagedPapers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return papers.slice(start, start + pageSize.value)
})

function handlePageChange(val) {
  currentPage.value = val
}

function handleSizeChange(val) {
  pageSize.value = val
  currentPage.value = 1
}

function openInviteDialog() {
  inviteDialogRef.value.openDialog()
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background-color: #f4f4f4;
  display: flex;
  flex-direction: column;
}

.main-wrapper {
  display: flex;
  flex: 1;
  padding: 20px;
  gap: 20px;
}

.left-table {
  width: 260px;
  background: white;
  border: 1px solid #ddd;
  padding: 10px;
  max-height: 400px;
  overflow-y: auto;
}

.member-table {
  width: 100%;
  border-collapse: collapse;
}

.member-table th,
.member-table td {
  border: 1px solid #ccc;
  padding: 6px;
  font-size: 14px;
  text-align: center;
}

.buttons {
  display: flex;
  justify-content: space-around;
  margin-top: 10px;
}

.content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.breadcrumb {
  font-size: 12px;
  margin-bottom: 8px;
  color: #555;
}

.table-wrapper {
  max-height: 300px;
  overflow-y: auto;
  background: white;
  border: 1px solid #ccc;
}

.custom-table {
  width: 100%;
  border-collapse: collapse;
}

.custom-table th,
.custom-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: center;
}

.action-icon {
  width: 20px;
  height: 20px;
  margin: 0 6px;
  cursor: pointer;
  transition: transform 0.2s ease, filter 0.2s ease;
}

.action-icon:hover {
  transform: scale(1.2);
  filter: brightness(1.2);
}

:deep(.el-pagination) {
  align-self: flex-end;
  margin-top: 20px;
}
</style>
