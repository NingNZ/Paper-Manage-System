import axios from "axios"
import utils from "./utils"

const noticeUtils= {
    getNotices_NoOP(selectId) {
        // 获取纯文本通知
        return axios.get(utils.url + '/notice/getNoticesNoOP', {
            params: {
                selectId:selectId
            },
            withCredentials: true,
            timeout: 3000
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
                code: 404,
                data: [],
                msg: "服务不可用"
            })
        })
    },
    getNotices_OP(selectId) {
        // 获取通知
        return axios.get(utils.url + '/notice/getNoticesOP', {
            params: {
                selectId:selectId
            },
            withCredentials: true,
            timeout: 3000
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
                code: 404,
                data: [],
                msg: "服务不可用"
            })
        })
    },

    accept(selectId,msgID){
        // 告诉后端该条消息已经通过
        return axios.get(utils.url + '/notice/accept', {
            params: {
                selectId,
                msgID
            },
            withCredentials: true,
            timeout: 3000
        }).then((response) => {
            // 处理返回数据
            return {
                code: response.data.code,
                msg: response.data.msg
            }
        }).catch(error => {
            // 处理错误
            return Promise.reject({
                code: 404,
                msg: "服务不可用"
            })
        })
    },

    reject(selectId,msgID){
        // 告诉后端该条消息已经拒绝
        return axios.get(utils.url + '/notice/reject', {
            params: {
                selectId,
                msgID
            },
            withCredentials: true,
            timeout: 3000
        }).then((response) => {
            // 处理返回数据
            return {
                code: response.data.code,
                msg: response.data.msg
            }
        }).catch(error => {
            // 处理错误
            return Promise.reject({
                code: 404,
                msg: "服务不可用"
            })
        })
    },

    Sys_getNotices_OP() {   //获取管理员的通知
        // 获取通知
        return axios.get(utils.url + '/notice/SysgetNoticesOP', {
            withCredentials: true,
            timeout: 3000
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
                code: 404,
                data: [],
                msg: "服务不可用"
            })
        })
    },

    Sys_accept(msgID){   //管理员通过消息，告诉后端
        // 告诉后端该条消息已经通过
        return axios.get(utils.url + '/notice/Sysaccept', {
            params: {
                msgID
            },
            withCredentials: true,
            timeout: 3000
        }).then((response) => {
            // 处理返回数据
            return {
                code: response.data.code,
                msg: response.data.msg
            }
        }).catch(error => {
            // 处理错误
            return Promise.reject({
                code: 404,
                msg: "服务不可用"
            })
        })
    },

    Sys_reject(msgID){   //管理员拒绝消息，告诉后端
        // 告诉后端该条消息已经拒绝
        return axios.get(utils.url + '/notice/Sysreject', {
            params: {
                msgID
            },
            withCredentials: true,
            timeout: 3000
        }).then((response) => {
            // 处理返回数据
            return {
                code: response.data.code,
                msg: response.data.msg
            }
        }).catch(error => {
            // 处理错误
            return Promise.reject({
                code: 404,
                msg: "服务不可用"
            })
        })
    }
}

export default noticeUtils