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
     * @param {String} uid 
     * @returns 
     */
    addMember(teamId,uid){
       return axios.get(utils.url+'/myTeam/addMember', {
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

}
export default teamUtils