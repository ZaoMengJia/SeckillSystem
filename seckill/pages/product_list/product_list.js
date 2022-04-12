// pages/product_list/product_list.js
import {secKillList} from "../../utils/api";

const app = getApp()

const MAX_PAGE_SIZE = 4
Page({

  data: {
    barIndex: 0,
    pageIndex: 1,
    isLoading: false,
    hasMore: true,
    productList:[]
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
    this.setData({
      isLoading: true
    })
    // if (res.data.hasNextPage == true) {
    //   this.setData({
    //     commodityList: this.data.commodityList.concat(res.data.pageInfo),
    //     pageIndex: this.data.pageIndex+1,
    //     hasMore: true,
    //     isLoading:false
    //   })
    // } else {
    //   this.setData({
    //     commodityList: this.data.commodityList.concat(res.data.pageInfo),
    //     hasMore: false,
    //     isLoading:false
    //   })
    // }
    this.setData({
      isLoading: false
    })
  },

  onSecKillListTab(){
    wx.redirectTo({
      url: '../seckill_result_list/seckill_result_list'
    })
  },
  onEnterDetail(e){
    let id = e.currentTarget.id;
    wx.navigateTo({
      url: '../product_detail/product_detail?id='+id
    })
  },
  initProductList() {
    secKillList({
      pageNum: this.data.pageIndex,
      pageSize: MAX_PAGE_SIZE
    }).then(res => {
      console.log(res)
    })
  }
})