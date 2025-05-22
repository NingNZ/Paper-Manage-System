<template>
  <div class="container">
    <!-- 顶部导航栏 -->
     <bar></bar>

    <!-- 中部搜索区域 -->
    <div class="search-wrapper">
      <h1 class="title">Milk&nbsp;&nbsp;Dragon</h1>
      <div class="search-box">
        <el-select
          placeholder="关键词"
          class="keyword-select bordered-black"
          size="large"
        >
          <el-option label="标题" value="title" />
          <el-option label="作者" value="author" />
          <el-option label="期刊" value="journal" />
        </el-select>
        <el-input
          placeholder="请输入查找内容"
          class="search-input bordered-black"
          size="large"
        />
        <el-button
          type="primary"
          class="search-button bordered-black"
          size="large"
        >
          <el-icon style="margin-right: 4px;"><i class="el-icon-search" /></el-icon>搜索
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>

import bar from '../components/bar.vue';


// 无逻辑代码
</script>

<style scoped>
/* 基本容器 */
.container {
  min-height: 100vh;
  background-color: #f4f4f4;
  display: flex;
  flex-direction: column;
}
/* 中部搜索区域 */
.search-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding-top: 60px;
  text-align: center;
}

.title {
  font-size: 64px;
  font-weight: 300;
  color: #aaa;
  margin-bottom: 40px;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 600px;
  max-width: 90%;
  justify-content: center;
}

/* 黑色边框统一样式 */
.bordered-black :deep(.el-input__wrapper),
.bordered-black :deep(.el-button),
.bordered-black :deep(.el-select .el-input__wrapper) {
  border: 1px solid black !important;
  border-radius: 4px;
}

/* 控件宽度设置 */
.keyword-select {
  width: 120px;
}

.search-input {
  flex: 1;
}

.search-button {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 20px;
  font-weight: bold;
}
</style>
