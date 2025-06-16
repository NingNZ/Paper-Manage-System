<template>
  <div class="container">
    <!-- 顶部导航栏 -->
    <bar />

    <!-- 面包屑 + 顶部操作按钮 -->
    <div class="breadcrumb-buttons">
      <div class="breadcrumb">
        <a href="/">首页</a> &gt; <a href="/mypaper">我的论文</a>
      </div>
      <div class="top-controls">
        <el-select v-model="selectedJournalTemp" placeholder="筛选期刊" style="width: 160px" clearable>
          <el-option label="全部期刊" :value="''" />
          <el-option v-for="journal in uniqueJournals" :key="journal" :label="journal" :value="journal" />
        </el-select>
        <el-button type="primary" @click="handleFilter">查询</el-button>
      </div>
    </div>

    <main class="main-wrapper">
      <!-- 左侧工作量分数展示 -->
      <aside class="left-panel">
        <div class="score-card">
          <h3>工作量分数</h3>
          <div class="score">{{ workloadScore }}</div>
        </div>
      </aside>

      <!-- 论文列表 -->
      <section class="content">
        <div class="table-wrapper">
          <table class="custom-table">
            <thead>
              <tr>
                <th>序号</th>
                <th>论文标题</th>
                <th>类别</th>
                <th>期刊</th>
                <th>上传日期</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(paper, index) in pagedPapers"
                :key="index"
                :class="index % 2 === 0 ? 'even-row' : 'odd-row'"
              >
                <td>#{{ (currentPage - 1) * pageSize + index + 1 }}</td>
                <td><em>{{ paper.title }}</em></td>
                <td>{{ paper.category }}</td>
                <td>{{ paper.journal }}</td>
                <td>{{ paper.uploadDate }}</td>
                <td>
                  <img src="../assets/download.svg" alt="下载" class="action-icon" @click="handleDownload(paper)" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="bottom-controls">
          <el-button type="primary" @click="exportToExcel">导出为 Excel</el-button>
          <el-pagination
            background
            layout="prev, pager, next, sizes, jumper"
            :total="filteredPapers.length"
            :page-size="pageSize"
            :page-sizes="[10, 20, 50]"
            :current-page="currentPage"
            @current-change="handlePageChange"
            @size-change="handleSizeChange"
            class="pagination-right"
          />
        </div>
      </section>
    </main>

    <!-- 管理分类弹窗 -->
    <el-dialog
      v-model="showCategoryManager"
      title="管理分类"
      width="600px"
      destroy-on-close
      :close-on-click-modal="false"
    >
      <CategoryManager />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCategoryManager = false">取消</el-button>
          <el-button type="primary" @click="showCategoryManager = false">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElButton, ElPagination, ElDialog, ElMessage, ElSelect, ElOption } from 'element-plus'
import bar from '../components/bar.vue'
import CategoryManager from '../components/ManageTeamArticle/CategoryManagerDialog.vue'
import * as XLSX from 'xlsx'
import { saveAs } from 'file-saver'
import userPaperUtils from '../scripts/userPaper'

// 期刊和类别定义
const journalOptions = ['IEEE', 'ACM', 'Springer', 'Elsevier', 'Nature', 'Science']
const categories = ['AI', '系统设计', '数据挖掘', '人机交互']

// 论文数据生成，分布在不同期刊
const papers = ref([])

// 工作量分数直接展示
const workloadScore = ref(85.5)

// 筛选相关变量
const selectedJournalTemp = ref('')  // 临时选择值
const selectedJournal = ref('')      // 实际筛选条件
const currentPage = ref(1)
const pageSize = ref(10)
const showCategoryManager = ref(false)

const filteredPapers = computed(() =>
  selectedJournal.value
    ? papers.value.filter(p => p.journal === selectedJournal.value)
    : papers.value
)

const pagedPapers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredPapers.value.slice(start, start + pageSize.value)
})

const uniqueJournals = computed(() =>
  [...new Set(papers.value.map(p => p.journal))]
)

function handlePageChange(val) {
  currentPage.value = val
}

function handleSizeChange(val) {
  pageSize.value = val
  currentPage.value = 1
}

// 查询按钮逻辑
function handleFilter() {
  selectedJournal.value = selectedJournalTemp.value
  currentPage.value = 1
  ElMessage.success('筛选完成')
}

function handleDownload(paper) {
  //userPaperUtils.downloadUserPaper(paper.id)
}

function exportToExcel() {
  const exportData = filteredPapers.value.map((paper, index) => ({
    序号: index + 1,
    论文标题: paper.title,
    类别: paper.category,
    期刊: paper.journal,
    上传日期: paper.uploadDate
  }))
  const worksheet = XLSX.utils.json_to_sheet(exportData)
  const workbook = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(workbook, worksheet, '我的论文')
  const wbout = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' })
  saveAs(new Blob([wbout], { type: 'application/octet-stream' }), '我的论文列表.xlsx')
  ElMessage.success('Excel 导出成功')
}

// 获取当前用户ID（假设已存储在localStorage）
const userId = localStorage.getItem('userId')

// 获取我的论文列表
const fetchMyPapers = () => {
  userPaperUtils.getUserPaper(userId)
    .then(({ code, msg, data }) => {
      if (code === 200) {
        papers.value = data
      } else {
        ElMessage.error(msg)
      }
    })
    .catch(() => {
      ElMessage.error('获取论文失败')
    })
}

onMounted(() => {
  fetchMyPapers()
})
</script>

<style scoped>
.even-row {
  background-color: #ffffff;
}
.odd-row {
  background-color: #f5f5f5;
}

.container {
  min-height: 100vh;
  background-color: #f4f4f4;
  display: flex;
  flex-direction: column;
}

.main-wrapper {
  display: flex;
  flex: 1;
  padding: 20px; 
  gap: 20px;
}

.left-panel {
  width: 150px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 15px;
  height: fit-content;
}

.score-card {
  text-align: center;
}

.score-card h3 {
  margin-bottom: 8px;
  font-size: 16px;
  color: #333;
}

.score {
  font-size: 28px;
  font-weight: bold;
  color: #409EFF;
}


.content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.breadcrumb-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 30px 0;
}

.top-controls {
  display: flex;
  gap: 10px;
  align-items: center;
}

.breadcrumb {
  font-size: 12px;
  color: #555;
}

.table-wrapper {
  max-height: 500px;
  overflow-y: auto;
  background: white;
  border: 1px solid #ccc;
  padding: 0;
  border-radius: 12px;
}

.custom-table {
  width: 100%;
  border-spacing: 0;
  margin: 0;
}

.custom-table th {
  position: sticky;
  top: 0;
  background-color: #f0f0f0;
  z-index: 1;
}

.custom-table th,
.custom-table td {
  border: 0px solid #ccc;
  padding: 10px;
  text-align: center;
}

.bottom-controls {
  margin-top: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

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

:deep(.el-pagination) {
  margin-top: 10px;
}
</style>


