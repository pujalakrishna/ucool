package dao;

import dao.entity.UserDO;
import org.springframework.beans.factory.InitializingBean;
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
public class UserDAOImpl implements UserDAO, InitializingBean {
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
            user.setName((String)map.get("name"));
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
    public boolean updateDir(Long userId, String newDir) {
        String sql = "update user set name=? where id=?";
        try {
            this.jdbcTemplate.update(sql, new Object[]{newDir, userId});
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        int userExist = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM sqlite_master where type=\'table\' and name=?", new Object[]{"user"});
        //create table
        if(userExist == 0) {
            jdbcTemplate.execute("CREATE TABLE \"user\" (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"host_name\" VARCHAR NOT NULL  UNIQUE , \"dir_id\" INTEGER DEFAULT 0, \"name\" VARCHAR NOT NULL , \"config\" INTEGER NOT NULL  DEFAULT 5)");
        }
    }
}
