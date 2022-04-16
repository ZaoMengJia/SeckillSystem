import {
    getActivityDetail,
    getSecKillResult,
    secKill,
    secKillPath
} from "../../utils/api";
import Toast from "../../miniprogram_npm/@vant/weapp/toast/toast";

const app = getApp()
Page({

    data: {
        aid: -1,
        pid: -1,
        secKillPath: null,
        productList: [],
        curProductPrice: 0,
        secKillStatus: "创建中......",
        secKillImg: "/images/creating.png",
        dialogShow: false
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
                endTime: res.data.endTime.substring(0, 10) + " " + res.data.endTime.substring(11, 16),
                productList: res.data.productList
            })
        })
    },
    getSecKillPath() {
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

    onChangeProduct(e) {
        this.setData({
            pid: e.detail
        })
        for (let i = 0; i < this.data.productList.length; i += 1) {
            const cur = this.data.productList[i]
            if (cur.id === this.data.pid) {
                this.setData({
                    curProductPrice: cur.price * 100
                })
                break
            }
        }
    },
    onSecKill() {
        if (this.data.pid === -1) {
            Toast.fail("请选择产品")
            return
        }
        const isAudited = wx.getStorageSync("isAudited")
        if (isAudited) {
            secKill({
                path: this.data.secKillPath,
                id: this.data.aid,
                pid: this.data.pid,
                token: app.globalData.token
            }).then(async res => {
                this.setData({
                    dialogShow: true,
                    secKillStatus: "创建中...",
                    secKillImg: "/images/creating.png"
                })
                let status
                while (true) {
                    status = await this.timeout(500, res.data.orderId).then(res => {
                        return res.data.status
                    })
                    if (status === 'NORMAL') {
                        this.setData({
                            secKillStatus: "抢购成功！",
                            secKillImg: "/images/success.png"
                        })
                        break;
                    } else if (status === 'ERROR') {
                        this.setData({
                            secKillStatus: "抢购失败！",
                            secKillImg: "/images/fail.png"
                        })
                        break;
                    }
                }

            })
        } else {
            Toast.fail("无抢购资格")
        }
    },
    timeout(duration, orderId) {
        return new Promise(resolve => {
            setTimeout(() => {
                getSecKillResult({
                    orderId: orderId,
                    token: app.globalData.token
                }).then(res => {
                    resolve(res)
                })
            }, duration);
        });
    },
    onConfirmDialog() {
        this.setData({
            dialogShow: false
        })
        wx.redirectTo({
            url: '../activity_list/activity_list',
        })
    },
    onCancelDialog() {
        this.setData({
            dialogShow: false
        })
    }
})