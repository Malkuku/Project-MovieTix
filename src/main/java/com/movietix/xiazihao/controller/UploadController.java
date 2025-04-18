package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.utils.AliyunOssUtils;
import com.movietix.xiazihao.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@WebServlet("/upload/*")
@MultipartConfig(maxFileSize = 1024 * 1024) // 限制文件大小为1MB
public class UploadController extends HttpServlet {
    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        uploadFile(req, resp);
    }
    //delete 请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        deleteFile(req, resp);
    }

    //上传文件
    private void uploadFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            // 1. 检查是否为multipart请求
            if (!ServletFileUpload.isMultipartContent(req)) {
                throw new RuntimeException("请求必须为multipart/form-data格式");
            }

            // 2. 获取上传的文件
            Part filePart = req.getPart("file"); // "file"是表单中的字段名
            String fileName = filePart.getSubmittedFileName();
            InputStream fileContent = filePart.getInputStream();

            // 3. 验证文件类型（示例：仅允许图片）
            String fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            if (!Arrays.asList(".jpg", ".png", ".jpeg").contains(fileExt)) {
                throw new IllegalArgumentException("仅支持JPG/PNG格式图片");
            }

            // 4. 上传到OSS
            String objectName = "img/" + UUID.randomUUID() + fileExt;
            AliyunOssUtils.uploadStream(objectName, fileContent);
            // 获取OSS访问URL
            String ossUrl = AliyunOssUtils.getFileUrl(objectName);
            ServletUtils.sendResponse(resp, Result.success(ossUrl));
        } catch (Exception e) {
            throw new RuntimeException("上传失败");
        }
    }

    //删除文件
    private void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String ossUrl = req.getParameter("ossUrl");
        log.info("接收到的OSS访问URL:{}", ossUrl);
        try {
            AliyunOssUtils.deleteFile(ossUrl);
            ServletUtils.sendResponse(resp, Result.success());
        } catch (Exception e) {
            throw new RuntimeException("删除失败");
        }
    }
}
