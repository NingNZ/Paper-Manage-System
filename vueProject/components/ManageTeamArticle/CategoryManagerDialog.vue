<template>
  <div class="category-container">
    <el-tree
      :data="treeData"
      :props="defaultProps"
      node-key="id"
      default-expand-all
    >
      <template #default="{ node, data }">
        <span class="tree-node">
          <img src="../../assets/文件夹.svg" class="folder-icon" />
          <span>{{ data.label }}</span>
          <span class="tree-actions">
            <img class="action-icon" src="../../assets/new.svg" alt="添加" />
            <img class="action-icon" src="../../assets/edit.svg" alt="编辑" />
            <img class="action-icon" src="../../assets/delete.svg" alt="删除" />
          </span>
        </span>
      </template>
    </el-tree>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const treeData = ref([
  {
    id: 1,
    label: 'Part1',
    children: [
      {
        id: 2,
        label: 'AI',
        children: [
          { id: 3, label: '体系结构' }
        ]
      }
    ]
  },
  {
    id: 4,
    label: 'Part2'
  }
])

const defaultProps = {
  children: 'children',
  label: 'label'
}
</script>

<style scoped>
.category-container {
  padding: 10px;
}

.tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
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
