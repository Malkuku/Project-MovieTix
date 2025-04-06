package com.movietix.xiazihao.utils;

import com.movietix.xiazihao.entity.result.Result;

import javax.servlet.http.HttpServletResponse;
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
}
