<template>
  <div class="container">
    <bar />

    <main class="main-wrapper">
      <!-- 左侧导航栏 -->
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

      <!-- 内容区域 -->
      <section class="content">
        <div class="breadcrumb">
          <a href="/">首页</a> &gt; <a href="/notice">我的消息</a> &gt; {{ activeSidebar }}
        </div>

        <div class="table-wrapper">
          <table class="custom-table">
            <thead>
              <tr v-if="activeSidebar === '待处理的通知'">
                <th class="message-col">消息</th>
                <th class="time-col">时间</th>
                <th class="status-col">状态</th>
                <th class="action-col">操作</th>
              </tr>
              <tr v-else>
                <th class="message-col">消息</th>
                <th class="time-col">时间</th>
                <th class="status-col">处理结果</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in pagedItems" :key="index" :class="index % 2 === 0 ? 'even-row' : 'odd-row'">
                <template v-if="activeSidebar === '待处理的通知'">
                  <td class="message-col">{{ item.message }}</td>
                  <td class="time-col">{{ formatDate(item.time) }}</td>
                  <td class="status-col">
                    <span :class="statusClass(item.status)">{{ item.status }}</span>
                  </td>
                  <td class="action-col">
                    <el-button size="small" type="success" plain>通过</el-button>
                    <el-button size="small" type="danger" plain>拒绝</el-button>
                  </td>
                </template>
                <template v-else>
                  <td class="message-col">{{ item.message }}</td>
                  <td class="time-col">{{ formatDate(item.time) }}</td>
                  <td class="status-col">
                    <span :class="resultClass(item.result)">{{ item.result }}</span>
                  </td>
                </template>
              </tr>
            </tbody>
          </table>
        </div>

        <el-pagination
          background
          layout="prev, pager, next, sizes, jumper"
          :total="activeSidebar === '待处理的通知' ? noticeList.length : receivedList.length"
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
const currentPage = ref(1)
const pageSize = ref(5)

const setActive = (val) => {
  activeSidebar.value = val
  currentPage.value = 1
}

// 模拟数据
const noticeList = [
  { message: '《AI研究进展》申请加入您的团队“AI研究小组”', time: '2025-06-01 10:30', status: '未处理' },
  { message: '《图神经网络》申请加入您的团队“图智能”', time: '2025-06-02 11:00', status: '已通过' },
  { message: '《Transformer结构优化》协作请求待处理', time: '2025-06-03 14:45', status: '已拒绝' },
  { message: '《神经网络安全性分析》加入请求', time: '2025-06-04 16:20', status: '未处理' },
  { message: '《大模型压缩》希望参与您的研究团队', time: '2025-06-05 09:50', status: '已拒绝' },
]

const receivedList = [
  { message: '您加入“AI研究小组”的请求已通过。', time: '2025-06-01 10:10', result: '通过' },
  { message: '您的论文《图神经网络》投稿未通过审核。', time: '2025-06-02 12:00', result: '拒绝' },
  { message: '“AI Lab”团队邀请您加入。', time: '2025-06-03 13:30', result: '通过' },
  { message: '您的请求被拒绝。', time: '2025-06-04 15:00', result: '拒绝' },
]

// 分页逻辑
const pagedItems = computed(() => {
  const list = activeSidebar.value === '待处理的通知' ? noticeList : receivedList
  const start = (currentPage.value - 1) * pageSize.value
  return list.slice(start, start + pageSize.value)
})

// 格式化时间为 YYYY-MM-DD
const formatDate = (datetime) => {
  return datetime.split(' ')[0]
}

// 状态样式绑定
const statusClass = (status) => {
  if (status === '未处理') return 'status-gray'
  if (status === '已通过') return 'status-green'
  if (status === '已拒绝') return 'status-red'
  return ''
}

const resultClass = (result) => {
  if (result === '通过') return 'status-green'
  if (result === '拒绝') return 'status-red'
  return ''
}

const handlePageChange = (page) => {
  currentPage.value = page
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}
</script>

<style scoped>
thead {
  background-color: #f0f0f0;
}

.even-row {
  background-color: #ffffff;
}
.odd-row {
  background-color: #f5f5f5;
}

.container {
  background: #f5f5f5;
  min-height: 100vh;
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
  padding: 12px 20px;
  cursor: pointer;
  color: #3398ff;
  transition: all 0.3s ease;
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
  display: flex;
  flex-direction: column;
}

.breadcrumb {
  padding-bottom: 10px;
  font-size: 14px;
  color: #666;
}

.custom-table thead th {
  position: sticky;
  top: 0;
  background-color: #f0f0f0;
  z-index: 1;
}

.table-wrapper {
  max-height: 500px;
  overflow-y: auto;
  background: white;
  border: 1px solid #ccc;
  padding: 0;
  border-radius: 12px;
}

.table-wrapper {
  background: white;
  border: 1px solid #ddd;
  overflow-x: auto;
}

.custom-table {
  width: 100%;
  border-collapse: collapse;
}

.custom-table th,
.custom-table td {
  border: 0px solid #ddd;
  padding: 10px;
  white-space: nowrap;
  text-align: center;
  font-size: 14px;
}

.message-col {
  width: 60%;
  text-align: left;
  padding-left: 12px;
}

.time-col {
  width: 18%;
}

.status-col {
  width: 12%;
}

.action-col {
  width: 10%;
}

:deep(.el-button) {
  margin: 0 4px;
}

.pagination-right {
  margin-top: 20px;
  align-self: flex-end;
}

/* 状态颜色样式 */
.status-gray {
  color: #888;
}

.status-green {
  color: #67c23a;
}

.status-red {
  color: #f56c6c;
}
</style>
