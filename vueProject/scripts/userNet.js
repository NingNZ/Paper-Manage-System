import axios from "axios"
import utils from "./utils"
import { stringify } from "querystring"
export const userNetUtils = {
    checkUserIdExist(userId){
        return axios.get(utils.url+"/net/checkId",{
            params:{
                userId:userId
            },
        }).then((response)=>{
            const data = response.data
            return data
        }).catch((error)=>{
            return Promise.reject({
                code:404,
                msg:"服务器连接失败"
            })
        })
    },
    getCoAuthor(userId) {
        return axios.get(utils.url+"/net/getCoAuthor", {
            params: {
                userId: userId
            },
        }).then((response) => {
            return {
                code: response.data[0].code,
                msg: response.data[0].msg,
                data: response.data[1]
            }

        }).catch((error) => {
            return Promise.reject({
                code: 404,
                msg: "服务器连接失败"
            })
        })
    }

}
