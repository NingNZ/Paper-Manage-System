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
        <a href="/myteam">我的团队</a>
        <a href="/mypaper">我的论文</a>
        <a href="/notice">我的消息</a>
      </nav>
      <div class="user-icon" ref="iconRef">
        <!-- ✅ 点击头像展开浮窗 -->
        <img
          src="../assets/head.svg"
          alt="user"
          @click="toggleUserInfo"
        />

        <!-- ✅ 浮窗部分，阻止冒泡 -->
        <div
          class="user-info-card"
          v-if="showUserInfo"
          ref="cardRef"
          @click.stop
        >
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
            <button @click.stop="toggleEdit">
              {{ isEditing ? '保存' : '编辑' }}
            </button>
          </div>
        </div>
      </div>
    </header>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

// 控制浮窗显示
const showUserInfo = ref(false)
// 控制编辑状态
const isEditing = ref(false)

// 用户信息
const username = ref('张三')
const userId = 'U123456'
const email = ref('zhangsan@example.com')

// 显示/隐藏浮窗
function toggleUserInfo() {
  showUserInfo.value = !showUserInfo.value
}

// 编辑/保存切换
function toggleEdit() {
  if (isEditing.value) {
    // 保存时关闭浮窗
    showUserInfo.value = false
  }
  isEditing.value = !isEditing.value
}

const iconRef = ref(null)
const cardRef = ref(null)

// 处理点击外部
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
  padding: 0.5rem 2rem;
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
</style>
