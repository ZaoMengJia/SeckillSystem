import Base64 from './encrypt/base64';
import MD5 from './encrypt/md5'
const appKey = 'IZe37zbJnGpPrZ5u'

/**
 * 请求类型
 */
export const RequestType = {
    query: 'query',
    form: 'form',
    json: 'json',
    body: 'json'
};

/**
 * 签名，不包含path参数
 * @param {string} requestType 
 * @param {Object} body 
 */
export function sign(requestType = RequestType.query, body = {}) {
    let nonce = getRandomString();
    let timestamp = new Date().getTime();

    let data = requestType != RequestType.body ? sortObject(body) : body;
    let input = JSON.stringify(data) + nonce + timestamp + appKey;
    let signature = Base64.encode(MD5.md5(input)).replaceAll(/[/=+]/g, '');
    console.log(input);
    console.log({nonce, timestamp, signature});
    return {nonce, timestamp, signature};
}


/**
 * 将对象按照key进行排序
 * @param {Object} object 
 * @returns 排序后的对象
 */
function sortObject(object = {}) {
    let newObject = {};
    Object.keys(object).sort().forEach(key => {
        newObject[key] = object[key];
    })

    return newObject;
}

function getRandomString() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        let r = Math.random() * 16 | 0,
            v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    }).replaceAll('-', '');
}

export default {
    sign,
    RequestType
}