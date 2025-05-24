<!-- 主界面 -->
<script setup>
  import bar from "../components/bar.vue";
</script>

<template>
  <div class="container">
    <bar></bar>
    <main class="main-content">
      <div class="breadcrumb">首页&gt;</div>
      <div class="center-box">
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
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 50px;
}

.breadcrumb {
  width: 100%;
  max-width: 1200px;
  text-align: left;
  font-size: 12px;
  color: #888;
  margin-bottom: 20px;
}

.center-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  transform: scale(1.15); /* 稍微放大一点点 */
}

.title {
  font-size: 52px; /* 比之前略大 */
  font-family: "Georgia", serif;
  color: #ccc;
  margin: 36px 0;
}

.search-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.search-bar select {
  padding: 7px 11px;
  font-size: 16px;
}

.search-bar input {
  width: 340px; /* 略微增大 */
  padding: 7px 13px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 20px 0 0 20px;
  outline: none;
}

.search-bar button {
  background-color: #3398ff;
  color: white;
  border: none;
  padding: 7px 17px;
  font-size: 16px;
  border-radius: 0 20px 20px 0;
  cursor: pointer;
}
</style>
