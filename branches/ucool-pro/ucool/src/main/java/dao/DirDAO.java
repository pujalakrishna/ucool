package dao;

import dao.entity.DirDO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangting
 * Date: 11-1-14
 * Time: ����3:44
 * To change this template use File | Settings | File Templates.
 */
public interface DirDAO {
    List<DirDO> loadAllDir();
}