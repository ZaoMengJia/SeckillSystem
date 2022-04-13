import {getUserStatus} from "../../utils/api";

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
        getUserStatus({
          userId: app.globalData.userId,
          token: app.globalData.token
        }).then(res => {
          wx.setStorageSync("isRegistered", res.data.isRegistered)
          wx.setStorageSync("isAudited", res.data.isAudited)
          wx.redirectTo({
            url: '../activity_list/activity_list'
          })
          // if(res.data.isRegistered){
          //   wx.redirectTo({
          //     url: '../activity_list/activity_list'
          //   })
          // }else{
          //   wx.redirectTo({
          //     url: '../register/register'
          //   })
          // }
        })
      }
    })
  }
})
