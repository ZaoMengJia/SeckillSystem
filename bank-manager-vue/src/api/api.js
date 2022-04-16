import {request} from "@/api/http";
export default {
    getWeixinUserList: async (pageNum, pageSize) => request({
        method: 'get',
        url: '/web/weixin-user',
        data: {
            pageNum, pageSize
        }
    }),
    searchWeixinUserList: async (keyword, type, pageNum, pageSize) => request({
        method: 'get',
        url: '/web/weixin-user/search',
        data: {
            keyword, type, pageNum, pageSize
        }
    }),
    deleteWeixinUser: async (userId) => request({
       method: 'delete',
       url: `/web/weixin-user/${userId}`
    }),
    getAdminUserList: async (pageNum, pageSize) => request({
        method: 'get',
        url: '/web/admin',
        data: {
            pageNum, pageSize
        }
    }),
    searchAdminUserList: async (keyword, pageNum, pageSize) => request({
        method: 'get',
        url: '/web/admin/search',
        data: {
            keyword, pageNum, pageSize
        }
    }),
    insertAdminUser: async (username, password) => request({
        method: 'post',
        url: '/web/admin',
        data: {
            username, password
        }
    }, true),
    deleteAdminUser: async (userId) => request({
        method: 'delete',
        url: `/web/admin/${userId}`
    }),
    editAdminUser: async (userId, username, password) => request({
        method: 'put',
        url: `/web/admin/${userId}`,
        data: {
            username, password
        }
    }),
    getSeckillActivityList: async (pageNum, pageSize) => request({
        method: 'get',
        url: `/web/seckill/detail`,
        data: {
            pageNum, pageSize
        }
    }),
    searchSeckillActivity: async (keyword, pageNum, pageSize) => request({
        method: 'get',
        url: `/web/seckillActivity/searchSeckillActivity/${keyword}/${pageNum}/${pageSize}`
    }),
    getSeckillActivityDetail: async (seckillActivityId) => request({
        method: 'get',
        url: `/web/seckill/detail/${seckillActivityId}`
    }),
    deleteSeckillActivity: async (id) => request({
        method: 'delete',
        url: `/web/seckillActivity/deleteSeckillActivity/${id}`
    }),
    getOrderList: async (pageNum, pageSize) => request({
        method: 'get',
        url: `/web/order/getAllOrders/${pageNum}/${pageSize}`
    }),
    searchOrder: async (keyword, pageNum, pageSize) => request({
        method: 'get',
        url: `/web/order/searchOrder/${keyword}/${pageNum}/${pageSize}`
    }),
    insertOrder: async ({id, userId, financialProductId, seckillActivityId, quantity}) => request({
        method: 'post',
        url: `/web/order/addOrder`,
        data: {
            id, userId, financialProductId, seckillActivityId, quantity
        }
    })
}