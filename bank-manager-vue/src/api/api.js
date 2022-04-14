import {request} from "@/api/http";
async function getWeixinUserList(pageNum, pageSize, token) {
    return request({
        method: 'get',
        url: `/web/weixin-user`,
        data: {
          pageNum, pageSize
        },
        headers: {
            Authorization: token
        }
    })
}

async function searchWeixinUserList(keyword, pageNum, pageSize, token) {
    return request({
        method: 'get',
        url: `/web/weixin-user?pageNum=${pageNum}&pageSize=${pageSize}`,
        headers: {
            Authorization: token
        }
    })
}

export default {
    getWeixinUserList
}