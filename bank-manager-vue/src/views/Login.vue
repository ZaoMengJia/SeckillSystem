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
      backgroundImageUrl: "https://side-effect-1302668817.cos.ap-nanjing.myqcloud.com/95b7eb2d61a47da96895e46f3c146626.jpg?q-sign-algorithm=sha1&q-ak=AKIDXIjg8PDGjnBktc-082UE2vmQxrPsu-fzQ0qpxiR0UdME7anvqiOPDuXYtsSMaYMe&q-sign-time=1650213313;1650216913&q-key-time=1650213313;1650216913&q-header-list=&q-url-param-list=&q-signature=f5df172b399a7a6514c1dfcebd01a70ab681f371&x-cos-security-token=9ITkORePuz7IBfBdhi8QIJ2L2sOb5sladc86f8456b9438a71fb138e88c8ee258jpChbaeQND6NlsgSlfMxP_Vm6k76HTJCTVSraHctyiBrGvQQGdv-egWWSvkSDdGO9_5lFaidRaLYiEa3N3pwPyFU-gBuZAmDQCDwnlutT6iW__wskuohC118RZrkcZtUhkVquLcWD8He1CFMr0vtQX1h0LT5llS5mSZtYIl1tgnZDp5FzUuX3phY3fSHIJBB",
      urls: [
          "https://side-effect-1302668817.cos.ap-nanjing.myqcloud.com/95b7eb2d61a47da96895e46f3c146626.jpg?q-sign-algorithm=sha1&q-ak=AKIDXIjg8PDGjnBktc-082UE2vmQxrPsu-fzQ0qpxiR0UdME7anvqiOPDuXYtsSMaYMe&q-sign-time=1650213313;1650216913&q-key-time=1650213313;1650216913&q-header-list=&q-url-param-list=&q-signature=f5df172b399a7a6514c1dfcebd01a70ab681f371&x-cos-security-token=9ITkORePuz7IBfBdhi8QIJ2L2sOb5sladc86f8456b9438a71fb138e88c8ee258jpChbaeQND6NlsgSlfMxP_Vm6k76HTJCTVSraHctyiBrGvQQGdv-egWWSvkSDdGO9_5lFaidRaLYiEa3N3pwPyFU-gBuZAmDQCDwnlutT6iW__wskuohC118RZrkcZtUhkVquLcWD8He1CFMr0vtQX1h0LT5llS5mSZtYIl1tgnZDp5FzUuX3phY3fSHIJBB",
          "https://side-effect-1302668817.cos.ap-nanjing.myqcloud.com/20760b79fccca8b41e5b5b3c10802155b85aab25.jpg?q-sign-algorithm=sha1&q-ak=AKIDg852crmD3KjDFTKayhDy6KGt06cFjTWHw2xpKZOd6LxAHlKt7Es9L1_lvqhKxU6k&q-sign-time=1650213322;1650216922&q-key-time=1650213322;1650216922&q-header-list=&q-url-param-list=&q-signature=b1ef62043766fb7cceaa6ff436c0b5b51dee26ab&x-cos-security-token=9ITkORePuz7IBfBdhi8QIJ2L2sOb5sla24484de2713ccb368c97391a505b40c0jpChbaeQND6NlsgSlfMxP-1qBj7MhqD9HTclBy0kHlN8mwspBUBjH-aF35ihnMAhgxXZO1CClmykIudlUavBckD5dUNXbIkMnI_rQ_xCL-hY5xBKWn6iL79Lc0aJk3hyIfXx4xmVII2SLZZODu4ltfpihnzdJ471pBAfq_4r4qV375g81QokY3BzrgTxDvHJ",
          "https://side-effect-1302668817.cos.ap-nanjing.myqcloud.com/20160910233049_emswi.jpeg?q-sign-algorithm=sha1&q-ak=AKID-eovuTw-Voh8d-eTIkAIQH0bNZ40aG11Ks8WYI8R5LhLgSMxlB6A31jFmtZVdz-q&q-sign-time=1650213334;1650216934&q-key-time=1650213334;1650216934&q-header-list=&q-url-param-list=&q-signature=1edc43bdbe2080a0ba40fd3b51a503769a60580d&x-cos-security-token=5O07AuGdlY8d2UwQ2ujDKGrEFvH2B1Fab4546d4d4c689fe66c999c2b215fe6d5VRoqTSTVXv5PvDnUbKzO5FrVwIbIIeVXV5Lj1XRD0c6hzw-iMzo5YgvDZc-gtEiCH6r8euFBvYEb5kdcCodnpdF72RZPmJLTnD4f8A9AHWquV_hj-SR1UxwpPc0NN2g_8XBziGRoCwmldBoML9npeJnDL5QpVXygsUkTAd5eXRC0cnsLeKBpwMsEJP6ZT2UL"
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