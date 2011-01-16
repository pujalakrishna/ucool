package dao;

import dao.entity.DirDO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangting
 * Date: 11-1-14
 * Time: ÏÂÎç3:45
 * To change this template use File | Settings | File Templates.
 */
public class DirDAOImpl implements DirDAO, InitializingBean {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<DirDO> loadAllDir() {
        final List<DirDO> list = new ArrayList<DirDO>();
        try{
        String sql = "select * from dir";
        jdbcTemplate.query(sql,
                new RowCallbackHandler() {
                    public void processRow(ResultSet rs) throws SQLException {
                        DirDO dir = new DirDO();
                        dir.setId(rs.getLong("id"));
                        dir.setName(rs.getString("name"));
                        dir.setConfig(rs.getInt("config"));
                        list.add(dir);
                    }
                });
        }catch (Exception e) {
        }
        return list;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        int userExist = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM sqlite_master where type=\'table\' and name=?", new Object[]{"user"});
        int dirExist = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM sqlite_master where type=\'table\' and name=?", new Object[]{"dir"});
        //create table
        if(userExist == 0) {
            jdbcTemplate.execute("CREATE TABLE \"user\" IF NOT EXISTS \"user\" (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"host_name\" VARCHAR NOT NULL  UNIQUE , \"dir_id\" INTEGER DEFAULT 0)");
        }
        if(dirExist == 0) {
            jdbcTemplate.execute("CREATE TABLE \"dir\" IF NOT EXISTS \"dir\" (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"name\" VARCHAR NOT NULL , \"config\" INTEGER NOT NULL  DEFAULT 5)");
        }
    }
}
