const app = getApp()

Page({
  data: {

  },
  onLoad() {

  },
  onEnterList(){
    wx.getUserProfile({
      desc: '获取用户信息参加银行秒杀活动',
      lang: 'zh_CN',
      success: (res) => {
        wx.setStorageSync("userInfo", res.userInfo)
        wx.navigateTo({
          url: '../product_list/product_list'
        })
      }
    })
  }
})
