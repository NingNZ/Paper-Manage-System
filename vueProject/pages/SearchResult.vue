<script setup>
import bar from "../components/bar.vue";
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
              <th>#</th>
              <th>论文标题</th>
              <th>作者</th>
              <th>时间</th>
              <th>期刊</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="i in 10" :key="i">
              <td>#{{ i }}</td>
              <td><i>Short title {{ i }}</i></td>
              <td><span style="color: #666;">Author A</span></td>
              <td>2025-03</td>
              <td><b>IEEE</b></td>
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
        <span>每页显示 <b>10</b> 条记录</span>
        <div class="pages">
          <button>1</button>
          <button>2</button>
          <button>3</button>
          <button>4</button>
        </div>
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
.container {
  min-height: 100vh;
  background-color: #f4f4f4;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  text-align: center;
  padding-top: 10px; /* 向上移动页面内容，原为20px */
}

.breadcrumb {
  text-align: left;
  margin-left: 50px;
  font-size: 12px;
  color: #888;
  margin-bottom: 10px;
}

/* 搜索外层容器 */
.search-wrapper {
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 10px;
  margin-top: -10px; /* 向上微调搜索框位置 */
}

/* 搜索栏 */
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

/* 表格容器 */
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

/* 表格样式 */
table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
  table-layout: fixed;
}

thead {
  background-color: #f0f0f0;
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

/* 图标大小和交互效果 */
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
  margin: 10px auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.pages button {
  margin: 0 3px;
  padding: 5px 10px;
  border: 1px solid #3398ff;
  background-color: white;
  color: #3398ff;
  border-radius: 4px;
  cursor: pointer;
}

.pages button:hover {
  background-color: #3398ff;
  color: white;
}
</style>
