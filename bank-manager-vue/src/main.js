import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Element from 'element-ui'
import axios from 'axios'

import "element-ui/lib/theme-chalk/index.css"

Vue.use(Element)

Vue.config.productionTip = false
Vue.prototype.$http = axios;

axios.interceptors.request.use(config=>{
  config.headers.Authorization=window.sessionStorage.getItem('token');
  return config;
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
