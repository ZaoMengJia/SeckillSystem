import {getActivityDetail, getSecKillResult, secKill, secKillPath} from "../../utils/api";
import Toast from "../../miniprogram_npm/@vant/weapp/toast/toast";

const app = getApp()
Page({

    data: {
        aid: -1,
        pid: -1,
        secKillPath: null,
        productList: [],
        curProductPrice: 0
    },

    onLoad: function (options) {
        this.setData({
            aid: options.aid
        })
        this.getDetail()
        this.getSecKillPath()
    },
    onNavigateBack() {
        wx.navigateBack({
            delta: 1
        })
    },


    getDetail() {
        getActivityDetail({
            id: this.data.aid,
            token: app.globalData.token
        }).then(res => {
            this.setData({
                url: res.data.image,
                detail: res.data.detail,
                title: res.data.name,
                endTime: res.data.endTime.substring(0, 10)+" "+res.data.endTime.substring(11,16),
                productList: res.data.productList
            })
        })
    },
    getSecKillPath(){
        secKillPath({
            id: this.data.aid,
            token: app.globalData.token
        }).then(res => {
            console.log(res)
            this.setData({
                secKillPath: res.data.path
            })
        })
    },

    onChangeProduct(e){
        this.setData({
            pid: e.detail
        })
        for(let i = 0; i < this.data.productList.length; i+=1){
            const cur = this.data.productList[i]
            if(cur.id === this.data.pid){
                this.setData({
                    curProductPrice: cur.price*100
                })
                break
            }
        }
    },
    onSecKill() {
        if(this.data.pid === -1){
            Toast.fail("请选择产品")
            return
        }
        const isAudited = wx.getStorageSync("isAudited")
        if (isAudited) {
            wx.showLoading({
                title: '等待结果中'
            })
            secKill({
                path: this.data.secKillPath,
                id: this.data.aid,
                pid: this.data.pid,
                token: app.globalData.token
            }).then(async res => {
                console.log(res)
                let status
                do {
                    status = await this.timeout(500, res.data.orderId).then(res => {
                        return res.data.status
                    })
                } while (status === 'CREATING')
                wx.hideLoading()
                if (status === 'NORMAL') {
                    Toast.success("抢购成功")
                } else {
                    Toast.fail("抢购失败")
                }
                wx.redirectTo({
                  url: '../activity_list/activity_list',
                })
            })
        } else {
            Toast.fail("无抢购资格")
        }
    },
    timeout(duration, orderId) {
        return new Promise(resolve => {
            setTimeout(()=>{
                 getSecKillResult({
                    orderId: orderId,
                    token: app.globalData.token
                }).then(res => {
                    resolve(res)
                })
            },duration);
        });
    },
})