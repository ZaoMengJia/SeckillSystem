<template>
  <!-- 用户管理界面 -->
  <div class="bread">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>用户</el-breadcrumb-item>
      <el-breadcrumb-item>用户列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索头部input框 -->
    <div class="table">
      <el-card>
        <el-row>
          <el-col :span="15">
            <el-input
                placeholder="请输入用户名关键词"
                v-model="keyword"
                class="input-with-select"
                clearable
                @clear="getUserList"
            >
              <el-button
                  slot="append"
                  icon="el-icon-search"
                  @click="getUserList"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="10
">
            <el-radio-group v-model="type">
              <el-radio label="nickname">nickname</el-radio>
              <el-radio label="realname">realname</el-radio>
            </el-radio-group>
          </el-col>
        </el-row>
        <!-- 渲染数据表格 -->
        <el-table :data="userList" border style="width: 100%">
          <el-table-column type="index" width="50" :index="indexFn">
          </el-table-column>
          <!-- 所有的prop值必须要userList里的属性名改成一样的 -->
          <el-table-column prop="id" label="编号" width="100"> </el-table-column>
          <el-table-column prop="nickname" label="昵称" width="100"></el-table-column>
          <el-table-column prop="realName" label="真实姓名" width="100"></el-table-column>
          <el-table-column prop="idCard" label="身份证号" width="300"></el-table-column>
          <!-- <el-table-column prop="password" label="密码" width="150">
          </el-table-column> -->
          <el-table-column prop="sexForm" label="性别" width="100">
            <template slot-scope="scope">
              {{ scope.row.gender === 0 ? '女' : '男' }}
            </template>
          </el-table-column>
          <el-table-column prop="avatarUrl" label="头像" width="100">
<!--            <template slot-scope="scope">-->
<!--              <img-->
<!--                  :src="scope.row.avartarUrl"-->
<!--                  alt=""-->
<!--                  style="width: 50px; height: 50px"-->
<!--              />-->
<!--            </template>-->
            <template slot-scope="scope">
              <el-avatar :src="scope.row.avatarUrl"></el-avatar>
            </template>
          </el-table-column>
          <el-table-column prop="hasJob" label="是否工作" width="100"> </el-table-column>
          <el-table-column prop="overdueRecord" label="失约次数" width="100"> </el-table-column>
          <el-table-column prop="isDiscredit" label="是否为失约人" width="100"> </el-table-column>
          <el-table-column prop="operate" label="操作" width="200">
            <template slot-scope="scope">
              <el-button
                  type="danger"
                  icon="el-icon-delete"
                  circle
                  @click="removeUserItem(scope.row)"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页功能 -->
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryInfo.pageIndex"
            :page-sizes="[5,10,15,20]"
            :page-size="queryInfo.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
        >
        </el-pagination>
      </el-card>
    </div>
  </div>
</template>

<script>
import api from "@/api/api";
export default {
  name: "UserService",
  data() {
    return {
      keyword: "",
      //用户搜索的方式
      type:"nickname",
      // 请求用户列表的参数
      queryInfo: {
        pageIndex: 1,
        pageSize: 10,
      },
      //用户列表数据
      userList: [],
      userParams: {
        id: 0,
        nickname: "",
        realname:"",
        password: "",
        idNumber:"",
        avartarUrl:"",
        sexForm: "",
        hasJob: "",
        overdueRecord:0,
        isDiscredit:"否",
      },
      total: 0,
      //上传头像
      headers: {
        token: window.localStorage["token"],
      },
    };
  },
  methods: {
    //请求用户列表数据
    async getUserList() {
      const that = this;
      if (this.keyword === "") {
        let [res, err] = await api.getWeixinUserList(this.queryInfo.pageIndex, this.queryInfo.pageSize, this.$store.getters.token);
        if(err !== null) {
          this.$message.error(err.message);
          return;
        }
        this.userList = res.data.data.data;
        this.total = res.data.data.total;
      }
      // else {
      //   this.$http
      //       .get(
      //           "/back/web/weixin-user/search?keyword=" +
      //           this.keyword +
      //           "&type=" +
      //           this.type+
      //           "&pageNum="+
      //           this.queryInfo.pageIndex +
      //           "&pageSize=" +
      //           this.queryInfo.pageSize
      //       )
      //       .then((ress) => {
      //         if (ress.data.code === 10000) {
      //           this.userList = [];
      //           const tempUserList = ress.data.data.records;
      //           let temp = {};
      //           for (let i = 0; i < tempUserList.length; i++) {
      //             temp = {};
      //             temp.id = tempUserList[i].id;
      //             temp.nickname = tempUserList[i].nickname;
      //             temp.realName = tempUserList[i].realName;
      //             temp.idNumber = tempUserList[i].idCard;
      //             temp.password = tempUserList[i].password;
      //             temp.sexForm = tempUserList[i].gender ? "女" : "男";
      //             temp.hasJob = tempUserList[i].hasJob ? "是":"否";
      //             temp.overdueRecord = tempUserList[i].overdueRecord;
      //             temp.isDiscredit = tempUserList[i].isDiscredit ? "是":"否";
      //             this.userList.push(temp);
      //           }
      //           this.total = ress.data.data.total;
      //         } else {
      //           that.$message.error("请求用户列表失败");
      //         }
      //       });
      // }
    },
    //当前页面数据条数发生改变的时候触发
    handleSizeChange(val) {
      this.queryInfo.pageSize = val;
      this.getUserList();
    },
    //当前页码发生改变的时候触发
    handleCurrentChange(val) {
      this.queryInfo.pageIndex = val;
      this.getUserList();
    },
    //删除用户
    removeUserItem(row) {
      const that = this;
      this.$confirm("此操作将永久删除该用户, 是否继续?", "删除用户", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.$http.delete("/back/web/weixin-user/" + row.id).then((ress) => {
              if (ress.data.code === 10000) {
                that.$message.success("删除用户成功");
                this.getUserList();
              } else {
                that.$message.error("删除用户失败");
              }
            });
          })
          .catch(() => {
            //点击取消按钮，取消该次操作
            that.$message({
              type: "info",
              message: "已取消删除",
            });
          });
    },
    // 表格编号
    indexFn(index) {
      index =
          index + 1 + (this.queryInfo.pageIndex - 1) * this.queryInfo.pageSize;
      return index;
    },
  },
  created() {
    this.getUserList();
  },
}
</script>

<style scoped>
.bread {
  margin-left: 250px;
}

.table {
  margin: 15px 20px 0 0;
}

.add {
  background-color: rgba(2, 153, 208, 1);
  border: 1px solid rgba(149, 200, 229, 1);
}

.el-row {
  margin-bottom: 20px;
}

.el-table td,
.el-table th {
  text-align: center;
}

.el-pagination {
  margin-top: 20px;
}


</style>