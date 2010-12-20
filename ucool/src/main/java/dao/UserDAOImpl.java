package dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-3 ÉÏÎç12:16
 */
public class UserDAOImpl implements UserDAO {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override public UserDO getPersonInfo(String hostName) {
        String sql = "select * from user where host_name=?";
        final UserDO user = new UserDO();
        this.jdbcTemplate.queryForObject(sql, new Object[]{hostName}, new RowMapper() {
            @Override public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                user.setId(rs.getLong("id"));
                user.setHostName(rs.getString("host_name"));
                user.setDir(rs.getString("dir"));
                user.setConfig(rs.getInt("config"));
                return null;
            }
        });
        return user;
    }

    @Override public boolean createNewUser(UserDO userDO) {
        String sql = "insert into user (host_name,dir, config) values (?,?,?)";
        return this.jdbcTemplate.update(sql, new Object[]{userDO.getHostName(), userDO.getDir(), 5}) > 0;
    }

    @Override public boolean updateConfig(UserDO userDO, int srcConfig) {
        String sql = "update user set config=? where host_name=? and config=?";
        return this.jdbcTemplate.update(sql, new Object[]{userDO.getConfig(), userDO.getHostName(), srcConfig}) > 0;
    }
}
