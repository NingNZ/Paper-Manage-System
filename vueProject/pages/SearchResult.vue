<script setup>
import bar from "../components/bar.vue";
import UploadDialog from "../components/UploadTest/UploadTestDialog.vue";
import EditDialog from "../components/SearchResult/EditDialog.vue";
import DeleteDialog from "../components/SearchResult/DeleteDialog.vue";
import CategoryManagerDialog from "../components/SearchResult/CategoryManagerDialog.vue";
import JournalManagerDialog from "../components/SearchResult/JournalManagerDialog.vue";
import { ref, computed } from "vue";

// 数据初始化
const allData = ref(Array.from({ length: 100 }, (_, i) => ({
  id: i + 1,
  title: `Short title ${i + 1}`,
  author: 'Author A',
  time: '2025-03',
  journal: 'IEEE',
  category: '默认分类'
})))

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(allData.value.length)

const handlePageChange = (page) => {
  currentPage.value = page
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return allData.value.slice(start, start + pageSize.value)
})

// 弹窗控制
const showUploadDialog = ref(false)
const showEditDialog = ref(false)
const showDeleteDialog = ref(false)
const showCategoryDialog = ref(false)
const showJournalDialog = ref(false)

const currentEditItem = ref(null)
const currentDeleteItem = ref(null)

const handleEdit = (item) => {
  currentEditItem.value = { ...item }
  showEditDialog.value = true
}

const updateCategory = (newCategory) => {
  const index = allData.value.findIndex(i => i.id === currentEditItem.value.id)
  if (index !== -1) {
    allData.value[index].category = newCategory
  }
  showEditDialog.value = false
}

const handleDelete = (item) => {
  currentDeleteItem.value = item
  showDeleteDialog.value = true
}

const confirmDelete = () => {
  allData.value = allData.value.filter(i => i.id !== currentDeleteItem.value.id)
  showDeleteDialog.value = false
}
</script>

<template>
  <div class="container">
    <bar />

    <div class="breadcrumb"><a href="/">首页</a> &gt; <a href="/search">查询结果</a></div>

    <div class="upload-button-wrapper">
      <button class="upload-btn" @click="showUploadDialog = true">上传论文</button>
    </div>

    <main class="main-content">
      <!-- 搜索栏 -->
      <div class="search-wrapper">
        <div class="search-bar">
          <select><option>关键词</option></select>
          <input type="text" placeholder="请输入查找内容" />
          <button>搜索</button>
        </div>
      </div>

      <!-- 表格区域 -->
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>序号</th>
              <th>论文标题</th>
              <th>作者</th>
              <th>时间</th>
              <th>期刊</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(item, index) in paginatedData"
              :key="item.id"
              :class="index % 2 === 0 ? 'even-row' : 'odd-row'"
            >
              <td>#{{ item.id }}</td>
              <td><i>{{ item.title }}</i></td>
              <td><span style="color: #666;">{{ item.author }}</span></td>
              <td>{{ item.time }}</td>
              <td><b>{{ item.journal }}</b></td>
              <td>
                <img src="../assets/download.svg" alt="下载" class="icon-action" />
                &nbsp;
                <img src="../assets/edit.svg" alt="编辑" class="icon-action" @click="handleEdit(item)" />
                &nbsp;
                <img src="../assets/delete.svg" alt="删除" class="icon-action" @click="handleDelete(item)" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>

  <div class="pagination">
    <div class="bottom-left-buttons">
      <button class="manage-btn" @click="showJournalDialog = true">管理期刊</button>
      <button class="manage-btn" @click="showCategoryDialog = true">管理分类</button>
    </div>

    <!--分页器-->
  <el-pagination
    background
    layout="prev, pager, next, sizes, jumper"
    :total="total"
    :page-size="pageSize"
    :current-page="currentPage"
    :page-sizes="[10, 20, 50]"
    @current-change="handlePageChange"
    @size-change="handleSizeChange"
  />
</div>

    </main>

    <!-- 左下角管理按钮 -->
    <!-- <div class="bottom-left-buttons">
      <button class="manage-btn" @click="showJournalDialog = true">管理期刊</button>
      <button class="manage-btn" @click="showCategoryDialog = true">管理分类</button>
    </div> -->

    <!-- 弹窗组件 -->
    <UploadDialog v-model:visible="showUploadDialog" />
    <EditDialog
      v-model="showEditDialog"
      :item="currentEditItem"
      @confirm="updateCategory"
      @close="showEditDialog = false"
    />
    <DeleteDialog
      v-model="showDeleteDialog"
      :item="currentDeleteItem"
      @confirm="confirmDelete"
      @close="showDeleteDialog = false"
    />
    <CategoryManagerDialog v-model:visible="showCategoryDialog" />
    <JournalManagerDialog v-model:visible="showJournalDialog" />
  </div>
</template>

<style scoped>
.container {
  min-height: 100vh;
  background-color: #f4f4f4;
  display: flex;
  flex-direction: column;
}
.breadcrumb {
  font-size: 12px;
  color: #888;
}
.upload-button-wrapper {
  margin: 6px 0 10px 20px;
}
.upload-btn {
  padding: 4px 12px;
  background-color: #3398ff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}
.main-content {
  flex: 1;
  text-align: center;
  padding-top: 10px;
}
.search-wrapper {
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 10px;
}
.search-bar {
  display: flex;
  align-items: center;
  gap: 8px;
}
.search-bar select,
.search-bar input,
.search-bar button {
  height: 32px;
}
.search-bar input {
  width: 500px;
  border-radius: 20px 0 0 20px;
  border: 1px solid black;
  padding: 0 12px;
}
.search-bar button {
  border-radius: 0 20px 20px 0;
  background-color: #3398ff;
  color: white;
  padding: 0 18px;
  border: none;
  font-weight: bold;
}
.table-container {
  width: 90%;
  max-width: 1000px;
  height: 440px;
  margin: 0 auto 20px;
  overflow-y: auto;
  border: 1px solid #ccc;
  background: white;
  border-radius: 6px;
}
table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}
th, td {
  height: 40px;
  padding: 0 10px;
  border-bottom: 1px solid #eee;
  text-align: center;
}
thead {
  background-color: #f0f0f0;
}
th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: #f0f0f0;
}
.icon-action {
  height: 1em;
  cursor: pointer;
  transition: transform 0.2s, filter 0.2s;
}
.icon-action:hover {
  transform: scale(1.2);
  filter: brightness(1.2);
}
.even-row {
  background: #fff;
}
.odd-row {
  background: #f5f5f5;
}
.pagination {
  width: 90%;
  max-width: 1000px;
  margin: 20px auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.bottom-left-buttons {
  display: flex;
  flex-direction: row;
  gap: 10px;
}
.manage-btn {
  padding: 6px 14px;
  background-color: #3398ff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 13px;
  cursor: pointer;
}
</style>
