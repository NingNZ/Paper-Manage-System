<!-- 我的消息界面 -->
<template>
  <div class="container">
    <!-- 顶部导航栏 -->
    <bar></bar>

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
        <div class="breadcrumb" style="padding-bottom: 1%;"><a href="/">首页</a>&gt; <a href="/notice">我的消息</a> &gt; {{ activeSidebar }}</div>
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
              <tr v-for="(item, index) in pagedPapers" :key="index">
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
          :total="papers.length"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
          :page-sizes="[5, 10, 20]"
          class="pagination-right"
        />
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import bar from '../components/bar.vue'

const activeSidebar = ref('待处理的通知')
const setActive = (item) => {
  activeSidebar.value = item
}

const currentPage = ref(1)
const pageSize = ref(5)

const papers = [
  { title: "Hello's life", authors: "Linda, Jack", team: "AI科研小组", journal: "IEEE", date: "2025-02" },
  { title: "Moore's raw", authors: "Jason, Alice", team: "CS科研小组", journal: "IEEE", date: "2025-03" },
  { title: "Graph Theory", authors: "Alice, Jky", team: "Web小队", journal: "IEEE", date: "2025-03" },
  { title: "Algebra", authors: "Mock, Chicaco", team: "机器学习研究", journal: "IEEE", date: "2025-04" },
  { title: "Set Theory", authors: "Jack", team: "算法设计", journal: "IEEE", date: "2025-05" },
  { title: "Extra Paper", authors: "John Doe", team: "New Lab", journal: "Nature", date: "2025-06" },
  { title: "Another Paper", authors: "Jane Doe", team: "Deep Lab", journal: "Science", date: "2025-07" },
  { title: "Big Data", authors: "Zhang Wei", team: "数据科学", journal: "IEEE", date: "2025-07" },
  { title: "Quantum Net", authors: "Li Hua", team: "量子计算", journal: "Nature", date: "2025-08" },
  { title: "Neural Graph", authors: "Tom, Jerry", team: "AI Lab", journal: "IEEE", date: "2025-08" },
  { title: "Knowledge Mining", authors: "Emma", team: "语义小组", journal: "ACM", date: "2025-09" },
  { title: "Multimodal", authors: "Liu Qiang", team: "认知组", journal: "Science", date: "2025-09" },
  { title: "GAN Vision", authors: "Sam", team: "生成网络", journal: "CVPR", date: "2025-10" },
  { title: "Text Encoder", authors: "Jin", team: "自然语言", journal: "ACL", date: "2025-10" },
  { title: "MLP & GNN", authors: "Chen", team: "神经网络", journal: "ICLR", date: "2025-11" },
  { title: "Hybrid Systems", authors: "Lee", team: "控制组", journal: "CDC", date: "2025-11" },
  { title: "Vision Transformers", authors: "Kim", team: "视觉小组", journal: "NeurIPS", date: "2025-11" },
  { title: "Prompt Tuning", authors: "Zhao", team: "大模型组", journal: "EMNLP", date: "2025-12" },
  { title: "Meta Learning", authors: "Wang", team: "迁移学习", journal: "AAAI", date: "2025-12" },
  { title: "Low-rank Adaptation", authors: "Zhang", team: "LoRA", journal: "ICML", date: "2025-12" }
]

const pagedPapers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return papers.slice(start, end)
})

const handlePageChange = (page) => {
  currentPage.value = page
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}
</script>

<style scoped>
/* 通用设置 */
.container {
  min-height: 100vh;
  background-color: #f4f4f4;
  display: flex;
  flex-direction: column;
}

.main-wrapper {
  display: flex;
  flex: 1;
}

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

.content {
  flex: 1;
  padding: 20px;
  padding-top: 0;
  display: flex;
  flex-direction: column;
  position: relative;
}
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

/* 分页器靠右 */
:deep(.el-pagination) {
  align-self: flex-end;
  margin-top: 20px;
}
</style>

