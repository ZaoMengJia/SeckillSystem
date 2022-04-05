<template>
  <!-- 管理员管理界面 -->
  <div class="bread">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理员</el-breadcrumb-item>
      <el-breadcrumb-item>管理员列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索头部input框 -->
    <div class="table">
      <el-card>
        <el-row>
          <el-col :span="15">
            <el-input
                placeholder="请输入管理员关键词"
                v-model="keyword"
                class="input-with-select"
                clearable
                @clear="getAdminList"
            >
              <el-button
                  slot="append"
                  icon="el-icon-search"
                  @click="getAdminList"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" class="add" @click="addAdminVisible = true"
            >添加管理员
            </el-button>
          </el-col>
        </el-row>
        <!-- 渲染数据表格 -->
        <el-table :data="adminList" border style="width: 100%">
          <el-table-column type="index" width="100" :index="indexFn">
          </el-table-column>
          <!-- 所有的prop值必须要adminList里的属性名改成一样的 -->
          <el-table-column prop="id" label="编号" width="100"> </el-table-column>
          <el-table-column prop="userName" label="管理员名" width="150">
          </el-table-column>
          <el-table-column prop="idNumber" label="身份证号" width="300"></el-table-column>
          <!-- <el-table-column prop="password" label="密码" width="150">
          </el-table-column> -->
          <el-table-column prop="sexForm" label="性别" width="150">
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
                  @click="editAdmin(scope.row)"
              ></el-button>
              <el-button
                  type="danger"
                  icon="el-icon-delete"
                  circle
                  @click="removeAdminItem(scope.row)"
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
      <!-- 添加管理员dialog对话框 -->
      <el-dialog
          title="添加管理员"
          :visible.sync="addAdminVisible"
          width="50%"
          @close="closeaddAdminDialog"
      >
        <el-form
            :model="addAdminForm"
            :rules="addAdminFormRul"
            ref="addAdminFormRef"
            label-width="100px"
            class="demo-ruleForm"
        >
          <el-form-item label="管理员名" prop="userName">
            <el-input v-model="addAdminForm.userName" clearable></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
                v-model="addAdminForm.password"
                type="password"
                clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="sexForm">
            <el-radio-group v-model="addAdminForm.sexForm">
              <el-radio label="男"></el-radio>
              <el-radio label="女"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="身份证号" prop="idNumber">
            <el-input v-model="addAdminForm.idNumber" clearable></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="addAdmin">确 定</el-button>
          <el-button @click="addAdminVisible = false">取 消</el-button>
        </span>
      </el-dialog>

      <!-- 修改管理员dialog对话框 -->
      <el-dialog
          title="修改管理员"
          :visible.sync="editAdminVisible"
          width="50%"
          @close="editAdminVisible = false"
      >
        <el-form
            :model="editAdminParams"
            :rules="editAdminParamsRul"
            ref="editAdminParams"
            label-width="100px"
            class="demo-ruleForm-edit"
        >
          <el-form-item label="管理员名" prop="userName">
            <el-input v-model="editAdminParams.userName" clearable></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="sexForm">
            <el-radio-group v-model="editAdminParams.sexForm">
              <el-radio label="男"></el-radio>
              <el-radio label="女"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="身份证号" prop="idNumber">
            <el-input v-model="editAdminParams.idNumber" clearable></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="edit-footer">
          <el-button type="primary" @click="editAdminList">确 定</el-button>
          <el-button @click="editAdminVisible = false">取 消</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminManager.vue",
  data() {
    return {
      keyword: "",
      // 请求管理员列表的参数
      queryInfo: {
        pageIndex: 1,
        pageSize: 10,
      },
      //管理员列表数据
      adminList: [],
      adminParams: {
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
      // 添加管理员dialog显示/隐藏
      addAdminVisible: false,
      //添加管理员参数
      addAdminForm: {
        userName: "",
        password: "",
        idNumber:"",
        sexForm: "男",
        hasJob: "",
        overdueRecord:0,
        isDiscredit:"否",
      },
      //添加管理员对话框验证规则
      addAdminFormRul: {
        userName: [
          { required: true, message: "管理员名不能为空", trigger: "blur" },
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
      //存储获取到的管理员信息
      editAdminParams: {
        id: 0,
        userName: "",
        password: "",
        idNumber:"",
        sexForm: "",
        hasJob: "",
        overdueRecord:0,
        isDiscredit:"否",
      },
      //编辑管理员对话框验证规则
      editAdminParamsRul: {
        userName: [
          { required: true, message: "管理员名不能为空", trigger: "blur" },
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
      editAdminVisible: false,
      dialogVisible: false,
      //上传头像
      headers: {
        token: window.localStorage["token"],
      },
    };
  },
  methods: {
    //请求管理员列表数据
    getAdminList() {
      const that = this;
      if (this.keyword === "") {
        this.$http
            .get(
                "/back/admin/getAllAdmin/" +
                this.queryInfo.pageIndex +
                "/" +
                this.queryInfo.pageSize
            )
            .then((ress) => {
              if (ress.data.code === 10000) {
                that.adminList = [];
                const tempAdminList = ress.data.data.records;
                let temp = {};
                for (let i = 0; i < tempAdminList.length; i++) {
                  temp = {};
                  temp.id = tempAdminList[i].uid;
                  temp.userName = tempAdminList[i].userName;
                  temp.idNumber = tempAdminList[i].idNumber;
                  temp.password = tempAdminList[i].password;
                  temp.sexForm = tempAdminList[i].sex ? "女" : "男";
                  temp.hasJob = tempAdminList[i].hasJob ? "是":"否";
                  temp.overdueRecord = tempAdminList[i].overdueRecord;
                  temp.isDiscredit = tempAdminList[i].isDiscredit ? "是":"否";
                  that.adminList.push(temp);
                }
                that.total = tempAdminList.length;
              } else {
                that.$message.error("请求管理员列表失败");
              }
            });
      } else {
        this.$http
            .get(
                "/back/admin/searchAdmin/" +
                this.keyword +
                "/" +
                this.queryInfo.pageIndex +
                "/" +
                this.queryInfo.pageSize
            )
            .then((ress) => {
              if (ress.data.code === 10000) {
                this.adminList = [];
                const tempAdminList = ress.data.data.records;
                let temp = {};
                for (let i = 0; i < tempAdminList.length; i++) {
                  temp = {};
                  temp.id = tempAdminList[i].uid;
                  temp.userName = tempAdminList[i].userName;
                  temp.password = tempAdminList[i].password;
                  temp.idNumber = tempAdminList[i].idNumber
                  temp.sexForm = tempAdminList[i].sex ? "女" : "男";
                  temp.hasJob = tempAdminList[i].hasJob ? "是":"否";
                  temp.overdueRecord = tempAdminList[i].overdueRecord;
                  temp.isDiscredit = tempAdminList[i].isDiscredit ? "是":"否";
                  this.adminList.push(temp);
                }
                this.total = tempAdminList.length;
              } else {
                that.$message.error("请求管理员列表失败");
              }
            });
      }
    },
    //当前页面数据条数发生改变的时候触发
    handleSizeChange(val) {
      this.queryInfo.pageSize = val;
      this.getAdminList();
    },
    //当前页码发生改变的时候触发
    handleCurrentChange(val) {
      this.queryInfo.pageIndex = val;
      this.getAdminList();
    },
    //添加管理员
    addAdmin() {
      this.$refs.addAdminFormRef.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http.get('/back/admin/userNameExist/'+this.addAdminForm.userName).then
          this.$http
              .post("/back/admin/addAdmin", {
                userName: this.addAdminForm.userName,
                idNumber: this.addAdminForm.idNumber,
                password: this.addAdminForm.password,
                sex: this.addAdminForm.sexForm !== "男",
                hasJob: this.addAdminForm.hasJob !== "否",
                overdueRecord: this.addAdminForm.overdueRecord,
                isDiscredit: this.addAdminForm.isDiscredit !== "否",
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  that.$message.success("添加成功");
                } else {
                  that.$message.error("添加失败");
                }
              });
          //关闭dialog对话框
          this.addAdminVisible = false;
          this.getAdminList();
        }
      });
    },
    //关闭对话框事件
    closeaddAdminDialog() {
      //重置表单
      if (this.$refs.addAdminFormRef !== undefined) {
        this.$refs.addAdminFormRef.resetFields();
      }
    },
    //点击编辑按钮，编辑管理员信息
    editAdmin(row) {
      //根据管理员id获取当前管理员信息
      this.$http.get("/back/admin/getAdminById/" + row.id).then((ress) => {
        //存储获取到的管理员信息
        this.editAdminParams.id = ress.data.data.uid;
        this.editAdminParams.userName = ress.data.data.userName;
        this.editAdminParams.password = ress.data.data.password;
        this.editAdminParams.sexForm = ress.data.data.sex ? "女" : "男";
        this.editAdminParams.idNumber = ress.data.data.idNumber;
        this.editAdminParams.hasJob = ress.data.data.hasJob ? "是":"否";
        this.editAdminParams.overdueRecord = ress.data.data.overdueRecord;
        this.editAdminParams.isDiscredit = ress.data.data.isDiscredit ? "是":"否";
        this.editAdminVisible = !this.editAdminVisible;
      });
    },
    editAdminList() {
      this.$refs.editAdminParams.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http
              .put("/back/admin/updateUser", {
                id: this.editAdminParams.uid,
                userName: this.editAdminParams.userName,
                password: this.editAdminParams.password,
                idNumber: this.editAdminParams.idNumber,
                hasJob: this.editAdminParams.hasJob !=="否",
                overdueRecord: this.editAdminParams.overdueRecord,
                isDiscredit: this.editAdminParams.isDiscredit !=="否",
                sex: this.editAdminParams.sexForm !== "男",
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  this.editAdminVisible = !this.editAdminVisible;
                  that.$message.success("修改成功");
                  this.getAdminList();
                } else {
                  that.$message.error("修改失败");
                }
              });
        }
      });
    },
    //删除管理员
    removeAdminItem(row) {
      const that = this;
      this.$confirm("此操作将永久删除该管理员, 是否继续?", "删除管理员", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.$http.delete("/back/admin/deleteUser/" + row.id).then((ress) => {
              if (ress.data.code === 10000) {
                that.$message.success("删除管理员成功");
                this.getAdminList();
              } else {
                that.$message.error("删除管理员失败");
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
    this.getAdminList();
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