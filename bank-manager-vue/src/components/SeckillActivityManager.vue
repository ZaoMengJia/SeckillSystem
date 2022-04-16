<template>
  <!-- 秒杀活动管理界面 -->
  <div class="bread">
    <div class="table">
      <div>
        <div class="header" style="display: flex;">
          <div style="height: 10px"/>
          <span style="font-size: 32px; flex-basis: 62%; font-weight: bolder">已添加的抢购活动</span><br>
          <div style="flex-basis: 38%">
            <div style="display: inline-flex; align-items: end">
              <el-input
                  style="margin-right: 16px;"
                  placeholder="输入关键词"
                  v-model="keyword"
                  class="input-with-select"
                  clearable
                  @clear="getActivityList"
              >
                <el-button slot="append" icon="el-icon-search" @click="getActivityList"/>
              </el-input>
              <el-button type="primary" plain @click="insertActivityDialog.visible = true">新增</el-button>
            </div>

          </div>
<!--          <div style="flex-basis: 25%"/>-->

        </div>
        <div style="height: 40px"/>
        <el-skeleton v-if="isLoading" :rows="12" animated/>
        <div v-if="!isLoading && activityList.length > 0">
          <div class="activity-card-parent">
            <el-card @click.native="toDetail(activity.id)" shadow="never" v-for="(activity, index) in activityList" :key="activity.id" class="activity-card" :body-style="{ padding: '0px' }" style="margin-right: 12px; margin-bottom: 12px">
              <el-image
                  style="height: 300px;"
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
              </div>
            </el-card>
          </div>

          <!-- 分页功能 -->
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="queryInfo.pageIndex"
              :page-sizes="[5,10,15,20]"
              :page-size="queryInfo.pageSize"
              layout="total, prev, pager, next"
              :total="total"
              style="float:right;margin-bottom: 16px"
              :hide-on-single-page="true"
          >
          </el-pagination>
        </div>
        <el-empty v-if="!isLoading && activityList.length === 0" description="没有已添加的抢购活动"/>

      </div>
      <!-- 添加秒杀活动dialog对话框 -->
      <el-dialog title="编辑" :visible.sync="insertActivityDialog.visible" :close-on-click-modal="false">
        <el-form label-position="left" label-width="80px">
          <el-form-item label="名称">
            <el-input v-model="insertActivityDialog.data.name"/>
          </el-form-item>
          <el-form-item label="活动详情">
            <el-input type="textarea" v-model="insertActivityDialog.data.detail"/>
          </el-form-item>
          <el-form-item label="时间">
            <el-date-picker
                v-model="insertActivityDialog.data.date"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="图片地址">
            <el-input v-model="insertActivityDialog.data.image"/>
          </el-form-item>

        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="commitInsertActivityInfo">应用</el-button>
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
      insertActivityDialog: {
        visible: false,
        data: {}
      },
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
        name: "",
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
    async commitInsertActivityInfo() {
      //检验信息
      let beginTime = this.insertActivityDialog.data.date[0];
      let endTime = this.insertActivityDialog.data.date[1];

      if(beginTime == null || endTime == null || beginTime >= endTime) {
        this.$message.error('日期填写有误');
        return;
      }

      let image = '';
      try {
        image = decodeURIComponent(this.insertActivityDialog.data.image);
      } catch (e) {
        image = this.insertActivityDialog.data.image;
      }

      let [, err] = await api.insertSeckillActivity({
        id: this.seckillActivityId,
        name: this.insertActivityDialog.data.name,
        detail: this.insertActivityDialog.data.detail,
        image: image,
        beginTime: beginTime,
        endTime: endTime
      });

      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      this.insertActivityDialog = {
        visible: false,
        data: {}
      };
      await this.getActivityList();
    },
    toDetail(id) {
      console.log(id)
      this.$router.push({
        name: 'seckillActivityDetail',
        query: {
          id
        }
      });
    },

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
          this.$http.get('/back/web/seckillActivity/activityExist/' + this.addActivityForm.name).then
          this.$http
              .post("/back/web/seckillActivity/addSeckillActivity/", {
                id: this.addActivityForm.id,
                name: this.addActivityForm.name,
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

.activity-card-parent {
  display: flex;
  flex-wrap: wrap;
  width: 1200px;
}

.activity-card {
  border-radius: 10px;
  width: 32%;
  height: 450px;
  box-sizing: border-box;

  cursor: pointer;
}

.activity-card:hover {
  border-color: #bebebe;
}


</style>