package dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

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
public class DirDAOImpl implements DirDAO {

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
}
