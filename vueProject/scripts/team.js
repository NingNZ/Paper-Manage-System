import axios from "axios"
import utils from "./utils"
import { ElMessage } from "element-plus"
const teamUtils = {
    /**
     * 
     * @param {String} userId 
     * @returns 
     */
    getMyTeamList (userId){
     return axios.get(utils.url+'/myTeam/get', {
            params: {
                userId:userId
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
    /**
     * 
     * @param {String} name 
     */
    checkTeamExists(name){
        
    }

}
export default teamUtils