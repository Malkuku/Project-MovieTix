package com.movietix.xiazihao.utils;

import com.movietix.xiazihao.entity.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletUtils {
    // 封装返回方法
    public static void sendResponse(HttpServletResponse resp, Result result) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(JsonUtils.toJson(result));
        out.flush();
    }
    // 封装获取请求体方法
    public static String getRequestBody(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

    //TODO实现一个RequestParamBinder类？？
}
