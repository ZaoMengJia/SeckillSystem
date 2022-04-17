<template>
  <div>
    <div style="margin-left: 250px" shadow="never" body-style="padding: 50px">
      <el-page-header @back="$router.push({name: 'SeckillActivity'})" content="详情"/>
      <el-skeleton style="margin-top: 40px" v-if="isLoading" :rows="18" animated />
      <div style="padding: 50px">
        <div v-if="!isLoading">
          <div style="display: inline-flex; margin-bottom: 24px">
            <el-image :src="data.image" style="height: 300px;border-radius: 5px;" fit="cover">
              <div slot="placeholder" class="image-slot">
                加载中<span class="dot">...</span>
              </div>
            </el-image>
            <div style="margin-left: 40px; width: 600px;">
              <span style="font-size: 32px;font-weight: bold">{{data.name}}</span><br>
              <span class="font-normal">{{data.detail}}</span><br>
              <div style="height: 30px"/>
              <el-form class="demo-table-expand" label-width="70px" label-position="left">
                <el-form-item label="活动编号"><span>{{ data.id }}</span></el-form-item>
                <el-form-item label="开始时间"><span>{{ new Date(data.beginTime).Format('yyyy-MM-dd hh:mm') }}</span></el-form-item>
                <el-form-item label="结束时间"><span>{{ new Date(data.endTime).Format('yyyy-MM-dd hh:mm') }}</span></el-form-item>
              </el-form>

              <el-button type="text" slot="reference" @click="openEditActivityInfoDialog">编辑</el-button>

              <!--        {{JSON.stringify(data)}}-->
            </div>

          </div>
          <div style="margin-bottom: 12px">
            <div style="display: inline-flex;">
              <span style="font-size: 24px;font-weight: bold; margin-right: 16px;">商品列表</span>
              <el-button type="text" style="margin-top: -2px;" @click="openInsertProductDialog">添加商品</el-button>
            </div>
          </div>
          <div>
            <div v-if="data.productList.length > 0" v-for="(product, index) in data.productList" :key="product.id" style="margin-bottom: 32px">
              <span style="font-size: 20px;font-weight: bolder">{{product.name}}</span><br>
              <span class="font-caption" style="margin-bottom: 8px;">商品编号: {{product.id}}</span><br>
              <span class="font-caption" style="margin-bottom: 2px;">价格: <span style="margin-left: 28px;">{{product.price}} 元</span></span><br>

              <el-popover
                  placement="right"
                  width="200"
                  trigger="click">
                <div>
                  <el-form>
                    <el-form-item label="库存">
                      <el-input-number size="mini" v-model="product.editQuantity" :min="1" :max="1000000" label="描述文字"></el-input-number>
                    </el-form-item>
                    <el-form-item label="总量">
                      <el-input-number size="mini" v-model="product.editTotal" :min="1" :max="1000000" label="描述文字"></el-input-number>
                    </el-form-item>
                  </el-form>
                  <el-button style="float:left;color: red;margin-top: -4px;" type="text" @click="commitDeleteProductInfo(product)">删除</el-button>
                  <el-button style="float:right;" size="mini" @click="commitEditProductInfo(product)">确定</el-button>
                </div>
                <el-button type="text" slot="reference">编辑</el-button>
              </el-popover>


              <el-progress :percentage="product.quantity / product.total * 100" :format="() => ''"/>
              <span style="float:right;margin-right: 50px;">{{product.quantity}}/{{product.total}}</span>
            </div>
            <el-empty v-if="data.productList === undefined || data.productList.length === 0" description="暂无商品"></el-empty>
          </div>
        </div>
      </div>
    </div>

    <!--弹窗部分-->
    <el-dialog title="编辑" :visible.sync="editActivityInfoDialog.visible" :close-on-click-modal="false">
      <el-form label-position="left" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="editActivityInfoDialog.data.name"/>
        </el-form-item>
        <el-form-item label="活动详情">
          <el-input type="textarea" v-model="editActivityInfoDialog.data.detail"/>
        </el-form-item>
        <el-form-item label="时间">
          <el-date-picker
              v-model="editActivityInfoDialog.data.date"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="图片地址">
          <el-input v-model="editActivityInfoDialog.data.image"/>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button style="float:left;color: red" type="text" @click="deleteActivity">删除这个活动</el-button>
    <el-button type="primary" @click="commitEditActivityInfo">应用</el-button>
  </span>
    </el-dialog>
    <el-dialog  title="选择商品" :visible.sync="insertProductDialog.visible" :close-on-click-modal="false">
      <el-form label-position="left" label-width="80px">
        <el-form-item label="商品">
          <el-select v-model="insertProductDialog.data.selectProductId" placeholder="请选择">
            <el-option
                v-for="product in insertProductDialog.data.productList"
                :key="product.id"
                :label="product.name"
                :value="product.id"
            >
              <span style="float: left">{{ product.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ product.id }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="总数">
          <el-input-number v-model="insertProductDialog.data.quantity" :min="1" :max="10000000" label="描述文字"/>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="commitInsertProduct">确定</el-button>
        </span>
    </el-dialog>



  </div>

</template>

<script>
import api from "@/api/api";
export default {
  name: "SeckillActivityDetailView",
  data() {
    return {
      editActivityInfoDialog: {
        visible: false,
        data: {}
      },
      insertProductDialog: {
        visible: false,
        data: {
          selectProductId: null,
          productList: [],
          quantity: 10
        }
      },
      isLoading: false,
      seckillActivityId: null,
      data: {}
    }
  },
  methods: {
    async commitDeleteProductInfo(product) {
      let [,err] = await api.deleteActivityProduct(this.seckillActivityId, product.id);
      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      await this.getSeckillActivityList();
    },
    async commitEditProductInfo(product) {
      if(product.editQuantity > product.editTotal) {
        this.$message.error('商品数量输入有误')
        return;
      }
      let [,err] = await api.updateSeckillActivityProduct({
        financialProductId: product.id,
        seckillActivityId: this.seckillActivityId,
        quantity: product.editQuantity,
        total: product.editTotal
      });

      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      await this.getSeckillActivityList();
    },
    async commitInsertProduct() {
      let [, err] = await api.insertSeckillActivityProduct({
        seckillActivityId: this.seckillActivityId,
        financialProductId: this.insertProductDialog.data.selectProductId,
        quantity: this.insertProductDialog.data.quantity
      });
      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      this.insertProductDialog.visible = false;
      await this.getSeckillActivityList();

    },
    async openInsertProductDialog() {
      let [res, err] = await api.getFinancialProductList(1, 99999);
      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      this.insertProductDialog.data.productList = res.data.data.data;
      this.insertProductDialog.visible = true;
    },
    openEditActivityInfoDialog() {
      this.editActivityInfoDialog.data = {...this.data, date: [new Date(this.data.beginTime), new Date(this.data.endTime)]};
      this.editActivityInfoDialog.visible = true;
    },
    async commitEditActivityInfo() {
      //检验信息
      let beginTime = this.editActivityInfoDialog.data.date[0];
      let endTime = this.editActivityInfoDialog.data.date[1];

      if(beginTime == null || endTime == null || beginTime >= endTime) {
        this.$message.error('日期填写有误');
        return;
      }


      //组建信息，准备发送
      let [, err] = await api.updateSeckillActivity({
        id: this.seckillActivityId,
        name: this.editActivityInfoDialog.data.name,
        detail: this.editActivityInfoDialog.data.detail,
        image: this.editActivityInfoDialog.data.image,
        beginTime: beginTime,
        endTime: endTime
      });

      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      this.editActivityInfoDialog.visible = false;
      await this.getSeckillActivityList();
    },
    async deleteActivity() {
      let [, err] = await api.deleteSeckillActivity(this.seckillActivityId);
      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      this.$router.push({
        name: 'SeckillActivity'
      });
    },
    async getSeckillActivityList() {
      this.isLoading = true;
      let [res, err] = await api.getSeckillActivityDetail(this.seckillActivityId);
      this.isLoading = false;

      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      res.data.data.productList.forEach(c => {
        c.editTotal = c.total;
        c.editQuantity = c.quantity;
      });
      this.data = res.data.data;

    }
  },
  created() {
    this.seckillActivityId = this.$route.query.id;
    this.getSeckillActivityList();
  }
}
</script>

<style scoped>
.demo-table-expand label {
  color: rgba(151, 167, 190, 0.76);
}

.font-normal {
  font-size: 16px;
}

.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

.font-caption {
  font-size: 14px;
}
</style>