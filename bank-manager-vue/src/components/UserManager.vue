<template>
  <!-- 用户管理界面 -->
  <div class="bread">

    <!-- 搜索头部input框 -->
    <div class="table">
      <span class="font-title">用户</span><br>
      <div style="height: 10px"/>
      <div>
        <div style="display: inline-flex; justify-content: center; align-items: center; margin-bottom: 20px;">
          <el-input
              style="width: 300px"
              placeholder="输入关键词"
              v-model="keyword"
              class="input-with-select"
              clearable
              @clear="getUserList"
          >
            <el-button
                slot="append"
                icon="el-icon-search"
                @click="getUserList"
            />
          </el-input>
          <transition name="el-fade-in-linear">
            <el-radio-group v-if="keyword !== ''" style="margin-left: 16px;" v-model="type">
              <el-radio-button label="nickname">昵称</el-radio-button>
              <el-radio-button label="realName">真实姓名</el-radio-button>
            </el-radio-group>

          </transition>
        </div>

        <div v-if="!isLoading">
          <!-- 渲染数据表格 -->
          <el-table :data="userList" :header-cell-style="{'text-align':'center'}"
                    :cell-style="{'text-align':'center'}" style="width: 100%">
            <el-table-column type="index" width="30" align="right" :index="indexFn">
            </el-table-column>
            <!-- 所有的prop值必须要userList里的属性名改成一样的 -->
            <el-table-column prop="avatarUrl" label="头像" align="right" width="60">
              <template slot-scope="scope">
                <el-avatar :src="scope.row.avatarUrl"></el-avatar>
              </template>
            </el-table-column>
            <el-table-column prop="id" width="300" label="用户编号"></el-table-column>

            <el-table-column prop="nickname" label="昵称" width="100"></el-table-column>
            <el-table-column prop="realName" label="真实姓名" width="100"></el-table-column>
            <el-table-column prop="idCard" label="身份证号" width="200"></el-table-column>
            <!-- <el-table-column prop="password" label="密码" width="150">
            </el-table-column> -->
            <el-table-column prop="sexForm" label="性别" width="100">
              <template slot-scope="scope">
                {{ scope.row.gender !== 0 ? '女' : '男' }}
              </template>
            </el-table-column>

            <el-table-column prop="hasJob" label="是否工作" width="100"></el-table-column>
            <el-table-column prop="overdueRecord" label="失约次数" width="100"></el-table-column>
            <el-table-column prop="isDiscredit" label="是否为失约人" width="120"></el-table-column>
            <el-table-column prop="operate" label="操作">
              <template slot-scope="scope">
                <el-button
                    type="danger"
                    icon="el-icon-delete"
                    plain
                    circle
                    @click="removeUserItem(scope.row)"
                ></el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页功能 -->
          <el-pagination
              style="float:right;margin-top: 10px;"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="queryInfo.pageIndex"
              :page-sizes="[5,10,15,20]"
              :page-size="queryInfo.pageSize"
              layout="prev, pager, next"
              :total="total"
              :hide-on-single-page="true"
          >
          </el-pagination>
        </div>
        <el-skeleton v-if="isLoading" :count="2" animated/>

      </div>

    </div>
  </div>
</template>

<script>
import api from "@/api/api";

export default {
  name: "UserService",
  data() {
    return {
      isLoading: false,
      keyword: "",
      //用户搜索的方式
      type: "nickname",
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
        realname: "",
        password: "",
        idNumber: "",
        avartarUrl: "",
        sexForm: "",
        hasJob: "",
        overdueRecord: 0,
        isDiscredit: "否",
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
      this.isLoading = true;
      let [res, err] = this.keyword === '' ?
          await api.getWeixinUserList(this.queryInfo.pageIndex, this.queryInfo.pageSize) :
          await api.searchWeixinUserList(this.keyword, this.type, this.queryInfo.pageIndex, this.queryInfo.pageSize);
      this.isLoading = false;
      if (err !== null) {
        this.$message.error(err.message);
        return;
      }
      this.userList = res.data.data.data;
      this.total = res.data.data.total;
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
          .then(async () => {
            let [, err] = await api.deleteWeixinUser(row.id);
            if (err != null) {
              this.$message.error(err.message);
              return;
            }

            await this.getUserList();
          })
          .catch(() => {
          })
    },
    // 表格编号
    indexFn(index) {
      index =
          index + 1 + (this.queryInfo.pageIndex - 1) * this.queryInfo.pageSize;
      return index;
    },
  },
  mounted() {
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