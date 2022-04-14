// pages/register/register.js
import {saveInfo, login} from "../../utils/api";
import {validateIdCard} from "../../utils/validate";
import Toast from "../../miniprogram_npm/@vant/weapp/toast/toast";

const app = getApp()

Page({

    data: {
        avatarUrl: null,
        nickName: null,
        gender: "male",
        idCard: null,
        birthday: new Date().getTime(),
        realName: null,
        cellTitle: "选择出生日期",
        minDate: new Date(1950, 1, 1).getTime(),
        maxDate: new Date().getTime(),
        formatter(type, value) {
            if (type === 'year') {
                return `${value}年`;
            }
            if (type === 'month') {
                return `${value}月`;
            }
            return value;
        },
        show: false
    },
    onRedirectIndex() {
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

    //日期选择器
    formatDate(date) {
        date = new Date(date);
        const YY = date.getFullYear() + '年';
        const MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '月';
        const DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + '日';
        return YY + MM + DD;
    },
    showPopup() {
        this.setData({
            show: true,
            cellTitle: "选择出生日期"
        });
    },
    onClose() {
        this.setData({
            show: false,
            cellTitle: this.formatDate(this.data.birthday)
        });
    },

    // 表单相关
    onChangeNickName(event) {
        this.setData({
            nickName: event.detail.value
        })
    },
    onChangeRealName(event) {
        this.setData({
            realName: event.detail.value
        })
    },
    onChangeIdCard(event) {
        this.setData({
            idCard: event.detail.value
        })
    },
    onChangeGender(event) {
        this.setData({
            gender: event.detail,
        })
    },
    onChangeDate(event) {
        this.setData({
            birthday: event.detail,
        })
    },
    onSaveInfo() {
        const validation = validateIdCard(this.data.idCard)
        if (!validation.pass) {
            Toast.fail(validation.msg)
            return
        }
        const data = {
            userId: app.globalData.userId,
            body: {
                "openid": app.globalData.openId,
                "nickname": this.data.nickName,
                "gender": this.data.gender === 'male' ? 1 : 0,
                "avatarUrl": this.data.avatarUrl,
                "idCard": this.data.idCard,
                "birthday": this.data.birthday,
                "realName": this.data.realName
            },
            token: app.globalData.token
        }
        saveInfo(data).then(res => {
            Toast.success("保存信息成功")
            wx.redirectTo({
                url: '../index/index'
            })
        })
    }
})