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
            <el-table-column align="left" label="身份信息" width="200">
              <template slot-scope="scope">
                <span>{{ scope.row.realName }}</span><br>
                  <span>{{ scope.row.idCard }}</span>
              </template>
            </el-table-column>
            <!-- <el-table-column prop="password" label="密码" width="150">
            </el-table-column> -->
            <el-table-column prop="sexForm" label="性别" width="100">
              <template slot-scope="scope">
                {{ scope.row.gender !== 0 ? '女' : '男' }}
              </template>
            </el-table-column>

            <el-table-column prop="hasJob" label="已工作" width="100">
              <template slot-scope="scope">
                <span>{{scope.row.hasJob === null ? '未设置' : (scope.row.hasJob ? '是' : '否')}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="overdueRecord" label="失约次数" width="100">

            </el-table-column>
            <el-table-column prop="isDiscredit" label="失约人" width="120">
              <template slot-scope="scope">
                <span>{{scope.row.isDiscredit === null ? '未设置' : (scope.row.isDiscredit ? '是' : '否')}}</span>
              </template>
            </el-table-column>
            <el-table-column label="审核">
              <template slot-scope="scope">
                <el-button v-if="scope.row.audit === 0"
                    type="success"
                    plain
                    size="mini"
                    @click="setUserAudit(scope.row, 1)"
                >设为通过</el-button>
                <el-button v-else
                           type="info"
                           plain
                           size="mini"
                           @click="setUserAudit(scope.row, 0)"
                >已通过</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="operate" label="操作" width="120">
              <template slot-scope="scope">
                <el-button
                    type="primary"
                    icon="el-icon-edit"
                    circle
                    plain
                    @click="editUser(scope.row)"
                />
                <el-button
                    type="danger"
                    icon="el-icon-delete"
                    plain
                    circle
                    @click="removeUserItem(scope.row)"
                />

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

    <el-dialog
        title="编辑"
        :visible.sync="editUserDialog.visible"
        width="50%"
    >
      <el-form label-position="left" label-width="120px">
        <el-form-item label="真实姓名">
          <el-input v-model="editUserDialog.data.realName"/>
        </el-form-item>
        <el-form-item label="身份证号码">
          <el-input v-model="editUserDialog.data.idCard"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="editUserDialog.data.gender">
            <el-radio-button :label="0">男</el-radio-button>
            <el-radio-button :label="1">女</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="已工作">
          <el-radio-group v-model="editUserDialog.data.hasJob">
            <el-radio-button :label="true">是</el-radio-button>
            <el-radio-button :label="false">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="失约次数">
          <el-input-number v-model="editUserDialog.data.overdueRecord"/>
        </el-form-item>
        <el-form-item label="失约人">
          <el-radio-group v-model="editUserDialog.data.isDiscredit">
            <el-radio-button :label="true">是</el-radio-button>
            <el-radio-button :label="false">否</el-radio-button>
          </el-radio-group>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="commitEditUser">确定</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api/api";

export default {
  name: "UserService",
  data() {
    return {
      editUserDialog: {
        visible: false,
        data: {}
      },
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
    async commitEditUser() {
      let [, err] = await api.updateWeixinUser({
        id: this.editUserDialog.data.id,
        realName: this.editUserDialog.data.realName,
        idCard: this.editUserDialog.data.idCard,
        gender: this.editUserDialog.data.gender,
        hasJob: this.editUserDialog.data.hasJob,
        isDiscredit: this.editUserDialog.data.isDiscredit,
        overdueRecord: this.editUserDialog.data.overdueRecord
      });

      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      this.editUserDialog.visible = false;
      await this.getUserList();

    },
    async setUserAudit(user, audit) {
      let [, err] = await api.setWeixinUserAudit(user.id, audit);
      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      for(let i = 0; i < this.userList.length; i++) {
        let u = this.userList[i];
        if(u.id === user.id) {
          u.audit = audit;
          break;
        }
      }
    },
    editUser(user) {
      this.editUserDialog.data = {
        id: user.id,
        realName: user.realName,
        idCard: user.idCard,
        gender: user.gender,
        hasJob: user.hasJob,
        isDiscredit: user.isDiscredit,
        overdueRecord: user.overdueRecord
      };

      this.editUserDialog.visible = true;
    },

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