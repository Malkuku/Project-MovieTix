import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Slf4j
public class TomcatApplication {
    public static void main(String[] args) throws Exception {
        // 1. 创建Tomcat实例
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        // 2. 配置webapp目录并获取Context
        String webappDir = new File("src/main/webapp").getAbsolutePath();
        Context ctx = tomcat.addWebapp("", webappDir);

        // 确保 target/classes 目录被添加到类加载路径中
        File classesDir = new File("target/classes");
        ctx.setReloadable(true); // 允许热加载
        ctx.setResources(new StandardRoot(ctx) {{
            addPreResources(new DirResourceSet(this,
                    "/WEB-INF/classes", classesDir.getAbsolutePath(), "/"));
        }});

        // 3. 启动服务器
        tomcat.start();
        log.info("Server running at http://localhost:{}",tomcat.getConnector().getPort());
        tomcat.getServer().await();
    }
}
