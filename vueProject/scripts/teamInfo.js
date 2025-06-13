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
    }
};