import axios from "axios"
import utils from "./utils";
export const sessionUtil={
    /**
     * 
     * @returns string
     */
    getUserInfo(){
        return axios.get(utils.url+'/session/userInfo',{
            withCredentials: true
        })
        .then(res => {
            if(res.data[0].code==200){ // 是普通用户 
                return {code: res.data[0].code , data: res.data[1]};
            }else if(res.data[0].code==300){ // 是管理员
                return {code: res.data[0].code , data: null};
            }
        }).catch(res=>{
            return null
        })
    },
    
    checkPermiss(){
        return axios.get(utils.url+'/session/check',{
            withCredentials:true
        })
        .then(res=>{
            if(res.data.code==200){
                return 1;
            }else if(res.data.code==300){
                return 0;
            }else{
                return -1;
            }
        }).catch(res=>{
            return -1;
        })
    }
}