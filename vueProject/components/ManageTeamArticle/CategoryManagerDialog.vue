<template>
  <div style="display: flex; align-items: center; justify-content: space-between;">
      <span style="font-size: 20px; font-weight: bold;">管理分类</span>
      <el-button
        class="buttons"
        size="small"
        type="primary"
        @click="handleAdd({id:'&_#$H~~H$#_&'})"
      >
        添加分类
      </el-button>
  </div>
  <div class="category-container">
    <el-tree
      :data="treeData"
      :props="defaultProps"
      node-key="id"
      :draggable="true"
      @node-drop="handleNodeDrop"
      :allow-drag="(node)=>!node.data.isDefault"
      :allow-drop="allowDrop"
      default-expand-all
      ref="treeRef"
    >
      <template #default="{ node, data }">
        <span class="tree-node">
          <img src="../../assets/文件夹.svg" class="folder-icon" />
          <span>{{ data.label }}</span>
          <span class="tree-actions">
            <!-- 分类节点只允许新建 -->
            <template v-if="data.isDefault || data.isBan">
            </template>
            <!-- 其他节点正常有 新建/编辑/删除 -->
            <template v-else>
              <img class="action-icon" src="../../assets/new.svg" alt="新建" @click.stop="handleAdd(data)" />
              <img class="action-icon" src="../../assets/edit.svg" alt="编辑" @click.stop="handleEdit(data)" />
              <img class="action-icon" src="../../assets/delete.svg" alt="删除" @click.stop="handleDelete(data)" />
            </template>
          </span>
        </span>
      </template>
    </el-tree>

    <!-- 编辑/新增弹窗 -->
    <el-dialog :title="isEditing ? '编辑分类' : '新增分类'" v-model="dialogVisible" width="400px">
      <el-input v-model="inputValue" placeholder="请输入分类名称" />
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmDialog">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox,ElLoading } from 'element-plus'
import { teamInfoUtils } from '../../scripts/teamInfo'
import axios from 'axios'
import utils from '../../scripts/utils'

 const sendAndSet = ()=>{
    const loadingInstance = ElLoading.service({
      lock: true,
      text: '加载中...',
      background: 'white',
      target:'.category-container'
    })
  const teamId = localStorage.getItem("teamId");
  teamInfoUtils.getTeamCategory(teamId)
  .then(({code,data,msg})=>{
    if(code==200){
      treeData.value = data
      ElMessage.success({message:"刷新团队论文分类",duration: 2000})
    }else{
      ElMessage.error(msg)
    }
  }).catch(({code,data,msg})=>{
    ElMessage.error(msg)
  }).finally(()=>{
    setTimeout(() => {
      loadingInstance.close()
    }, 500);
  })
 }
onMounted(()=>{
  sendAndSet()
})
// 树数据：外层套一层 "分类"
const treeData = ref([])

const defaultProps = { children: 'children', label: 'label' }

const dialogVisible = ref(false)
const isEditing = ref(false)
const inputValue = ref('')
let currentNode = null
let parentNode = null
let maxId = 5  // 简单自增ID模拟

// 添加节点
function handleAdd(node) {
  currentNode = node
  parentNode = node
  inputValue.value = ''
  isEditing.value = false
  dialogVisible.value = true
}

// 编辑节点
function handleEdit(node) {
  currentNode = node
  inputValue.value = node.label
  isEditing.value = true
  dialogVisible.value = true
}

// 删除节点
function handleDelete(node) {
  ElMessageBox.confirm(`确定要删除 "${node.label}" 吗？`, '删除确认', {
    type: 'warning'
  }).then(() => {
    deleteNode(node.id)
  }).catch(() => {})
}

// 确认弹窗提交
function confirmDialog() {
  if(inputValue.value.trim()==''){
    ElMessage.info("输入不能为空");
    return ;
  }
  const inlabel = inputValue.value.trim()
  const teamId = localStorage.getItem("teamId");
  if (isEditing.value) {
    teamInfoUtils.CategoryEdit(teamId,currentNode.id,inlabel)
    .then(({code,msg})=>{
      if(code==200){
        ElMessage.success("修改成功")
        currentNode.label = inlabel
      }else{
        ElMessage.error(msg);
      }
    }).catch(({code,msg})=>{
      ElMessage.error(msg);
    }).finally(()=>{
      dialogVisible.value = false
    })
    
  } else {
    const fid = parentNode.id
    teamInfoUtils.CategoryAdd(teamId,fid,inlabel)
    .then(({code,data,msg})=>{
      if(code==200){
        ElMessage.success("添加成功")
        treeData.value = data
      }else{
        ElMessage.error(msg);
      }
    }).catch(({code,msg})=>{
      ElMessage.error(msg);
    }).finally(()=>{
      dialogVisible.value = false
    })
  }
  
}

// 删除函数
function deleteNode(id) {
  const teamId = localStorage.getItem("teamId")
  teamInfoUtils.CategoryDelete(teamId,id)
  .then(({code,msg,data})=>{
      if(code==200){
        ElMessage.success("删除成功")
        sendAndSet()
      }else{
        ElMessage.error(msg)
      }
  })
  .catch(({code,data,msg})=>{
    ElMessage.error(msg)
  })
}
//拖动
const handleNodeDrop = (draggingNode, dropNode, dropType, event) => {
  // draggingNode.data 是被拖动的节点
  // dropNode.data 是目标节点
  // dropType: 'prev' | 'inner' | 'after'
  // event 是原生事件
  console.log('拖动节点:', draggingNode.data.id)
  console.log('目标节点:', dropNode.data.id)
  console.log('拖动类型:', dropType)
  const teamId = localStorage.getItem("teamId")
  axios.get(utils.url+"/teamInfo/CategoryDrag",{
    params:{
      fromId:draggingNode.data.id,
      toId:dropNode.data.id,
      type:dropType,
      teamId:teamId
    },
    withCredentials:true
  }).then((response)=>{
    if(response.data.code==200){
      ElMessage.success("success");
      sendAndSet()
    }else{
      ElMessage.error(response.data.msg)
      sendAndSet()
    }
  }).catch(()=>{
    ElMessage.error("服务不可用")
  })
}
const allowDrop = (draggingNode, dropNode, type) => {
  if (type === 'prev' && dropNode.data.isDefault) {
    return false
  }else if(type === 'inner' && dropNode.data.isDefault){
    return false
  }else;
  return true
}
</script>

<style scoped>
.category-container {
  padding: 10px;
}
.tree-node {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}
.folder-icon {
  width: 18px;
  height: 18px;
  margin-right: 6px;
}
.tree-actions {
  display: flex;
  gap: 10px;
  margin-left: auto;
}
.action-icon {
  width: 16px;
  height: 16px;
  cursor: pointer;
}
.buttons {
  display: flex;
  justify-content: space-around;
  margin-top: 10px;
}
</style>
