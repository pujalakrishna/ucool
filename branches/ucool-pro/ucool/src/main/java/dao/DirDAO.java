package dao;

import dao.entity.DirDO;
import dao.entity.UserDO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangting
 * Date: 11-1-14
 * Time: обнГ3:44
 * To change this template use File | Settings | File Templates.
 */
public interface DirDAO {
    List<DirDO> loadAllDir();

    boolean updateConfig(DirDO dirDO, int srcConfig);

    DirDO getDirByName(String name);

    /**
     * Method createNewDir ...
     * @return boolean
     */
    boolean createNewDir(DirDO name);
}
