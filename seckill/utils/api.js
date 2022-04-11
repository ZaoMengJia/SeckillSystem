const { sign, RequestType } = require("./signUtils");

const BASE_PATH ='http://localhost:8811';

const request = (url, method, data, header, showLoading) => {
    return new Promise((resolve, reject) => {
        if (showLoading){
            wx.showLoading({
                title: '加载中',
            })
        }
        let isJson = false;
        if(method === 'POST' || method === 'post') {
            //微信默认POST传json
            isJson = true;
        }
        let signData = sign(isJson ? RequestType.json : RequestType.query, data);
        
        if(method === 'GET') {
            data.t = new Date().getTime();
        }
        let headers = {
            'app-id': wx.getAccountInfoSync().miniProgram.appId,
            ...signData
        }
        if(header!=null){
            headers = Object.assign(header, headers)
        }
        wx.request({
            url: BASE_PATH + url,
            method: method,
            data: data,
            header: headers,
            success(res) {
                console.log(res.data)
                resolve(res.data)
            },
            fail(error) {
                console.log(error)
                wx.showModal({
                    title: '提示',
                    content: '接口请求出错!',
                    success(res) {}
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
    BASE_PATH,
    request,
    login: (data) => {//登录
        return request('/auth/weixin', 'POST', data, {'content-type':'application/x-www-form-urlencoded'}, false)
    },
    saveInfo: (data , header) =>{//注册
        return request('/weixin/user/'+data.userId, 'PUT', data.body, header, false)
    },
    secKillList:(data) =>{//秒杀活动列表
        return request('/weixin/sec-kill/list?pageNum='+data.pageNum+'&pageSize='+data.pageSize, 'GET', {},null,true)
    }
}