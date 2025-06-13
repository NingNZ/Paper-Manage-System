import axios from "axios"
import utils from "./utils";

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
                msg: "服务不可用"
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
                msg: "服务不可用"
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
                msg: "服务不可用"
            })
        })
    },

    // 获取团队成员列表
    getTeamMemberList(teamId) {
        return axios.get(utils.url + '/teamInfo/Member', {
            withCredentials: true,
            params: { teamId }
        }).then((response) => {
            return {
                code: response.data[0].code,
                data: response.data.slice(1),
                msg: response.data[0].msg
            };
        }).catch(error => {
            return Promise.reject({
                code: 404,
                data: [],
                msg: "服务不可用"
            });
        });
    },

    // 获取团队分类信息
    getTeamCatagory(teamId) {
        return axios.get(utils.url + '/teamInfo/Catagory', {
            withCredentials: true,
            params: { teamId }
        }).then((response) => {
            return response.data;
        }).catch(error => {
            return Promise.reject({
                code: 404,
                data: [],
                msg: "服务不可用"
            });
        });
    },

    // 新增分类
    CatagoryAdd(teamId, parentlable, lable) {
        return axios.post(utils.url + '/teamInfo/CatagoryAdd', {
            teamId,
            parentlable,
            lable
        }, {
            withCredentials: true
        }).then((response) => {
            return response.data;
        }).catch(error => {
            return Promise.reject({
                code: 404,
                data: [],
                msg: "服务不可用"
            });
        });
    },

    // 编辑分类
    CatagoryEdit(teamId, catagorylable, lable) {
        return axios.post(utils.url + '/teamInfo/CatagoryEdit', {
            teamId,
            catagorylable,
            lable
        }, {
            withCredentials: true
        }).then((response) => {
            return response.data;
        }).catch(error => {
            return Promise.reject({
                code: 404,
                data: [],
                msg: "服务不可用"
            });
        });
    },

    // 删除分类
    CatagoryDelete(teamId, catagorylable) {
        return axios.post(utils.url + '/teamInfo/CatagoryDelete', {
            teamId,
            catagorylable
        }, {
            withCredentials: true
        }).then((response) => {
            return response.data;
        }).catch(error => {
            return Promise.reject({
                code: 404,
                data: [],
                msg: "服务不可用"
            });
        });
    }
};