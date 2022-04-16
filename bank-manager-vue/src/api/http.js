import axios from "axios";
import {RequestType, sign} from "@/utils/signUtils_raw";
import qs from "qs";

axios.defaults.baseURL = 'http://localhost:8811'

export async function request(data, isBodyJson = true) {
    let isJson = true;
    let rawData = data.data;
    if (!isBodyJson || data.method.toLowerCase() === 'get') {
        isJson = false;
        if (data.method.toLowerCase() === 'get' && data.data !== {}) {
            let arr = [];
            for (let p in data.data) {
                if (data.data.hasOwnProperty(p)) {
                    arr.push(encodeURIComponent(p) + "=" + encodeURIComponent(data.data[p]));
                }
            }

            if (arr.length > 0) {
                data.url += `?${arr.join('&')}`;
            }

            data.data = {};
        } else {
            data.data = qs.stringify(data.data)
        }
    }

    if (isBodyJson) {
        data.headers = {...data.headers, 'content-type': 'application/json'};
    }


    let signatureData = sign(isJson ? RequestType.json : RequestType.query, rawData);
    data.headers = {...data.headers, ...signatureData}

    //添加token
    let token = getToken();
    if (token !== null) {
        data.headers.Authorization = token;
    }

    return axios(data)
        .then(res => {
            if (res.data.code === 10000) {
                return [res, null];
            } else {
                return [null, res.data];
            }
        })
        .catch(err => [null, err]);
}

function getToken() {
    let vuex = localStorage.getItem("vuex");
    if (vuex === '') {
        return null;
    }
    vuex = JSON.parse(vuex);
    return vuex.token ?? null;
}

export default {
    request,
}