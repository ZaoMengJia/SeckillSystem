import {getUserStatus, login} from "../../utils/api";

const app = getApp()

Page({
    data: {},
    onLoad() {
        // 登录
        wx.login({
            success: res => {
                //发送 res.code 到后台换取 openId, token
                login({
                    code: res.code
                }).then(res => {
                    app.globalData.token = res.data.token
                    app.globalData.auth = app.globalData.token.replace("Bearer ", "")
                    app.globalData.userId = res.data.userId
                    app.globalData.openId = res.data.openId
                }).then(res => {
                    console.log(app.globalData)
                })
            }
        })
    },
    onEnterList() {
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
                    if (res.data.isRegistered) {
                        wx.redirectTo({
                            url: '../activity_list/activity_list'
                        })
                    } else {
                        wx.redirectTo({
                            url: '../register/register'
                        })
                    }
                })
            }
        })
    }
})
