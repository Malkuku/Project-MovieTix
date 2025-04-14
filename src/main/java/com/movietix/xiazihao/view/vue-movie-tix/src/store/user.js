import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useUserStore = defineStore('user', () => {
    const userId = ref(null);
    const username = ref('');
    const token = ref('');

    const setUserInfo = (userInfo) => {
        userId.value = userInfo.id;
        username.value = userInfo.username;
        token.value = userInfo.token;
        localStorage.setItem('token', token.value);
    };

    const clearUserInfo = () => {
        userId.value = null;
        username.value = '';
        token.value = '';
        localStorage.removeItem('token');
    };

    const isAuthenticated = () => {
        return !!token.value;
    };

    return {
        userId,
        username,
        token,
        setUserInfo,
        clearUserInfo,
        isAuthenticated
    };
});