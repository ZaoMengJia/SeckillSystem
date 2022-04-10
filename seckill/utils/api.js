const BASE_PATH ='http://localhost';

const request = (url, method, data, showLoading) => {
    return new Promise((resolve, reject) => {
        if (showLoading){
            wx.showLoading({
                title: '加载中',
            })
        }
        wx.request({
            url: BASE_PATH + url,
            method: method,
            data: data,
            header: {
                'app-id': wx.getAccountInfoSync().miniProgram.appId
            },
            success(res) {
                resolve(res)
            },
            fail(error) {
                console.log(error)
                wx.showModal({
                    title: '提示',
                    content: '接口请求出错!',
                    success(res) {
                    }
                })
                reject(error)
            },
            complete(res) {
                wx.hideLoading()
            }
        })
    })
}

module.exports = {
    request,
    login: (data) => {//登录
        return request('/weixin/auth', 'post', data, false)
    }
}