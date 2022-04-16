<template>
  <!-- 订单管理界面 -->
  <div class="bread">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>订单</el-breadcrumb-item>
      <el-breadcrumb-item>订单列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索头部input框 -->
    <div class="table">
      <el-card>
        <el-row>
          <el-col :span="15">
            <el-input
                placeholder="请输入订单关键词"
                v-model="keyword"
                class="input-with-select"
                clearable
                @clear="getOrderList"
            >
              <el-button
                  slot="append"
                  icon="el-icon-search"
                  @click="getOrderList"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" class="add" @click="addOrderVisible = true"
            >添加订单
            </el-button>
          </el-col>
        </el-row>
        <!-- 渲染数据表格 -->
        <el-table :data="orderList" border style="width: 100%">
          <el-table-column type="index" width="100" :index="indexFn">
          </el-table-column>
          <!-- 所有的prop值必须要orderList里的属性名改成一样的 -->
          <el-table-column prop="id" label="订单号" width="100"> </el-table-column>
          <el-table-column prop="userId" label="用户id" width="150">
          </el-table-column>
          <el-table-column prop="financialProductId" label="产品id" width="150"></el-table-column>
          <el-table-column prop="seckillActivityId" label="秒杀活动id" width="150"></el-table-column>
          <el-table-column prop="quantity" label="购买产品数量" width="150"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
          <el-table-column prop="operate" label="操作" width="200">
            <template slot-scope="scope">
              <el-button
                  type="primary"
                  icon="el-icon-edit"
                  circle
                  @click="editOrder(scope.row)"
              ></el-button>
              <el-button
                  type="danger"
                  icon="el-icon-delete"
                  circle
                  @click="removeOrderItem(scope.row)"
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
      <!-- 添加订单dialog对话框 -->
      <el-dialog
          title="添加订单"
          :visible.sync="addOrderVisible"
          width="50%"
          @close="closeaddOrderDialog"
      >
        <el-form
            :model="addOrderForm"
            :rules="addOrderFormRul"
            ref="addOrderFormRef"
            label-width="100px"
            class="demo-ruleForm"
        >
          <el-form-item label="用户id" prop="userId">
            <el-input v-model="addOrderForm.userId" clearable></el-input>
          </el-form-item>
          <el-form-item label="产品id" prop="financialProductId">
            <el-input v-model="addOrderForm.financialProductId" clearable></el-input>
          </el-form-item>
          <el-form-item label="秒杀活动id" prop="seckillActivityId">
            <el-input v-model="addOrderForm.seckillActivityId" clearable></el-input>
          </el-form-item>
          <el-form-item label="购买产品数量" prop="quantity">
            <el-input v-model="addOrderForm.quantity" clearable></el-input>
          </el-form-item>
          <el-form-item label="创建时间" prop="createTime">
            <el-col :span="11">
              <el-date-picker type="date" placeholder="选择日期" v-model="addOrderForm.createTime" style="width: 100%;" ></el-date-picker>
            </el-col>
            <el-col class="line" :span="0.2">-</el-col>
            <el-col :span="11">
              <el-time-picker placeholder="选择时间" v-model="addOrderForm.createTime" style="width: 100%;" :default-time="['0:0:0']"></el-time-picker>
            </el-col>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="addOrder">确 定</el-button>
          <el-button @click="addOrderVisible = false">取 消</el-button>
        </span>
      </el-dialog>

      <!-- 修改订单dialog对话框 -->
      <el-dialog
          title="修改订单"
          :visible.sync="editOrderVisible"
          width="50%"
          @close="editOrderVisible = false"
      >
        <el-form
            :model="editOrderParams"
            :rules="editOrderParamsRul"
            ref="editOrderParams"
            label-width="100px"
            class="demo-ruleForm-edit"
        >
          <el-form-item label="用户名" prop="userId">
            <el-input v-model="editOrderParams.userId" clearable></el-input>
          </el-form-item>
          <el-form-item label="产品名" prop="financialProductId">
            <el-input v-model="editOrderParams.financialProductId" clearable></el-input>
          </el-form-item>
          <el-form-item label="秒杀活动id" prop="seckillActivityId">
            <el-input v-model="editOrderParams.seckillActivityId" clearable></el-input>
          </el-form-item>
          <el-form-item label="创建时间" prop="createTime">
            <el-col :span="11">
              <el-date-picker type="date" placeholder="选择日期" v-model="editOrderParams.createTime" style="width: 100%;"></el-date-picker>
            </el-col>
            <el-col class="line" :span="0.2">-</el-col>
            <el-col :span="11">
              <el-time-picker placeholder="选择时间" v-model="editOrderParams.createTime" style="width: 100%;"></el-time-picker>
            </el-col>
          </el-form-item>
        </el-form>
        <span slot="footer" class="edit-footer">
          <el-button type="primary" @click="editOrderList">确 定</el-button>
          <el-button @click="editOrderVisible = false">取 消</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import api from "@/api/api";

export default {
  name: "OrderService",
  data() {
    return {
      keyword: "",
      // 请求订单列表的参数
      queryInfo: {
        pageIndex: 1,
        pageSize: 10,
      },
      //订单列表数据
      orderList: [],
      orderParams: {
        id: 0,
        userId: "",
        financialProductId: "",
        seckillActivityId: "",
        quantity:0,
        createTime:"",
      },
      total: 0,
      // 添加订单dialog显示/隐藏
      addOrderVisible: false,
      //添加订单参数
      addOrderForm: {
        id: 0,
        userId: "",
        financialProductId: "",
        seckillActivityId: "",
        quantity:0,
        createTime:new Date(),
      },
      //添加订单对话框验证规则
      addOrderFormRul: {
        userId: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
        ],
        financialProductId: [
          { required: true, message: "产品名不能为空", trigger: "blur" },
        ],
        seckillActivityId:[
          {required: true, message: "秒杀活动不能为空", trigger: "blur"},
        ],
      },
      //存储获取到的订单信息
      editOrderParams: {
        id: 0,
        userId: "",
        financialProductId: "",
        seckillActivityId: "",
        quantity:0,
        createTime:"",
      },
      //编辑订单对话框验证规则
      editOrderParamsRul: {
        userId: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
        ],
        financialProductId: [
          { required: true, message: "产品名不能为空", trigger: "blur" },
        ],
        seckillActivityId:[
          {required: true, message: "秒杀活动不能为空", trigger: "blur"},
        ],
      },
      editOrderVisible: false,
      dialogVisible: false,
      headers: {
        token: window.localStorage["token"],
      },
    };
  },
  methods: {
    //请求订单列表数据
    async getOrderList() {

      let [res, err] = this.keyword === '' ?
          await api.getOrderList(this.queryInfo.pageIndex, this.queryInfo.pageSize) :
          await api.searchOrder(this.keyword, this.queryInfo.pageIndex, this.queryInfo.pageSize);

      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      this.orderList = res.data.data.data;
      this.total = res.data.data.total;
    },
    //当前页面数据条数发生改变的时候触发
    handleSizeChange(val) {
      this.queryInfo.pageSize = val;
      this.getOrderList();
    },
    //当前页码发生改变的时候触发
    handleCurrentChange(val) {
      this.queryInfo.pageIndex = val;
      this.getOrderList();
    },
    //添加订单
    addOrder() {
      this.$refs.addOrderFormRef.validate(async (valid) => {
        if(!valid) {
          return;
        }

        let [, err] = await api.insertOrder({...this.addOrderForm});
        if(err != null) {
          this.$message.error(err.message);
          return;
        }

        this.addOrderVisible = false;
        await this.getOrderList();
      });
    },
    //关闭对话框事件
    closeaddOrderDialog() {
      //重置表单
      if (this.$refs.addOrderFormRef !== undefined) {
        this.$refs.addOrderFormRef.resetFields();
      }
    },
    //点击编辑按钮，编辑订单信息
    editOrder(row) {
      //根据订单id获取当前订单信息
      this.$http.get("/back/web/order/getOrderById/" + row.id).then((ress) => {
        //存储获取到的订单信息
        this.editOrderParams.id = ress.data.data.id;
        this.editOrderParams.userId = ress.data.data.userId;
        this.editOrderParams.financialProductId = ress.data.data.financialProductId;
        this.editOrderParams.seckillActivityId = ress.data.data.seckillActivityId;
        this.editOrderParams.quantity = ress.data.data.quantity;
        this.editOrderParams.createTime = ress.data.data.createTime;

        this.editOrderVisible = !this.editOrderVisible;
      });
    },
    editOrderList() {
      this.$refs.editOrderParams.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http
              .put("/back/web/order/updateOrder", {
                id: this.editOrderParams.id,
                userId: this.editOrderParams.userId,
                financialProductId: this.editOrderParams.financialProductId,
                seckillActivityId: this.editOrderParams.seckillActivityId,
                quantity: this.editOrderParams.quantity,
                createTime: this.editOrderParams.createTime,
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  this.editOrderVisible = !this.editOrderVisible;
                  that.$message.success("修改成功");
                  this.getOrderList();
                } else {
                  that.$message.error(ress.data.message);
                }
              });
        }
      });
    },
    //删除订单
    removeOrderItem(row) {
      const that = this;
      this.$confirm("此操作将永久删除该订单, 是否继续?", "删除订单", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.$http.delete("/back/web/financialOrder/deleteOrder/" + row.id).then((ress) => {
              if (ress.data.code === 10000) {
                that.$message.success("删除订单成功");
                this.getOrderList();
              } else {
                that.$message.error(ress.data.message);
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
    this.getOrderList();
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