import {request} from "@/api/http";
async function getWeixinUserList(pageNum, pageSize, token) {
    return request({
        method: 'get',
        url: '/web/weixin-user',
        data: {
          pageNum, pageSize
        },
        headers: {
            Authorization: token
        }
    });
}

async function searchWeixinUserList(keyword, type, pageNum, pageSize, token) {
    return request({
        method: 'get',
        url: '/web/weixin-user/search',
        data: {
          keyword, type, pageNum, pageSize
        },
        headers: {
            Authorization: token
        }
    });
}

async function getAdminUserList(pageNum, pageSize, token) {
    return request({
        method: 'get',
        url: '/web/admin',
        data: {
            pageNum, pageSize
        },
        headers: {
            Authorization: token
        }
    });
}

export default {
    getWeixinUserList,
    searchWeixinUserList,
    getAdminUserList
}