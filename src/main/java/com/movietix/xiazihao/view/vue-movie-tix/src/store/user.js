import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useUserStore = defineStore('user', () => {
    const id = ref(null);
    const username = ref('');
    const token = ref('');

    const setUserInfo = (userInfo) => {
        id.value = userInfo.id;
        username.value = userInfo.username;
        token.value = userInfo.token;
        localStorage.setItem('token', token.value);
    };

    const clearUserInfo = () => {
        id.value = null;
        username.value = '';
        token.value = '';
        localStorage.removeItem('token');
    };

    const isAuthenticated = () => {
        return !!token.value;
    };

    return {
        id,
        username,
        token,
        setUserInfo,
        clearUserInfo,
        isAuthenticated
    };
});