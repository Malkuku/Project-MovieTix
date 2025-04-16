package com.movietix.xiazihao.constants;

public class ConstantsManager {
    //数据库配置信息
    private static final String URL = "jdbc:mysql://localhost:3306/movie_tix";
    private static final String USER = "root";
    private static final String PASSWORD = "pin666";
    //数据库引擎
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    //Jwt密钥
    private static final String SECRET_KEY = "ProjectMovieTixKaCatIsMyLongLongSecretKeyTo256Bits"; // 秘钥
    //管理员Jwt密钥
    private static final String ADMIN_SECRET_KEY = "ProjectMovieTixKaCatIsMyLongLongSecretKeyTo256BitsUsingByAdmin";
    //设置有效时间
    private static final long EXPIRATION_TIME = 12 * 60 * 60 * 1000; // 12小时
    //控制过滤器的开关
    private static final boolean FILTER_SWITCH = false;

    private static ConstantsManager instance;

    private ConstantsManager() {}

    public static ConstantsManager getInstance() {
        if (instance == null) {
            instance = new ConstantsManager();
        }
        return instance;
    }

    //获取数据库配置信息
    public String getDataBaseURL() {
        return URL;
    }
    public String getDataBaseUsername() {
        return USER;
    }
    public String getDataBasePassword() {
        return PASSWORD;
    }
    //获取引擎信息
    public String getDataBaseDriver() {
        return DRIVER;
    }
    //获取Jwt密钥
    public String getSecretKey() {
        return SECRET_KEY;
    }
    public String getAdminSecretKey() {
        return ADMIN_SECRET_KEY;
    }
    //获取Jwt有效时间
    public long getExpirationTime() {
        return EXPIRATION_TIME;
    }
    //控制过滤器的开关
    public boolean getFilterSwitch() {
        return !FILTER_SWITCH;
    }
}
