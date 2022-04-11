// pages/register/register.js
Page({

  data: {
    avatarUrl: null,
    nickName: null,
    gender: 0,
    idCard: null,
    birthday: new Date().getTime(),
    realName: null,
    minDate: new Date(1950,1,1).getTime(),
    maxDate: new Date().getTime(),
  },
  onredirectIndex(){
    wx.redirectTo({
      url: '../index/index',
    })
  },
  onLoad: function (options) {
    this.setData({
      avatarUrl: wx.getStorageSync("userInfo").avatarUrl,
      nickName: wx.getStorageSync("userInfo").nickName
    })
  },


  // 表单相关
  onChangeNickName(){

  },
  onChangeRealName(){

  },
  onChangeIdCard(){

  },
  onChangeGender(){

  },
  onChangeDate(){

  }
})