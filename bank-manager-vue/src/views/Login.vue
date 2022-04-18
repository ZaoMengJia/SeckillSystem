<template>
  <el-container>
    <el-main class="background-image" :style="{'--backgroundImageUrl': backgroundImageUrl}">

    </el-main>
    <el-aside width="300px">
      <div style="overflow: hidden">
        <div class="aside-background" :style="{'--backgroundImageUrl': backgroundImageUrl}">
          <div class="aside-modal">
            <div style="margin: 25px">
              <div style="height: 230px"></div>
              <div style="display: flex;flex-direction: column;">
                <span class="title">{{ ref ? '登录以继续' : '登录' }}</span>
                <div style="height: 20px"></div>

                <el-input placeholder="用户名" v-model="username"></el-input>
                <div style="height: 10px"></div>
                <el-input placeholder="密码" v-model="password" show-password></el-input>

                <transition name="el-fade-in">
                  <div v-if="username !== '' && password !== ''">
                    <div style="height: 20px"></div>
                    <el-button icon="el-icon-right" type="info" style="margin-left: 50%;transform: translateX(-50%)"
                               plain circle @click="onLoginButtonClick"></el-button>
                  </div>
                </transition>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-aside>
  </el-container>
</template>

<script>
import api from "@/api/api";
import qs from "qs";

export default {
  name: "Login",
  data() {
    return {
      username: '',
      password: '',
      backgroundImageUrl: '',
      urls: [
          "https://side-effect-1302668817.cos.ap-nanjing.myqcloud.com/95b7eb2d61a47da96895e46f3c146626.jpg",
          "https://side-effect-1302668817.cos.ap-nanjing.myqcloud.com/20760b79fccca8b41e5b5b3c10802155b85aab25.jpg",
          "https://side-effect-1302668817.cos.ap-nanjing.myqcloud.com/20160910233049_emswi.jpeg"
      ],
      ref: null
    }
  },
  async mounted() {
    if (localStorage.getItem('Authorization') !== null) {
      await this.$router.push('/')
      return
    }
    let index = Math.floor(Math.random() * 3)
    this.backgroundImageUrl = `url("${this.urls[index]}")`
    if (this.$route.query.ref) {
      this.ref = this.$route.query.ref
      if (this.ref === '/login') {
        this.ref = null
      }
    }
  },
  methods: {
    onLoginButtonClick: function () {
      const that = this;
      this.$http({
        method: 'post',
        url: '/auth/web',
        data: qs.stringify({
          username: this.username,
          password: this.password
        }),
        headers: {
          'content-type': 'application/x-www-form-urlencoded'
        }
      }).then(res => {
        if (res.data.code === 10000) {
          this.$store.commit('setToken', res.data.data.token);
          // window.sessionStorage.setItem("adminLogin", "true")
          // window.sessionStorage.setItem("adminId", res.data.data.id)
          this.$router.push('/user'); //跳转到首页
        } else if (res.data.code === 40001) {
          that.$message.error("用户名或密码错误");
        } else {
          that.$message.error(res.data.message);
        }
      })

    }
  }
}
</script>

<style scoped>
body {
  margin: 0;
  padding: 0;
}

@keyframes on-show-fade-in {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.title {
  width: 100%;
  text-align: center;
  font-weight: normal;
  font-size: 25px;
}

.aside-modal {
  z-index: 5;
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.3);
}

.aside-background {
  z-index: 2;
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  height: 100%;
  width: 350px;
  overflow: hidden;
  opacity: 0;
  animation: on-show-fade-in 1s;
  animation-delay: 0.3s;
  animation-fill-mode: forwards;
}

.aside-background::after {
  content: '';
  z-index: 1;
  position: absolute;
  top: -10%;
  bottom: -10%;
  height: 120%;
  width: 350px;
  /*background-image: url("https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg");*/
  background-image: var(--backgroundImageUrl);
  background-attachment: fixed;
  background-repeat: no-repeat;
  background-position: center center;
  background-size: cover;
  -webkit-filter: blur(10px);
  -moz-filter: blur(10px);
  -ms-filter: blur(10px);
  -o-filter: blur(10px);
  filter: blur(10px);
  transition-delay: 10s;
  transition: all 1.0s ease-in-out;
}

.background-image {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background-image: var(--backgroundImageUrl);
  background-repeat: no-repeat;
  background-position: center center;
  background-size: cover;
}
</style>