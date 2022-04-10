// pages/product_list/product_list.js
Page({

  data: {
    pageIndex: 0,
  },

  onLoad: function (options) {

  },

  onPullDownRefresh() {

  },

  onSecKillListTab(){
    wx.redirectTo({
      url: '../seckill_result_list/seckill_result_list'
    })
  }
})