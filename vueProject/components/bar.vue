<template>
  <div id="bar">
    <header class="navbar">
      <div class="logo">
        <div class="content">
          <img src="../assets/logo.svg" alt="logo" class="logo-icon" />
          <span style="height: 32px; padding: 0;">Paper</span><br />Manage
        </div>
      </div>
      <nav class="menu">
        <a href="/">首页</a>
        <a v-if="permission === 1" href="javascript:void(0)" class="disabled-link">我的团队</a>
        <a v-else href="javascript:void(0)" @click="handleNavClick('/myteam')">我的团队</a>
        <a v-if="permission === 1" href="javascript:void(0)" class="disabled-link">我的论文</a>
        <a v-else href="javascript:void(0)" @click="handleNavClick('/mypaper')">我的论文</a>
        <a href="javascript:void(0)" @click="handleNavClick('/notice')">我的消息</a>
      </nav>

      <div class="user-icon" ref="iconRef">
        <img src="../assets/head.svg" alt="user" @click="toggleUserInfo" />
        <div class="user-info-card" v-if="showUserInfo" ref="cardRef" @click.stop>
          <template v-if="permission === 0">
            <div class="info-item">
              <label>ID：</label>
              <span>{{ userId }}</span>
            </div>
            <div class="info-item">
              <label>用户名：</label>
              <span v-if="!isEditing">{{ username }}</span>
              <input v-else v-model="username" />
            </div>
            <div class="info-item">
              <label>邮箱：</label>
              <span v-if="!isEditing">{{ email }}</span>
              <input v-else v-model="email" />
            </div>
            <div class="button-area">
              <button @click.stop="toggleEdit">{{ isEditing ? '保存' : '编辑' }}</button>
            </div>
          </template>

          <template v-else-if="permission === 1">
            <div class="info-item">
              <span>hello,管理员</span>
            </div>
          </template>

          <template v-else>
            <div class="info-item">
              <span>未登录</span>
            </div>
            <div class="button-area" style="justify-content: flex-end; margin-top: 1rem;">
              <button @click="goLogin">登录</button>
            </div>
          </template>
        </div>
      </div>
    </header>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { sessionUtil } from "../scripts/session";
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'

const router = useRouter()

const permission = ref(-1)

const username = ref('')
const userId = ref('')
const email = ref('')

onMounted(() => {
  sessionUtil.checkPermiss()
    .then(res => {
      permission.value = res
      // 若是普通用户，再请求用户信息
      if (res === 0) {
        sessionUtil.getUserInfo().then(result => {
          if (result && result.code === 200 && result.data) {
            username.value = result.data.name
            userId.value = result.data.id
            if(result.data.email == null){
              email.value = "未设置邮箱"
            }
            else{
              email.value = result.data.email
            }
          }
        }).catch(() => {
          ElMessage.error("获取用户信息失败")
        })
      }
    })
    .catch(() => {
      permission.value = -1
      ElMessage.error("服务器未连接")
    })
})

function handleNavClick(targetPath) {
  if (permission.value === -1) {
    ElMessageBox.confirm(
      '您还未登录，是否前往登录？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    ).then(() => {
      router.push('/login')
    }).catch(() => {})
  } else {
    router.push(targetPath)
  }
}

const showUserInfo = ref(false)
const isEditing = ref(false)


function toggleUserInfo() {
  showUserInfo.value = !showUserInfo.value
}

function toggleEdit() {
  if (isEditing.value) {
    showUserInfo.value = false
  }
  isEditing.value = !isEditing.value
}

function goLogin() {
  router.push('/login')
}

const iconRef = ref(null)
const cardRef = ref(null)

function handleClickOutside(event) {
  const isInsideCard = cardRef.value?.contains(event.target)
  const isInsideIcon = iconRef.value?.contains(event.target)

  if (!isInsideCard && !isInsideIcon) {
    if (isEditing.value) {
      alert('还有修改未保存')
    } else {
      showUserInfo.value = false
    }
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})
onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>



<style>
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #3398ff;
  color: white;
  padding: 0.5rem 2rem ;
  border-radius: 12px 12px 0 0;
}

.logo {
  font-weight: bold;
  font-size: 1rem;
  line-height: 1.2;
  display: flex;
  align-items: center;
  gap: 8px;
}
.logo > .content {
  font-family: 'Times New Roman', Times, serif;
  font-size: larger;
  font-style: italic;
  line-height: 100%;
  padding: 0%;
}
.logo-icon {
  width: 32px;
  height: 32px;
}

.menu a {
  color: white;
  margin: 0 1rem;
  text-decoration: none;
  padding: 12px 16px;
  border-radius: 4px;
  transition: background 0.3s ease, transform 0.2s ease;
  display: inline-block;
}

.menu a:hover {
  background: linear-gradient(45deg, #6fb1fc, #1c92f2);
  transform: scale(1.05);
}

.user-icon {
  position: relative;
  cursor: pointer;
}

.user-icon img {
  width: 50px;
  height: 50px;
}

.user-info-card {
  position: absolute;
  top: 60px;
  right: 0;
  background-color: white;
  color: #333;
  border-radius: 8px;
  padding: 1rem;
  width: 240px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
  z-index: 10;
}

.info-item {
  margin-bottom: 1rem;
  display: flex;
  flex-direction: column;
}

.info-item label {
  font-weight: bold;
  margin-bottom: 0.3rem;
}

.info-item input {
  padding: 0.3rem;
  font-size: 0.9rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.button-area {
  display: flex;
  justify-content: flex-end;
}

.button-area button {
  background-color: #3398ff;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 6px 12px;
  cursor: pointer;
  font-size: 0.9rem;
}

.button-area button:hover {
  background-color: #1c7ed6;
}

.disabled-link {
  color: rgba(19, 18, 18, 0.7);  /* 深色的透明效果，增加对比度 */
  background-color: rgba(169, 169, 169, 0.5);  /* 灰色背景，透明效果 */
  border: 1px solid rgba(169, 169, 169, 0.5);  /* 灰色边框 */
  pointer-events: none;  /* 禁用点击事件 */
  cursor: not-allowed;  /* 禁用鼠标点击指针 */
  padding: 12px 16px;  /* 保持原内边距 */
  border-radius: 4px;  /* 圆角 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);  /* 微弱阴影 */
  transition: all 0.3s ease-in-out;  /* 平滑过渡 */
}

.disabled-link:hover {
  background-color: rgba(169, 169, 169, 0.6);  /* 悬停时背景略深 */
  transform: scale(1.02);  /* 放大效果 */
}

.disabled-link:focus {
  outline: none;  /* 禁用焦点样式 */
  border-color: rgba(169, 169, 169, 0.7);  /* 聚焦时边框稍深 */
}

/* 通过添加辅助图标来增强禁用状态的可识别性 */
.disabled-link::after {
  content: "🔒";  /* 例子使用锁定图标 */
  margin-left: 8px;
  font-size: 1.1rem;
  color: rgba(19, 18, 18, 0.5);  /* 与文字颜色匹配的透明度 */
}


</style>
