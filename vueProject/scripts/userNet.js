import axios from "axios"
import utils from "./utils"
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
}
