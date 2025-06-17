<!-- src/components/JoinTeamDialog.vue -->
<template>
  <el-dialog
    v-model="visible"
    width="600px"
    center
    :before-close="closeDialog"
    :show-close="true"
    modal-class="join-dialog-bg"
  >
    <div class="dialog-content">
      <el-input
        v-model="searchText"
        placeholder="输入要搜索的团队编号"
        clearable
        class="search-box"
      >
        <template #suffix>
          <el-icon><Search /></el-icon>
        </template>
        
      </el-input>
      <button @click="search">搜索</button>

      <el-table :data="joinableTeams" border stripe style="margin-top: 20px;">
        <el-table-column prop="name" label="团队名" />
        <el-table-column prop="id" label="团队编号" />
        <el-table-column prop="leader" label="团队组长" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" size="small" @click="joinTeam(scope.row)">加入</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, watch, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { Search } from '@element-plus/icons-vue';
import teamUtils from '../../scripts/team';

const props = defineProps({
  modelValue: Boolean,
});
const emits = defineEmits(['update:modelValue']);
/**
 * @type {Array<{name:string,id:string,leader:string}>}
 */
const joinableTeams = ref([]);
const visible = ref(props.modelValue);
watch(() => props.modelValue, val => {
  if(val) joinableTeams.value = []
  visible.value = val
});
watch(visible, val => emits('update:modelValue', val));

const searchText = ref('');
const search = ()=>{
  if(searchText.value.trim()==''){
    ElMessage.info("输入不能为空")
    return;
  }
  teamUtils.searchOneTeam(searchText.value.trim())
  .then(({code,data,msg})=>{
    if(code==200){
      joinableTeams.value = Array.from({length:data.length},(_,i)=>({
        name:data[i].name,
        id:data[i].id,
        leader:data[i].leaderName
      }))
      ElMessage.success("搜索成功")
      joinableTeams.value = [...joinableTeams.value]
    }else{
      ElMessage.info(`团队id"${searchText.value.trim()}"不存在`)
    }
  })
  .catch(({code,data,msg})=>{
    ElMessage.error(msg)
  })
}

function joinTeam(team) {
  // ElMessage.success(`已申请加入 ${team.name}`);
  teamUtils.teamApply(team.id)
  .then(({code,msg})=>{
    if(code==200){
      ElMessage.success("申请成功，等待对方组长审核")
    }else if(code==400){
      ElMessage.error(msg)
    }else{
      ElMessage.info(msg);
    }
  })
  .catch(({code,msg})=>{
    ElMessage.error(msg);
  })
}

function closeDialog() {
  visible.value = false;
  searchText.value = '';
}
</script>

<style scoped>
.dialog-content {
  padding: 10px 20px;
}
.search-box {
  margin-top: -1%;
  width: 85%
}
.dialog-content>button {
  background-color: #3398ff;
  color: white;
  border: none;
  margin-left: 1%;
  padding: 4px 10px;
  font-size: 16px;
  border-radius: 0 20px 20px 0;
  cursor: pointer;
}
</style>

<style>
.join-dialog-bg .el-dialog__body {
  background-color: white;
}
</style>
