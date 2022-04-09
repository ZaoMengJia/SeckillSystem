// pages/seckill_result/seckill_result.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  onLoad: function (options) {

  },
  onRedirectList(){
    wx.redirectTo({
      url: '../product_list/product_list'
    })
  }
})