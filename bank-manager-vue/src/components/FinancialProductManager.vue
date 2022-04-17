<template>
  <!-- 理财产品管理界面 -->
  <div class="bread">
    <!-- 搜索头部input框 -->
    <div class="table">
      <span style="font-size: 32px; flex-basis: 62%; font-weight: bolder">商品列表</span><br>
      <div style="height: 10px"/>
      <div>
        <div style="display: inline-flex; justify-content: center; align-items: center; margin-bottom: 20px;">
          <el-input
              placeholder="输入关键词"
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
          <el-button type="primary" style="margin-left: 20px;" plain @click="addProductVisible = true"
          >添加
          </el-button>
        </div>

        <div v-if="!isLoading" style="width: 1030px;">
          <el-table :data="productList" :header-cell-style="{'text-align':'center'}"
                    :cell-style="{'text-align':'center'}" style="width: 100%;">
            <el-table-column type="index" width="50" :index="indexFn">
            </el-table-column>
            <!-- 所有的prop值必须要productList里的属性名改成一样的 -->
            <el-table-column prop="id" label="编号" width="300"></el-table-column>
            <el-table-column prop="name" label="产品名称" width="250"></el-table-column>
            <el-table-column prop="price" label="每份价格" width="250"></el-table-column>
            <el-table-column prop="operate" label="操作" width="180">
              <template slot-scope="scope">
                <el-button
                    type="primary"
                    icon="el-icon-edit"
                    circle
                    plain
                    @click="editProduct(scope.row)"
                ></el-button>
                <el-button
                    plain
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
              style="float:right;margin-top: 10px;"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="queryInfo.pageIndex"
              :page-sizes="[5,10,15,20]"
              :page-size="queryInfo.pageSize"
              layout="prev, pager, next"
              :total="total"
              :hide-on-single-page="true"
          >
          </el-pagination>
        </div>
        <el-skeleton v-if="isLoading" :count="2" animated/>
        <!-- 渲染数据表格 -->

      </div>
      <!-- 添加产品dialog对话框 -->
      <el-dialog
          title="添加产品"
          :visible.sync="addProductVisible"
          width="50%"
          @close="closeAddProductDialog"
      >
        <el-form
            :model="addProductForm"
            :rules="addProductFormRul"
            ref="addProductFormRef"
            label-width="100px"
            class="demo-ruleForm"
        >
          <el-form-item label="产品名称" prop="name">
            <el-input v-model="addProductForm.name" clearable></el-input>
          </el-form-item>
          <el-form-item label="每份价格" prop="price">
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
          <el-form-item label="产品名称" prop="name">
            <el-input v-model="editProductParams.name" clearable></el-input>
          </el-form-item>
          <el-form-item label="每份价格" prop="price">
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
import api from "@/api/api";

export default {
  name: "FinancialProductService",
  data() {
    return {
      isLoading: false,
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
        name: "",
        price: "",
      },
      total: 0,
      // 添加产品dialog显示/隐藏
      addProductVisible: false,
      //添加产品参数
      addProductForm: {
        name: "",
        price: "",
      },
      //添加产品对话框验证规则
      addProductFormRul: {
        name: [
          {required: true, message: "产品名称不能为空", trigger: "blur"},
          {
            min: 2,
            max: 15,
            message: "长度在 2 到 15 个字符",
            trigger: "blur",
          },
        ],
        price: [
          {required: true, message: "价格不能为空", trigger: "blur"},
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
        name: "",
        price: "",
      },
      //编辑产品对话框验证规则
      editProductParamsRul: {
        name: [
          {required: true, message: "产品名称不能为空", trigger: "blur"},
          {
            min: 2,
            max: 15,
            message: "长度在 2 到 15 个字符",
            trigger: "blur",
          },
        ],
        price: [
          {required: true, message: "价格不能为空", trigger: "blur"},
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
    async getProductList() {
      this.isLoading = true;
      let [res, err] = this.keyword === '' ?
          await api.getProductList(this.queryInfo.pageIndex, this.queryInfo.pageSize) :
          await api.searchProduct(this.keyword, this.queryInfo.pageIndex, this.queryInfo.pageSize);
      this.isLoading = false;
      if (err != null) {
        this.$message.error(err.message);
        return;
      }
      this.productList = res.data.data.data;
      this.total = res.data.data.total;
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
      this.$refs.addProductFormRef.validate(async (valid) => {
        if (!valid) {
          return;
        }

        let [_, err] = await api.saveProduct(this.addProductForm.name, this.addProductForm.price);
        if (err != null) {
          this.$message.error(err.message);
          return;
        }

        this.$message.success('添加成功');
        this.addProductVisible = false;
        await this.getProductList();
      });
    },
    //关闭对话框事件
    closeAddProductDialog() {
      //重置表单
      if (this.$refs.addProductFormRef !== undefined) {
        this.$refs.addProductFormRef.resetFields();
      }
    },
    //点击编辑按钮，编辑产品信息
    editProduct(row) {
      //根据产品id获取当前产品信息
      this.editProductParams = {...row}
      this.editProductVisible = true;
    },
    editProductList() {
      this.$refs.editProductParams.validate(async (valid) => {
        if (!valid) {
          return;
        }
        console.log(this.editProductParams)
        let [, err] = await api.editProduct(this.editProductParams.id, this.editProductParams.name, this.editProductParams.price);
        if (err != null) {
          this.$message.error(err.message);
          return;
        }

        this.$message.info('修改成功');
        this.editProductVisible = false;
        await this.getProductList();
      });
    },
    //删除产品
    removeProductItem(row) {
      this.$confirm("此操作将永久删除该理财产品, 是否继续?", "删除产品", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(async () => {
            let [, err] = await api.deleteProduct(row.id);
            if (err != null) {
              this.$message.error(err.message);
              return;
            }

            await this.getProductList();
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
  },
  created() {
    this.getProductList();
  }
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