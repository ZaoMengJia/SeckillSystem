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
        <el-table v-loading="isTableLoading" :data="adminList" :header-cell-style="{'text-align':'center'}"
                  :cell-style="{'text-align':'center'}" border style="width: 100%">
          <el-table-column type="index" width="50" :index="indexFn">
          </el-table-column>
          <!-- 所有的prop值必须要adminList里的属性名改成一样的 -->
          <el-table-column prop="id" label="编号"> </el-table-column>
          <el-table-column prop="username" label="姓名" width="150">
          </el-table-column>
          <!-- <el-table-column prop="password" label="密码" width="150">
          </el-table-column> -->
<!--          <el-table-column prop="avatarUrl" label="头像" width="100">-->
<!--              <img-->
<!--                  v-if="adminList.avatarUrl"-->
<!--                  :src="adminList.avatarUrl"-->
<!--                  alt=""-->
<!--                  style="width: 50px; height: 50px"-->
<!--              />-->
<!--          </el-table-column>-->
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
          <el-form-item label="管理员名" prop="username">
            <el-input v-model="addAdminForm.username" clearable></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
                v-model="addAdminForm.password"
                type="password"
                clearable
            ></el-input>
          </el-form-item>
<!--          <el-form-item label="头像" prop="avatarUrl">-->
<!--            <el-upload-->
<!--                class="avatar-uploader"-->
<!--                action="#"-->
<!--                list-type="picture-card"-->
<!--                :limit="1"-->
<!--                :auto-upload="false"-->
<!--                :on-success="handleAddAdminAvatarSuccess"-->
<!--                :before-upload="beforeAddAdminAvatarUpload"-->
<!--            >-->
<!--              <img-->
<!--                  v-if="addAdminForm.avatarUrl"-->
<!--                  :src="addAdminForm.avatarUrl"-->
<!--                  class="userAddImg"-->
<!--              />-->
<!--              <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
<!--            </el-upload>-->
<!--          </el-form-item>-->
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
          <el-form-item label="管理员名" prop="username">
            <el-input v-model="editAdminParams.username" clearable></el-input>
          </el-form-item>
<!--          <el-form-item label="头像" prop="avatarUrl">-->
<!--            <el-upload-->
<!--                class="avatar-uploader"-->
<!--                action="#"-->
<!--                name="picture"-->
<!--                list-type="picture-card"-->
<!--                :auto-upload="false"-->
<!--                :limit="1"-->
<!--                :on-success="handleEditAdminAvatarSuccess"-->
<!--                :before-upload="beforeEditAdminAvatarUpload"-->
<!--            >-->
<!--              <img-->
<!--                  v-if="editAdminParams.avatarUrl"-->
<!--                  :src="editAdminParams.avatarUrl"-->
<!--                  class="userEditAvatar"-->
<!--              />-->
<!--              <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
<!--            </el-upload>-->
<!--          </el-form-item>-->
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
import api from "@/api/api";

export default {
  name: "AdminManager.vue",
  data() {
    return {
      isTableLoading: false,
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
        avatarUrl: "",
      },
      total: 0,
      // 添加管理员dialog显示/隐藏
      addAdminVisible: false,
      //添加管理员参数
      addAdminForm: {
        username: "",
        password: "",
        // avatarUrl:"",
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

      },
      //存储获取到的管理员信息
      editAdminParams: {
        id: 0,
        userName: "",
        password: "",
        avatarUrl: "",
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
      },
      editAdminVisible: false,
      //上传头像
      headers: {
        token: window.localStorage["token"],
      },
    };
  },
  methods: {
    /**
     * 请求管理员列表
     * @returns {Promise<void>}
     */
    async getAdminList() {
      this.isTableLoading = true;
      let [res, err] = this.keyword === '' ?
          await api.getAdminUserList(this.queryInfo.pageIndex, this.queryInfo.pageSize) :
          await api.searchAdminUserList(this.keyword, this.queryInfo.pageIndex, this.queryInfo.pageSize);
      this.isTableLoading = false;
      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      this.adminList = res.data.data.data;
      this.total = res.data.data.total;
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
      this.$refs.addAdminFormRef.validate(async (valid) => {
        if(!valid) {
          return;
        }

        let [_, err] = await api.insertAdminUser(this.addAdminForm.username, this.addAdminForm.password);
        if(err != null) {
          this.$message.error(err.message);
          return;
        }

        this.$message.success('添加成功');
        this.addAdminVisible = false;
        await this.getAdminList();

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
      this.editAdminParams = {...row}
      this.editAdminVisible = true;
    },
    editAdminList() {
      this.$refs.editAdminParams.validate(async (valid) => {
        if(!valid) {
          return;
        }
        let [, err] = await api.editAdminUser(this.editAdminParams.id, this.editAdminParams.username, this.editAdminParams.password);
        if(err != null) {
          this.$message.error(err.message);
          return;
        }

        this.$message.info('修改成功');
        this.editAdminVisible = false;
        await this.getAdminList();
      });
    },
    //删除管理员
    removeAdminItem(row) {
      this.$confirm("此操作将永久删除该管理员, 是否继续?", "删除管理员", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(async () => {
            let [_, err] = await api.deleteAdminUser(row.id);
            if(err != null) {
              this.$message.error(err.message);
              return;
            }

            this.$message.info('已删除');
            await this.getAdminList();
          }).catch(() => {});
    },
    // 表格编号
    indexFn(index) {
      index =
          index + 1 + (this.queryInfo.pageIndex - 1) * this.queryInfo.pageSize;
      return index;
    },
    handleAddAdminAvatarSuccess(res,file) {
      this.addAdminForm.avatarUrl = file.url;
    },
    beforeAddAdminAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isPNG = file.type === "image/png";
      const isLt5M = file.size / 1024 / 1024 < 5;

      if (!(isJPG || isPNG)) {
        this.$message.error("上传头像图片只能是 JPG或PNG 格式!");
      }
      if (!isLt5M) {
        this.$message.error("上传头像图片大小不能超过 5MB!");
      }
      return (isJPG || isPNG) && isLt5M;
    },
    handleRemove(file){
      file.clean();
    },
    handleEditAdminAvatarSuccess(res, file) {
      this.editAdminParams.avatarUrl = file.url;
    },
    beforeEditAdminAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isPNG = file.type === "image/png";
      const isLt5M = file.size / 1024 / 1024 < 5;

      if (!(isJPG || isPNG)) {
        this.$message.error("上传头像图片只能是 JPG或PNG 格式!");
      }
      if (!isLt5M) {
        this.$message.error("上传头像图片大小不能超过 5MB!");
      }
      return (isJPG || isPNG) && isLt5M;
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

.avatar-uploader .el-upload {
  border: 2px solid powderblue;
  border-radius: 6px;
  cursor: pointer;
  position: absolute;
  overflow: hidden;
  top: 0px;
  left: 170px;
}

.avatar-uploader .el-upload:hover {
  border-color: powderblue;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: powderblue;
  width: 130px;
  height: 130px;
  line-height: 130px;
  text-align: center;
}

.userAddImg {
  width: 130px;
  height: 130px;
  display: block;
}

.userEditAvatar {
  width: 130px;
  height: 130px;
  display: block;
}
</style>