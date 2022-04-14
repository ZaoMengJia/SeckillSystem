<template>
  <div class="back">
    <div class="mengban">
      <div class="center">
        <div class="left">
          <div class="image">
            <img src="../assets/造梦珈橙.png" alt="">
          </div>
        </div>
        <div class="right">

          <div class="login_title">管理员登录</div>

          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item prop="account">
              <el-input label="" class="username" v-model="ruleForm.account" placeholder="请输入账号" clearable></el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input label="" class="password" type="password" v-model="ruleForm.password" placeholder="请输入密码"
                        clearable></el-input>
            </el-form-item>

            <Vcode :show="isShow" @success="success" @close="close"/>
            <el-form-item>
              <el-button class="login" type="primary" @click="submitForm('ruleForm')">登&nbsp;&nbsp;&nbsp;录</el-button>
            </el-form-item>
          </el-form>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Vcode from "vue-puzzle-vcode"
import qs from "qs";
import api from "@/api/api";
export default {
  name: "Login",
  data() {
    return {
      isShow: false, // 验证码模态框是否出现
      ruleForm: {    //初始化
        account: '',
        password: ''
      },
      rules: {   //校验
        account: [
          {required: true, message: '账号不能为空', trigger: 'blur'},
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          { min: 6, max: 16, message: '长度在 6 到 16 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  components: {
    Vcode
  },
  methods: {
    submitForm() {
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          this.isShow = true;
        }
      })
    },
    //用户通过了验证
    success() {
      this.isShow = false; // 通过验证后，需要手动隐藏模态框
      let {account, password} = this.ruleForm;
      const that = this;
      this.$http({
        method: 'post',
        url: '/auth/web',
        data: qs.stringify({
          username : account,
          password: password
        }),
        headers: {
          'content-type': 'application/x-www-form-urlencoded'
        }
      })
      .then(res => {
        if(res.data.code === 10000) {
          this.$store.commit('setToken', res.data.data.token);
          // window.sessionStorage.setItem("adminLogin", "true")
          // window.sessionStorage.setItem("adminId", res.data.data.id)
          this.$router.push('/user'); //跳转到首页
        }
        else if(res.data.code === 40001) {
          that.$message.error("用户名或密码错误");
        }
        else {
          that.$message.error(res.data.message);
        }
      });
    },
    // 用户点击遮罩层，应该关闭模态框
    close() {
      this.isShow = false;
    }
  },
  mounted() {
    window.sessionStorage.clear()
  }
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}

html.body {
  margin: 0;
  height: 100%;
}

.back {
  height: 100%;
  width: 100%;
  background-image: url("../assets/login/海洋.jpg");
  background-repeat: no-repeat;
  background-size: 100% 100%;
  background-position: top;
  position: fixed;
}

.mengban {
  height: 100%;
  width: 100%;
  background-color: rgba(255,165,0,0.7);
}

.center {
  float: left;
  width: 1059px;
  height: 585px;
  background-color: #fff;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 50px;
  box-shadow: 0 2px 6px 0 rgba(0, 0, 0, .4);
}

.left {
  float: left;
  width: 58%;
  height: 100%;
  background-color: rgba(253, 190, 1,0.3);
  border-radius: 50px 0 0 50px;
  position: relative;
  overflow: hidden;
}

.left .image {
  height: 300px;
  width: 400px;
  position: absolute;
  margin: 0 auto;
  top: 100px;
  right: 0px;
  bottom: 0px;
  left: 0px;
}

.left img {
  height: 171px;
  width: 390px;
  margin: 78px 0 0 0;
}

.right {
  float: left;
  width: 340px;
  margin-left: -50px;
}

.username{
  margin-left: 50px;
}

.username .el-input__inner {
  background: url(../assets/login/账号.png) no-repeat 10px center;
  padding: 0px 50px;
  width: 340px;
  height: 51px;
  font-size: 18px;
  border: none;
  border-bottom: 1px solid;
}

.password{
  margin-left: 50px;
}

.password .el-input__inner {
  background: url(../assets/login/密码.png) no-repeat 10px center;
  padding: 0px 50px;
  width: 340px;
  height: 60px;
  font-size: 18px;
  border: none;
  border-bottom: 1px solid;
}

.login {
  width: 340px;
  height: 45px;
  background-color: rgba(2, 153, 208, 1);
  font-size: 22px;
  border-radius: 3px;
  margin-top: 50px;
}

.login_title {
  width: 130px;
  margin: 103px 160px 50px 210px;
  color: rgba(2, 153, 208, 1);
  font-size: 24px;
}
</style>