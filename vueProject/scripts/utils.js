import axios from "axios"

const utils = {
    url:"http://10.68.200.226:5123",
    /**
    * @param {string} isSystem 
    * @param {string} teamId
    */
    getSysType (isSystem="false",teamId=''){
     return axios.get(utils.url+'/type', {
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
     return axios.get(utils.url+'/journal', {
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