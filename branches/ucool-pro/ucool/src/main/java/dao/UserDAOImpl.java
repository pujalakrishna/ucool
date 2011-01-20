package dao;

import dao.entity.UserDO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
        UserDO user = null;
        List perList = this.jdbcTemplate.queryForList(sql, new Object[]{hostName});
        if(perList.size() == 1) {
            user = new UserDO();
            Map map = (Map) perList.get(0);
            user.setId(Long.valueOf(String.valueOf(map.get("id"))));
            user.setHostName((String) map.get("host_name"));
            user.setDirId(Long.valueOf(String.valueOf(map.get("dir_id"))));
        }
        return user;
    }

    @Override public boolean createNewUser(UserDO userDO) {
        String sql = "insert into user (host_name, dir_id) values (?,?)";
        try {
            if (this.jdbcTemplate.update(sql, new Object[]{userDO.getHostName(), userDO.getDirId()}) > 0) {
                UserDO newUser = getPersonInfo(userDO.getHostName());
                userDO.setId(newUser.getId());
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean updateDir(Long userId, Long newDirId, Long srcDirId) {
        String sql = "update user set dir_id=? where id=? and dir_id=?";
        try {
            this.jdbcTemplate.update(sql, new Object[]{newDirId, userId, srcDirId});
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
