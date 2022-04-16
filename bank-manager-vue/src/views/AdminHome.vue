<template>
  <el-container class="home_container">
    <el-header>
      <div class="title">
        <img src="../assets/造梦珈白.png" alt=""/>
        <span>后台管理系统</span>
      </div>
      <div class="quit">
        <el-button type="primary" @click="loginOut()">退出</el-button>
      </div>
    </el-header>
    <el-container>
      <!-- 左边菜单部分 -->
      <div>
        <el-aside :width="isCollapse ? '64px' : '225px'">
          <el-menu
              style="height: 0"
              :default-active="activePath"
              active-text-color="#ffd04b"
              :unique-opened="true"
              :router="true"
              :collapse="isCollapse"
              :collapse-transition="false"
          >
            <div class="nav">
              <!-- 用户管理 -->
              <el-menu-item
                  index="/user"
                  class="user"
                  @click="activeChoose('/user')"
              >
                <i class="el-icon-user"></i>
                <span slot="title">用户管理</span>
              </el-menu-item>
              <!-- 管理员管理 -->
              <el-menu-item
                  index="/admin"
                  class="admin"
                  @click="activeChoose('/admin')"
              >
                <i class="el-icon-user-solid"></i>
                <span slot="title">管理员管理</span>
              </el-menu-item>
              <!-- 秒杀活动管理 -->
              <el-menu-item
                  index="/seckillActivity"
                  class="activity"
                  @click="activeChoose('/seckillActivity')"
              >
                <i class="el-icon-shopping-cart-full"></i>
                <span slot="title">秒杀活动管理</span>
              </el-menu-item>
              <!-- 订单管理 -->
              <el-menu-item
                  index="/order"
                  class="order"
                  @click="activeChoose('/order')"
              >
                <i class="el-icon-s-order"></i>
                <span slot="title">订单管理</span>
              </el-menu-item>
              <!-- 秒杀活动明细管理 -->
<!--              <el-menu-item-->
<!--                  index="/saleProductDetail"-->
<!--                  class="detail"-->
<!--                  @click="activeChoose('/saleProductDetail')"-->
<!--              >-->
<!--                <i class="el-icon-magic-stick"></i>-->
<!--                <span slot="title">秒杀活动明细管理</span>-->
<!--              </el-menu-item>-->
              <!-- 理财产品管理 -->
              <el-menu-item
                  index="/financialProduct"
                  class="product"
                  @click="activeChoose('/financialProduct')"
              >
                <i class="el-icon-goods"></i>
                <span slot="title">理财产品管理</span>
              </el-menu-item>
            </div>
          </el-menu>
        </el-aside>
      </div>
      <el-main
          :style="{
        left:this.isCollapse ? '64px' : '225px',
        width:this.isCollapse ? '95.05%' : '82.8%'
      }">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: "AdminHome",
  data() {
    return {
      menuList: [],
      isCollapse: false,
      activePath: "",
    };
  },
  methods: {
    //退出登录
    loginOut() {
      this.$store.commit('setToken', null);
      this.$router.push("/login");
    },
    activeChoose(path) {
      window.localStorage.setItem("path", path);
      this.activePath = path;
    },
  },

  watch: {
    $route: function (to, from) {
      switch (to.path) {
        case "/financialProduct":
          this.activePath = "/financialProduct";
          break;
        case "/order":
          this.activePath = "/order";
          break;
        case "/saleProductDetail":
          this.activePath = "/saleProductDetail";
          break;
        case "/seckillActivity":
          this.activePath = "/seckillActivity";
          break;
        case "/user":
          this.activePath = "/user";
          break;
      }
    },
  },
  created() {
    switch (this.$route.path) {
      case "/financialProduct":
        this.activePath = "/financialProduct";
        break;
      case "/order":
        this.activePath = "/order";
        break;
      case "/saleProductDetail":
        this.activePath = "/saleProductDetail";
        break;
      case "/seckillActivity":
        this.activePath = "/seckillActivity";
        break;
      case "/user":
        this.activePath = "/user";
        break;
    }
  },
  beforeCreate() {
    // 修改背景色
    document.querySelector('body').setAttribute('style', 'background-color:#fff')
    // let loginStatus = window.sessionStorage.getItem("adminLogin")
    // if(loginStatus !== "true"){
    //   this.$message.error("请先登录")
    //   this.$router.push('/login')
    // }
  },
}
</script>

<style scoped>
html {
  overflow: hidden;
}

body,
#app {
  height: 100%;
  margin: 0;
  padding: 0;
}

.home_container {
  height: 100%;
}

.el-header {
  display: flex;
  position: relative;
  justify-content: space-between;
  height: 100px !important;
  line-height: 100px;
  background-color: rgba(255,165,0,0.7);
  color: #fff;
  font-size: 22px;
  font-weight: 700;
}

.el-aside {
  height: 80%;
  display: block;
  position: absolute;
  transition: width 0.15s;
  -webkit-transition: width 0.15s;
  -moz-transition: width 0.15s;
  -webkit-transition: width 0.15s;
  -o-transition: width 0.15s;
  /*box-shadow: 1px 12px 9px 12px rgba(0, 0, 0, 0.2);*/
}


.title img {
  height: 100px;
  padding: 10px 35px 10px 45px;
  vertical-align: middle;
  margin-left: -50px;
  margin-top: -10px;
}

.quit .el-button {
  width: 80px;
  background-color: rgba(0, 0, 0, 0);
  border-color: #fff;
  margin-right: 60px;
  border-radius: 25px;
  font-size: 18px;
}

.quit .el-button:hover {
  background-color: rgba(255, 255, 255, 0.3);
}

.nav {
  text-align: left;
}

.order,
.activity,
.user,
.detail,
.product,
.admin {
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}
</style>