<!-- 主界面 -->
<script setup>
  import bar from "../components/bar.vue";
  import axios from 'axios'
  import { onMounted, ref } from "vue";
  import { ElMessage } from "element-plus";
  import { useRouter } from "vue-router";
  import { sessionUtil } from "../scripts/session";
  const route = useRouter()
  localStorage.setItem('isSearch',true)
  const selectedType = ref(0)
  const searchWord = ref("")
  if(localStorage.getItem('typeSave')){
    selectedType.value = localStorage.getItem('typeSave')
  }
  const search = ()=>{
    if(!searchWord.value.trim()){
      ElMessage.error("输入框不能为空")
    }
    else{
      route.push('/search')
      localStorage.setItem('typeSave',selectedType.value)
      localStorage.setItem('keySave',searchWord.value)
    }
  }
</script>

<template>
  <div class="container">
    <bar></bar>
    <div class="breadcrumb"><a href="/">首页</a>&gt;</div>
    <main class="main-content">
      <div class="center-box">
        <h1 class="title">Milk&nbsp;&nbsp;Dragon</h1>
        <div class="search-bar">
          <select v-model="selectedType">
            <option :value="0">关键字</option>
            <option :value="1">标题</option>
            <option :value="2">作者</option>
            <option :value="3">刊物</option>
            <option :value="4">类别</option>
          </select>
          <input type="text" v-model="searchWord" placeholder="请输入查找内容" />          
            <button @click="search">搜索</button>
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
