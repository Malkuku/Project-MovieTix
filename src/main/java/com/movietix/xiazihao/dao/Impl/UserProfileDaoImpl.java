package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.UserProfileDao;
import com.movietix.xiazihao.entity.pojo.UserProfile;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserProfileDaoImpl implements UserProfileDao {
    @Override
    public void addUserProfile(UserProfile userProfile,Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "INSERT INTO user_profiles " +
                "(user_id, nickname, real_name, id_card, phone, gender, email, " +
                "birthday, avatar, city, province, signature) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn,
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

    @Override
    public void updateUserProfile(UserProfile userProfile,Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "UPDATE user_profiles SET " +
                "nickname = COALESCE(?, nickname), " +
                "gender = COALESCE(?, gender), " +
                "email = COALESCE(?, email), " +
                "birthday = COALESCE(?, birthday), " +
                "avatar = COALESCE(?, avatar), " +
                "city = COALESCE(?, city), " +
                "province = COALESCE(?, province), " +
                "signature = COALESCE(?, signature), " +
                "phone = COALESCE(?, phone), " +
                "real_name = COALESCE(?, real_name), " +
                "id_card = COALESCE(?, id_card), " +
                "updated_at = NOW() " +
                "WHERE user_id = ?";

        JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn,
                userProfile.getNickname(),
                userProfile.getGender(),
                userProfile.getEmail(),
                userProfile.getBirthday(),
                userProfile.getAvatar(),
                userProfile.getCity(),
                userProfile.getProvince(),
                userProfile.getSignature(),
                userProfile.getPhone(),
                userProfile.getRealName(),
                userProfile.getIdCard(),
                userProfile.getUserId()
        );
    }

    @Override
    public void deleteUserProfileByIds(List<Integer> ids, Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "DELETE FROM user_profiles WHERE id IN (" + String.join(",", ids.stream().map(id->"?").toArray(String[]::new)) + ")";
        JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn, ids.toArray());
    }
}
