<!-- src/components/JoinTeamDialog.vue -->
<template>
  <el-dialog
    v-model="visible"
    width="600px"
    center
    :before-close="closeDialog"
    :show-close="false"
    modal-class="join-dialog-bg"
  >
    <div class="dialog-content">
      <el-input
        v-model="searchText"
        placeholder="输入要搜索的团队"
        clearable
        class="search-box"
      >
        <template #suffix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>

      <el-table :data="filteredTeams" border stripe style="margin-top: 20px;">
        <el-table-column prop="name" label="团队名" />
        <el-table-column prop="number" label="团队编号" />
        <el-table-column prop="leader" label="团队组长" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="info" size="small" @click="joinTeam(scope.row)">加入</el-button>
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

const props = defineProps({
  modelValue: Boolean,
  teams: Array,
});
const emits = defineEmits(['update:modelValue']);

const visible = ref(props.modelValue);
watch(() => props.modelValue, val => visible.value = val);
watch(visible, val => emits('update:modelValue', val));

const searchText = ref('');

const filteredTeams = computed(() => {
  if (!searchText.value) return props.teams;
  return props.teams.filter(team =>
    team.name.includes(searchText.value.trim())
  );
});

function joinTeam(team) {
  ElMessage.success(`已申请加入 ${team.name}`);
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
  width: 100%;
}
</style>

<style>
.join-dialog-bg .el-dialog__body {
  background-color: white;
}
</style>
