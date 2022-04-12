// pages/product_detail/product_detail.js
Page({

  data: {
    productId: -1,
    secKillPath: null
  },

  onLoad: function (options) {
    this.setData({
      productId:options.id
    })
  },

  onNavigateBack(){
    wx.navigateBack({
      delta: 1
    })
  },

  onSecKill(){

  }
})