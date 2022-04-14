import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    username: '',
    token: ''
  },
  getters: {
    token: state => state.token
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
    }
  },
  actions: {
  },
  modules: {
  },
  plugins: [createPersistedState()],
})
