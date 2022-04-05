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
          <el-table-column prop="id" label="编号" width="100"> </el-table-column>
          <el-table-column prop="userName" label="用户名" width="150">
          </el-table-column>
          <el-table-column prop="productName" label="产品名" width="150"></el-table-column>
          <el-table-column prop="said" label="秒杀活动id" width="150"></el-table-column>
          <!-- <el-table-column prop="password" label="密码" width="150">
          </el-table-column> -->
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
          <el-form-item label="用户名" prop="userName">
            <el-input v-model="addOrderForm.userName" clearable></el-input>
          </el-form-item>
          <el-form-item label="产品名" prop="productName">
            <el-input v-model="addOrderForm.productName" clearable></el-input>
          </el-form-item>
          <el-form-item label="秒杀活动id" prop="said">
            <el-input v-model="addOrderForm.said" clearable></el-input>
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
          <el-form-item label="用户名" prop="userName">
            <el-input v-model="editOrderParams.userName" clearable></el-input>
          </el-form-item>
          <el-form-item label="产品名" prop="productName">
            <el-input v-model="editOrderParams.productName" clearable></el-input>
          </el-form-item>
          <el-form-item label="秒杀活动id" prop="said">
            <el-input v-model="editOrderParams.said" clearable></el-input>
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
export default {
  name: "OrderService",
  data() {
    return {
      keyword: "",
      // 请求订单列表的参数
      queryInfo: {
        userName: "",
        pageIndex: 1,
        pageSize: 10,
      },
      //订单列表数据
      orderList: [],
      orderParams: {
        id: 0,
        userName: "",
        productName: "",
        said: 0,
      },
      total: 0,
      // 添加订单dialog显示/隐藏
      addOrderVisible: false,
      //添加订单参数
      addOrderForm: {
        id: 0,
        userName: "",
        productName: "",
        said: 0,
      },
      //添加订单对话框验证规则
      addOrderFormRul: {
        userName: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
          {
            min: 2,
            max: 10,
            message: "长度在 2 到 10 个字符",
            trigger: "blur",
          },
        ],
        productName: [
          { required: true, message: "产品名不能为空", trigger: "blur" },
          {
            min: 2,
            max: 10,
            message: "长度在 2 到 10 个字符",
            trigger: "blur",
          },
        ],
      },
      //存储获取到的订单信息
      editOrderParams: {
        id: 0,
        userName: "",
        productName: "",
        said: 0,
      },
      //编辑订单对话框验证规则
      editOrderParamsRul: {
        userName: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
          {
            min: 2,
            max: 10,
            message: "长度在 2 到 10 个字符",
            trigger: "blur",
          },
        ],
        productName: [
          { required: true, message: "产品名不能为空", trigger: "blur" },
          {
            min: 2,
            max: 10,
            message: "长度在 2 到 10 个字符",
            trigger: "blur",
          },
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
    getOrderList() {
      const that = this;
      if (this.keyword === "") {
        this.$http
            .get(
                "/back/order/getAllOrders/" +
                this.queryInfo.pageIndex +
                "/" +
                this.queryInfo.pageSize
            )
            .then((ress) => {
              if (ress.data.code === 10000) {
                that.orderList = [];
                const tempOrderList = ress.data.data.records;
                let temp = {};
                for (let i = 0; i < tempOrderList.length; i++) {
                  temp = {};
                  temp.id = tempOrderList[i].oid;
                  temp.userName = tempOrderList[i].userName;
                  temp.productName = tempOrderList[i].productName;
                  temp.said = tempOrderList[i].said;
                  that.orderList.push(temp);
                }
                that.total = tempOrderList.length;
              } else {
                that.$message.error("请求订单列表失败");
              }
            });
      } else {
        this.$http
            .get(
                "/back/order/searchOrder/" +
                this.keyword +
                "/" +
                this.queryInfo.pageIndex +
                "/" +
                this.queryInfo.pageSize
            )
            .then((ress) => {
              if (ress.data.code === 10000) {
                this.orderList = [];
                const tempOrderList = ress.data.data.records;
                let temp = {};
                for (let i = 0; i < tempOrderList.length; i++) {
                  temp = {};
                  temp.id = tempOrderList[i].oid;
                  temp.userName = tempOrderList[i].userName;
                  temp.productName = tempOrderList[i].productName;
                  temp.said = tempOrderList[i].said;
                  that.orderList.push(temp);
                }
                this.total = tempOrderList.length;
              } else {
                that.$message.error("请求订单列表失败");
              }
            });
      }
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
      this.$refs.addOrderFormRef.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http
              .post("/back/order/addOrder", {
                oid: this.addOrderForm.id,
                userName: this.addOrderForm.userName,
                financialProductName: this.addOrderForm.productName,
                said: this.addOrderForm.said,
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  that.$message.success("添加成功");
                } else {
                  that.$message.error("添加失败");
                }
              });
          //关闭dialog对话框
          this.addOrderVisible = false;
          this.getOrderList();
        }
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
      this.$http.get("/back/financialOrder/getOrderById/" + row.id).then((ress) => {
        //存储获取到的订单信息
        this.editOrderParams.id = ress.data.data.oid;
        this.editOrderParams.userName = ress.data.data.userName;
        this.editOrderParams.productName = ress.data.data.financialProductName;
        this.editOrderParams.said = ress.data.data.said;
        this.editOrderVisible = !this.editOrderVisible;
      });
    },
    editOrderList() {
      this.$refs.editOrderParams.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http
              .put("/back/financialOrder/updateOrder", {
                oid: this.editOrderParams.id,
                userName: this.editOrderParams.userName,
                financialProductName: this.editOrderParams.productName,
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  this.editOrderVisible = !this.editOrderVisible;
                  that.$message.success("修改成功");
                  this.getOrderList();
                } else {
                  that.$message.error("修改失败");
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
            this.$http.delete("/back/financialOrder/deleteOrder/" + row.id).then((ress) => {
              if (ress.data.code === 10000) {
                that.$message.success("删除订单成功");
                this.getOrderList();
              } else {
                that.$message.error("删除订单失败");
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