<template>
  <el-dialog v-model="visible" title="上传论文" width="500px" :close-on-click-modal="false">
    <el-form label-width="100px">
      <el-form-item label="论文标题">
        <el-input v-model="newPaper.title" placeholder="请输入论文标题" />
      </el-form-item>
      <el-form-item label="论文类别">
        <el-tree-select
          v-model="newPaper.category"
          :data="props.categoryTree"
          :props="{ label: 'label', value: 'id', children: 'children' }"
          placeholder="请选择分类"
          style="width: 100%; margin-top: 10px;"
          check-strictly
        />
      </el-form-item>
      <el-form-item label="上传PDF">
        <el-upload
          class="upload-demo"
          :auto-upload="false"
          :on-change="handleFileChange"
          :file-list="fileList"
          accept=".pdf"
          :limit="1"
        >
          <el-button type="primary">选择文件</el-button>
          <template #tip>
            <div style="font-size: 12px; color: #999;">仅支持 PDF 文件</div>
          </template>
        </el-upload>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="submit">上传</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import utils from '../../scripts/utils'

const props = defineProps({
  modelValue: Boolean,
  categoryTree: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update:modelValue','fresh'])

const visible = ref(props.modelValue)
const newPaper = ref({ title: '', category: '', file: null })
const fileList = ref([])
const selectedCategory = ref(null)

watch(() => props.modelValue, (val) => {
  visible.value = val
  if (val) {
    resetForm()
  }
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

function resetForm() {
  newPaper.value = { title: '', category: '', file: null }
  fileList.value = []
  selectedCategory.value = null
}

function handleFileChange(file, fileList_) {
  newPaper.value.file = file.raw
  fileList.value = fileList_
}

function submit() {
  if (!newPaper.value.title || newPaper.value.title.trim()=='' || !newPaper.value.category || !newPaper.value.file) {
    ElMessage.error('请完整填写所有信息')
    return
  }
  handleUpload()
  visible.value = false
}

// 论文上传逻辑
function handleUpload() {
  console.log('论文标题:', newPaper.value.title)
  console.log('论文分类:', newPaper.value.category)
  console.log('上传文件:', newPaper.value.file)
  
  const formData = new FormData();
  formData.append('title', newPaper.value.title.trim().replaceAll(" ","_"));
  formData.append('typeId', newPaper.value.category);
  formData.append('file', newPaper.value.file); // 文件
  //测试代码
  const teamId = localStorage.getItem("teamId");
  formData.append('teamId',teamId);
  //
  axios.post(utils.url+"/teamInfo/newPapers",formData,{
    headers:{
      'Content-Type':'multipart/form-data'
    },
    withCredentials:true
  })
  .then(response =>{
    if(response.data.code==200){
      ElMessage.success("上传成功")
      emit('fresh',teamId)
    }else{
      ElMessage.error(response.data.msg)
    }
  })
  .catch(error=>{
    console.log("submit fail",error)
    ElMessage.error("上传失败")
  })
}
</script>
