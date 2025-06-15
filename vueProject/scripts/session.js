import axios from "axios"
import utils from "./utils";
export const sessionUtil={
    /**
     * 
     * @returns string
     */
    getUser(){
        return axios.get(utils.url+'/session/userInfo',{
            withCredentials: true
        })
        .then(res => {
            return res.data 
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