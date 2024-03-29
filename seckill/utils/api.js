const {sign, RequestType} = require("./signUtils");

const BASE_PATH = 'http://52.82.45.59:8888';
// const BASE_PATH = 'http://localhost:8811';

const request = (url, method, data, header, showLoading, isBody=false) => {
    return new Promise((resolve, reject) => {
        if (showLoading) {
            wx.showLoading({
                title: '加载中',
            })
        }
        let isJson = true;
        if (method === 'GET' || method === 'get' || (header != null && header['content-type'] === 'application/x-www-form-urlencoded')) {
            //微信默认POST传json
            isJson = false;
        }
        let signData;
        if(isBody){
            signData = sign(RequestType.body, data);
        }else{
            signData = sign(isJson ? RequestType.json : RequestType.query, data);
        }
        if (method === 'GET') {
            data.t = new Date().getTime();
        }
        let headers = {
            'app-id': wx.getAccountInfoSync().miniProgram.appId,
            ...signData
        }
        if (header != null) {
            headers = Object.assign(header, headers)
        }
        wx.request({
            url: BASE_PATH + url,
            method: method,
            data: data,
            header: headers,
            success(res) {
                if (res.data.code == 10000) {
                    resolve(res.data)
                } else {
                    wx.showModal({
                        title: '提示',
                        content: res.data.message,
                        success(res) {
                        }
                    })
                    reject(res.data.message)
                }
            },
            fail(error) {
                console.log(error)
                wx.showModal({
                    title: '提示',
                    content: '接口请求失败',
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
    //用户
    login: (data) => {//登录
        return request('/auth/weixin', 'POST', data, {'content-type': 'application/x-www-form-urlencoded', 'Authorization': ''}, false)
    },
    saveInfo: (data) => {//注册信息
        return request('/weixin/user/' + data.userId, 'PUT', data.body, {'Authorization': data.token}, false, true)
    },
    getUserStatus: (data) => {//用户信息状态
        return request('/weixin/user/status/' + data.userId, 'GET', {}, {'Authorization': data.token}, false)
    },
    //秒杀活动
    secKillList: (data) => {//秒杀活动列表
        return request('/weixin/seckill/list', 'GET', {
            pageNum: data.pageNum,
            pageSize: data.pageSize
        }, {'Authorization': data.token}, true)
    },
    getActivityDetail: (data) => {//秒杀活动详情
        return request('/weixin/seckill/' + data.id, 'GET', {}, {'Authorization': data.token}, false)
    },

    //秒杀
    secKillPath: (data) => {//获取秒杀链接
        return request('/weixin/seckill/url/' + data.id, 'GET', {}, {'Authorization': data.token}, false)
    },
    secKill: (data) => {//秒杀接口
        return request('/weixin/seckill' + data.path+'?seckillActivityId='+data.id+'&financialProductId='+data.pid, 'POST',
        {}
        , {'Authorization': data.token}, false)
    },
    getSecKillResult: (data) => {//获取秒杀结果
        return request('/weixin/seckill/order/status/' + data.orderId, 'GET', {}, {'Authorization': data.token}, false)
    },

    //订单
    getSecKillResultListByUser: (data) => {//获取个人所有秒杀结果列表
        return request('/weixin/user/order/' + data.userId, 'GET', {}, {'Authorization': data.token}, true)
    }
}