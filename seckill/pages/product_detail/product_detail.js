// pages/product_detail/product_detail.js
import {getProductDetail} from "../../utils/api";

Page({

  data: {
    id: -1,
    secKillPath: null
  },

  onLoad: function (options) {
    this.setData({
      id:options.id
    })
    this.getDetail()
  },
  onNavigateBack(){
    wx.navigateBack({
      delta: 1
    })
  },

  getDetail(){
    getProductDetail({
      id: this.data.id
    }).then(res => {
      console.log(res)
    })
  },

  onSecKill(){

  }
})