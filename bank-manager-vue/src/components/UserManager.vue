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
          <el-col :span="4">
            <el-button type="primary" class="add" @click="addUserVisible = true"
            >添加用户
            </el-button>
          </el-col>
        </el-row>
        <!-- 渲染数据表格 -->
        <el-table :data="userList" border style="width: 100%">
          <el-table-column type="index" width="50" :index="indexFn">
          </el-table-column>
          <!-- 所有的prop值必须要userList里的属性名改成一样的 -->
          <el-table-column prop="id" label="编号" width="100"> </el-table-column>
          <el-table-column prop="userName" label="用户名" width="100">
          </el-table-column>
          <el-table-column prop="idNumber" label="身份证号" width="300"></el-table-column>
          <!-- <el-table-column prop="password" label="密码" width="150">
          </el-table-column> -->
          <el-table-column prop="sexForm" label="性别" width="100">
          </el-table-column>
          <el-table-column prop="hasJob" label="是否工作" width="100"> </el-table-column>
          <el-table-column prop="overdueRecord" label="失约次数" width="100"> </el-table-column>
          <el-table-column prop="isDiscredit" label="是否为失约人" width="100"> </el-table-column>
          <el-table-column prop="operate" label="操作" width="200">
            <template slot-scope="scope">
              <el-button
                  type="primary"
                  icon="el-icon-edit"
                  circle
                  @click="editUser(scope.row)"
              ></el-button>
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
      <!-- 添加用户dialog对话框 -->
      <el-dialog
          title="添加用户"
          :visible.sync="addUserVisible"
          width="50%"
          @close="closeaddUserDialog"
      >
        <el-form
            :model="addUserForm"
            :rules="addUserFormRul"
            ref="addUserFormRef"
            label-width="100px"
            class="demo-ruleForm"
        >
          <el-form-item label="用户名" prop="userName">
            <el-input v-model="addUserForm.userName" clearable></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
                v-model="addUserForm.password"
                type="password"
                clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="sexForm">
            <el-radio-group v-model="addUserForm.sexForm">
              <el-radio label="男"></el-radio>
              <el-radio label="女"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="身份证号" prop="idNumber">
            <el-input v-model="addUserForm.idNumber" clearable></el-input>
          </el-form-item>
          <el-form-item label="是否工作" prop="hasJob">
            <el-radio-group v-model="addUserForm.hasJob">
              <el-radio label="否"></el-radio>
              <el-radio label="是"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="失约次数" prop="overdueRecord">
            <el-input v-model="addUserForm.overdueRecord" clearable></el-input>
          </el-form-item>
          <el-form-item label="是否为失约人" prop="isDiscredit">
            <el-radio-group v-model="addUserForm.isDiscredit">
              <el-radio label="否"></el-radio>
              <el-radio label="是"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="addUser">确 定</el-button>
          <el-button @click="addUserVisible = false">取 消</el-button>
        </span>
      </el-dialog>

      <!-- 修改用户dialog对话框 -->
      <el-dialog
          title="修改用户"
          :visible.sync="editUserVisible"
          width="50%"
          @close="editUserVisible = false"
      >
        <el-form
            :model="editUserParams"
            :rules="editUserParamsRul"
            ref="editUserParams"
            label-width="100px"
            class="demo-ruleForm-edit"
        >
          <el-form-item label="用户名" prop="userName">
            <el-input v-model="editUserParams.userName" clearable></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="sexForm">
            <el-radio-group v-model="editUserParams.sexForm">
              <el-radio label="男"></el-radio>
              <el-radio label="女"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="身份证号" prop="idNumber">
            <el-input v-model="editUserParams.idNumber" clearable></el-input>
          </el-form-item>
          <el-form-item label="是否工作" prop="hasJob">
            <el-radio-group v-model="editUserParams.hasJob">
              <el-radio label="否"></el-radio>
              <el-radio label="是"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="失约次数" prop="overdueRecord">
            <el-input v-model="editUserParams.overdueRecord" clearable></el-input>
          </el-form-item>
          <el-form-item label="是否为失约人" prop="isDiscredit">
            <el-radio-group v-model="editUserParams.isDiscredit">
              <el-radio label="否"></el-radio>
              <el-radio label="是"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <span slot="footer" class="edit-footer">
          <el-button type="primary" @click="editUserList">确 定</el-button>
          <el-button @click="editUserVisible = false">取 消</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserService",
  data() {
    return {
      keyword: "",
      // 请求用户列表的参数
      queryInfo: {
        pageIndex: 1,
        pageSize: 10,
      },
      //用户列表数据
      userList: [],
      userParams: {
        id: 0,
        userName: "",
        password: "",
        idNumber:"",
        sexForm: "",
        hasJob: "",
        overdueRecord:0,
        isDiscredit:"否",
      },
      total: 0,
      // 添加用户dialog显示/隐藏
      addUserVisible: false,
      //添加用户参数
      addUserForm: {
        userName: "",
        password: "",
        idNumber:"",
        sexForm: "男",
        hasJob: "否",
        overdueRecord:0,
        isDiscredit:"否",
      },
      //添加用户对话框验证规则
      addUserFormRul: {
        userName: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
          {
            min: 2,
            max: 10,
            message: "长度在 2 到 10 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          {
            min: 6,
            max: 16,
            message: "长度在 6 到 16 个字符",
            trigger: "blur",
          },
        ],
        idNumber: [
          { required: true, message: "身份证号不能为空", trigger: "blur" },
          {
            min: 18,
            max: 18,
            message: "长度为18个字符",
            trigger: "blur",
          },
        ],
      },
      //存储获取到的用户信息
      editUserParams: {
        id: 0,
        userName: "",
        password: "",
        idNumber:"",
        sexForm: "",
        hasJob: "",
        overdueRecord: 0,
        isDiscredit:"",
      },
      //编辑用户对话框验证规则
      editUserParamsRul: {
        userName: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
          {
            min: 2,
            max: 10,
            message: "长度在 2 到 10 个字符",
            trigger: "blur",
          },
        ],
        idNumber: [
          { required: true, message: "身份证号不能为空", trigger: "blur" },
          {
            min: 18,
            max: 18,
            message: "长度为18个字符",
            trigger: "blur",
          },
        ]
      },
      editUserVisible: false,
      dialogVisible: false,
      //上传头像
      headers: {
        token: window.localStorage["token"],
      },
    };
  },
  methods: {
    //请求用户列表数据
    getUserList() {
      const that = this;
      if (this.keyword === "") {
        this.$http
            .get(
                "/back/admin/getAllUser/" +
                this.queryInfo.pageIndex +
                "/" +
                this.queryInfo.pageSize
            )
            .then((ress) => {
              if (ress.data.code === 10000) {
                that.userList = [];
                const tempUserList = ress.data.data.records;
                let temp = {};
                for (let i = 0; i < tempUserList.length; i++) {
                  temp = {};
                  temp.id = tempUserList[i].uid;
                  temp.userName = tempUserList[i].userName;
                  temp.idNumber = tempUserList[i].idNumber;
                  temp.password = tempUserList[i].password;
                  temp.sexForm = tempUserList[i].sex ? "女" : "男";
                  temp.hasJob = tempUserList[i].hasJob ? "是":"否";
                  temp.overdueRecord = tempUserList[i].overdueRecord;
                  temp.isDiscredit = tempUserList[i].isDiscredit ? "是":"否";
                  that.userList.push(temp);
                }
                that.total = tempUserList.length;
              } else {
                that.$message.error("请求用户列表失败");
              }
            });
      } else {
        this.$http
            .get(
                "/back/admin/searchUser/" +
                this.keyword +
                "/" +
                this.queryInfo.pageIndex +
                "/" +
                this.queryInfo.pageSize
            )
            .then((ress) => {
              if (ress.data.code === 10000) {
                this.userList = [];
                const tempUserList = ress.data.data.records;
                let temp = {};
                for (let i = 0; i < tempUserList.length; i++) {
                  temp = {};
                  temp.id = tempUserList[i].uid;
                  temp.userName = tempUserList[i].userName;
                  temp.password = tempUserList[i].password;
                  temp.idNumber = tempUserList[i].idNumber
                  temp.sexForm = tempUserList[i].sex ? "女" : "男";
                  temp.hasJob = tempUserList[i].hasJob ? "是" : "否";
                  temp.overdueRecord = tempUserList[i].overdueRecord;
                  temp.isDiscredit = tempUserList[i].isDiscredit ? "是" : "否";
                  this.userList.push(temp);
                }
                this.total = tempUserList.length;
              } else {
                that.$message.error("请求用户列表失败");
              }
            });
      }
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
    //添加用户
    addUser() {
      this.$refs.addUserFormRef.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http.get('/back/admin/userNameExist/'+this.addUserForm.userName).then
          this.$http
              .post("/back/admin/addUser", {
                userName: this.addUserForm.userName,
                idNumber: this.addUserForm.idNumber,
                password: this.addUserForm.password,
                sex: this.addUserForm.sexForm !== "男",
                hasJob: this.addUserForm.hasJob !== "否",
                overdueRecord: this.addUserForm.overdueRecord,
                isDiscredit: this.addUserForm.isDiscredit !== "否",
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  that.$message.success("添加成功");
                } else {
                  that.$message.error("添加失败");
                }
              });
          //关闭dialog对话框
          this.addUserVisible = false;
          this.getUserList();
        }
      });
    },
    //关闭对话框事件
    closeaddUserDialog() {
      //重置表单
      if (this.$refs.addUserFormRef !== undefined) {
        this.$refs.addUserFormRef.resetFields();
      }
    },
    //点击编辑按钮，编辑用户信息
    editUser(row) {
      //根据用户id获取当前用户信息
      this.$http.get("/back/admin/getUserById/" + row.id).then((ress) => {
        //存储获取到的用户信息
        this.editUserParams.id = ress.data.data.uid;
        this.editUserParams.userName = ress.data.data.userName;
        this.editUserParams.password = ress.data.data.password;
        this.editUserParams.sexForm = ress.data.data.sex ? "女" : "男";
        this.editUserParams.idNumber = ress.data.data.idNumber;
        this.editUserParams.hasJob = ress.data.data.hasJob ? "是" : "否";
        this.editUserParams.overdueRecord = ress.data.data.overdueRecord;
        this.editUserParams.isDiscredit = ress.data.data.isDiscredit ? "是" : "否";
        this.editUserVisible = !this.editUserVisible;
      });
    },
    editUserList() {
      this.$refs.editUserParams.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http
              .put("/back/admin/updateUser", {
                id: this.editUserParams.uid,
                userName: this.editUserParams.userName,
                password: this.editUserParams.password,
                idNumber: this.editUserParams.idNumber,
                hasJob: this.editUserParams.hasJob !== "否",
                overdueRecord: this.editUserParams.overdueRecord,
                isDiscredit: this.editUserParams.isDiscredit !== "否",
                sex: this.editUserParams.sexForm !== "男",
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  this.editUserVisible = !this.editUserVisible;
                  that.$message.success("修改成功");
                  this.getUserList();
                } else {
                  that.$message.error("修改失败");
                }
              });
        }
      });
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
            this.$http.delete("/back/admin/deleteUser/" + row.id).then((ress) => {
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