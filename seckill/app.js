App({
    onLaunch() {
        this.globalData = {
            BASE_PATH: 'http://localhost'
        }
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
                wx.request({
                    url: this.globalData.BASE_PATH + ':8811/auth/weixin',
                    method: 'POST',
                    data: {
                        code: res.code
                    },
                    header: {
                        'content-type': 'application/x-www-form-urlencoded;charset=utf-8',
                    },
                    success: res => {
                        res = res.data
                        this.globalData.token = res.data.token
                        this.globalData.auth = this.globalData.token.replace("Bearer ", "")
                        this.globalData.userId = res.data.userId
                        wx.request({
                            url: this.globalData.BASE_PATH + ':8090/weixin/user/' + this.globalData.userId,
                            header: {
                                "Authorization": this.globalData.auth
                            },
                            success: res => {
                                res = res.data
                                this.globalData.openid = res.data.openid
                            }
                        })
                    },
                    complete: res => {
                        console.log(this.globalData)
                    }
                })
            }
        })
    }
})
