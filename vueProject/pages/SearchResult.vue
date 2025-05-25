<script setup>
import bar from "../components/bar.vue";
import { ref, computed } from 'vue';

// 模拟全部论文数据
const allData = ref(Array.from({ length: 100 }, (_, i) => ({
  id: i + 1,
  title: `Short title ${i + 1}`,
  author: 'Author A',
  time: '2025-03',
  journal: 'IEEE'
})))

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(allData.value.length)

const handlePageChange = (page) => {
  currentPage.value = page
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1 // 切换每页条数时回到第一页
}

// 当前页展示的数据
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return allData.value.slice(start, start + pageSize.value)
})
</script>

<template>
  <div class="container">
    <bar></bar>
    <main class="main-content">
      <div class="breadcrumb">首页&gt;</div>

      <!-- 搜索区域 -->
      <div class="search-wrapper">
        <div class="search-bar">
          <select>
            <option>关键词</option>
          </select>
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
                <img src="../assets/download.svg" alt="下载" title="下载" class="icon-action" />
                &nbsp;
                <img src="../assets/edit.svg" alt="编辑" title="编辑" class="icon-action" />
                &nbsp;
                <img src="../assets/delete.svg" alt="删除" title="删除" class="icon-action" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 分页器 -->
      <div class="pagination">
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
  </div>
</template>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: Arial, sans-serif;
}
</style>

<style scoped>
.result-item {
  padding: 16px;
  border-bottom: 1px solid #ddd;
}
.even-row {
  background-color: #ffffff;
}
.odd-row {
  background-color: #f5f5f5;
}
.result-item:hover {
  background-color: #e0f7fa;
}
.container {
  min-height: 100vh;
  background-color: #f4f4f4;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  text-align: center;
  padding-top: 10px;
}

.breadcrumb {
  text-align: left;
  margin-left: 50px;
  font-size: 12px;
  color: #888;
  margin-bottom: 10px;
}

.search-wrapper {
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 10px;
  margin-top: -10px;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-bar select {
  padding: 6px 12px;
  border: 1px solid black;
  border-radius: 4px;
}

.search-bar input {
  width: 500px;
  padding: 6px 12px;
  border: 1px solid black;
  border-radius: 20px 0 0 20px;
  outline: none;
}

.search-bar button {
  background-color: #3398ff;
  color: white;
  border: 1px solid black;
  padding: 6px 18px;
  border-radius: 0 20px 20px 0;
  cursor: pointer;
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
  table-layout: fixed;
}

thead {
  background-color: #f0f0f0;
}

th {
  position: sticky;
  top: 0;
  background-color: #f0f0f0;
  z-index: 2; /* 确保不被遮挡 */
}


th, td {
  height: 40px;
  padding: 0 10px;
  border-bottom: 1px solid #eee;
  text-align: center;
  word-break: break-word;
  white-space: normal;
  vertical-align: middle;
}

.icon-action {
  height: 1em;
  cursor: pointer;
  transition: transform 0.2s ease, filter 0.2s ease;
}

.icon-action:hover {
  transform: scale(1.2);
  filter: brightness(1.2);
}

/* 分页器样式 */
.pagination {
  width: 90%;
  max-width: 1000px;
  margin: 20px auto;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-pagination) {
  background: #f4f4f4; /* 与 .container 背景色一致 */
  padding: 12px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

:deep(.el-pagination.is-background .btn-prev),
:deep(.el-pagination.is-background .btn-next),
:deep(.el-pagination.is-background .el-pager li) {
  border: 1px solid #ddd;
  border-radius: 4px;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: #3398ff;
  color: white;
}

:deep(.el-pagination) {
  align-self: flex-end;
  margin-top: 20px;
  background: transparent !important;
  box-shadow: none !important;
  border: none !important;
  padding: 0 !important;
}
</style>