<template>
  <el-dialog :model-value="visible" @update:model-value="emit('update:visible', $event)" title="管理分类" width="400px">
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <span>管理分类</span>
        <button @click="addCategory" class="new-btn">新建</button>
      </div>
    </template>
    <ul>
      <li v-for="(item, index) in categories" :key="index" class="item-row">
        <template v-if="editIndex === index">
          <input v-model="editText" class="edit-input" />
          <div>
            <button class="save-btn" @click="saveEdit(index)">保存</button>
            <button class="cancel-btn" @click="cancelEdit">取消</button>
          </div>
        </template>
        <template v-else>
          <span>{{ item }}</span>
          <div>
            <img src="../../assets/edit.svg" class="icon-action" @click="startEdit(index, item)" />
            &nbsp;
            <img src="../../assets/delete.svg" class="icon-action" @click="deleteCategory(index)" />
          </div>
        </template>
      </li>
    </ul>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({ visible: Boolean })
const emit = defineEmits(['update:visible'])

const categories = ref(["默认分类", "AI", "网络安全"])
const editIndex = ref(null)
const editText = ref('')

const addCategory = () => {
  categories.value.push(`新分类${categories.value.length + 1}`)
}

const startEdit = (index, value) => {
  editIndex.value = index
  editText.value = value
}

const cancelEdit = () => {
  editIndex.value = null
  editText.value = ''
}

const saveEdit = (index) => {
  if (editText.value.trim()) {
    categories.value[index] = editText.value.trim()
  }
  cancelEdit()
}

const deleteCategory = (index) => {
  categories.value.splice(index, 1)
}
</script>

<style scoped>
.item-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 6px 0;
  border-bottom: 1px solid #eee;
}
.new-btn {
  font-size: 12px;
  background: #3398ff;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
}
.icon-action {
  height: 1em;
  cursor: pointer;
}
.edit-input {
  flex: 1;
  margin-right: 8px;
  padding: 4px;
}
.save-btn, .cancel-btn {
  margin-left: 4px;
  font-size: 12px;
  padding: 2px 6px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}
.save-btn {
  background: #67c23a;
  color: white;
}
.cancel-btn {
  background: #ccc;
  color: #333;
}
</style>
