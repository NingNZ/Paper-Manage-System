<template>
  <div class="container">
    <!-- 顶部导航栏 -->
    <header class="navbar">
      <div class="logo">
        <img src="../assets/logo.svg" alt="logo" class="logo-icon" />
        <span>Paper<br />Manage</span>
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
      <!-- 左侧表格 -->
      <aside class="left-table">
        <h3>团队成员</h3>
        <table class="member-table">
          <thead>
            <tr>
              <th>用户ID</th>
              <th>用户名</th>
              <th>成员角色</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(member, index) in members" :key="index">
              <td>{{ member.id }}</td>
              <td>{{ member.name }}</td>
              <td>{{ member.role }}</td>
            </tr>
          </tbody>
        </table>
        <div class="buttons">
          <el-button type="primary" size="small">邀请成员</el-button>
          <el-button type="info" size="small">管理分类</el-button>
        </div>
      </aside>

      <!-- 右侧内容 -->
      <section class="content">
        <div class="breadcrumb">首页 &gt; 我的团队 &gt; 团队xxx</div>

        <div class="table-wrapper">
          <table class="custom-table">
            <thead>
              <tr>
                <th>序号</th>
                <th>参考论文标题</th>
                <th>类别</th>
                <th>上传者</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(paper, index) in papers" :key="index">
                <td>#{{ index + 1 }}</td>
                <td><em>{{ paper.title }}</em></td>
                <td>{{ paper.category }}</td>
                <td>{{ paper.uploader }}</td>
                <td class="actions">
                  <img src="@/assets/download.svg" alt="下载" class="action-icon" />
                  <img src="@/assets/edit.svg" alt="编辑" class="action-icon" />
                  <img src="@/assets/delete.svg" alt="删除" class="action-icon" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <el-pagination
          background
          layout="prev, pager, next"
          :total="20"
          :page-size="6"
          style="margin-top: 10px"
        />
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElButton, ElPagination } from 'element-plus'

const members = [
  { id: 'uid01', name: 'Anda', role: '组长' },
  { id: 'uid02', name: 'Lily', role: '组员' },
  { id: 'uid03', name: 'Lily', role: '组员' },
  { id: 'uid04', name: 'Lily', role: '组员' },
  { id: 'uid05', name: 'Lily', role: '组员' },
  { id: 'uid06', name: 'Lily', role: '组员' }
]

const papers = [
  { title: 'Title of paper 1', category: 'AI', uploader: 'xxx' },
  { title: 'Title of paper 2', category: 'AI', uploader: 'xxx' },
  { title: 'Title of paper 3', category: 'AI', uploader: 'xxx' },
  { title: 'Title of paper 4', category: 'AI', uploader: 'xxx' },
  { title: 'Title of paper 5', category: 'AI', uploader: 'xxx' },
  { title: 'Title of paper 6', category: 'AI', uploader: 'xxx' }
]
</script>

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
  display: flex;
  align-items: center;
  gap: 8px;
}
.logo-icon {
  width: 32px;
  height: 32px;
}
.menu a {
  color: white;
  margin: 0 1rem;
  text-decoration: none;
  padding: 12px 16px;
  border-radius: 4px;
  transition: background 0.3s ease, transform 0.2s ease;
}
.menu a:hover {
  background: linear-gradient(45deg, #6fb1fc, #1c92f2);
  transform: scale(1.05);
}
.user-icon img {
  width: 40px;
  height: 40px;
}

.main-wrapper {
  display: flex;
  flex: 1;
  padding: 20px;
  gap: 20px;
}

/* 左侧成员表 */
.left-table {
  width: 260px;
  background: white;
  border: 1px solid #ddd;
  padding: 10px;
  max-height: 400px;
  overflow-y: auto;
}
.member-table {
  width: 100%;
  border-collapse: collapse;
}
.member-table th,
.member-table td {
  border: 1px solid #ccc;
  padding: 6px;
  font-size: 14px;
  text-align: center;
}
.buttons {
  display: flex;
  justify-content: space-around;
  margin-top: 10px;
}

/* 右侧内容 */
.content {
  flex: 1;
}
.breadcrumb {
  font-size: 12px;
  margin-bottom: 8px;
  color: #555;
}
.table-wrapper {
  max-height: 300px;
  overflow-y: auto;
  background: white;
  border: 1px solid #ccc;
}
.custom-table {
  width: 100%;
  border-collapse: collapse;
}
.custom-table th,
.custom-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: center;
}

/* 操作图标样式 */
.action-icon {
  width: 20px;
  height: 20px;
  margin: 0 6px;
  cursor: pointer;
  transition: transform 0.2s ease, filter 0.2s ease;
}
.action-icon:hover {
  transform: scale(1.2);
  filter: brightness(1.2);
}
</style>
