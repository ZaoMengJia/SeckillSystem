import {getActivityDetail, secKill} from "../../utils/api";
import Toast from "../../miniprogram_npm/@vant/weapp/toast/toast";

const app = getApp()
Page({

    data: {
        aid: -1,
        pid: -1,
        secKillPath: null,
        productList: [],
        price: 0
    },

    onLoad: function (options) {
        this.setData({
            aid: options.aid
        })
        this.getDetail()
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
            console.log(res)
            this.setData({
                url: res.data.image,
                detail: res.data.detail,
                title: res.data.name,
                endTime: res.data.endTime.substring(0, 9),
                productList: res.data.productList
            })
        })
    },

    onSecKill() {
        const isAudited = wx.getStorageSync("isAudited")
        if (isAudited) {
            //TODO 秒杀
            secKill({
                path: this.data.secKillPath,
                id: this.data.aid,
                pid: this.data.pid,
                token: app.globalData.token
            }).then(res => {
                console.log("seckill", res)
            })
        } else {
            Toast.fail("无抢购资格!")
        }
    }
})