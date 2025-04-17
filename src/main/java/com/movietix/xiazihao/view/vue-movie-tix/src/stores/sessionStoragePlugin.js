export default function (store) {
    // 定义存储键
    const storageKey = `pinia-${store.$id}`;

    // 初始化状态时，从 sessionStorage 中恢复状态
    const savedState = sessionStorage.getItem(storageKey);
    if (savedState) {
        store.$patch(JSON.parse(savedState));
    }

    // 监听状态变化，自动保存到 sessionStorage
    store.$subscribe((mutation, state) => {
        sessionStorage.setItem(storageKey, JSON.stringify(state));
    });
}