Page({
    data: {
        pageIndex: 1,
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

    }
});