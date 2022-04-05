<template>
  <!-- 明细管理界面 -->
  <div class="bread">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>明细</el-breadcrumb-item>
      <el-breadcrumb-item>明细列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索头部input框 -->
    <div class="table">
      <el-card>
        <el-row>
          <el-col :span="15">
            <el-input
                placeholder="请输入明细关键词"
                v-model="keyword"
                class="input-with-select"
                clearable
                @clear="getDetailList"
            >
              <el-button
                  slot="append"
                  icon="el-icon-search"
                  @click="getDetailList"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" class="add" @click="addDetailVisible = true"
            >添加明细
            </el-button>
          </el-col>
        </el-row>
        <!-- 渲染数据表格 -->
        <el-table :data="detailList" border style="width: 100%">
          <el-table-column type="index" width="100" :index="indexFn">
          </el-table-column>
          <!-- 所有的prop值必须要detailList里的属性名改成一样的 -->
          <el-table-column prop="said" label="秒杀活动id" width="100"> </el-table-column>
          <el-table-column prop="fpid" label="产品id" width="100">
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="150"></el-table-column>
          <!-- <el-table-column prop="password" label="密码" width="150">
          </el-table-column> -->
          <el-table-column prop="operate" label="操作" width="200">
            <template slot-scope="scope">
              <el-button
                  type="primary"
                  icon="el-icon-edit"
                  circle
                  @click="editDetail(scope.row)"
              ></el-button>
              <el-button
                  type="danger"
                  icon="el-icon-delete"
                  circle
                  @click="removeDetailItem(scope.row)"
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
      <!-- 添加明细dialog对话框 -->
      <el-dialog
          title="添加明细"
          :visible.sync="addDetailVisible"
          width="50%"
          @close="closeaddDetailDialog"
      >
        <el-form
            :model="addDetailForm"
            :rules="addDetailFormRul"
            ref="addDetailFormRef"
            label-width="100px"
            class="demo-ruleForm"
        >
          <el-form-item label="秒杀活动id" prop="said">
            <el-input v-model="addDetailForm.said" clearable></el-input>
          </el-form-item>
          <el-form-item label="产品id" prop="fpid">
            <el-input v-model="addDetailForm.fpid" clearable></el-input>
          </el-form-item>
          <el-form-item label="数量" prop="quantity">
            <el-input v-model="addDetailForm.quantity" clearable></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="addDetail">确 定</el-button>
          <el-button @click="addDetailVisible = false">取 消</el-button>
        </span>
      </el-dialog>

      <!-- 修改明细dialog对话框 -->
      <el-dialog
          title="修改明细"
          :visible.sync="editDetailVisible"
          width="50%"
          @close="editDetailVisible = false"
      >
        <el-form
            :model="editDetailParams"
            :rules="editDetailParamsRul"
            ref="editDetailParams"
            label-width="100px"
            class="demo-ruleForm-edit"
        >
          <el-form-item label="秒杀活动id" prop="said">
            <el-input v-model="editDetailParams.said" clearable></el-input>
          </el-form-item>
          <el-form-item label="产品id" prop="fpid">
            <el-input v-model="editDetailParams.fpid" clearable></el-input>
          </el-form-item>
          <el-form-item label="数量" prop="quantity">
            <el-input v-model="editDetailParams.quantity" clearable></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="edit-footer">
          <el-button type="primary" @click="editDetailList">确 定</el-button>
          <el-button @click="editDetailVisible = false">取 消</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "SaleProductDetailManager",
  data() {
    return {
      keyword: "",
      // 请求明细列表的参数
      queryInfo: {
        pageIndex: 1,
        pageSize: 10,
      },
      //明细列表数据
      detailList: [],
      detailParams: {
        said: 0,
        fpid: 0,
        quantity: "",
      },
      total: 0,
      // 添加明细dialog显示/隐藏
      addDetailVisible: false,
      //添加明细参数
      addDetailForm: {
        said: 0,
        fpid: 0,
        quantity: "",
      },
      //添加明细对话框验证规则
      addDetailFormRul: {
        said: [
          { required: true, message: "秒杀活动id不能为空", trigger: "blur" },
        ],
        fpid: [
          { required: true, message: "产品id不能为空", trigger: "blur" },
        ],
      },
      //存储获取到的明细信息
      editDetailParams: {
        said: 0,
        fpid: 0,
        quantity: "",
      },
      //编辑明细对话框验证规则
      editDetailParamsRul: {
        said: [
          { required: true, message: "秒杀活动id不能为空", trigger: "blur" },
        ],
        fpid: [
          { required: true, message: "产品id不能为空", trigger: "blur" },
        ],
      },
      editDetailVisible: false,
      dialogVisible: false,
      headers: {
        token: window.localStorage["token"],
      },
    };
  },
  methods: {
    //请求明细列表数据
    getDetailList() {
      const that = this;
      if (this.keyword === "") {
        this.$http
            .get(
                "/back/saleProductDetail/getAllSaleProductDetail/" +
                this.queryInfo.pageIndex +
                "/" +
                this.queryInfo.pageSize
            )
            .then((ress) => {
              if (ress.data.code === 10000) {
                that.detailList = [];
                const tempDetailList = ress.data.data.records;
                let temp = {};
                for (let i = 0; i < tempDetailList.length; i++) {
                  temp = {};
                  temp.said = tempDetailList[i].said;
                  temp.fpid = tempDetailList[i].fpid;
                  temp.quantity = tempDetailList[i].quantity;
                  that.detailList.push(temp);
                }
                that.total = tempDetailList.length;
              } else {
                that.$message.error("请求明细列表失败");
              }
            });
      } else {
        this.$http
            .get(
                "/back/saleProductDetail/searchSaleProductDetail/" +
                this.keyword +
                "/" +
                this.queryInfo.pageIndex +
                "/" +
                this.queryInfo.pageSize
            )
            .then((ress) => {
              if (ress.data.code === 10000) {
                this.detailList = [];
                const tempDetailList = ress.data.data.records;
                let temp = {};
                for (let i = 0; i < tempDetailList.length; i++) {
                  temp = {};
                  temp.said = tempDetailList[i].said;
                  temp.fpid = tempDetailList[i].fpid;
                  temp.quantity = tempDetailList[i].quantity;
                  that.detailList.push(temp);
                }
                this.total = tempDetailList.length;
              } else {
                that.$message.error("请求明细列表失败");
              }
            });
      }
    },
    //当前页面数据条数发生改变的时候触发
    handleSizeChange(val) {
      this.queryInfo.pageSize = val;
      this.getDetailList();
    },
    //当前页码发生改变的时候触发
    handleCurrentChange(val) {
      this.queryInfo.pageIndex = val;
      this.getDetailList();
    },
    //添加明细
    addDetail() {
      this.$refs.addDetailFormRef.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http
              .post("/back/saleProductDetail/addSaleProductDetail", {
                fpid: this.addDetailForm.fpid,
                said: this.addDetailForm.said,
                quantity: this.addDetailForm.quantity,
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  that.$message.success("添加成功");
                } else {
                  that.$message.error("添加失败");
                }
              });
          //关闭dialog对话框
          this.addDetailVisible = false;
          this.getDetailList();
        }
      });
    },
    //关闭对话框事件
    closeaddDetailDialog() {
      //重置表单
      if (this.$refs.addDetailFormRef !== undefined) {
        this.$refs.addDetailFormRef.resetFields();
      }
    },
    //点击编辑按钮，编辑明细信息
    editDetail(row) {
      //根据明细id获取当前明细信息
      this.$http.get("/back/saleProductDetail/getSaleProductDetailBySaidAndFpid/" + row.said+'/'+row.fpid).then((ress) => {
        //存储获取到的明细信息
        this.editDetailParams.said = ress.data.data.said;
        this.editDetailParams.fpid = ress.data.data.fpid;
        this.editDetailParams.quantity = ress.data.data.quantity;
        this.editDetailVisible = !this.editDetailVisible;
      });
    },
    editDetailList() {
      this.$refs.editDetailParams.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http
              .put("/back/saleProductDetail/updateSaleProductDetail", {
                fpid: this.addDetailForm.fpid,
                said: this.addDetailForm.said,
                quantity: this.addDetailForm.quantity,
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  this.editDetailVisible = !this.editDetailVisible;
                  that.$message.success("修改成功");
                  this.getDetailList();
                } else {
                  that.$message.error("修改失败");
                }
              });
        }
      });
    },
    //删除明细
    removeDetailItem(row) {
      const that = this;
      this.$confirm("此操作将永久删除该明细, 是否继续?", "删除明细", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.$http.delete("/back/saleProductDetail/deleteSaleProductDetail/" + row.id).then((ress) => {
              if (ress.data.code === 10000) {
                that.$message.success("删除明细成功");
                this.getDetailList();
              } else {
                that.$message.error("删除明细失败");
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
    this.getDetailList();
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