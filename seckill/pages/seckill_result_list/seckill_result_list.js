Page({
    data: {
        pageIndex: 1,
        userName:"",
        userAvatarUrl:""
    },
    onLoad: function (options) {
        this.setData({
            userAvatarUrl: wx.getStorageSync("userInfo").avatarUrl,
            userName: wx.getStorageSync("userInfo").nickName
        })
    },
    onPullDownRefresh() {

    },
    onProductListTab() {
        wx.redirectTo({
            url: '../product_list/product_list'
        })
    }
});