package sqlite;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-2 ÏÂÎç11:11
 */
public class SpringTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
        System.out.println(jdbcTemplate == null);
        String sql = "insert into user (host_name,dir, config) values (?,?,?)";
        if (jdbcTemplate != null) {
            jdbcTemplate.update(sql, new Object[]{"czy-notebook", "zhangting", 5});
        }
    }
}
