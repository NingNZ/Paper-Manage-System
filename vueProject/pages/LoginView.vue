<script setup>
import { loadSlim } from "tsparticles-slim";
import { ElMessage } from 'element-plus'
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios'
import qs from 'querystring'
import AnimatedText from "../components/AnimatedText.vue";
import {sessionUtil} from "../scripts/session"
import utils from "../scripts/utils";
import {share} from "../scripts/share"

const particlesInit = async engine => {
  await loadSlim(engine);
};
const particlesLoaded = async container => {}

const options = ref({
  background: { color: { value: '#99ccff' } },
  fpsLimit: 120,
  interactivity: {
    events: {
      onClick: { enable: false, mode: 'push' },
      onHover: { enable: true, mode: 'grab' },
      resize: true
    },
    modes: {
      bubble: { distance: 400, duration: 2, opacity: 0.8, size: 40 },
      push: { quantity: 4 },
      repulse: { distance: 200, duration: 0.4 },
      grab: { distance: 200, duration: 0.4 }
    }
  },
  particles: {
    color: { value: '#Ffd700' },
    links: {
      color: '#Ffd700',
      distance: 150,
      enable: true,
      opacity: 0.6,
      width: 1
    },
    collisions: { enable: true },
    move: {
      direction: 'none',
      enable: true,
      outModes: { default: 'bounce' },
      random: false,
      speed: 1,
      straight: false
    },
    number: {
      density: { enable: true, area: 800 },
      value: 80
    },
    opacity: { value: 0.6 },
    shape: { type: 'star' },
    size: { value: { min: 1, max: 5 } }
  },
  detectRetina: true
})

let username = ref('');
let password = ref('');
let accountRegi = ref('');
let userRegi = ref("");
let passRegi = ref("");
let DialogVisible2 = ref(false);
let DialogVisible = ref(false);
const router = useRouter();

function handleLogin() {
  if (!username.value || !password.value) {
    ElMessage.error("账号和密码不能为空");
    return;
  }

  let data = {
    username: username.value,
    password: password.value,
    access: ' ',
  }
  axios.post(utils.url +"/login", qs.stringify(data))
    .then((res) => {
      if (res.data.code == 200) {
        sessionUtil.getUser().then(data=>{
          ElMessage.success("Welcome, "+data.msg)
          sessionUtil.checkPermiss()
          .then(data=>{
            if(data==200){
              share.setPermisson(1)
              ElMessage.info("你是管理员")
            }else if(data==300){
              share.setPermisson(0)
              ElMessage.info("你是用户")
            }else{
              share.setPermisson(-1)
              ElMessage.info("你是游客")
            }
          })
        }).catch(()=>{
          ElMessage.error("服务器连接出错")
        })
      }else if(res.data.code == 404){
        ElMessage.error("用户不存在")
      }else{
        ElMessage.error("密码错误")
      }
    })
    .catch(()=>{
      ElMessage.error("服务器连接出错")
    })
}

function handleRegi() {
  // 用户账号校验
  const accountPattern = /^[A-Za-z0-9]{6,18}$/;
  if (!accountPattern.test(accountRegi.value)) {
    ElMessage.error("用户账号格式不正确：应为 6-18 位字母或数字组合");
    return;
  }

  // 用户名校验
  const usernamePattern = /^([\u4e00-\u9fa5]{1,18}|[a-zA-Z·\s]{1,18})$/;
  if (!usernamePattern.test(userRegi.value)) {
    ElMessage.error("用户名格式不正确：应为 1-18 位纯汉字，或纯英文（可含空格或·）");
    return;
  }

  // 密码校验
  const passwordPattern = /^[A-Za-z0-9]{6,18}$/;
  if (!passwordPattern.test(passRegi.value)) {
    ElMessage.error("密码格式不正确：应为 6-18 位字母或数字的组合");
    return;
  }

  let data1 = {
    username: userRegi.value,
    password: passRegi.value,
    access: 'c'
  }

  axios.post("http://localhost:5213/last/user/register", qs.stringify(data1))
    .then((res) => {
      if (res.data.code == 200) {
        router.replace('/')
        ElMessage.success(res.data.msg);
      } else {
        ElMessage.error(res.data.msg);
      }
      userRegi.value = '';
      passRegi.value = '';
      accountRegi.value = '';
    })
  DialogVisible.value = false;
}

function changePassword() {
  const data2 = {
    username: userRegi.value,
    password: passRegi.value,
    access: 'c'
  }
  axios.post("http://localhost:5213/last/user/change", qs.stringify(data2))
    .then((res) => {
      if (res.data.code == 200) {
        ElMessage.success(res.data.msg);
      } else {
        ElMessage.error(res.data.msg);
      }
      userRegi.value = '';
      passRegi.value = '';
    })
  DialogVisible2.value = false;
};
</script>

<template>
  <div class="loginblock">
    <animated-text></animated-text>
    <el-card class="card" style="opacity:0.96;" shadow="always">
      <img src="../assets/loginLogo.jpg" style="width: 30%; margin-inline-start:30%; border-radius:100%;
        filter: drop-shadow(3px 3px 6px #000000 );"> <br>
      <el-input v-model="username" style="width: 240px" placeholder="用户账号" /><br><br>
      <el-input
        v-model="password"
        style="width: 240px"
        type="password"
        placeholder="密码"
        show-password
      /><br><br>
      <el-button type="primary" @click="handleLogin" style="margin-left: 50px;">登录</el-button>
      <el-button type="primary" @click="DialogVisible=true;">注册</el-button>
      <a @click="DialogVisible2=true" style="font-style: italic; font-size: small; text-decoration: underline;">修改密码</a>
    </el-card>
  </div>

  <vue-particles id="tsparticles" class="particlebac" :particlesInit="particlesInit"
    :particlesLoaded="particlesLoaded" :options="options" />

  <el-dialog
    v-model="DialogVisible"
    title="用户注册"
    width="500"
    align-center
  >
    <el-input v-model="accountRegi" style="width: 240px; margin-left: 25%" placeholder="用户账号" /><br><br>
    <el-input v-model="userRegi" style="width: 240px; margin-left: 25%" placeholder="用户名" /><br><br>
    <el-input
      v-model="passRegi"
      style="width: 240px; margin-left: 25%;"
      type="password"
      placeholder="密码"
      show-password
    /><br><br>
    <div style="margin-inline-start: 70%;">
      <el-button @click="DialogVisible = false; accountRegi=''; userRegi=''; passRegi='';">取消</el-button>
      <el-button type="primary" @click="handleRegi">确定</el-button>
    </div>
  </el-dialog>

  <el-dialog
    v-model="DialogVisible2"
    title="修改密码"
    width="500"
    align-center
  >
    <el-input v-model="userRegi" style="width: 240px; margin-left: 25%" placeholder="用户名" /><br><br>
    <el-input
      v-model="passRegi"
      style="width: 240px; margin-left: 25%;"
      type="password"
      placeholder="密码"
      show-password
    /><br><br>
    <div style="margin-inline-start: 70%;">
      <el-button @click="DialogVisible2 = false; userRegi=''; passRegi='';">取消</el-button>
      <el-button type="primary" @click="changePassword">确定</el-button>
    </div>
  </el-dialog>
</template>

<style>
.particlebac {
  opacity: 0.9;
  position: absolute;
  z-index: 1;
}
.loginblock {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 999;
}
.card {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 450px;
  border-radius: 20px;
  background: radial-gradient(hsl(54, 92%, 69%), #d6ecf0);
}
</style>
