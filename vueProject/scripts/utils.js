import axios from "axios"
import { ElMessage } from "element-plus";

const utils = {
    url:"http://10.68.200.226:5123",
    /**
    * @param {string} isSystem 
    * @param {string} teamId
    */
    getSysType (isSystem="false",teamId=''){
     return axios.get(utils.url+'/type', {
            params: {
                isSystem: isSystem,
                teamId : teamId
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
    getSysJournal (){
     return axios.get(utils.url+'/journal', {
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
    getMemberList (teamId){
     return axios.get(utils.url+'/teamMember', {
            params:{
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
     * @param {String} paperId 
     */
    downloadSysPaper(paperId){
        // 1. 发送 GET 请求（关键：responseType: 'blob'）
        axios({
            method: 'get',
            url: this.url+'/sysPaper/download',
            params:{
                paperId:paperId
            },
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
        }).catch ((error)=> {
            console.log("download fail");
            ElMessage.error("下载失败")
            console.error('Download error:', error); // 错误处理
        })         
    },
    /**
     * 
     * @param {String} paperId 
     */
    deleteSysPaper(paperId){
     return axios.delete(utils.url+'/sysPaper/delete', {
            params: {
                paperId: paperId,
            },
            timeout:3000
        }).then((response) => {
        // 处理返回数据
            return {
                code: response.data.code,
                data:[],
                msg: response.data.msg
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
     * @param {String} paperId 
     * @param {String} typeId 
     * @returns 
     */
    updateSysPaper(paperId,typeId){
     return axios.get(utils.url+'/sysPaper/update', {
            params: {
                paperId: paperId,
                typeId:typeId
            },
            timeout:3000
        }).then((response) => {
        // 处理返回数据
            return {
                code: response.data.code,
                data:[],
                msg: response.data.msg
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
     * @returns 
     */
    newSysType (name){
     return axios.get(utils.url+'/sysType/new', {
            params: {
                name:name
            },
            timeout:3000
        }).then((response) => {
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
     * @param {String} name 
     * @param {String} id 
     * @returns 
     */
    updateSysType (name,id){
     return axios.get(utils.url+'/sysType/update', {
            params: {
                name:name,
                id:id
            },
            timeout:3000
        }).then((response) => {
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
     * @param {String} name 
     * @returns 
     */
    newSysJournal (name){
     return axios.get(utils.url+'/sysJournal/new', {
            params: {
                name:name
            },
            timeout:3000
        }).then((response) => {
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
     * @param {String} name 
     * @param {String} id 
     * @returns 
     */
    updateSysJournal (name,id){
     return axios.get(utils.url+'/sysJournal/update', {
            params: {
                name:name,
                id:id
            },
            timeout:3000
        }).then((response) => {
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
};
export default utils;