import {secKillList} from "../../utils/api";
import Toast from "../../miniprogram_npm/@vant/weapp/toast/toast";

const app = getApp()

const MAX_PAGE_SIZE = 4
Page({

    data: {
        barIndex: 0,
        pageIndex: 0,
        isLoading: false,
        hasMore: true,
        activityList: []
    },

    onLoad: function (options) {
        this.initProductList()
    },
    onPullDownRefresh() {
        this.initProductList()
    },
    onReachBottom() {
        if (!this.data.hasMore) {
            return
        }
        const that = this
        this.setData({
            isLoading: true
        })
        secKillList({
            pageNum: this.data.pageIndex,
            pageSize: MAX_PAGE_SIZE
        }).then(res => {
            if (this.data.pageIndex === res.data.pageTotal - 1) {
                this.setData({
                    activityList: that.processStatus(this.data.activityList.concat(res.data.data)),
                    hasMore: false
                })
            } else if (this.data.pageIndex < res.data.pageTotal - 1) {
                this.setData({
                    activityList: that.processStatus(this.data.activityList.concat(res.data.data)),
                    pageIndex: this.data.pageIndex + 1,
                    hasMore: true
                })
            }
        }).then(res => {
            this.processStatus()
            this.setData({
                isLoading: false
            })
        })
    },

    onSecKillListTab() {
        wx.redirectTo({
            url: '../seckill_result_list/seckill_result_list'
        })
    },
    onEnterDetail(e) {
        const id = e.currentTarget.dataset.id;
        for (let i = 0; i < this.data.activityList.length; i += 1) {
            const cur = this.data.activityList[i]
            if (cur.id === id) {
                const result = this.judgeTime(cur.beginTime, cur.endTime)
                console.log(result)
                if (result === 0) {
                    wx.navigateTo({
                        url: '../activity_detail/activity_detail?aid=' + id
                    })
                } else if (result === 1) {
                    Toast.fail("活动未开始!")
                } else if (result === -1) {
                    Toast.fail("活动已结束!")
                }
                break
            }
        }
    },
    initProductList() {
        const that = this
        this.setData({
            pageIndex: 0
        })
        secKillList({
            pageNum: this.data.pageIndex,
            pageSize: MAX_PAGE_SIZE
        }).then(res => {

            if (this.data.pageIndex === res.data.pageTotal - 1) {
                this.setData({
                    activityList: that.processStatus(res.data.data),
                    hasMore: false
                })
            } else if (this.data.pageIndex < res.data.pageTotal - 1) {
                this.setData({
                    activityList: that.processStatus(res.data.data),
                    pageIndex: this.data.pageIndex + 1,
                    hasMore: true
                })
            }
        }).then(res => {

        })
    },
    judgeTime(beginTime, endTime) {
        let curDate = new Date()
        let beginDate = new Date(beginTime)
        let endDate = new Date(endTime)
        if (curDate < beginDate) {//活动未开始
            return 1
        } else if (curDate <= endDate) {//活动进行中
            return 0
        } else {//活动已结束
            return -1
        }
    },
    processStatus(list) {
        for (let i = 0; i < list.length; i += 1) {
            const cur = list[i]
            list[i].status = this.judgeTime(cur.beginTime, cur.endTime)
        }
        return list
    }
})