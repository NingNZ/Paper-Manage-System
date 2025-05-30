<template>
  <el-dialog :model-value="internalVisible" @update:modelValue="val=>internalVisible=val" title="管理分类" width="400px">
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <span>管理分类</span>
        <button @click="addCategory" class="new-btn">新建</button>
      </div>
    </template>
    <ul>
      <li v-for="(item,index) in categories" 
          :key="item.value"
          class="item-row">
        <template v-if="editIndex === index">
          <input
            v-model="editText"
            class="edit-input"
            placeholder="新建分类"
          />
          <div>
            <button class="save-btn" @click="saveEdit(index)">保存</button>
            <button class="cancel-btn" @click="cancelEdit">取消</button>
          </div>
        </template>
        <template v-else>
          <span>{{ item.label }}</span>
          <div>
            <img src="../../assets/edit.svg" class="icon-action" @click="startEdit(index,item.value, item.label)" />
            <!-- &nbsp;
            <img src="../../assets/delete.svg" class="icon-action" @click="confirmDelete(index)" /> -->
          </div>
        </template>
      </li>
    </ul>
  </el-dialog>

  <!-- 确认删除弹窗 -->
  <!-- <el-dialog v-model="showDeleteConfirm" title="确认删除" width="400px">
    <span>确定要删除分类 "{{ categories[deleteIndex].label }}" 吗？此操作不可恢复。</span>
    <template #footer>
      <el-button @click="showDeleteConfirm = false">取消</el-button>
      <el-button type="danger" @click="doDelete">确认删除</el-button>
    </template>
  </el-dialog> -->

  <!-- 重复名称警告弹窗 -->
  <el-dialog v-model="showDuplicateWarning" title="分类名重复" width="400px">
    <span>已有相同名称的分类，请修改后再保存。</span>
    <template #footer>
      <el-button type="primary" @click="showDuplicateWarning = false">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import utils from '../../scripts/utils'
import { ElMessage } from 'element-plus'

const props = defineProps({ visible: Boolean })
const internalVisible = ref(props.visible)
const emit = defineEmits(['update:visible'])
watch(()=>props.visible,(val)=>{
  if(val){
    internalVisible.value=props.visible
  }
})
watch(internalVisible,(val)=>{
  if(!val){
    emit("update:visible",val)
  }else{
    utils.getSysType("true","")
    .then(({code,data,msg})=>{
      if(code==200){
        categories.value= Array.from({length:data.length},(_,i)=>({
          value:data[i].id,
          label:data[i].name
        }))
      }
    })
    .catch(({code,data,msg})=>{ElMessage.error(msg)})
  }
})

const categories = ref([{label:'A',value:'0'}, {label:'B',value:'1'},{label:'C',value:'2'}])
const editIndex = ref(null)
const editId = ref(0)
const editText = ref('')

const showDeleteConfirm = ref(false)
const deleteIndex = ref(null)

const showDuplicateWarning = ref(false)

const addCategory = () => {
  if (editIndex.value !== null) return // 当前已有正在编辑的项
  categories.value.push('')
  editIndex.value = categories.value.length - 1
  editText.value = ''
}

const startEdit = (index,id,name) => {
  editIndex.value = index
  console.log(index)
  editId.value = id
  editText.value = name
}

const cancelEdit = () => {
  // 删除空的新建分类
  if (categories.value[editIndex.value] === '' && editIndex.value === categories.value.length - 1) {
    categories.value.pop()
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

  // 判断是否有重名（除自己）
  const duplicate = categories.value.some((c, i) => i !== index && c === newName)
  if (duplicate) {
    showDuplicateWarning.value = true
    return
  }

  categories.value[index] = newName
  cancelEdit()
}

const confirmDelete = (index) => {
  deleteIndex.value = index
  showDeleteConfirm.value = true
}

const doDelete = () => {
  categories.value.splice(deleteIndex.value, 1)
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
