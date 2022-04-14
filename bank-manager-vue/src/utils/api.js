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
                if(res.data.code == 10000){
                    resolve(res.data)
                }else{
                    wx.showModal({
                        title: '提示',
                        content: '接口异常错误!',
                        success(res) {}
                    })
                    reject(res.data.message)
                }
            },
            fail(error) {
                console.log(error)
                wx.showModal({
                    title: '提示',
                    content: '接口请求失败!',
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
    },
    getProductDetail:(data)=>{//秒杀活动详情
        return request('/weixin/sec-kill/'+data.id, 'GET', {}, null, false)
    },
    secKillPath:(data , header) =>{//秒杀链接
        return request('/weixin/sec-kill/url/'+data.id, 'GET', {}, header, false)
    },
    secKill: (data) =>{//秒杀接口
        return request('/weixin/sec-kill/'+data.path+'?seckillActivityId='+data.id+'&financialProductId='+data.pid, 'POST', {}, header, false)
    },
    getSecKillResult: (data, header) =>{//获取秒杀结果

    },
    getSecKillResultListByUser: (data, header) =>{//获取个人所有秒杀结果列表

    }
}