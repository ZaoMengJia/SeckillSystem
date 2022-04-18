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
        redirect: '/login'
    },
    {
        path: '/login',
        name: "Login",
        component: Login,
        meta:{title:"后台登录"}
    },
    {
        path: '/adminHome',
        name: "AdminHome",
        component: AdminHome,
        children: [
            {
                path: '/financialProduct',
                name: 'FinancialProduct',
                component: FinancialProductManager,
                meta:{title:"理财产品管理"}
            },
            {
                path: '/order',
                name: 'Order',
                component: OrderManager,
                meta:{title:"订单管理"}
            },
            {
                path: '/saleProductDetail',
                name: 'SaleProductDetail',
                component: SaleProductDetailManager,
                meta:{title:"产品细节管理"}
            },
            {
                path: '/seckillActivity',
                name: 'SeckillActivity',
                component: SeckillActivityManager,
                meta:{title:"秒杀活动管理"}
            },
            {
                path: '/user',
                name: 'User',
                component: UserManager,
                meta:{title:"用户管理"}
            },
            {
                path: '/admin',
                name: 'Admin',
                component: AdminManager,
                meta:{title:"管理员管理"}
            },
            {
                path: '/seckill-activity-detail',
                name: 'seckillActivityDetail',
                component: SeckillActivityDetailView,
                meta:{title:"秒杀活动设置"}
            }
        ]
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

function getToken() {
    let vuex = localStorage.getItem("vuex");
    if (vuex === null || vuex === '') {
        return null;
    }
    vuex = JSON.parse(vuex);
    return vuex.token ?? null;
}

router.beforeEach(((to, from, next) => {
    if(to.path === '/login') {
        next();
        return;
    }

    if(getToken() === null) {
        next({
            path: '/login'
        });
        return;
    }

    next();


}))

export default router
