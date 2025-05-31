<template>
  <el-dialog title="确认删除" v-model="internalVisible" width="400px" @close="$emit('close')">
    <div>
      <p>确定要删除论文 "<b>{{ item.title }}</b>" 吗？此操作不可恢复。</p>
    </div>
    <template #footer>
      <el-button @click="$emit('close')">取消</el-button>
      <el-button type="danger" @click="$emit('confirm')">确认删除</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  modelValue: Boolean,
  item: Object
})
const emit = defineEmits(['update:modelValue', 'close', 'confirm'])

const internalVisible = ref(props.modelValue)

watch(() => props.modelValue, (val) => {
  internalVisible.value = val
})

watch(internalVisible, (val) => {
  emit('update:modelValue', val)
})
</script>
