import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Element from 'element-ui'
import axios from 'axios'

import "element-ui/lib/theme-chalk/index.css"
import {RequestType, sign} from "@/utils/signUtils";

Vue.use(Element)

Vue.config.productionTip = false
Vue.prototype.$http = axios;


axios.interceptors.request.use(req => {
  req.headers.nonce = sign(RequestType.query,req.data).nonce;
  req.headers.signature = sign(RequestType.query,req.data).signature;
  req.headers.timestamp = sign(RequestType.query,req.data).timestamp;
  return req
}, err => {
  return Promise.reject(err)
})

let vue = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

export default vue
