import org.apache.catalina.startup.Tomcat;
import java.io.File;

public class TomcatApplication {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        String webappDir = new File("src/main/webapp").getAbsolutePath();
        tomcat.addWebapp("", webappDir);

        tomcat.start();
        tomcat.getServer().await();
    }
}