<template>
  <el-card style="margin-left: 250px" shadow="never" body-style="padding: 50px">
    <h1>啥啥啥商品</h1>
    <div style="display: inline-flex">
      <el-image src="https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg" style="height: 200px;border-radius: 5px;">
        <div slot="placeholder" class="image-slot">
          加载中<span class="dot">...</span>
        </div>
      </el-image>

      <el-descriptions title="用户信息" style="width: 60%; margin-left: 50px;">
        <el-descriptions-item label="用户名">{{data.name}}</el-descriptions-item>
        <el-descriptions-item label="手机号">18100000000</el-descriptions-item>
        <el-descriptions-item label="居住地">苏州市</el-descriptions-item>
        <el-descriptions-item label="备注">
          <el-tag size="small">学校</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="联系地址">江苏省苏州市吴中区吴中大道 1188 号</el-descriptions-item>
      </el-descriptions>

      <el-table
          stripe
          style="width: 100%">
        <el-table-column
            prop="date"
            label="日期"
            width="180">
        </el-table-column>
        <el-table-column
            prop="name"
            label="姓名"
            width="180">
        </el-table-column>
        <el-table-column
            prop="address"
            label="地址">
        </el-table-column>
      </el-table>
    </div>
  </el-card>
</template>

<script>
import api from "@/api/api";
export default {
  name: "SeckillActivityDetailView",
  data() {
    return {
      seckillActivityId: null,
      data: {}
    }
  },
  methods: {
    async getSeckillActivityList() {
      let [res, err] = await api.getSeckillActivityDetail(this.seckillActivityId);
      if(err != null) {
        this.$message.error(err.message);
        return;
      }

      this.data = res.data.data;
    }
  },
  created() {
    this.seckillActivityId = this.$route.query.seckillActivityId;
    this.getSeckillActivityList();
  }
}
</script>

<style scoped>
.demo-table-expand label {
  color: rgba(151, 167, 190, 0.76);
}
</style>