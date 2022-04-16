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
    //订单
    getOrderList: async (pageNum, pageSize) => request({
        method: 'get',
        url: '/web/order',
        data: {
            pageNum, pageSize
        }
    }),
    searchOrder: async (keyword, pageNum, pageSize) => request({
        method: 'get',
        url: '/web/order/search',
        data: {
            keyword, pageNum, pageSize
        }
    }),
    //理财产品
    getProductList: async (pageNum, pageSize) => request({
        method: 'get',
        url: '/web/product',
        data: {
            pageNum, pageSize
        }
    }),
    searchProduct: async (keyword, pageNum, pageSize) => request({
        method: 'get',
        url: '/web/product/search',
        data: {
            keyword, pageNum, pageSize
        }
    }),
    saveProduct: async (name, price) => request({
        method: 'post',
        url: '/web/product',
        data:{
            name, price
        }
    }),
    editProduct: async (id, name, price) => request({
        method: 'put',
        url: `/web/product/${id}`,
        data:{
            name, price
        }
    }),
    deleteProduct: async (id) => request({
        method: 'delete',
        url: `/web/product/${id}`
    })
}