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
        <el-select
          v-model="form.authors"
          multiple
          collapse-tags
          collapse-tags-tooltip
          placeholder="选择作者"
          :formatter="authorFormatter"
        >
          <el-option
            v-for="member in teamMembers"
            :key="member.id"
            :label="member.name"
            :value="member.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="类型">
        <el-select v-model="form.type" placeholder="选择论文类型">
          <el-option label="期刊论文" value="journal" />
          <el-option label="会议论文" value="conference" />
          <el-option label="其他" value="other" />
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
          action=""
          :file-list="fileList"
          :auto-upload="false"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
          accept=".pdf"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">
            将 PDF 拖到此处，或 <em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">只能上传一个 PDF 文件</div>
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
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  visible: Boolean
})
const emit = defineEmits(['update:visible'])

// 模拟团队成员数据
const teamMembers = ref([
  { id: 1, name: '张三' },
  { id: 2, name: '李四' },
  { id: 3, name: '王五' },
  { id: 4, name: '赵六' }
])

const form = ref({
  title: '',
  authors: [],  // 绑定成员id
  type: '',
  journal: '',
  date: '',
  file: null
})

const fileList = ref([])

watch(() => props.visible, (val) => {
  if (!val) {
    resetForm()
  }
})

const resetForm = () => {
  form.value = {
    title: '',
    authors: [],
    type: '',
    journal: '',
    date: '',
    file: null
  }
  fileList.value = []
}

// 显示作者名顿号分隔
const authorFormatter = () => {
  const names = form.value.authors
    .map(id => {
      const member = teamMembers.value.find(m => m.id === id)
      return member ? member.name : ''
    })
    .filter(name => name !== '')
  return names.join('、')
}

// 文件变更
const handleFileChange = (uploadFile, uploadFiles) => {
  const isPDF = uploadFile.raw.type === 'application/pdf'
  if (!isPDF) {
    ElMessage.error('只能上传 PDF 文件')
    fileList.value = []
    form.value.file = null
    return
  }

  fileList.value = [uploadFile]
  form.value.file = uploadFile.raw
}

// 文件删除
const handleFileRemove = () => {
  fileList.value = []
  form.value.file = null
}

const submit = () => {
  if (!form.value.file) {
    ElMessage.error('请上传一个 PDF 文件')
    return
  }

  console.log('上传信息：', form.value)
  // 提交 form.value.authors 是 ID 数组，例如 [1, 2, 4]
  emit('update:visible', false)
}
</script>

<style scoped>
.el-upload__tip {
  font-size: 12px;
  color: #999;
}
</style>
