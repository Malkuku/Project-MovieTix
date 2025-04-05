package com.movietix.xiazihao.constants;

public class ConstantsManager {
    //数据库配置信息
    private static final String URL = "jdbc:mysql://localhost:3306/movie_tix";
    private static final String USER = "root";
    private static final String PASSWORD = "pin666";

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
    public String getUserDataBasename() {
        return USER;
    }
    public String getDataBasePassword() {
        return PASSWORD;
    }
}
