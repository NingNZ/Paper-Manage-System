<template>
  <div class="container">
    <header class="navbar">
      <div class="logo">Paper<br>Manage</div>
      <nav class="menu">
        <a href="https://www.baidu.com">首页</a>
        <a href="#">我的团队</a>
        <a href="#">我的论文</a>
        <a href="#">我的消息</a>
      </nav>
      <div class="user-icon">
        <img src="https://img.icons8.com/ios-filled/50/user-male-circle.png" alt="user" />
      </div>
    </header>

    <main class="main-content">
      <div class="breadcrumb">首页&gt;</div>
      <h1 class="title">Milk&nbsp;&nbsp;Dragon</h1>
      <div class="search-bar">
        <select>
          <option>关键词</option>
        </select>
        <input type="text" placeholder="请输入查找内容" />
        <button>搜索</button>
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

.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #3398ff;
  color: white;
  padding: 0.5rem 2rem;
}

.logo {
  font-weight: bold;
  font-size: 1rem;
  line-height: 1.2;
}

.menu a {
  color: white;
  margin: 0 1rem;
  text-decoration: none;
}

.user-icon img {
  width: 32px;
  height: 32px;
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

