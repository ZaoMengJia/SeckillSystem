<template>
  <!-- 理财产品管理界面 -->
  <div class="bread">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>理财产品</el-breadcrumb-item>
      <el-breadcrumb-item>理财产品列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索头部input框 -->
    <div class="table">
      <el-card>
        <el-row>
          <el-col :span="15">
            <el-input
                placeholder="请输入理财产品关键词"
                v-model="keyword"
                class="input-with-select"
                clearable
                @clear="getProductList"
            >
              <el-button
                  slot="append"
                  icon="el-icon-search"
                  @click="getProductList"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" class="add" @click="addProductVisible = true"
            >添加理财产品
            </el-button>
          </el-col>
        </el-row>
        <!-- 渲染数据表格 -->
        <el-table :data="productList" border style="width: 100%">
          <el-table-column type="index" width="100" :index="indexFn">
          </el-table-column>
          <!-- 所有的prop值必须要productList里的属性名改成一样的 -->
          <el-table-column prop="id" label="编号" width="100"> </el-table-column>
          <el-table-column prop="fname" label="理财产品名" width="150">
          </el-table-column>
          <el-table-column prop="price" label="理财产品价格(每份)" width="150"></el-table-column>
          <!-- <el-table-column prop="password" label="密码" width="150">
          </el-table-column> -->
          <el-table-column prop="operate" label="操作" width="200">
            <template slot-scope="scope">
              <el-button
                  type="primary"
                  icon="el-icon-edit"
                  circle
                  @click="editProduct(scope.row)"
              ></el-button>
              <el-button
                  type="danger"
                  icon="el-icon-delete"
                  circle
                  @click="removeProductItem(scope.row)"
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
      <!-- 添加产品dialog对话框 -->
      <el-dialog
          title="添加产品"
          :visible.sync="addProductVisible"
          width="50%"
          @close="closeaddProductDialog"
      >
        <el-form
            :model="addProductForm"
            :rules="addProductFormRul"
            ref="addProductFormRef"
            label-width="100px"
            class="demo-ruleForm"
        >
          <el-form-item label="产品名" prop="fname">
            <el-input v-model="addProductForm.fname" clearable></el-input>
          </el-form-item>
          <el-form-item label="价格" prop="price">
            <el-input v-model="addProductForm.price" clearable></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="addProduct">确 定</el-button>
          <el-button @click="addProductVisible = false">取 消</el-button>
        </span>
      </el-dialog>

      <!-- 修改产品dialog对话框 -->
      <el-dialog
          title="修改产品"
          :visible.sync="editProductVisible"
          width="50%"
          @close="editProductVisible = false"
      >
        <el-form
            :model="editProductParams"
            :rules="editProductParamsRul"
            ref="editProductParams"
            label-width="100px"
            class="demo-ruleForm-edit"
        >
          <el-form-item label="产品名" prop="fname">
            <el-input v-model="editProductParams.fname" clearable></el-input>
          </el-form-item>
          <el-form-item label="价格" prop="price">
            <el-input v-model="editProductParams.price" clearable></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="edit-footer">
          <el-button type="primary" @click="editProductList">确 定</el-button>
          <el-button @click="editProductVisible = false">取 消</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "FinancialProductService",
  data() {
    return {
      keyword: "",
      // 请求产品列表的参数
      queryInfo: {
        pageIndex: 1,
        pageSize: 10,
      },
      //产品列表数据
      productList: [],
      productParams: {
        id: 0,
        fname: "",
        price: "",
      },
      total: 0,
      // 添加产品dialog显示/隐藏
      addProductVisible: false,
      //添加产品参数
      addProductForm: {
        id: 0,
        fname: "",
        price: "",
      },
      //添加产品对话框验证规则
      addProductFormRul: {
        fname: [
          { required: true, message: "产品名不能为空", trigger: "blur" },
          {
            min: 2,
            max: 10,
            message: "长度在 2 到 10 个字符",
            trigger: "blur",
          },
        ],
        price: [
          { required: true, message: "价格不能为空", trigger: "blur" },
          {
            min: 1,
            max: 8,
            message: "长度在 1 到 8 个字符",
            trigger: "blur",
          },
        ],
      },
      //存储获取到的产品信息
      editProductParams: {
        id: 0,
        fname: "",
        price: "",
      },
      //编辑产品对话框验证规则
      editProductParamsRul: {
        fname: [
          { required: true, message: "产品名不能为空", trigger: "blur" },
          {
            min: 2,
            max: 10,
            message: "长度在 2 到 10 个字符",
            trigger: "blur",
          },
        ],
        price: [
          { required: true, message: "价格不能为空", trigger: "blur" },
          {
            min: 1,
            max: 8,
            message: "长度在 1 到 8 个字符",
            trigger: "blur",
          },
        ],
      },
      editProductVisible: false,
      dialogVisible: false,
      headers: {
        token: window.localStorage["token"],
      },
    };
  },
  methods: {
    //请求产品列表数据
    getProductList() {
      const that = this;
      if (this.keyword === "") {
        this.$http
            .get(
                "/back/financialProduct/getAllProduct/" +
                this.queryInfo.pageIndex +
                "/" +
                this.queryInfo.pageSize
            )
            .then((ress) => {
              if (ress.data.code === 10000) {
                that.productList = [];
                const tempProductList = ress.data.data.records;
                let temp = {};
                for (let i = 0; i < tempProductList.length; i++) {
                  temp = {};
                  temp.id = tempProductList[i].fpid;
                  temp.fname = tempProductList[i].fname;
                  temp.price = tempProductList[i].price;
                  that.productList.push(temp);
                }
                that.total = tempProductList.length;
              } else {
                that.$message.error("请求产品列表失败");
              }
            });
      } else {
        this.$http
            .get(
                "/back/financialProduct/searchProduct/" +
                this.keyword +
                "/" +
                this.queryInfo.pageIndex +
                "/" +
                this.queryInfo.pageSize
            )
            .then((ress) => {
              if (ress.data.code === 10000) {
                this.productList = [];
                const tempProductList = ress.data.data.records;
                let temp = {};
                for (let i = 0; i < tempProductList.length; i++) {
                  temp = {};
                  temp.id = tempProductList[i].fpid;
                  temp.fname = tempProductList[i].fname;
                  temp.price = tempProductList[i].price;
                  that.productList.push(temp);
                }
                this.total = tempProductList.length;
              } else {
                that.$message.error("请求产品列表失败");
              }
            });
      }
    },
    //当前页面数据条数发生改变的时候触发
    handleSizeChange(val) {
      this.queryInfo.pageSize = val;
      this.getProductList();
    },
    //当前页码发生改变的时候触发
    handleCurrentChange(val) {
      this.queryInfo.pageIndex = val;
      this.getProductList();
    },
    //添加产品
    addProduct() {
      this.$refs.addProductFormRef.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http.get('/back/financialProduct/productExist/'+this.addProductForm.fname).then
          this.$http
              .post("/back/financialProduct/addProduct", {
                fpid: this.addProductForm.id,
                fname: this.addProductForm.fname,
                price: this.addProductForm.price,
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  that.$message.success("添加成功");
                } else {
                  that.$message.error("添加失败");
                }
              });
          //关闭dialog对话框
          this.addProductVisible = false;
          this.getProductList();
        }
      });
    },
    //关闭对话框事件
    closeaddProductDialog() {
      //重置表单
      if (this.$refs.addProductFormRef !== undefined) {
        this.$refs.addProductFormRef.resetFields();
      }
    },
    //点击编辑按钮，编辑产品信息
    editProduct(row) {
      //根据产品id获取当前产品信息
      this.$http.get("/back/financialProduct/getProductById/" + row.id).then((ress) => {
        //存储获取到的产品信息
        this.editProductParams.id = ress.data.data.fpid;
        this.editProductParams.fname = ress.data.data.fname;
        this.editProductParams.price = ress.data.data.price;
        this.editProductVisible = !this.editProductVisible;
      });
    },
    editProductList() {
      this.$refs.editProductParams.validate((valid) => {
        const that = this;
        if (valid) {
          this.$http
              .put("/back/financialProduct/updateProduct", {
                fpid: this.editProductParams.id,
                fname: this.editProductParams.fname,
                price: this.editProductParams.price,
              })
              .then((ress) => {
                if (ress.data.code === 10000) {
                  this.editProductVisible = !this.editProductVisible;
                  that.$message.success("修改成功");
                  this.getProductList();
                } else {
                  that.$message.error("修改失败");
                }
              });
        }
      });
    },
    //删除产品
    removeProductItem(row) {
      const that = this;
      this.$confirm("此操作将永久删除该理财产品, 是否继续?", "删除产品", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.$http.delete("/back/financialProduct/deleteProduct/" + row.id).then((ress) => {
              if (ress.data.code === 10000) {
                that.$message.success("删除理财产品成功");
                this.getProductList();
              } else {
                that.$message.error("删除理财产品失败");
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
    this.getProductList();
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