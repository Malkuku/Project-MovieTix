import {defineStore} from 'pinia';
import {ref} from 'vue';


export const useUserStore = defineStore('user', () => {
    const id = ref(null);
    const username = ref('');
    const token = ref('');
    const adminToken = ref('');
    //TODO 用户头像avatar 和 名字nickname的储存 用户余额的显示-》store需要更新

    const setUserInfo = (userInfo) => {
        id.value = userInfo.id;
        username.value = userInfo.username;
        token.value = userInfo.token;
        adminToken.value = userInfo.adminToken;
    };

    const clearUserInfo = () => {
        id.value = null;
        username.value = '';
        token.value = '';
        adminToken.value = '';
    };

    const isAuthenticated = () => {
        return !!token.value;
    };

    return {
        id,
        username,
        token,
        adminToken,
        setUserInfo,
        clearUserInfo,
        isAuthenticated
    };
}, {
    persist: {
        enabled: true, // 启用持久化
        strategies: [
            {
                key: 'user', // 存储在 sessionStorage 中的键名
                storage: sessionStorage, // 使用 sessionStorage 持久化
                autoPrune: true
            },
        ],
    },
});