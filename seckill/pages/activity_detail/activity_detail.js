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
    //图片预览
    onPreviewImg() {
        wx.previewImage({
            current: this.data.url,
            urls: this.data.url
        })
    },


    getDetail() {
        getActivityDetail({
            id: this.data.aid
        }).then(res => {
            this.setData({
                url: res.data.image,
                detail: res.data.detail,
                title: res.data.name,
                endTime: res.data.endTime.substring(0, 9),
                productList: res.data.productList
            })
        })
    },
    getSecKillPath(){
        secKillPath({
            id: this.data.aid,
            token: app.globalData.token
        }).then(res => {
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
            }).then(res => {
                let status
                do {
                    setTimeout(function () {},500)
                    getSecKillResult({
                        orderId: res.data.orderId
                    }).then(res => {
                        status = res.data.status
                    })
                }while(status === 'CREATING')
                if(status === 'NORMAL'){
                    Toast.success("抢购成功")
                }else{
                    Toast.fail("抢购失败")
                }
                wx.hideLoading()
            })
        } else {
            Toast.fail("无抢购资格")
        }
    }
})