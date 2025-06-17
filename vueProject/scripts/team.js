import axios from "axios"
import utils from "./utils"
import { ElMessage } from "element-plus"
const teamUtils = {
    /**
     * 
     * @returns 
     */
    getMyTeamList (){
     return axios.get(utils.url+'/myTeam/get', {
            withCredentials:true,
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
       return axios.get(utils.url+'/myTeam/check', {
            params: {
                name:name
            },
            timeout:3000
        }).then((response) => {
        // 处理返回数据
            return {
                code: response.data.code,
                msg: response.data.msg
            }
        }).catch(error => {
        // 处理错误
            return Promise.reject({
                code:404,
                msg:"服务不可用"
            })
        })      
    },
    createTeam(name){
       return axios.get(utils.url+'/myTeam/new', {
            params: {
                name:name,
            },
            withCredentials:true,
            timeout:3000
        }).then((response) => {
        // 处理返回数据
            return {
                code: response.data.code,
                msg: response.data.msg
            }
        }).catch(error => {
        // 处理错误
            return Promise.reject({
                code:404,
                msg:"服务不可用"
            })
        })      
    },
    /**
     * 
     * @param {String} teamId 
     * @param {String} uid 
     * @returns 
     */
    dropMember(teamId,uid){
       return axios.get(utils.url+'/myTeam/dropMember', {
            params: {
                teamId:teamId,
                uid:uid
            },
            timeout:3000
        }).then((response) => {
        // 处理返回数据
            return {
                code: response.data.code,
                msg: response.data.msg
            }
        }).catch(error => {
        // 处理错误
            return Promise.reject({
                code:404,
                msg:"服务不可用"
            })
        })      
    },
    /**
     * 搜索团队成员
     * @param {String} teamId 团队ID
     * @param {String} memberId 成员ID
     * @returns {Promise<{code: number, data: object, msg: string}>}
     * code: 200 成员存在且未加入团队
     * code: 300 成员已在团队中
     * code: 400 未找到成员
     */
    searchMember(teamId, memberId) {
        return axios.get(utils.url + '/myTeam/searchMember', {
            params: {
                teamId: teamId,
                memberId: memberId
            },
            withCredentials:true,
            timeout: 3000
        }).then((response) => {
            // 假设后端返回格式为 { code, data, msg }
            return {
                code: response.data[0].code,
                data: response.data.slice(1),
                msg: response.data[0].msg
            }
        }).catch(error => {
            // 处理错误
            return Promise.reject({
                code: 404,
                data: [],
                msg: "服务不可用"
            })
        })
    },
    /**
     * 
     * @param {String} teamId 
     * @returns 
     */
    searchOneTeam (teamId){
     return axios.get(utils.url+'/myTeam/searchOne', {
            params: {
                teamId:teamId
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
     * @param {String} teamId 
     * @returns 
     */
    addMember(teamId){
       return axios.get(utils.url+'/myTeam/addMember', {
            params: {
                teamId:teamId,
            },
            withCredentials:true,
            timeout:3000
        }).then((response) => {
        // 处理返回数据
            return {
                code: response.data.code,
                msg: response.data.msg
            }
        }).catch(error => {
        // 处理错误
            return Promise.reject({
                code:404,
                msg:"服务不可用"
            })
        })      
    },

    getMainTeam(){
        return axios.get(utils.url+"/myTeam/getManagerTeam",{
            withCredentials:true
        })
        .then((response)=>{
            return {
                code: response.data[0].code,
                msg: response.data[0].msg,
                data: response.data.slice(1)
            }
        }).catch(()=>{
            return Promise.reject({
                code:404,
                msg:"服务不可用"
            })
        })
    }
}
export default teamUtils