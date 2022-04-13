import {getSecKillResultListByUser} from "../../utils/api";
const app = getApp()

Page({
    data: {
        barIndex: 1,
        userName: "",
        userAvatarUrl: "",
        resultList: []
    },
    onLoad: function (options) {
        this.setData({
            userAvatarUrl: wx.getStorageSync("userInfo").avatarUrl,
            userName: wx.getStorageSync("userInfo").nickName
        })
        this.getUserAllResult()
    },
    onPullDownRefresh() {
        this.getUserAllResult()
    },
    onProductListTab() {
        wx.redirectTo({
            url: '../product_list/product_list'
        })
    },
    getUserAllResult() {
        getSecKillResultListByUser({
            userId: app.globalData.userId,
            token: app.globalData.token
        }).then(res =>{
            console.log(res)
            this.setData({
                resultList: res.data.data
            })
        })
    }
});