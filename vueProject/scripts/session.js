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
    }
}