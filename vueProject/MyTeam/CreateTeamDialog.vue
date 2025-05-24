<!-- src/components/CreateTeamDialog.vue -->
<template>
  <el-dialog
    v-model="visible"
    width="400px"
    center
    :before-close="handleCancel"
    :modal="true"
    :show-close="false"
    modal-class="custom-dialog-bg"
  >
    <div class="dialog-content">
      <h2 class="title">创建你的团队</h2>
      <el-input
        v-model="teamName"
        placeholder="请输入团队名"
        prefix-icon="UserFilled"
        class="input"
      />
      <div class="button-group">
        <el-button type="primary" @click="handleCreate">确认创建</el-button>
        <el-button @click="handleCancel">取消创建</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue';
import { ElMessage } from 'element-plus';

const props = defineProps({
  modelValue: Boolean,
});
const emits = defineEmits(['update:modelValue', 'create']);

const visible = ref(props.modelValue);
const teamName = ref('');

watch(() => props.modelValue, val => (visible.value = val));
watch(visible, val => emits('update:modelValue', val));

function handleCreate() {
  if (!teamName.value.trim()) {
    ElMessage.warning('请输入团队名称');
    return;
  }
  emits('create', teamName.value.trim());
  visible.value = false;
  teamName.value = '';
}

function handleCancel() {
  visible.value = false;
}
</script>

<style scoped>
.dialog-content {
  text-align: center;
  padding: 20px;
}

.title {
  font-size: 22px;
  margin-bottom: 20px;
  color: white;
}

.input {
  width: 100%;
  margin-bottom: 20px;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style>

<style>
/* Custom dialog background style */
.custom-dialog-bg .el-dialog__body {
  background-color: #86c5f2;
}
</style>
