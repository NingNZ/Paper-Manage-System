import axios from "axios"
import utils from "./utils"

export const userPaperUtils = {
    getUserPaper(userId) {
        // return axios.get(utils.url + "/userPaper/Paper", {
        //     params: {
        //         userId: userId,
        //     },
        // }).then((response) => {
        //     const data = response.data
        //     return data
        // }).catch((error) => {
        //     return Promise.reject({
        //         code: 404,
        //         msg: "服务器连接失败"
        //     })
        // })
        return Promise.resolve({
            code: 200,
            msg: '获取成功',
            data: [
            {
                title: "示例论文1",
                category: "示例分类1",
                journal: "示例期刊1",
                uploadDate: "2023-10-01",
            },
            {
                title: "示例论文2",
                category: "示例分类2",
                journal: "示例期刊2",
                uploadDate: "2023-10-01",
            }
            ]
        })
    },

    downloadUserPaper(paperId) {
        // 发送 GET 请求，下载用户论文
        // return axios({
        //     method: 'get',
        //     url: utils.url + '/userPaper/download',
        //     params: {
        //         paperId: paperId,
        //         userId: userId
        //     },
        //     withCredentials: true,
        //     responseType: 'blob'
        // }).then((response) => {
        //     const contentDisposition = response.headers['content-disposition'] || response.headers['Content-Disposition'];
        //     let finalFileName = 'default';
        //     if (contentDisposition) {
        //         const match = contentDisposition.match(/filename\*?="?([^"]+)"?/);
        //         if (match) finalFileName = decodeURIComponent(match[1]);
        //     }
        //     const blob = new Blob([response.data], {
        //         type: response.headers['content-type']
        //     });
        //     const objectUrl = URL.createObjectURL(blob);
        //     const link = document.createElement('a');
        //     link.download = finalFileName;
        //     link.href = objectUrl;
        //     document.body.appendChild(link);
        //     link.click();
        //     document.body.removeChild(link);
        //     URL.revokeObjectURL(objectUrl);
        // }).catch(async (err) => {
        //     console.log("download fail");
        //     const blob = err.response?.data;
        //     const text = await blob.text();
        //     alert(text);
        //     return Promise.reject();
        // })
        
        const url = '/test.pdf' 
        const a = document.createElement('a')
        a.href = url
        a.download = '我的论文.pdf' // 下载时显示的文件名
        document.body.appendChild(a)
        a.click()
        document.body.removeChild(a)
        // 可以返回一个 Promise 以保持接口一致
        return Promise.resolve()
    },
}
export default userPaperUtils;
