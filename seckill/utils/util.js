const validate = require('./validate.js')

const formatTime = date => {
    const year = date.getFullYear()
    const month = date.getMonth() + 1
    const day = date.getDate()
    const hour = date.getHours()
    const minute = date.getMinutes()
    const second = date.getSeconds()

    return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatNumber = n => {
    n = n.toString()
    return n[1] ? n : '0' + n
}

//空值过滤器
const filterForm = (form) => {
    let obj = {};
    Object.keys(form).forEach(ele => {
        if (!validate.validateNull(form[ele])) {
            obj[ele] = form[ele]
        }
    });
    return obj;
}

module.exports = {
    formatTime: formatTime,
    formatNumber: formatNumber,
    filterForm: filterForm
}