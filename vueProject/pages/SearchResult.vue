<script setup>
import bar from "../components/bar.vue";
import UploadDialog from "../components/UploadTest/UploadTestDialog.vue";
import EditDialog from "../components/SearchResult/EditDialog.vue";
import DeleteDialog from "../components/SearchResult/DeleteDialog.vue";
import { ref, computed, onMounted } from 'vue';
import { useRoute } from "vue-router";
import { ElMessage } from "element-plus";
import axios from 'axios'

const allData = ref([]);
const searchWord = ref("")
const selectedType = ref('0')
const route = useRoute()
var code = 0
var msg = ""

const setAllData = (data) =>{
    allData.value = Array.from({length:data.length},(_,i)=>({
    id:data[i].id,
    seq: i+1,
    title:data[i].title,
    author:data[i].authors,
    time:data[i].time,
    journal:data[i].journal,
    type:data[i].type
  }));
}
const sendAndGet=() =>{
    axios.get('http://localhost:5123/search', {
    params: {
      type: selectedType.value,
      key: searchWord.value
    },
    timeout:3000
  })
  .then((response) => {
    // 处理返回数据
    console.log("enter")
    console.log(response.data[0])
    code = response.data[0].code
    msg = response.data[0].msg
    if(code==200){
      ElMessage.success("搜索成功")
    }
    else if(code==201){
      ElMessage.info("数据库中不存在相关论文")
    }
    console.log(code)
    console.log(msg)
    setAllData(response.data.slice(1))
  })
  .catch(error => {
    // 处理错误
    ElMessage.error("服务器未连接")
    allData.value = []
    console.error(error)
  })
}
onMounted(()=>{
  if(localStorage.getItem('typeSave')){
    selectedType.value = localStorage.getItem('typeSave')
    searchWord.value = localStorage.getItem('keySave')
    sendAndGet()
  }else{
    selectedType.value='0'
    searchWord.value=[]
  }
})
const search = ()=>{
    if(!searchWord.value.trim()){
      ElMessage.error("输入框不能为空")
    }else{
      localStorage.setItem('typeSave',selectedType.value)
      localStorage.setItem('keySave',searchWord.value)
      sendAndGet()
    }
}
localStorage.setItem('isSearch',true)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(allData.value.length)

const handlePageChange = (page) => {
  currentPage.value = page
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1 // 切换每页条数时回到第一页
}

// 当前页展示的数据
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return allData.value.slice(start, start + pageSize.value)
})

// 上传弹窗
const showUploadDialog = ref(false)

// 编辑弹窗
const showEditDialog = ref(false)
const currentEditItem = ref(null)

const handleEdit = (item) => {
  currentEditItem.value = { ...item }
  showEditDialog.value = true
}

const updateCategory = (newCategory) => {
  const index = allData.value.findIndex(i => i.seq === currentEditItem.value.seq)
  if (index !== -1) {
    allData.value[index].category = newCategory
  }
  showEditDialog.value = false
}

// 删除弹窗
const showDeleteDialog = ref(false)
const currentDeleteItem = ref(null)

const handleDelete = (item) => {
  currentDeleteItem.value = item
  showDeleteDialog.value = true
}

const confirmDelete = () => {
  allData.value = allData.value.filter(i => i.seq !== currentDeleteItem.value.seq)
  showDeleteDialog.value = false
}
</script>

<template>
  <div class="container">
    <bar />

    <div class="breadcrumb"><a href="/">首页</a> &gt; <a href="/search">查询结果</a></div>

    <div class="upload-button-wrapper">
      <button class="upload-btn" @click="showUploadDialog = true">上传论文</button>
    </div>
    <main class="main-content">
      <!-- 搜索栏 -->
      <div class="search-wrapper">
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

      <!-- 表格区域 -->
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th v-if="false">id</th>
              <th>序号</th>
              <th>论文标题</th>
              <th>作者</th>
              <th>时间</th>
              <th>期刊</th>
              <th>类别</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(item, index) in paginatedData"
              :key="item.seq"
              :class="index % 2 === 0 ? 'even-row' : 'odd-row'"
            >
              <td v-if="false">{{ item.id }}</td>
              <td>#{{ item.seq }}</td>
              <td><i>{{ item.title }}</i></td>
              <td><span style="color: #666;">{{ item.author }}</span></td>
              <td>{{ item.time }}</td>
              <td><b>{{ item.journal }}</b></td>
              <td><b>{{ item.type }}</b></td>
              <td>
                <img src="../assets/download.svg" alt="下载" class="icon-action" />
                &nbsp;
                <img src="../assets/edit.svg" alt="编辑" class="icon-action" @click="handleEdit(item)" />
                &nbsp;
                <img src="../assets/delete.svg" alt="删除" class="icon-action" @click="handleDelete(item)" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 分页器 -->
  <div class="pagination">
    <div class="bottom-left-buttons">
      <button class="manage-btn" @click="showJournalDialog = true">管理期刊</button>
      <button class="manage-btn" @click="showCategoryDialog = true">管理分类</button>
    </div>

  <el-pagination
    background
    layout="prev, pager, next, sizes, jumper"
    :total="total"
    :page-size="pageSize"
    :current-page="currentPage"
    :page-sizes="[10, 20, 50]"
    @current-change="handlePageChange"
    @size-change="handleSizeChange"
  />
</div>

    </main>

    <!-- 上传弹窗组件 -->
    <UploadDialog v-model:visible="showUploadDialog" />
    <EditDialog
      v-model="showEditDialog"
      :item="currentEditItem"
      @confirm="updateCategory"
      @close="showEditDialog = false"
    />
    <DeleteDialog
      v-model="showDeleteDialog"
      :item="currentDeleteItem"
      @confirm="confirmDelete"
      @close="showDeleteDialog = false"
    />
    <EditDialog
      v-model="showEditDialog"
      :item="currentEditItem"
      @confirm="updateCategory"
      @close="showEditDialog = false"
    />
    <DeleteDialog
      v-model="showDeleteDialog"
      :item="currentDeleteItem"
      @confirm="confirmDelete"
      @close="showDeleteDialog = false"
    />
    <CategoryManagerDialog v-model:visible="showCategoryDialog" />
    <JournalManagerDialog v-model:visible="showJournalDialog" />
  </div>
</template>

<style scoped>
.result-item {
  padding: 16px;
  border-bottom: 1px solid #ddd;
}
.even-row {
  background-color: #ffffff;
}
.odd-row {
  background-color: #f5f5f5;
}
.result-item:hover {
  background-color: #e0f7fa;
}
.container {
  min-height: 100vh;
  background-color: #f4f4f4;
  display: flex;
  flex-direction: column;
}
.breadcrumb {
  font-size: 12px;
  color: #888;
}
.upload-button-wrapper {
  margin: 6px 0 10px 20px;
  margin: 6px 0 10px 20px;
}
.upload-btn {
  padding: 4px 12px;
  background-color: #3398ff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}
.main-content {
  flex: 1;
  text-align: center;
  padding-top: 10px;
}
.main-content {
  flex: 1;
  text-align: center;
  padding-top: 10px;
}
.search-wrapper {
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 10px;
}
.search-bar {
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-bar select {
  padding: 6px 12px;
  border: 1px solid black;
  border-radius: 4px;
}

.search-bar input {
  width: 500px;
  border-radius: 20px 0 0 20px;
  border: 1px solid #ccc;
  padding: 6px 12px;
}
.search-bar button {
  border-radius: 0 20px 20px 0;
  background-color: #3398ff;
  color: white;
  padding: 6px 18px;
  border: none;
  font-weight: bold;
  cursor: pointer;
}
.table-container {
  width: 90%;
  max-width: 1000px;
  height: 440px;
  margin: 0 auto 20px;
  overflow-y: auto;
  border: 1px solid #ccc;
  background: white;
  border-radius: 6px;
}
table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

th {
  position: sticky;
  top: 0;
  background-color: #f0f0f0;
  z-index: 2; /* 确保不被遮挡 */
}

th, td {
  height: 40px;
  padding: 0 10px;
  border-bottom: 1px solid #eee;
  text-align: center;
}
thead {
  background-color: #f0f0f0;
}
th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: #f0f0f0;
}
thead {
  background-color: #f0f0f0;
}
th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: #f0f0f0;
}
.icon-action {
  height: 1em;
  cursor: pointer;
  transition: transform 0.2s, filter 0.2s;
  transition: transform 0.2s, filter 0.2s;
}
.icon-action:hover {
  transform: scale(1.2);
  filter: brightness(1.2);
}
.even-row {
  background: #fff;
}
.odd-row {
  background: #f5f5f5;
}

/* 分页器样式 */
.pagination {
  width: 90%;
  max-width: 1000px;
  margin: 20px auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.bottom-left-buttons {
  display: flex;
  flex-direction: row;
  gap: 10px;
}
.manage-btn {
  padding: 6px 14px;
  background-color: #3398ff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 13px;
  cursor: pointer;
}
</style>