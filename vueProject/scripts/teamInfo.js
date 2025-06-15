import axios from "axios"
import utils from "./utils";
import qs from 'querystring'

export const teamInfoUtils = {
    /**
     
     */
    getRefPapersList(teamId) {
        return axios.get(utils.url + '/teamInfo/papers', {
            params: {
                teamId: teamId
            },
            withCredentials:true,
            timeout: 3000
        }).then((response) => {
            return {
                code: response.data[0].code,
                data: response.data.slice(1),
                msg: response.data[0].msg
            }
        }).catch(error => {
            // 处理错误
            return Promise.reject({
                code: 404,
                data:[],
                msg: "服务器未连接"
            })
        })
    },

    editRefPaper(teamId,paperId) {
        return axios.get(utils.url + '/teamInfo/editPaper', {
            params: {
                teamId: teamId,
                paperId: paperId
            },
            withCredentials:true,
            timeout: 3000
        }).then((response) => {
            return {
                code: response.data[0].code,
                data: response.data.slice(1),
                msg: response.data[0].msg
            }
        }).catch(error => {
            // 处理错误
            return Promise.reject({
                code: 404,
                msg: "服务器未连接"
            })
        })
    },

    deleteRefPaper(teamId,paperId) {
        return axios.get(utils.url + '/teamInfo/deletePaper', {
            params: {
                teamId: teamId,
                paperId: paperId
            },
            withCredentials:true,
            timeout: 3000
        }).then((response) => {
            return {
                code: response.data[0].code,
                data: response.data.slice(1),
                msg: response.data[0].msg
            }
        }).catch(error => {
            // 处理错误
            return Promise.reject({
                code: 404,
                msg: "服务器未连接"
            })
        })
    },

    // 获取团队成员列表
        /**
     * 
     * @param {String} teamId 
     * @returns 
     */
    getMemberList (teamId){
     return axios.get(utils.url+'/teamMember', {
            params:{
                teamId:teamId
            },
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

    // 获取团队分类信息
    getTeamCategory(teamId) {
        return axios.get(utils.url + '/teamInfo/Category', {
            withCredentials: true,
            params: { teamId },
            timeout: 3000
        }).then((response) => {
            return {
                code: response.data[0].code,
                msg: response.data[0].msg,
                data: response.data.slice(1)
            }
        }).catch(error => {
            return Promise.reject({
                code: 404,
                data: [],
                msg: "服务器未连接"
            });
        });
    },

    // 新增分类
    CategoryAdd(teamId, fid, label) {
        return axios.post(
            utils.url + '/teamInfo/CategoryAdd',
            qs.stringify({ teamId, fid, label }),
            {
                withCredentials: true
            }
        ).then((response) => {
            return {
                code: response.data[0].code,
                data: response.data.slice(1),
                msg: response.data[0].msg
            }
        }).catch(error => {
            return Promise.reject({
                code: 404,
                data: [],
                msg: "服务器未连接"
            });
        });
    },

    // 编辑分类
    CategoryEdit(teamId,id,label) {
        return axios.get(utils.url + '/teamInfo/CategoryEdit', {
            params:{
                teamId:teamId,
                id:id,
                label:label
            },
            withCredentials: true
        }).then((response) => {
            return {
                code:response.data.code,
                msg:response.data.msg
            }
        }).catch(error => {
            return Promise.reject({
                code: 404,
                msg: "服务器未连接"
            });
        });
    },

    // 删除分类
    CategoryDelete(teamId, id) {
        return axios.get(utils.url + '/teamInfo/CategoryDelete', {
            params:{
                teamId:teamId,
                id:id
            },
            withCredentials: true
        }).then((response) => {
            return{
                code:response.data[0].code,
                msg:response.data[0].msg,
                data:response.data.slice(1)
            }
        }).catch(error => {
            return Promise.reject({
                code: 404,
                data: [],
                msg: "服务器未连接"
            });
        });
    },

    //进入团队时判断是否是组长
    CheckTeamRole(teamId) {
        return axios.get(utils.url + '/teamInfo/CheckRole', {
            params: { teamId },
            withCredentials: true
        }).then((response) => {
            console.log(response.data);
            if( response.data.code === 200) {
                return 1;
            }
            else if (response.data.code === 400) {
                return 0;
            }
        }).catch(error => {
            return Promise.reject({
                code: 404,
                data: [],
                msg: "服务器未连接"
            });
        });
    }
};