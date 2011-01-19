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
 * Time: 下午3:45
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
    public boolean updateConfig(DirDO dirDO, int srcConfig) {
        if(dirDO.getConfig() == srcConfig) {
            //这里return true不知道会不会有什么问题
            return true;
        }
        try {
            if(jdbcTemplate.update("update dir set config=? where id=? and config=?", new Object[]{dirDO.getConfig(), dirDO.getId(), srcConfig}) > 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        int userExist = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM sqlite_master where type=\'table\' and name=?", new Object[]{"user"});
        int dirExist = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM sqlite_master where type=\'table\' and name=?", new Object[]{"dir"});
        //create table
        if(userExist == 0) {
            jdbcTemplate.execute("CREATE TABLE \"user\" (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"host_name\" VARCHAR NOT NULL  UNIQUE , \"dir_id\" INTEGER DEFAULT 0)");
        }
        if(dirExist == 0) {
            jdbcTemplate.execute("CREATE TABLE \"dir\" (\"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"name\" VARCHAR NOT NULL , \"config\" INTEGER NOT NULL  DEFAULT 5)");
        }
    }

    public DirDO getDirByName(String name) {
        final List<DirDO> list = new ArrayList<DirDO>();
        try {
            String sql = "SELECT * FROM dir where name=?";
            jdbcTemplate.query(sql, new Object[]{name},
                    new RowCallbackHandler() {
                        public void processRow(ResultSet rs) throws SQLException {
                            DirDO dir = new DirDO();
                            dir.setId(rs.getLong("id"));
                            dir.setName(rs.getString("name"));
                            dir.setConfig(rs.getInt("config"));
                            list.add(dir);
                        }
                    });
            if (list.size() > 0) {
                return list.get(0);
            } else {
                return null;
            }
        }catch (Exception e) {
        }
        return null;
    }

    public boolean createNewDir(DirDO dirDO) {
        try {
            if(jdbcTemplate.update("insert into dir(name, config) values(?, 5)", new Object[]{dirDO.getName()}) > 0) {
                DirDO newDO = getDirByName(dirDO.getName());
                dirDO.setId(newDO.getId());
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public boolean deleteDir(Long dirId) {
        try {
            if(jdbcTemplate.update("delete from dir where id=?", new Object[]{dirId}) > 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
