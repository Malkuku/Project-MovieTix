package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.UserProfileDao;
import com.movietix.xiazihao.entity.pojo.UserProfile;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class UserProfileDaoImpl implements UserProfileDao {
    @Override
    public void addUserProfile(UserProfile userProfile,Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "INSERT INTO user_profiles " +
                "(user_id, nickname, real_name, id_card, phone, gender, email, " +
                "birthday, avatar, city, province, signature) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        JdbcUtils.executeUpdate(JdbcUtils.getConnection(), sql, isAutoCloseConn,
                userProfile.getUserId(),
                userProfile.getNickname(),
                userProfile.getRealName(),
                userProfile.getIdCard(),
                userProfile.getPhone(),
                userProfile.getGender(),
                userProfile.getEmail(),
                userProfile.getBirthday(),
                userProfile.getAvatar(),
                userProfile.getCity(),
                userProfile.getProvince(),
                userProfile.getSignature()
        );
    }
}
