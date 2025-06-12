<template>
  <div class="category-container">
    <el-tree
      :data="treeData"
      :props="defaultProps"
      node-key="id"
      default-expand-all
      ref="treeRef"
    >
      <template #default="{ node, data }">
        <span class="tree-node">
          <img src="../../assets/文件夹.svg" class="folder-icon" />
          <span>{{ data.label }}</span>
          <span class="tree-actions">
            <!-- 分类节点只允许新建 -->
            <template v-if="data.isRoot">
              <img class="action-icon" src="../../assets/new.svg" alt="新建" @click.stop="handleAdd(data)" />
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
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 树数据：外层套一层 "分类"
const treeData = ref([
  {
    id: 0,
    label: '分类',
    isRoot: true,
    children: [
      { id: 1, label: 'Part1', children: [{ id: 2, label: 'AI', children: [{ id: 3, label: '体系结构' }] }] },
      { id: 4, label: 'Part2' }
    ]
  }
])

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
    deleteNode(treeData.value, node.id)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 确认弹窗提交
function confirmDialog() {
  if (isEditing.value) {
    currentNode.label = inputValue.value
  } else {
    const newChild = { id: maxId++, label: inputValue.value }
    if (!parentNode.children) parentNode.children = []
    parentNode.children.push(newChild)
  }
  dialogVisible.value = false
}

// 递归删除函数
function deleteNode(arr, id) {
  for (let i = 0; i < arr.length; i++) {
    if (arr[i].id === id) {
      arr.splice(i, 1)
      return true
    } else if (arr[i].children) {
      const found = deleteNode(arr[i].children, id)
      if (found) return true
    }
  }
  return false
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
</style>
