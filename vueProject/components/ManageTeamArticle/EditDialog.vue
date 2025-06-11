<template>
  <el-dialog
    title="编辑论文分类"
    v-model="internalVisible"
    width="400px"
    @close="$emit('close')"
  >
    <div>
      <p>论文标题：{{ item.title }}</p>
      <el-select
        v-model="selectedCategory"
        placeholder="请选择分类"
        style="width: 100%; margin-top: 10px;"
      >
        <el-option
          v-for="option in categoryOptions"
          :key="option"
          :label="option"
          :value="option"
        />
      </el-select>
    </div>
    <template #footer>
      <el-button @click="$emit('close')">取消</el-button>
      <el-button type="primary" @click="confirmEdit">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  modelValue: Boolean,
  item: Object,
  categoryOptions: {
    type: Array,
    default: () => ['默认分类', '科技类', '综述类', '其他']  // 可选
  }
})

const emit = defineEmits(['update:modelValue', 'close', 'confirm'])

const internalVisible = ref(props.modelValue)
const selectedCategory = ref('')

watch(() => props.modelValue, (val) => {
  internalVisible.value = val
})
watch(internalVisible, (val) => {
  emit('update:modelValue', val)
})

// 确认保存
const confirmEdit = () => {
  emit('confirm', selectedCategory.value)
  internalVisible.value = false
}
</script>
