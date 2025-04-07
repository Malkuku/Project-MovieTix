package com.movietix.xiazihao.constants;

public class ConstantsManager {
    //数据库配置信息
    private static final String URL = "jdbc:mysql://localhost:3306/movie_tix";
    private static final String USER = "root";
    private static final String PASSWORD = "pin666";
    //数据库引擎
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    //Jwt密钥
    private static final String SECRET_KEY = "aXRoZWltYQ=="; // 秘钥
    //设置有效时间
    private static final long EXPIRATION_TIME = 12 * 60 * 60 * 1000; // 12小时

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
    //获取Jwt有效时间
    public long getExpirationTime() {
        return EXPIRATION_TIME;
    }
}
