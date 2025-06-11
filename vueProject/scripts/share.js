import { readonly } from "vue";

const _sharedPermission = ref(-1);
export const share ={
    permisson:readonly(_sharedPermission),
    setPermisson(val){
        _sharedPermission.value = val
    }
}