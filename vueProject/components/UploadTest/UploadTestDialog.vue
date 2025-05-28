<template>
  <el-dialog
    :model-value="props.visible"
    title="上传论文"
    width="500px"
    @close="emit('update:visible', false)"
  >
    <el-form label-width="80px">
      <el-form-item label="论文名称">
        <el-input v-model="form.title" placeholder="请输入论文标题" />
      </el-form-item>

      <el-form-item label="作者">
        <el-input v-model="form.author" placeholder="请输入作者名称" />
      </el-form-item>

      <el-form-item label="类型">
        <el-select v-model="form.type" placeholder="选择论文类型">
          <el-option            
            v-for = "item in options"
            :key = "item.value"
            :label = "item.label"
            :value = "item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="期刊">
        <el-select v-model="form.journal" placeholder="选择期刊">
          <el-option label="IEEE" value="IEEE" />
          <el-option label="ACM" value="ACM" />
          <el-option label="Nature" value="Nature" />
          <el-option label="Science" value="Science" />
        </el-select>
      </el-form-item>

      <el-form-item label="发布时间">
        <el-date-picker
          v-model="form.date"
          type="month"
          placeholder="选择月份"
          format="YYYY-MM"
        />
      </el-form-item>

      <el-form-item label="上传文件">
        <el-upload
          drag
          action="#"
          :file-list="fileList"
          :auto-upload="false"
          :on-change="handleFileChange"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将 PDF 拖到此处，或 <em>点击上传</em></div>
          <template #tip>
            <div class="el-upload__tip">只能上传 PDF 文件</div>
          </template>
        </el-upload>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="emit('update:visible', false)">取消</el-button>
      <el-button type="primary" @click="submit">确认</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref,watch } from 'vue'

const props = defineProps({
  visible: Boolean
})
const emit = defineEmits(['update:visible'])

const form = ref({
  title: '',
  author: '',
  type: '',
  journal: '',
  date: '',
  file: null
})
const options = ref([
    { value: 'journal', label: 'A' },
    { value: 'conference', label: 'B' },
    { value: 'other', label: 'C' }
])
const fileList = ref([])

watch(() => props.visible, (val) => {
  if (!val) {
    form.value = {
      title: '',
      author: '',
      type: '',
      journal: '',
      date: '',
      file: null
    }
    fileList.value = []
  }
})

const handleFileChange = (file) => {
  form.value.file = file.raw
}

const submit = () => {
  console.log('上传信息：', form.value)
  emit('update:visible', false)
}
</script>

<style scoped>
.el-upload__tip {
  font-size: 12px;
  color: #999;
}
</style>
