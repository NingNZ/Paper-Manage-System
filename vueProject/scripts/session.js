import axios from "axios"
import utils from "./utils";
export const sessionUtil={
    /**
     * 
     * @returns string
     */
    getUser(){
        return axios.get(utils.url+'/session/userid',{
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
            console.log(res)
            return res.data.code
        }).catch(res=>{
            return null;
        })
    }
}