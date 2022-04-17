<template>
  <!-- 订单管理界面 -->
  <div class="bread">
    <div class="table">
      <span style="font-size: 32px; flex-basis: 62%; font-weight: bolder">成功添加的所有订单</span><br>
      <div style="height: 10px"/>

      <div>
        <div style="display: inline-flex; justify-content: center; align-items: center;margin-bottom: 20px;">
          <el-input
              style="width: 300px"
              placeholder="输入订单号"
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
        </div>

        <div  v-if="!isLoading">
          <!-- 渲染数据表格 -->
          <el-table :data="orderList" :header-cell-style="{'text-align':'center'}"
                    :cell-style="{'text-align':'center'}" style="width: 100%">
            <el-table-column type="index" width="50" :index="indexFn"></el-table-column>
            <!-- 所有的prop值必须要orderList里的属性名改成一样的 -->
            <el-table-column prop="id" label="订单号" width="300"></el-table-column>
            <el-table-column prop="weixinUser.id" label="用户编号" width="300"></el-table-column>
            <el-table-column prop="weixinUser.nickname" label="用户昵称" width="100"></el-table-column>
            <el-table-column label="活动" width="200">
              <template slot-scope="scope">
                <div v-if="scope.row.seckillActivity != null">
                  <span>{{scope.row.seckillActivity.name}}</span><br>
                  <span>({{scope.row.seckillActivity.id}})</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="产品" width="200">
              <template slot-scope="scope">
                <div v-if="scope.row.financialProduct != null">
                  <span>{{scope.row.financialProduct.name}}</span><br>
                  <span>({{scope.row.financialProduct.id}})</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="购买数量" width="100"></el-table-column>
            <el-table-column prop="createTime" label="创建时间">
              <template slot-scope="scope">
                <span>{{new Date(scope.row.createTime).Format('yyyy-MM-dd hh:mm:ss')}}</span>
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

      </div>

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