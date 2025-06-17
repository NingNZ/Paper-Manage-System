<template>
  <div class="container">
    <bar />

    <main class="main-wrapper">
      <!-- 左侧导航栏 -->
      <aside class="sidebar">
        <div class="menu-group">
          <div
            v-if="permission !== 1"
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
        <div class="breadcrumb-wrapper">
          <div class="breadcrumb">
            <a href="/">首页</a> &gt; <a href="/notice">我的消息</a> &gt; {{ activeSidebar }}
          </div>
          <!-- 筛选框放在这里，右对齐 -->
          <div class="top-controls">
            <span class="select-label"></span>
            <select v-model="selectedType" class="simple-select">
              <option :value="'0#0#0#0'">个人消息</option>
              <option
                v-for="item in teamOptions"
                :key="item.id"
                :value="item.id"
              >
                {{ item.value }}
              </option>
            </select>
          </div>
        </div>

        <div class="table-wrapper">
          <!-- 表格头部 -->
          <table v-if="hasItems" class="custom-table">
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
                <th class="action-col">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in pagedItems" :key="index" :class="index % 2 === 0 ? 'even-row' : 'odd-row'">
                <!-- 待处理通知 -->
                <template v-if="activeSidebar === '待处理的通知'">
                  <td class="message-col"><span v-html="item.message"></span></td>
                  <td class="time-col">{{ formatDate(item.time) }}</td>
                  <td class="status-col"><span class="status-gray">{{ item.results }}</span></td>
                  <td class="action-col">
                    <el-button v-if="!item.state&&item.isRoot" size="small" type="primary" plain @click="handleDownload(index)">下载</el-button>
                    <el-button v-if="!item.state" size="small" type="success" plain @click="handleDecision(index, 1)">通过</el-button>
                    <el-button v-if="!item.state" size="small" type="danger" plain @click="handleDecision(index, 0)">拒绝</el-button>
                  </td>
                </template>

                <!-- 我收到的消息 -->
                <template v-else>
                  <td class="message-col">{{ item.message }}</td>
                  <td class="time-col">{{ formatDate(item.time) }}</td>
                  <td class="status-col"><span :class="resultClass(item.state,item.ispass)">{{ item.results }}</span></td>
                  <td class="action-col"></td>
                </template>
              </tr>
            </tbody>
          </table>

          <!-- 空数据时提示 -->
          <div v-else class="empty-text">
            <template v-if="activeSidebar === '待处理的通知'">
              您没有未处理的通知
            </template>
            <template v-else>
              您还没有收到任何消息
            </template>
          </div>
        </div>

        <!-- 分页 -->
        <el-pagination
          v-if="hasItems"
          background
          layout="prev, pager, next, sizes, jumper"
          :total="totalItems"
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
import { ref, computed, onMounted ,watch} from 'vue';
import bar from '../components/bar.vue'
import { sessionUtil } from "../scripts/session";
import teamUtils from '../scripts/team';
import { ElMessage,ElLoading } from 'element-plus';
import noticeUtils from '../scripts/notice.js';
import { waitPaperUtils } from '../scripts/waitPaper.js';

const permission = ref(-1)
const teamOptions = ref([])  //得到我作为组长的团队列表
const selectedType = ref('0#0#0#0')// 默认选中 "关键字"（value 为 0#0#0#0）

onMounted(() => {
  selectedType.value ='0#0#0#0'
  localStorage.setItem("selectedType",selectedType.value);
  teamUtils.getMainTeam()
    .then(res => {
      teamOptions.value = res.data.map(item => ({
        id : item.id, //获取团队id
        value : item.name //获取团队name
      }))
    })
    .catch(({code,msg}) => {
      // console.error("获取团队列表失败")
      teamOptions.value = []
      ElMessage.error(msg)
    });

    sessionUtil.checkPermiss()
    .then(res => {
      permission.value = res
        })
    .catch(() => {
      permission.value = -1
      ElMessage.error("服务器未连接")
    }).finally(()=>{
      loadNotices();  // 初始加载
    })
    
});

const activeSidebar = ref('待处理的通知')   // 默认选中 "待处理的通知"
const currentPage = ref(1)
const pageSize = ref(5)


watch(selectedType, () => {
  currentPage.value = 1;  // 每次切换重置分页
  localStorage.setItem("selectedType",selectedType.value);
  loadNotices();
});
watch(activeSidebar,()=>{
  const localSaveOption = localStorage.getItem("selectedType")
  selectedType.value = (localSaveOption==null)?'0#0#0#0': localSaveOption
  currentPage.value = 1;  // 每次切换重置分页
  loadNotices();
})

const setActive = (val) => {
  activeSidebar.value = val
  currentPage.value = 1
}

const noticeList = ref([])

const loadNotices = () => {
    console.log(permission.value)
    if(permission.value === 1) { // 如果是管理员
      const loadingInstance = ElLoading.service({
        lock: true,
        text: '加载中...',
        background: 'rgba(0, 0, 0, 0.2)'
      })
      noticeUtils.Sys_getNotices_OP()
      .then(({code,msg,data})=>{
        setTableData_Sys(data)
      })
      .catch(({code,msg,data})=>{
        ElMessage.error(msg)
        noticeList.value = []
      }).finally(()=>{
        setTimeout(()=>{
          loadingInstance.close()
        },500)
      })
    } 
    else{                        // 如果是用户
      const loadingInstance = ElLoading.service({
        lock: true,
        text: '加载中...',
        background: 'rgba(0, 0, 0, 0.2)'
      })
        if (activeSidebar.value === '待处理的通知') {
              noticeUtils.getNotices_OP(selectedType.value)
              .then(({code,msg,data})=>{
                setTableData_WithOp(data)
              })
              .catch(({code,msg,data})=>{
                ElMessage.error(msg)
              }).finally(()=>{
                setTimeout(()=>{
                  loadingInstance.close();
                },500)
              })
        } else { // 我收到的消息
               noticeUtils.getNotices_NoOP(selectedType.value)
              .then(({code,msg,data})=>{
                setTableData_WithoutOp(data)
              })
              .catch(({code,msg,data})=>{
                ElMessage.error(msg)
              }).finally(()=>{
                setTimeout(()=>{
                  loadingInstance.close();
                },500)
              })
            
        }
    }
};
const setTableData_Sys = (data)=>{
  noticeList.value = data.map(item => ({
  msgId : item.msgId, // 假设有一个唯一的消息ID
  message: `<ul class="notice-list">
              <li><strong>团队</strong> “${item.teamName}” <span style="color:#888;">(${item.teamId})</span> 上传了一篇论文：</li>
              <li><strong>Title:</strong> <span style="color:#1976d2;">${item.paper.title}</span></li>
              <li><strong>Journal:</strong> <span style="color:#1976d2;">${item.paper.journal}</span>&nbsp;&nbsp;&nbsp;<strong>Type:</strong> <span style="color:#1976d2;">${item.paper.type}</span></li>
              <li><strong>Authors:</strong> <span style="color:#1976d2;"><i>${item.paper.authors}</i></span></li>
              <li><strong>Time:</strong> <span style="color:#1976d2;">${item.paper.time}</span></li>
            </ul>`,
  time: item.time,
  state:item.state,
  paperId:item.paper.id,
  isRoot:1,
  results: item.state === 0 ? '未处理' : item.result === 1 ? '我已通过' : '我已拒绝'
  }))
}


const setTableData_WithOp = (data)=>{
            // 假设后端返回字段：paperName, teamName, time
  if(selectedType.value === '0#0#0#0'){    //待处理的通知，个人消息
        noticeList.value = data.map(item => ({
        msgId : item.id, // 假设有一个唯一的消息ID
        message: `团队“${item.team}”  (${item.teamId})邀请您加入`,
        time: item.time,
        state:item.state,
        results: item.state === 0 ? '未处理' : item.result === 1 ? '我已同意' : '我已拒绝'
      }))
  } else {   //待处理的通知，团队消息
    noticeList.value = data.map(item => ({
      msgId : item.id, // 假设有一个唯一的消息ID
      message: `${item.userName}(id:${item.userId})申请加入您的团队“${item.team}”`,
      time: item.time,
      state:item.state,
      results: item.state === 0 ? '未处理' : item.result === 1 ? '我已同意' : '我已拒绝'
    }));
  }
}

const setTableData_WithoutOp = (data)=>{
   // 假设后端返回字段：paperName, teamName, time, result
    if(selectedType.value === '0#0#0#0'){ // 我收到的消息，个人消息
        noticeList.value = data.map(item => ({
        msgId : item.id, // 假设有一个唯一的消息ID
        message: `我申请加入团队“${item.team}”`,
        time: item.time,
        state:item.state,
        ispass:item.result,
        results: item.state === 0 ? '等待对方处理' : item.result === 1 ? '已被接受' : '已被拒绝'
      }));
    } else { // 我收到的消息，团队消息
        const inviteData = data[0].invite
        const submitData = data[0].submit
        const inviteList = inviteData.map(item => ({
        msgId: item.id,
        message: `邀请<${item.userName}(id:${item.userId})>加入我的团队“${item.team}”`,
        time: item.time,
        state: item.state,
        ispass: item.result,
        results: item.state === 0 ? '等待对方处理' : item.result === 1 ? '已被接受' : '已被拒绝'
        }));

        const submitList = submitData.map(item => ({
        msgId: item.id,
        message: `团队提交的论文《${item.paper.title}》`,
        time: item.time,
        state: item.state,
        ispass: item.result,
        results: item.state === 0 ? '等待管理员审核' : item.result === 1 ? '已被接受' : '已被拒绝'
        }));

        noticeList.value = [...inviteList, ...submitList];
        console.log(inviteData)
        console.log(submitData)
    }
    console.log(noticeList.value)
}

const handleDecision = (index,select)=>{
  if(permission.value === 1){
    console.log(select);
    noticeUtils.Sys_Solve(noticeList.value[index].msgId,select)
    .then(({code,msg})=>{
      if(code==200){
        ElMessage.success("操作成功")
      }else{
        ElMessage.error(msg)
      }
    }).catch(({code,msg})=>{
      ElMessage.error(msg)
    })
  }else{
    noticeUtils.userSolveMsg(noticeList.value[index].msgId,select)
    .then(({code,msg})=>{
      if(code==200){
        ElMessage.success("操作成功");
      }
      else if(code==300){
        ElMessage.info(msg)
      }else{
        ElMessage.error(msg)
      }
    })
    .catch(({code,msg})=>{
      ElMessage.error(msg)
    })
  }

}

const pagedItems = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return noticeList.value.slice(start, start + pageSize.value);
});

const totalItems = computed(() => noticeList.value.length);


const hasItems = computed(() => totalItems.value > 0)

const formatDate = (datetime) => datetime.split(' ')[0]


const handlePageChange = (page) => {
  currentPage.value = page
}
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

const resultClass = (state,ispass) => {
  console.log(state,ispass)
  if (state == '0') return 'status-gray'
  else if (state=='1' && ispass =='0') return 'status-red'
  else if (state=='1' && ispass =='1') return 'status-green'
  return 'status-gray'
}

const handleDownload = (index)=>{
  console.log(noticeList.value[index].paperId)
  waitPaperUtils.downloadWaitPaper(noticeList.value[index].paperId)
  .then(()=>{
    ElMessage.success("下载成功")
  })
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

.breadcrumb-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.icon-action {
  width: 18px;
  height: 18px;
  cursor: pointer;
  transition: transform 0.2s ease;
}
.icon-action:hover {
  transform: scale(1.2);
}
.empty-text {
  padding: 40px;
  text-align: center;
  color: #999;
  font-size: 16px;
}

.top-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.select-label {
  font-size: 14px;
  color: #333;
  white-space: nowrap;
  width: auto; /* Ensure the text won't break */
}

.simple-select {
  padding: 6px 12px;
  font-size: 14px;
  border-radius: 6px;
  border: 1px solid #ccc;
  background-color: #fff;
  transition: border-color 0.3s ease;

  width: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.simple-select:hover {
  border-color: #3398ff;
}

.simple-select:focus {
  outline: none;
  border-color: #3398ff;
  box-shadow: 0 0 0 2px rgba(51, 152, 255, 0.2);
}
.notice-list {
  margin: 0 0 0 10px;
  padding: 12px 18px;
  text-align: left ;
  background: #f4f8fb;
  border-radius: 8px;
  list-style-type: disc;
  color: #333;
  font-size: 15px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}
.notice-list li {
  margin-bottom: 6px;
  line-height: 1.8;
}
.notice-list li:last-child {
  margin-bottom: 0;
}
</style>
