// share.js
import { ref } from 'vue'

const permission = ref(-1); // 初始为游客权限（-1）

export const share = {
  setPermisson(value) {
    if (value === 1 || value === 0 || value === -1) {
      permission.value = value;
    } else {
      console.warn("非法权限值:", value);
    }
  },
  getPermission() {
    return permission;
  }
}
