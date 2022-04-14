    import api, {BASE_PATH, login} from "./utils/api";
App({
    onLaunch() {
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
                //发送 res.code 到后台换取 openId, token
                login({
                    code: res.code
                }).then(res => {
                    this.globalData.token = res.data.token
                    this.globalData.auth = this.globalData.token.replace("Bearer ", "")
                    this.globalData.userId = res.data.userId
                    this.globalData.openId = res.data.openId
                }).then(res => {
                    console.log(this.globalData)
                })
            }
        })
    }
})
