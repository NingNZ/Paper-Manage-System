<script setup>
import { loadSlim } from "tsparticles-slim";
import { ElMessage } from 'element-plus'
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios'
import qs from 'querystring'
const particlesInit = async engine =>{
  await loadSlim(engine);
};
const particlesLoaded = async container => {
    console.log('Particles container loaded', container)
}
const options=ref({
      background: {
        color: {
          value: '#99ccff'
        }
      },
      fpsLimit: 120,
      interactivity: {
        events: {
          onClick: {
            enable: false,
            mode: 'push'
          },
          onHover: {
            enable: true,
            mode: 'grab'
          },
          resize: true
        },
        modes: {
          bubble: {
            distance: 400,
            duration: 2,
            opacity: 0.8,
            size: 40
          },
          push: {
            quantity: 4
          },
          repulse: {
            distance: 200,
            duration: 0.4
          },
          grab:{
            distance: 200,
            duration: 0.4            
          }
        }
      },
      particles: {
        color: {
          value: '#Ffd700'
        },
        links: {
          color: '#Ffd700',
          distance: 150,
          enable: true,
          opacity: 0.6,
          width: 1
        },
        collisions: {
          enable: true
        },
        move: {
          direction: 'none',
          enable: true,
          outModes: {
            default: 'bounce'
          },
          random: false,
          speed: 1,
          straight: false
        },
        number: {
          density: {
            enable: true,
            area: 800
          },
          value: 80
        },
        opacity: {
          value: 0.6
        },
        shape: {
          type: 'star'
        },
        size: {
          value: { min: 1, max: 5 }
        }
      },
      detectRetina: true
    })
let username=ref('');
let password=ref('');
let userRegi=ref("");
let passRegi=ref("");
let DialogVisible2=ref(false);
let DialogVisible =ref(false);
const router=useRouter();
function handleLogin() {
  let data = {
    username: username.value,
    password: password.value,
    access: ' ',
  }
  axios.post("http://localhost:5213/last/user/login", qs.stringify(data))
    .then((res) => {
      if (res.data.code == 200) {
        sessionStorage.setItem("username", username.value),
        sessionStorage.setItem("access",res.data.access);
        router.replace('/about')
      } else {
        ElMessage.error(res.data.msg);
      }
    }
    )
}
function handleRegi(){
  let data1={
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
      userRegi.value='';
      passRegi.value='';
    }
    )
  DialogVisible.value=false;  
};
function changePassword(){
  const data2={
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
      userRegi.value='';
      passRegi.value='';
    }
    )
  DialogVisible2.value=false;  
};


</script>
<template>
  <div>
    <div class="loginblock">
      <el-card  class="card"  style="opacity:0.96;" shadow="always">
        <img src="../assets/loginLogo.jpg"  style="width: 30%; margin-inline-start:30% ;border-radius:100%;
          filter: drop-shadow(3px 3px 6px #000000 );" > <br>
        <el-input v-model="username" style="width: 240px" placeholder="用户名" /><br><br>
        <el-input
         v-model="password"
         style="width: 240px"
         type="password"
         placeholder="密码"
         show-password
         /><br><br>
        <el-button type="primary" @click="handleLogin" style="margin-left: 50px;">登录</el-button> 
        <el-button type="primary" @click="DialogVisible=true;">注册</el-button><a @click="DialogVisible2=true"  style="font-style: italic;font-size: small;text-decoration: underline;">修改密码</a>
      </el-card>
    </div>
    <vue-particles id="tsparticles" class="particlebac" :particlesInit="particlesInit"
      :particlesLoaded="particlesLoaded" :options="options" />
  </div>
  <el-dialog
    v-model="DialogVisible"
    title="用户注册"
    width="500"
    align-center
  >
    <el-input v-model="userRegi" style="width: 240px;margin-left: 25%" placeholder="用户名" /><br><br>
    <el-input
         v-model="passRegi"
         style="width: 240px;margin-left: 25%;"
         type="password"
         placeholder="密码"
         show-password
         /><br><br>
        <div style="margin-inline-start: 70%;">
        <el-button @click="DialogVisible = false;userRegi='';passRegi='';">取消</el-button>
        <el-button type="primary"  @click="handleRegi">
          确定
        </el-button>
       </div>
  </el-dialog>

    <el-dialog
    v-model="DialogVisible2"
    title="修改密码"
    width="500"
    align-center
  >
    <el-input v-model="userRegi" style="width: 240px;margin-left: 25%" placeholder="用户名" /><br><br>
    <el-input
         v-model="passRegi"
         style="width: 240px;margin-left: 25%;"
         type="password"
         placeholder="密码"
         show-password
         /><br><br>
        <div style="margin-inline-start: 70%;">
        <el-button @click="DialogVisible2 = false;userRegi='';passRegi='';">取消</el-button>
        <el-button type="primary"  @click="changePassword">
          确定
        </el-button>
       </div>
  </el-dialog>
</template>
<style>
.particlebac{
  opacity: 0.9;
  position: absolute;
  z-index: 1;
}
.loginblock{
  height: 300px;
  margin-top: 20%;
  width: 500px;
  margin-left: 35%;
  position: relative;
  z-index: 999;
}
.card{
  display:flex;
  justify-content:center;
	align-items:center;
  width: 450px;
  border-radius: 20px;
  background:radial-gradient( hsl(54, 92%, 69%),#d6ecf0);
}


</style>

