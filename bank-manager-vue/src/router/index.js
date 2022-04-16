import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import AdminHome from '../views/AdminHome.vue'
import FinancialProductManager from '../components/FinancialProductManager.vue'
import OrderManager from '../components/OrderManager.vue'
import SaleProductDetailManager from '../components/SaleProductDetailManager.vue'
import SeckillActivityManager from '../components/SeckillActivityManager.vue'
import UserManager from '../components/UserManager.vue'
import AdminManager from '../components/AdminManager.vue'
import SeckillActivityDetailView from "@/views/seckillActivityDetail/SeckillActivityDetailView";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        redirect: '/login',
    },
    {
        path: '/login',
        name: "Login",
        component: Login
    },
    {
        path: '/adminHome',
        name: "AdminHome",
        component: AdminHome,
        children: [
            {
                path: '/financialProduct',
                name: 'FinancialProduct',
                component: FinancialProductManager
            },
            {
                path: '/order',
                name: 'Order',
                component: OrderManager
            },
            {
                path: '/saleProductDetail',
                name: 'SaleProductDetail',
                component: SaleProductDetailManager
            },
            {
                path: '/seckillActivity',
                name: 'SeckillActivity',
                component: SeckillActivityManager
            },
            {
                path: '/user',
                name: 'User',
                component: UserManager
            },
            {
                path: '/admin',
                name: 'Admin',
                component: AdminManager
            },
            {
                path: '/seckill-activity-detail',
                name: 'seckillActivityDetail',
                component: SeckillActivityDetailView
            }
        ]
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
