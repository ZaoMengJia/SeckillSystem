// pages/product_detail/product_detail.js
import {getProductDetail} from "../../utils/api";
import Toast from "../../miniprogram_npm/@vant/weapp/toast/toast";

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
    const isAudited = wx.getStorageSync("isAudited")
    if(isAudited){
      //TODO 秒杀
    }else{
      Toast.fail("无抢购资格!")
    }
  }
})