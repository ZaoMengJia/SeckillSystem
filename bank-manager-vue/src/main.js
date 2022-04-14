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


//baseurl
axios.defaults.baseURL = 'http://localhost:8811'

//添加签名验证
axios.interceptors.request.use(req => {
  let contentType = req.headers['content-type'] ?? 'application/json';
  let method = req.method;

  let isJson = true;
  if(method === 'get' || method === 'GET' || contentType.indexOf('json') === -1) {
    isJson = false;
  }

  let signData = isJson ? RequestType.body : RequestType.query;

  req.headers = {
    ...req.headers,
    ...signData
  };

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
