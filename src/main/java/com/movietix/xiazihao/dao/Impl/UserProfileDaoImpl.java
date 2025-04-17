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

    @Override
    public UserProfile selectUserProfileByUserId(Integer userId, Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT up.*,u.username as username,u.balance as balance FROM user_profiles up join users u on up.user_id = u.id WHERE up.user_id = ?";
        List<UserProfile> userProfileList = JdbcUtils.executeQuery(conn, sql, isAutoCloseConn,
                rs -> {
                    UserProfile userProfile = new UserProfile();
                    try {
                        userProfile.setId(rs.getInt("id"));
                        userProfile.setUserId(rs.getInt("user_id"));
                        userProfile.setUsername(rs.getString("username"));
                        userProfile.setNickname(rs.getString("nickname"));
                        userProfile.setGender(rs.getInt("gender"));
                        userProfile.setEmail(rs.getString("email"));
                        userProfile.setBirthday(rs.getDate("birthday") != null ? rs.getDate("birthday").toLocalDate() : null);
                        userProfile.setAvatar(rs.getString("avatar"));
                        userProfile.setCity(rs.getString("city"));
                        userProfile.setProvince(rs.getString("province"));
                        userProfile.setSignature(rs.getString("signature"));
                        userProfile.setPhone(rs.getString("phone"));
                        userProfile.setBalance(rs.getDouble("balance"));
                        userProfile.setRealName(rs.getString("real_name"));
                        userProfile.setIdCard(rs.getString("id_card"));
                        userProfile.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
                        userProfile.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return userProfile;
                },
                userId
        );
        return userProfileList.isEmpty() ? null : userProfileList.get(0);
    }
}
