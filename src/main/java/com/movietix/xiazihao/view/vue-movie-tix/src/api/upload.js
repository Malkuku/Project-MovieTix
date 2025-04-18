import request from "@/utils/request";

// 文件上传
export const uploadFileApi = (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/upload', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

// 文件删除
export const deleteFileApi = (ossUrl) => {
    return request.delete('/upload', {
        params: { ossUrl }
    })
}