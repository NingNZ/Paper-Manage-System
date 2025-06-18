import utils from "./utils";
import axios from "axios";
export const waitPaperUtils={
    downloadWaitPaper(paperId){
        // 1. 发送 GET 请求（关键：responseType: 'blob'）
        return axios({
            method: 'get',
            url: utils.url+'/wait/download',
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
        }).catch (async err => {
            console.log("download fail");
            const blob = err.response.data;
            const text = await blob.text();
            alert(text);  // 错误处理
        })         
    },
}