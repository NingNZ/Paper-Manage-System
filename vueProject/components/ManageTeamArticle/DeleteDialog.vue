<template>
  <el-dialog title="确认删除" v-model="internalVisible" width="400px" @close="$emit('close')">
    <div>
      <p>确定要删除论文 "<b>{{ item.title }}</b>" 吗？此操作不可恢复。</p>
    </div>
    <template #footer>
      <el-button @click="$emit('close')">取消</el-button>
      <el-button type="danger" @click="confirmDelete">确认删除</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { teamInfoUtils } from '../../scripts/teamInfo'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: Boolean,
  item: Object
})
const emit = defineEmits(['update:modelValue', 'close', 'confirm','fresh'])

const internalVisible = ref(props.modelValue)

watch(() => props.modelValue, (val) => {
  internalVisible.value = val
})

watch(internalVisible, (val) => {
  emit('update:modelValue', val)
})
const confirmDelete = ()=>{
  const teamId = localStorage.getItem("teamId")
  console.log(teamId)
  console.log(props.item.id)
  teamInfoUtils.deleteRefPaper(teamId,props.item.id)
  .then(({code,msg})=>{
    if(code==200){
      ElMessage.success("删除成功")
    }else{
      ElMessage.error(msg)
    }
  }).catch(({code,msg})=>{
    ElMessage.error(msg)
  }).finally(()=>{
      internalVisible.value = false;
      emit('fresh',teamId);
  })
}
</script>
