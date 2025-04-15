//result的结果定义
declare type ApiResponse<T = any> = {
    code: number;
    data: T;
    msg?: string;
};
