import axios from "axios"
import utils from "./utils"

export const userPaperUtils = {
    getUserPaper(userId) {
        return axios.get(utils.url + "/userPaper/Paper", {
            params: {
                userId: userId,
            },
        }).then((response) => {
            return{
                code:response.data[0].code,
                msg:response.data[0].msg,
                data:response.data.slice(1)
            }
        }).catch((error) => {
            return Promise.reject({
                code: 404,
                data:[],
                msg: "服务器连接失败"
            })
        })
    }
}
export default userPaperUtils;
