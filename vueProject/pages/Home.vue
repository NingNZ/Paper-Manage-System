<!-- 主界面 -->
<script setup>
  import bar from "../components/bar.vue";
</script>
<template>
  <div class="container">
    <bar></bar>
    <main class="main-content">
      <div class="breadcrumb">首页&gt;</div>
      <h1 class="title">Milk&nbsp;&nbsp;Dragon</h1>
      <div class="search-bar">
        <select>
          <option>关键词</option>
        </select>
        <input type="text" placeholder="请输入查找内容" />
        <router-link to="/search">
          <button>搜索</button>
        </router-link>
      </div>
    </main>
  </div>
</template>

<style>
/* 全局基础样式 */
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
  padding-top: 50px;  /* 关键修改：margin-top改为padding-top */
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

.search-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
}

.search-bar select {
  padding: 6px 10px;
}

.search-bar input {
  width: 300px;
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-radius: 20px 0 0 20px;
  outline: none;
}

.search-bar button {
  background-color: #3398ff;
  color: white;
  border: none;
  padding: 6px 15px;
  border-radius: 0 20px 20px 0;
  cursor: pointer;
}
</style>

