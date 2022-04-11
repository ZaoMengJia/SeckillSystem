const { sign, RequestType } = require("./signUtils");

const BASE_PATH ='http://localhost:8811';

const request = (url, method, data, showLoading) => {
    return new Promise((resolve, reject) => {
        if (showLoading){
            wx.showLoading({
                title: '加载中',
            })
        }

        let isJson = false;
        if(method === 'POST' || method == 'post') {
            //微信默认POST传json
            isJson = true;
        }
        let signData = sign(isJson ? RequestType.json : RequestType.query, data);
        
        if(method == 'GET') {
            data.t = new Date().getTime();
        }

        wx.request({
            url: BASE_PATH + url,
            method: method,
            data: data,
            header: {
                'app-id': wx.getAccountInfoSync().miniProgram.appId,
                ...signData
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
        return request('/auth/weixin', 'GET', data, false)
    }
}