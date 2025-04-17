import {defineStore} from 'pinia';
import {ref} from 'vue';
import {getUserProfileApi} from "@/api/user_work";

export const useUserStore = defineStore('user', () => {
    const id = ref(null);
    const username = ref('');
    const token = ref('');
    const adminToken = ref('');
    const nickname = ref('');
    const balance = ref(0);
    const avatar = ref('');

    const setUserInfo = (userInfo) => {
        id.value = userInfo.id;
        username.value = userInfo.username;
        token.value = userInfo.token;
        adminToken.value = userInfo.adminToken;
        balance.value = userInfo.balance;
    };

    // 更新用户信息的方法
    const updateUserProfile = async () => {
        try {
            if (!id.value) return;
            const response = await getUserProfileApi(id.value);
            if (response.code === 1) {
                nickname.value = response.data.nickname || '';
                balance.value = response.data.balance || 0;
                avatar.value = response.data.avatar || '';
            }
        } catch (error) {
            console.error('更新用户信息失败:', error);
        }
    };

    const clearUserInfo = () => {
        id.value = null;
        username.value = '';
        token.value = '';
        adminToken.value = '';
        balance.value = 0;
        nickname.value = '';
        avatar.value = '';
    };

    const isAuthenticated = () => {
        return !!token.value;
    };

    return {
        id,
        username,
        token,
        adminToken,
        nickname,
        balance,
        avatar,
        updateUserProfile,
        setUserInfo,
        clearUserInfo,
        isAuthenticated
    };
    },
    {
        persist: {
            enabled: true, // 启用持久化
            storage: sessionStorage,
        },
    }
);
