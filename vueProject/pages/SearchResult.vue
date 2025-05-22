<script setup>
  import bar from "../components/bar.vue";
</script>

<template>
  <div class="container">
    <bar></bar>
    <main class="main-content">
      <div class="breadcrumb">首页&gt;</div>
      <h1 class="title">Milk&nbsp;&nbsp;Dragon</h1>

      <!-- 搜索区域 -->
      <div class="search-bar">
        <select>
          <option>关键词</option>
        </select>
        <input type="text" placeholder="请输入查找内容" />
        <button>搜索</button>
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
              <td><i>Title of paper {{ i }}</i></td>
              <td><span style="color: #666;">author A,B,C...</span></td>
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
  padding-top: 50px;
}

.breadcrumb {
  text-align: left;
  margin-left: 50px;
  font-size: 12px;
  color: #888;
}

.title {
  font-size: 48px;
  font-family: "Georgia", serif;
  color: #ccc;
  margin: 30px 0;
}

/* 搜索栏 */
.search-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
  margin-bottom: 20px;
}

.search-bar select {
  padding: 6px 10px;
  border: 1px solid black;
  border-radius: 4px;
}

.search-bar input {
  width: 300px;
  padding: 6px 12px;
  border: 1px solid black;
  border-radius: 20px 0 0 20px;
  outline: none;
}

.search-bar button {
  background-color: #3398ff;
  color: white;
  border: 1px solid black;
  padding: 6px 15px;
  border-radius: 0 20px 20px 0;
  cursor: pointer;
  font-weight: bold;
}

/* 表格容器 */
.table-container {
  width: 90%;
  max-width: 1000px;
  height: 300px;
  margin: 20px auto;
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

thead {
  background-color: #f0f0f0;
}

th, td {
  padding: 10px;
  border-bottom: 1px solid #eee;
  text-align: center;
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
