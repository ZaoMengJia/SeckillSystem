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
        <!--        <el-table :data="activityList" border style="width: 100%">-->
        <!--          <el-table-column type="index" width="100" :index="indexFn">-->
        <!--          </el-table-column>-->
        <!--          &lt;!&ndash; 所有的prop值必须要activityList里的属性名改成一样的 &ndash;&gt;-->
        <!--          <el-table-column prop="name" label="秒杀活动名" width="150"></el-table-column>-->
        <!--          <el-table-column prop="image" label="秒杀活动图片" width="150">-->
        <!--            <img-->
        <!--                :src="activityList.image"-->
        <!--                alt=""-->
        <!--                fit="fill"-->
        <!--                style="width: 100px; height: 100px; "-->
        <!--            />-->
        <!--          </el-table-column>-->
        <!--          <el-table-column prop="detail" label="秒杀活动描述" width="150"></el-table-column>-->
        <!--          <el-table-column prop="beginTime" label="秒杀活动开始时间" width="150"></el-table-column>-->
        <!--          <el-table-column prop="endTime" label="秒杀活动结束时间" width="150"></el-table-column>-->
        <!--          <el-table-column prop="createTime" label="秒杀活动创建时间" width="150"></el-table-column>-->
        <!--          &lt;!&ndash; <el-table-column prop="password" label="密码" width="150">-->
        <!--          </el-table-column> &ndash;&gt;-->
        <!--          <el-table-column prop="operate" label="操作" width="200">-->
        <!--            <template slot-scope="scope">-->
        <!--              <el-button-->
        <!--                  type="primary"-->
        <!--                  icon="el-icon-edit"-->
        <!--                  circle-->
        <!--                  @click="editActivity(scope.row)"-->
        <!--              ></el-button>-->
        <!--              <el-button-->
        <!--                  type="danger"-->
        <!--                  icon="el-icon-delete"-->
        <!--                  circle-->
        <!--                  @click="removeActivityItem(scope.row)"-->
        <!--              ></el-button>-->
        <!--            </template>-->
        <!--          </el-table-column>-->
        <!--        </el-table>-->

        <el-skeleton v-if="isLoading" :rows="6" animated/>
        <el-row v-else>
          <el-col :span="8" v-for="(activity, index) in activityList" :key="o">
            <el-card :body-style="{ padding: '0px' }" style="margin-right: 12px;border-radius: 5px;">
              <el-image
                  style="height: 150px;"
                  class="image"
                  :src="activity.image"
                  fit="cover"/>
              <div style="padding: 14px;">
                <span style="font-size: 24px; font-weight: bold">{{ activity.name }}</span><br>


                <span style="font-size: 14px">
                  {{
                    new Date(activity.beginTime).Format('yyyy-MM-dd hh:mm')
                  }} 至 {{ new Date(activity.endTime).Format('yyyy-MM-dd hh:mm') }}
                </span><br>
                <div style="height: 10px"/>
                {{ activity.productList.length === 0 ? '没有商品' : `${activity.productList.length}件商品` }}
                <div class="bottom clearfix">
                  <el-button type="text" class="button">详情</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>


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
          <el-form-item label="秒杀活动图片" prop="image">
            <el-upload
                class="avatar-uploader"
                action="#"
                list-type="picture-card"
                :auto-upload="false"
                :on-success="handleAddActivityImageSuccess"
                :before-upload="beforeAddActivityImageUpload"
            >
              <img
                  v-if="addActivityForm.image"
                  :src="addActivityForm.image"
                  class="activityAddImg"
              />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="秒杀活动描述" prop="detail">
            <el-input v-model="addActivityForm.detail" clearable></el-input>
          </el-form-item>
          <el-form-item label="创建时间" prop="createTime">
            <el-col :span="11">
              <el-date-picker type="date" placeholder="选择日期" v-model="addActivityForm.createTime"
                              style="width: 100%;"></el-date-picker>
            </el-col>
            <el-col class="line" :span="0.2">-</el-col>
            <el-col :span="11">
              <el-time-picker placeholder="选择时间" v-model="addActivityForm.createTime"
                              style="width: 100%;"></el-time-picker>
            </el-col>
          </el-form-item>
          <el-form-item label="开始时间" prop="beginTime">
            <el-col :span="11">
              <el-date-picker type="date" placeholder="选择日期" v-model="addActivityForm.beginTime"
                              style="width: 100%;"></el-date-picker>
            </el-col>
            <el-col class="line" :span="0.2">-</el-col>
            <el-col :span="11">
              <el-time-picker placeholder="选择时间" v-model="addActivityForm.beginTime"
                              style="width: 100%;"></el-time-picker>
            </el-col>
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-col :span="11">
              <el-date-picker type="date" placeholder="选择日期" v-model="addActivityForm.endTime"
                              style="width: 100%;"></el-date-picker>
            </el-col>
            <el-col class="line" :span="0.2">-</el-col>
            <el-col :span="11">
              <el-time-picker placeholder="选择时间" v-model="addActivityForm.endTime"
                              style="width: 100%;"></el-time-picker>
            </el-col>
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
          <el-form-item label="秒杀活动图片" prop="image">
            <el-upload
                class="avatar-uploader"
                action="#"
                list-type="picture-card"
                :auto-upload="false"
                :on-success="handleEditActivityImageSuccess"
                :before-upload="beforeEditActivityImageUpload"
            >
              <img
                  v-if="editActivityParams.image"
                  :src="editActivityParams.image"
                  class="activityAddImg"
              />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="秒杀活动描述" prop="detail">
            <el-input v-model="editActivityParams.detail" clearable></el-input>
          </el-form-item>
          <el-form-item label="创建时间" prop="createTime">
            <el-col :span="11">
              <el-date-picker type="date" placeholder="选择日期" v-model="editActivityParams.createTime"
                              style="width: 100%;"></el-date-picker>
            </el-col>
            <el-col class="line" :span="0.2">-</el-col>
            <el-col :span="11">
              <el-time-picker placeholder="选择时间" v-model="editActivityParams.createTime"
                              style="width: 100%;"></el-time-picker>
            </el-col>
          </el-form-item>
          <el-form-item label="开始时间" prop="beginTime">
            <el-col :span="11">
              <el-date-picker type="date" placeholder="选择日期" v-model="editActivityParams.beginTime"
                              style="width: 100%;"></el-date-picker>
            </el-col>
            <el-col class="line" :span="0.2">-</el-col>
            <el-col :span="11">
              <el-time-picker placeholder="选择时间" v-model="editActivityParams.beginTime"
                              style="width: 100%;"></el-time-picker>
            </el-col>
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-col :span="11">
              <el-date-picker type="date" placeholder="选择日期" v-model="editActivityParams.endTime"
                              style="width: 100%;"></el-date-picker>
            </el-col>
            <el-col class="line" :span="0.2">-</el-col>
            <el-col :span="11">
              <el-time-picker placeholder="选择时间" v-model="editActivityParams.endTime"
                              style="width: 100%;"></el-time-picker>
            </el-col>
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
import api from "@/api/api";

export default {
  name: "SeckillActivityService",
  data() {
    return {
      isLoading: false,
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
        image: "",
        detail: "",
        beginTime: new Date(),
        endTime: new Date(),
        createTime: new Date(),
      },
      total: 0,
      // 添加秒杀活动dialog显示/隐藏
      addActivityVisible: false,
      //添加秒杀活动参数
      addActivityForm: {
        sname: "",
        image: "",
        detail: "",
        beginTime: new Date(),
        endTime: new Date(),
        createTime: new Date(),
      },
      //添加秒杀活动对话框验证规则
      addActivityFormRul: {
        sname: [
          {required: true, message: "活动名不能为空", trigger: "blur"},
        ],
      },
      //存储获取到的秒杀活动信息
      editActivityParams: {
        id: 0,
        sname: "",
        image: "",
        detail: "",
        beginTime: new Date(),
        endTime: new Date(),
        createTime: new Date(),
      },
      //编辑秒杀活动对话框验证规则
      editActivityParamsRul: {
        id: [
          {required: true, message: "秒杀活动名不能为空", trigger: "blur"},
        ],
        sname: [
          {required: true, message: "价格不能为空", trigger: "blur"},
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
    async getActivityList() {
      this.isLoading = true;
      let [res, err] = this.keyword === '' ?
          await api.getSeckillActivityList(this.queryInfo.pageIndex, this.queryInfo.pageSize) :
          await api.searchSeckillActivity(this.keyword, this.queryInfo.pageIndex, this.queryInfo.pageSize);
      this.isLoading = false;
      if (err != null) {
        this.$message.error(err.message);
        return;
      }

      this.activityList = res.data.data.data;
      this.total = res.data.data.total;
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
          this.$http.get('/back/web/seckillActivity/activityExist/' + this.addActivityForm.sname).then
          this.$http
              .post("/back/web/seckillActivity/addSeckillActivity/", {
                id: this.addActivityForm.id,
                name: this.addActivityForm.sname,
                image: this.addActivityForm.image,
                detail: this.addActivityForm.detail,
                beginTime: this.addActivityForm.beginTime,
                endTime: this.addActivityForm.endTime,
                createTime: this.addActivityForm.createTime,
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  that.$message.success("添加成功");
                } else {
                  that.$message.error(ress.data.message);
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
      this.$http.get("/back/web/seckillActivity/getSeckillActivityById/" + row.id).then((ress) => {
        //存储获取到的秒杀活动信息
        this.editActivityParams.id = ress.data.data.id;
        this.editActivityParams.sname = ress.data.data.name;
        this.editActivityParams.image = ress.data.data.image;
        this.editActivityParams.detail = ress.data.data.detail;
        this.editActivityParams.beginTime = ress.data.data.beginTime;
        this.editActivityParams.endTime = ress.data.data.endTime;
        this.editActivityParams.createTime = ress.data.data.endTime;
        this.editActivityVisible = !this.editActivityVisible;
      });
    },
    editActivityList() {
      this.$refs.editActivityParams.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http
              .put("/back/web/seckillActivity/updateSeckillActivity", {
                id: this.editActivityParams.id,
                name: this.editActivityParams.sname,
                image: this.editActivityParams.image,
                detail: this.editActivityParams.detail,
                beginTime: this.editActivityParams.beginTime,
                endTime: this.editActivityParams.endTime,
                createTime: this.editActivityParams.createTime,
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  this.editActivityVisible = !this.editActivityVisible;
                  that.$message.success("修改成功");
                  this.getActivityList();
                } else {
                  that.$message.error(ress.data.message);
                }
              });
        }
      });
    },
    //删除秒杀活动
    removeActivityItem(row) {
      this.$confirm("此操作将永久删除该秒杀活动, 是否继续?", "删除秒杀活动", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(async () => {
            let [, err] = await api.deleteSeckillActivity(row.id);
            if (err != null) {
              this.$message.error(err.message);
              return;
            }

            await this.getActivityList();
          })
          .catch(() => {
          });
    },
    // 表格编号
    indexFn(index) {
      index =
          index + 1 + (this.queryInfo.pageIndex - 1) * this.queryInfo.pageSize;
      return index;
    },
    handleAddActivityImageSuccess(res, file) {
      this.addActivityForm.image = file.url;

    },
    beforeAddActivityImageUpload(file) {
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
    handleRemove(file) {
      file.clean();
    },
    handleEditActivityImageSuccess(res, file) {
      this.editActivityParams.image = file.url;
    },
    beforeEditActivityImageUpload(file) {
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

.activityAddImg {
  width: 130px;
  height: 130px;
  display: block;
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

.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}


</style>