import axios from "axios"

const utils = {
    /**
    * @param {string} isSystem 
    * @param {string} teamId
    */
    getSysType (isSystem="false",teamId=''){
     return axios.get('http://localhost:5123/type', {
            params: {
                isSystem: isSystem,
                teamId : teamId
            },
            timeout:3000
        }).then((response) => {
        // 处理返回数据
            return {
                code: response.data[0].code,
                data: response.data.slice(1),
                msg: response.data[0].msg
            }
        }).catch(error => {
        // 处理错误
            return Promise.reject({
                code:404,
                data:[],
                msg:"服务不可用"
            })
        })
    },
    getSysJournal (){
     return axios.get('http://localhost:5123/journal', {
            timeout:3000
        }).then((response) => {
        // 处理返回数据
            return {
                code: response.data[0].code,
                data: response.data.slice(1),
                msg: response.data[0].msg
            }
        }).catch(error => {
        // 处理错误
            return Promise.reject({
                code:404,
                data:[],
                msg:"服务不可用"
            })
        })
    }
};
export default utils;