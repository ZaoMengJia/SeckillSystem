<template>
  <!-- 秒杀活动管理界面 -->
  <div class="bread">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>秒杀活动</el-breadcrumb-item>
      <el-breadcrumb-item>秒杀活动列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索头部input框 -->
    <div class="table">
      <el-card>
        <el-row>
          <el-col :span="15">
            <el-input
                placeholder="请输入秒杀活动关键词"
                v-model="keyword"
                class="input-with-select"
                clearable
                @clear="getActivityList"
            >
              <el-button
                  slot="append"
                  icon="el-icon-search"
                  @click="getActivityList"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" class="add" @click="addActivityVisible = true"
            >添加秒杀活动
            </el-button>
          </el-col>
        </el-row>
        <!-- 渲染数据表格 -->
        <el-table :data="activityList" border style="width: 100%">
          <el-table-column type="index" width="100" :index="indexFn">
          </el-table-column>
          <!-- 所有的prop值必须要activityList里的属性名改成一样的 -->
          <el-table-column prop="id" label="秒杀活动id" width="100"> </el-table-column>
          <el-table-column prop="sname" label="秒杀活动名" width="150">
          </el-table-column>
          <!-- <el-table-column prop="password" label="密码" width="150">
          </el-table-column> -->
          <el-table-column prop="operate" label="操作" width="200">
            <template slot-scope="scope">
              <el-button
                  type="primary"
                  icon="el-icon-edit"
                  circle
                  @click="editActivity(scope.row)"
              ></el-button>
              <el-button
                  type="danger"
                  icon="el-icon-delete"
                  circle
                  @click="removeActivityItem(scope.row)"
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
      <!-- 添加秒杀活动dialog对话框 -->
      <el-dialog
          title="添加秒杀活动"
          :visible.sync="addActivityVisible"
          width="50%"
          @close="closeaddActivityDialog"
      >
        <el-form
            :model="addActivityForm"
            :rules="addActivityFormRul"
            ref="addActivityFormRef"
            label-width="100px"
            class="demo-ruleForm"
        >
          <el-form-item label="秒杀活动名" prop="sname">
            <el-input v-model="addActivityForm.sname" clearable></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="addActivity">确 定</el-button>
          <el-button @click="addActivityVisible = false">取 消</el-button>
        </span>
      </el-dialog>

      <!-- 修改秒杀活动dialog对话框 -->
      <el-dialog
          title="修改秒杀活动"
          :visible.sync="editActivityVisible"
          width="50%"
          @close="editActivityVisible = false"
      >
        <el-form
            :model="editActivityParams"
            :rules="editActivityParamsRul"
            ref="editActivityParams"
            label-width="100px"
            class="demo-ruleForm-edit"
        >
          <el-form-item label="秒杀活动名" prop="sname">
            <el-input v-model="editActivityParams.sname" clearable></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="edit-footer">
          <el-button type="primary" @click="editActivityList">确 定</el-button>
          <el-button @click="editActivityVisible = false">取 消</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "SeckillActivityService",
  data() {
    return {
      keyword: "",
      // 请求秒杀活动列表的参数
      queryInfo: {
        pageIndex: 1,
        pageSize: 10,
      },
      //秒杀活动列表数据
      activityList: [],
      activityParams: {
        id: 0,
        sname: "",
      },
      total: 0,
      // 添加秒杀活动dialog显示/隐藏
      addActivityVisible: false,
      //添加秒杀活动参数
      addActivityForm: {
        id: 0,
        sname: "",
      },
      //添加秒杀活动对话框验证规则
      addActivityFormRul: {
        id: [
          { required: true, message: "秒杀活动名不能为空", trigger: "blur" },
        ],
        sname: [
          { required: true, message: "价格不能为空", trigger: "blur" },
        ],
      },
      //存储获取到的秒杀活动信息
      editActivityParams: {
        id: 0,
        sname: "",
      },
      //编辑秒杀活动对话框验证规则
      editActivityParamsRul: {
        id: [
          { required: true, message: "秒杀活动名不能为空", trigger: "blur" },
        ],
        sname: [
          { required: true, message: "价格不能为空", trigger: "blur" },
        ],
      },
      editActivityVisible: false,
      dialogVisible: false,
      headers: {
        token: window.localStorage["token"],
      },
    };
  },
  methods: {
    //请求秒杀活动列表数据
    getActivityList() {
      const that = this;
      if (this.keyword === "") {
        this.$http
            .get(
                "/back/seckillActivity/getAllSeckillActivity/" +
                this.queryInfo.pageIndex +
                "/" +
                this.queryInfo.pageSize
            )
            .then((ress) => {
              if (ress.data.code === 10000) {
                that.activityList = [];
                const tempActivityList = ress.data.data.records;
                let temp = {};
                for (let i = 0; i < tempActivityList.length; i++) {
                  temp = {};
                  temp.id = tempActivityList[i].said;
                  temp.sname = tempActivityList[i].sname;
                  that.activityList.push(temp);
                }
                that.total = ress.data.data.total;
              } else {
                that.$message.error("请求秒杀活动列表失败");
              }
            });
      } else {
        this.$http
            .get(
                "/back/seckillActivity/searchActivity/" +
                this.keyword +
                "/" +
                this.queryInfo.pageIndex +
                "/" +
                this.queryInfo.pageSize
            )
            .then((ress) => {
              if (ress.data.code === 10000) {
                this.activityList = [];
                const tempActivityList = ress.data.data.records;
                let temp = {};
                for (let i = 0; i < tempActivityList.length; i++) {
                  temp = {};
                  temp.id = tempActivityList[i].said;
                  temp.sname = tempActivityList[i].sname;
                  that.activityList.push(temp);
                }
                this.total = ress.data.data.total;
              } else {
                that.$message.error("请求秒杀活动列表失败");
              }
            });
      }
    },
    //当前页面数据条数发生改变的时候触发
    handleSizeChange(val) {
      this.queryInfo.pageSize = val;
      this.getActivityList();
    },
    //当前页码发生改变的时候触发
    handleCurrentChange(val) {
      this.queryInfo.pageIndex = val;
      this.getActivityList();
    },
    //添加秒杀活动
    addActivity() {
      this.$refs.addActivityFormRef.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http.get('/back/seckillActivity/activityExist/'+this.addActivityForm.sname).then
          this.$http
              .post("/back/seckillActivity/addSeckillActivity", {
                said: this.addActivityForm.id,
                sname: this.addActivityForm.sname
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  that.$message.success("添加成功");
                } else {
                  that.$message.error("添加失败");
                }
              });
          //关闭dialog对话框
          this.addActivityVisible = false;
          this.getActivityList();
        }
      });
    },
    //关闭对话框事件
    closeaddActivityDialog() {
      //重置表单
      if (this.$refs.addActivityFormRef !== undefined) {
        this.$refs.addActivityFormRef.resetFields();
      }
    },
    //点击编辑按钮，编辑秒杀活动信息
    editActivity(row) {
      //根据秒杀活动id获取当前秒杀活动信息
      this.$http.get("/back/seckillActivity/getSeckillActivityById/" + row.id).then((ress) => {
        //存储获取到的秒杀活动信息
        this.editActivityParams.id = ress.data.data.said;
        this.editActivityParams.sname = ress.data.data.sname;
        this.editActivityVisible = !this.editActivityVisible;
      });
    },
    editActivityList() {
      this.$refs.editActivityParams.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http
              .put("/back/seckillActivity/updateSeckillActivity", {
                said: this.editActivityParams.id,
                sname: this.editActivityParams.sname,
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  this.editActivityVisible = !this.editActivityVisible;
                  that.$message.success("修改成功");
                  this.getActivityList();
                } else {
                  that.$message.error("修改失败");
                }
              });
        }
      });
    },
    //删除秒杀活动
    removeActivityItem(row) {
      const that = this;
      this.$confirm("此操作将永久删除该秒杀活动, 是否继续?", "删除秒杀活动", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.$http.delete("/back/seckillActivity/deleteSeckillActivity/" + row.id).then((ress) => {
              if (ress.data.code === 10000) {
                that.$message.success("删除秒杀活动成功");
                this.getActivityList();
              } else {
                that.$message.error("删除秒杀活动失败");
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
    this.getActivityList();
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