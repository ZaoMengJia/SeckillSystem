const api = require("./utils/api");
const { sign, RequestType } = require("./utils/signUtils");

App({
    onLaunch() {
        api.login({
            a: '1'
        });
        this.globalData = {}
        // 获得系统信息
        wx.getSystemInfo({
            success: e => {
                this.globalData.StatusBar = e.statusBarHeight;
                let custom = wx.getMenuButtonBoundingClientRect();
                this.globalData.Custom = custom;
                this.globalData.CustomBar = custom.bottom + custom.top - e.statusBarHeight;
            }
        })
        // 登录
        wx.login({
            success: res => {
                console.log(res)
                // 发送 res.code 到后台换取 openId, token
                // wx.request({
                //     url: 'http://localhost:8080/auth/weixin',
                //     method: 'POST',
                //     data: {
                //         code: res.code
                //     },
                //     success: res => {
                //         console.log(res)
                //     }
                // })
            }
        })
        console.log(this.globalData)
    }
})
