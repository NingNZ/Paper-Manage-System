<template>
  <el-dialog
    :model-value="visible"
    title="确认退出团队"
    width="30%"
    @close="$emit('update:visible', false)"
  >
    <div>你确定要退出团队 <b>{{ teamName }}</b> 吗？</div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="$emit('update:visible', false)">取消</el-button>
        <el-button type="danger" @click="handleConfirm">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
defineProps({
  visible: Boolean,
  teamName: String
});

const emit = defineEmits(['update:visible', 'confirm']);

function handleConfirm() {
  emit('confirm');
  emit('update:visible', false);
}
</script>
