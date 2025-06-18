import axios from "axios"
import utils from "./utils";
import qs from 'querystring'

export const teamInfoUtils = {
    /** 论文管理 */
    /************************************************ */
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

    editRefPaper(teamId,paperId,typeId) {
        return axios.get(utils.url + '/teamInfo/editPaper', {
            params: {
                teamId: teamId,
                paperId: paperId,
                typeId:typeId
            },
            withCredentials:true,
            timeout: 3000
        }).then((response) => {
            return {
                code: response.data.code,
                msg: response.data.msg
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
                code: response.data.code,
                msg: response.data.msg
            }
        }).catch(error => {
            // 处理错误
            return Promise.reject({
                code: 404,
                msg: "服务器未连接"
            })
        })
    },
    
    downloadRefPaper(paperId,teamId){
        // 1. 发送 GET 请求（关键：responseType: 'blob'）
        return axios({
            method: 'get',
            url: utils.url+'/teamInfo/download',
            params:{
                paperId:paperId,
                teamId:teamId
            },
            withCredentials:true,
            responseType: 'blob' // 告诉 axios 返回二进制数据（Blob）
        }).then((response)=>{
            //**** */
            const contentDisposition = response.headers['content-disposition']||response.headers['Content-Disposition'];
            let finalFileName = 'default';
            if (contentDisposition) {
                const match = contentDisposition.match(/filename*="(.+?)"$/);
                if (match) finalFileName = match[1];
            }
            // 2. 将响应数据转为 Blob 对象
            const blob = new Blob([response.data], { 
                type: response.headers['content-type'] // 设置文件类型（如 application/pdf）
            });
            // 3. 生成临时下载链接
            const objectUrl = URL.createObjectURL(blob);

            // 4. 创建隐藏的 <a> 标签触发下载
            const link = document.createElement('a');
            link.download = finalFileName;
            link.href = objectUrl;
            document.body.appendChild(link); // 添加到 DOM
            link.click(); // 模拟点击下载
            // 5. 清理资源
            document.body.removeChild(link); // 移除 <a> 标签
            URL.revokeObjectURL(objectUrl); // 释放内存（避免内存泄漏）
        }).catch (async err=> {
            console.log("download fail");
            const blob = err.response.data;
            const text = await blob.text();
            alert(text);  // 错误处理
            return Promise.reject()
        })         
    },

    //成员管理
    /************************************************ */
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



    /** 分类管理 */
    /************************************************ */
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