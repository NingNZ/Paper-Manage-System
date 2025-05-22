<template>
  <div class="container">
    <!-- 顶部导航栏 -->
    <header class="navbar">
      <div class="logo">
        <img src="../assets/logo.svg" alt="logo" class="logo-icon" />
        <span>Paper<br>Manage</span>
      </div>
      <nav class="menu">
        <a href="#">首页</a>
        <a href="#">我的团队</a>
        <a href="#">我的论文</a>
        <a href="#">我的消息</a>
      </nav>
      <div class="user-icon">
        <img src="https://img.icons8.com/ios-filled/50/user-male-circle.png" alt="user" />
      </div>
    </header>

    <!-- 页面主体 -->
    <main class="main-wrapper">
      <!-- 左侧菜单 -->
      <aside class="sidebar">
        <div class="menu-group">
          <div
            class="menu-item"
            :class="{ active: activeSidebar === '我收到的消息' }"
            @click="setActive('我收到的消息')"
          >
            我收到的消息
          </div>
          <div
            class="menu-item"
            :class="{ active: activeSidebar === '待处理的通知' }"
            @click="setActive('待处理的通知')"
          >
            待处理的通知
          </div>
        </div>
      </aside>

      <!-- 右侧内容 -->
      <section class="content">
        <div class="breadcrumb">首页 &gt; 我的消息 &gt; {{ activeSidebar }}</div>

        <!-- 表格容器，添加滚动条 -->
        <div class="table-wrapper">
          <table class="custom-table">
            <thead>
              <tr>
                <th>论文标题</th>
                <th>作者</th>
                <th>团队名</th>
                <th>刊物</th>
                <th>时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in papers" :key="index">
                <td>{{ item.title }}</td>
                <td>{{ item.authors }}</td>
                <td>{{ item.team }}</td>
                <td>{{ item.journal }}</td>
                <td>{{ item.date }}</td>
                <td class="actions">
                  <el-button size="small" type="info" plain>下载</el-button>
                  <el-button size="small" type="success" plain>通过</el-button>
                  <el-button size="small" type="danger" plain>拒绝</el-button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 分页器 -->
        <el-pagination
          background
          layout="prev, pager, next, sizes, jumper"
          :total="50"
          :page-size="5"
          :page-sizes="[5, 10, 20]"
          style="margin-top: 20px"
        />
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activeSidebar = ref('待处理的通知')

const setActive = (item) => {
  activeSidebar.value = item
}

const papers = [
  {
    title: "Hello's life",
    authors: "Linda, Jack",
    team: "AI科研小组",
    journal: "IEEE",
    date: "2025-02"
  },
  {
    title: "Moore's raw",
    authors: "Jason, Alice",
    team: "CS科研小组",
    journal: "IEEE",
    date: "2025-03"
  },
  {
    title: "Graph Theory",
    authors: "Alice, Jky",
    team: "Web小队",
    journal: "IEEE",
    date: "2025-03"
  },
  {
    title: "Algebra",
    authors: "Mock, Chicaco",
    team: "机器学习研究",
    journal: "IEEE",
    date: "2025-04"
  },
  {
    title: "Set Theory",
    authors: "Jack",
    team: "算法设计",
    journal: "IEEE",
    date: "2025-05"
  },
  {
    title: "Extra Paper",
    authors: "John Doe",
    team: "New Lab",
    journal: "Nature",
    date: "2025-06"
  },
  {
    title: "Another Paper",
    authors: "Jane Doe",
    team: "Deep Lab",
    journal: "Science",
    date: "2025-07"
  }
]
</script>

<style scoped>
/* 通用设置 */
.container {
  min-height: 100vh;
  background-color: #f4f4f4;
  display: flex;
  flex-direction: column;
}

/* 顶部导航 */
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
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  color: #FFFFFF;
}

.menu a {
  color: white;
  margin: 0 1rem;
  text-decoration: none;
  padding: 12px 16px;
  border-radius: 4px;
  transition: background 0.3s ease, transform 0.2s ease;
  display: inline-block;
}

.menu a:hover {
  background: linear-gradient(45deg, #6fb1fc, #1c92f2);
  transform: scale(1.05);
}

.user-icon img {
  width: 40px;
  height: 40px;
}

/* 页面布局 */
.main-wrapper {
  display: flex;
  flex: 1;
}

/* 左侧菜单栏 */
.sidebar {
  width: 180px;
  background-color: #e8f4ff;
  padding-top: 20px;
}

.menu-group {
  display: flex;
  flex-direction: column;
}

.menu-item {
  padding: 10px 20px;
  cursor: pointer;
  color: #3398ff;
  transition: background 0.3s ease, transform 0.2s ease;
}

.menu-item:hover {
  background-color: #dbefff;
  transform: translateX(4px);
}

.menu-item.active {
  background-color: white;
  border-left: 4px solid #3398ff;
  font-weight: bold;
}

/* 右侧内容区 */
.content {
  flex: 1;
  padding: 20px;
}

.breadcrumb {
  font-size: 12px;
  color: #666;
  margin-bottom: 10px;
}

/* 表格样式 */
.table-wrapper {
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid #ccc;
  background: white;
}

.custom-table {
  width: 100%;
  border-collapse: collapse;
}

.custom-table th,
.custom-table td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: center;
}

.actions :deep(.el-button) {
  margin: 0 2px;
  transition: all 0.3s ease;
}

.actions :deep(.el-button:hover) {
  transform: scale(1.1);
  filter: brightness(1.1);
}
</style>
