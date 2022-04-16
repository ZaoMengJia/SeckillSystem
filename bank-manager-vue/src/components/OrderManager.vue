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
                placeholder="请输入订单号"
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
        </el-row>
        <!-- 渲染数据表格 -->
        <el-table v-loading="isLoading" :data="orderList" :header-cell-style="{'text-align':'center'}"
                  :cell-style="{'text-align':'center'}" border style="width: 100%;margin-top: 20px;">
          <el-table-column type="index" width="50" :index="indexFn"></el-table-column>
          <!-- 所有的prop值必须要orderList里的属性名改成一样的 -->
          <el-table-column prop="id" label="订单号"></el-table-column>
          <el-table-column prop="weixinUser.id" label="用户id" width="120"></el-table-column>
          <el-table-column prop="weixinUser.realName" label="用户姓名" width="100"></el-table-column>
          <el-table-column prop="seckillActivity.id" label="秒杀活动id" width="120"></el-table-column>
          <el-table-column prop="seckillActivity.name" label="秒杀活动名称" width="120"></el-table-column>
          <el-table-column prop="financialProduct.id" label="产品id" width="120"></el-table-column>
          <el-table-column prop="financialProduct.name" label="产品名称" width="120"></el-table-column>
          <el-table-column prop="quantity" label="购买产品数量" width="60"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
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
    </div>
  </div>
</template>

<script>
import api from "@/api/api";

export default {
  name: "OrderService",
  data() {
    return {
      isLoading: false,
      keyword: "",
      // 请求订单列表的参数
      queryInfo: {
        pageIndex: 1,
        pageSize: 10,
      },
      //订单列表数据
      orderList: [],
      total: 0,
      headers: {
        token: window.localStorage["token"],
      },
    };
  },
  methods: {
    //请求订单列表数据
    async getOrderList() {
      this.isLoading = true;
      let [res, err] = this.keyword === '' ?
          await api.getOrderList(this.queryInfo.pageIndex, this.queryInfo.pageSize) :
          await api.searchOrder(this.keyword, this.queryInfo.pageIndex, this.queryInfo.pageSize);
      this.isLoading = false;
      if (err != null) {
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

.el-table td,
.el-table th {
  text-align: center;
}


</style>