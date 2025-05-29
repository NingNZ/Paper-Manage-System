<template>
  <el-dialog :model-value="visible" @update:model-value="emit('update:visible', $event)" title="管理期刊" width="400px">
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <span>管理期刊</span>
        <button @click="addJournal" class="new-btn">新建</button>
      </div>
    </template>
    <ul>
      <li v-for="(item, index) in journals" :key="index" class="item-row">
        <template v-if="editIndex === index">
          <input
            v-model="editText"
            class="edit-input"
            placeholder="新建期刊"
          />
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
            <img src="../../assets/delete.svg" class="icon-action" @click="confirmDelete(index)" />
          </div>
        </template>
      </li>
    </ul>
  </el-dialog>

  <!-- 确认删除弹窗 -->
  <el-dialog v-model="showDeleteConfirm" title="确认删除" width="400px">
    <span>确定要删除期刊 "{{ journals[deleteIndex] }}" 吗？此操作不可恢复。</span>
    <template #footer>
      <el-button @click="showDeleteConfirm = false">取消</el-button>
      <el-button type="danger" @click="doDelete">确认删除</el-button>
    </template>
  </el-dialog>

  <!-- 重复名称警告弹窗 -->
  <el-dialog v-model="showDuplicateWarning" title="期刊名重复" width="400px">
    <span>已有相同名称的期刊，请修改后再保存。</span>
    <template #footer>
      <el-button type="primary" @click="showDuplicateWarning = false">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({ visible: Boolean })
const emit = defineEmits(['update:visible'])

const journals = ref(["IEEE", "ACM", "Nature"])
const editIndex = ref(null)
const editText = ref('')

const showDeleteConfirm = ref(false)
const deleteIndex = ref(null)
const showDuplicateWarning = ref(false)

const addJournal = () => {
  if (editIndex.value !== null) return // 当前已有正在编辑的项
  journals.value.push('')
  editIndex.value = journals.value.length - 1
  editText.value = ''
}

const startEdit = (index, value) => {
  editIndex.value = index
  editText.value = value
}

const cancelEdit = () => {
  // 删除空的新建期刊
  if (journals.value[editIndex.value] === '' && editIndex.value === journals.value.length - 1) {
    journals.value.pop()
  }

  editIndex.value = null
  editText.value = ''
}

const saveEdit = (index) => {
  const newName = editText.value.trim()
  if (!newName) {
    cancelEdit()
    return
  }

  // 检查期刊名是否重复（排除当前编辑项）
  const duplicate = journals.value.some((j, i) => i !== index && j === newName)
  if (duplicate) {
    showDuplicateWarning.value = true
    return
  }

  journals.value[index] = newName
  cancelEdit()
}

const confirmDelete = (index) => {
  deleteIndex.value = index
  showDeleteConfirm.value = true
}

const doDelete = () => {
  journals.value.splice(deleteIndex.value, 1)
  showDeleteConfirm.value = false
  deleteIndex.value = null
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
