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
            <button class="save-btn" @click="saveEdit()">保存</button>
            <button class="cancel-btn" @click="cancelEdit">取消</button>
          </div>
        </template>
        <template v-else>
          <span>{{ item.label }}</span>
          <div>
            <img src="../../assets/edit.svg" class="icon-action" @click="startEdit(index,item.label,item.value)" />            
          </div>
        </template>
      </li>
    </ul>
  </el-dialog>

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
const isAdding = ref(false)
const categories = ref([{label:'A',value:'0'}, {label:'B',value:'1'},{label:'C',value:'2'}])
const editIndex = ref(null)
const editId = ref(null)
const editText = ref('')
const showDuplicateWarning = ref(false)

const addCategory = () => {
  if (editIndex.value !== null) return // 当前已有正在编辑的项
  categories.value.push({label:'',value:'0'})
  isAdding.value = true
  editIndex.value = categories.value.length - 1
  editText.value = ''
}

const startEdit = (index,name,id=null) => {
  if(isAdding.value) return
  editIndex.value = index
  editText.value = name
  editId.value = id
}

const cancelEdit = (newName) => {
  // 删除空的新建分类
  if (newName === '' && isAdding.value) {
    categories.value.pop()
  }
  editIndex.value = null
  editText.value = ''
  editId.value = null
  isAdding.value = false
  // internalVisible.value = false
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
  categories.value = [...categories.value]
}

const saveEdit = () => {
  const newName = editText.value.trim()
  let index = editIndex.value
  if (!newName) {
    cancelEdit()
    return
  }
  // 判断是否有重名（除自己）
  const duplicate = categories.value.some((c, i) => i !== index && c.label === newName)
  if (duplicate) {
    showDuplicateWarning.value = true
    return
  }
  if (newName === "未分类"){
    ElMessage.info("你不可以设置这个分类")
    return
  }
  if(isAdding.value){
    utils.newSysType(newName)
    .then(({code,msg})=>{
      if(code==200) ElMessage.success("添加成功")
      else {
        ElMessage.error(msg)
        categories.value[index]= {label:'',value:0}
      }
    })
    .catch((code,msg)=>{
      ElMessage.error(msg)
      categories.value[index]= {label:'',value:0}
    }).finally(()=>cancelEdit(newName))
  }else{
    utils.updateSysType(newName,editId.value)
    .then(({code,msg})=>{
      if(code==200) ElMessage.success("修改成功")
      else {
        ElMessage.error(msg)
      }
    })
    .catch((code,msg)=>{
      ElMessage.error(msg)
    }).finally(()=>cancelEdit(newName))
  }
  
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
