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
    onActivityListTab() {
        wx.redirectTo({
            url: '../activity_list/activity_list'
        })
    },
    getUserAllResult() {
        getSecKillResultListByUser({
            userId: app.globalData.userId,
            token: app.globalData.token
        }).then(res => {
            this.setData({
                resultList: res.data.data
            })
        })
    }
});