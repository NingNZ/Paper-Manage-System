<template>
  <div class="container">
    <!-- 顶部导航栏 -->
    <bar></bar>
    <div class="breadcrumb">
      <a href="/">首页</a> &gt; <a href="/myteam">我的团队</a> &gt; <a href="/other">团队xxx</a>
    </div>

    <!-- 页面主体 -->
    <main class="main-wrapper">
      <!-- 左侧表格 -->
      <aside class="left-table">
        <h3>团队成员</h3>
        <div class="member-table-container">
          <table class="member-table">
            <thead>
              <tr>
                <th>用户ID</th>
                <th>用户名</th>
                <th>成员角色</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(member, index) in members" :key="index" :class="index % 2 === 0 ? 'even-row' : 'odd-row'">
                <td>{{ member.id }}</td>
                <td>{{ member.name }}</td>
                <td>{{ member.role }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="buttons">
          <el-button type="primary" size="small" @click="openInviteDialog">邀请成员</el-button>
          <el-button type="info" size="small" @click="showCategoryManager = true">管理分类</el-button>
          <el-button type="success" size="small" @click="openUploadDialog">上传论文</el-button>
        </div>
      </aside>

      <!-- 右侧内容 -->
      <section class="content">
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
              <tr v-for="(paper, index) in pagedPapers" :key="index" :class="index % 2 === 0 ? 'even-row' : 'odd-row'">
                <td>#{{ (currentPage - 1) * pageSize + index + 1 }}</td>
                <td><em>{{ paper.title }}</em></td>
                <td>{{ paper.category }}</td>
                <td>{{ paper.uploader }}</td>
                <td class="actions">
                  <img src="../assets/download.svg" alt="下载" class="action-icon" />
                  <img src="../assets/edit.svg" alt="编辑" class="action-icon" @click="handleEdit(paper)" />
                  <img src="../assets/delete.svg" alt="删除" class="action-icon" @click="handleDelete(paper)" />
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
          :page-sizes="[10, 20, 50]"
          :current-page="currentPage"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
          class="pagination-right"
        />
      </section>
    </main>

    <!-- 管理分类弹窗 -->
    <el-dialog v-model="showCategoryManager" title="管理分类" width="600px" destroy-on-close :close-on-click-modal="false">
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

    <!-- 上传论文弹窗（组件化后） -->
    <UploadDialog v-model="showUploadDialog" :categories="categories" @upload="handleUpload" />

    <!-- 编辑弹窗 -->
    <EditDialog v-model="showEditDialog" :item="currentEditItem" @confirm="confirmEdit" @close="showEditDialog = false" />

    <!-- 删除弹窗 -->
    <DeleteDialog v-model="showDeleteDialog" :item="currentDeleteItem" @confirm="confirmDelete" @close="showDeleteDialog = false" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import bar from '../components/bar.vue'
import CategoryManager from '../components/ManageTeamArticle/CategoryManagerDialog.vue'
import InviteMemberDialog from '../components/ManageTeamArticle/InviteMemberDialog.vue'
import EditDialog from "../components/ManageTeamArticle/EditDialog.vue"
import DeleteDialog from "../components/ManageTeamArticle/DeleteDialog.vue"
import UploadDialog from "../components/ManageTeamArticle/UploadDialog.vue"

const members = [
  { id: 'uid01', name: 'Anda', role: '组长' },
  { id: 'uid02', name: 'Lily', role: '组员' },
  { id: 'uid03', name: 'Lily', role: '组员' },
  { id: 'uid04', name: 'Lily', role: '组员' },
  { id: 'uid05', name: 'Lily', role: '组员' },
  { id: 'uid06', name: 'Lily', role: '组员' },
  { id: 'uid07', name: 'Lily', role: '组员' },
  { id: 'uid08', name: 'Lily', role: '组员' }
]

const categories = ref(['AI', '机器学习', '自然语言处理', '计算机视觉'])
const papers = ref(Array.from({ length: 20 }, (_, i) => ({
  id: i + 1,
  title: `Title of paper ${i + 1}`,
  category: 'AI',
  uploader: 'xxx'
})))

const currentPage = ref(1)
const pageSize = ref(10)
const showCategoryManager = ref(false)
const inviteDialogRef = ref(null)
const showUploadDialog = ref(false)
const showEditDialog = ref(false)
const showDeleteDialog = ref(false)

const currentEditItem = ref(null)
const currentDeleteItem = ref(null)

const pagedPapers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return papers.value.slice(start, start + pageSize.value)
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

function openUploadDialog() {
  showUploadDialog.value = true
}

function handleUpload(newPaper) {
  papers.value.push({
    id: papers.value.length + 1,
    title: newPaper.title,
    category: newPaper.category,
    uploader: '当前用户'
  })
  ElMessage.success('上传成功！')
  showUploadDialog.value = false
}

function handleEdit(item) {
  currentEditItem.value = { ...item }
  showEditDialog.value = true
}

function confirmEdit(newCategory) {
  const index = papers.value.findIndex(p => p.id === currentEditItem.value.id)
  if (index !== -1) {
    papers.value[index].category = newCategory
  }
  showEditDialog.value = false
}

function handleDelete(item) {
  currentDeleteItem.value = item
  showDeleteDialog.value = true
}

function confirmDelete() {
  papers.value = papers.value.filter(p => p.id !== currentDeleteItem.value.id)
  showDeleteDialog.value = false
}
</script>

<style scoped>
/* 样式完全沿用你的，无需改动 */
.even-row { background-color: #ffffff; }
.odd-row { background-color: #f5f5f5; }

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
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 12px;
}

.member-table {
  width: 100%;
  border-spacing: 0;
  margin: 0;
}

.member-table thead th {
  position: sticky;
  top: 0;
  background-color: #f0f0f0;
  z-index: 1;
}

.member-table th,
.member-table td {
  border: 0px solid #ccc;
  padding: 6px;
  font-size: 14px;
  text-align: center;
}

.member-table-container {
  flex: 1;
  overflow-y: auto;
  max-height: 300px;
  margin-top: 10px;
  border: 1px solid #ccc;
  border-radius: 12px;
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
  max-height: 500px;
  overflow-y: auto;
  background: white;
  border: 1px solid #ccc;
  padding: 0;
  border-radius: 12px;
}

.custom-table {
  width: 100%;
  border-spacing: 0;
  margin: 0;
}

.custom-table th {
  position: sticky;
  top: 0;
  background-color: #f0f0f0;
  z-index: 1;
}

.custom-table th,
.custom-table td {
  border: 0px solid #ccc;
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
