package common;

import dao.DirDAO;
import dao.DirDO;
import dao.UserDAO;
import org.springframework.beans.factory.InitializingBean;
import web.handler.Handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于目录的配置的存取
 * Created by IntelliJ IDEA.
 * User: zhangting
 * Date: 11-1-14
 * Time: 下午2:59
 * To change this template use File | Settings | File Templates.
 */
public class DirMapping implements InitializingBean {
    private Map<Long, DirDO> mappingTable = null;

    private DirDAO dirDAO;

    public Map<Long, DirDO> getMappingTable() {
        return mappingTable;
    }

    public void setDirDAO(DirDAO dirDAO) {
        this.dirDAO = dirDAO;
    }

    public void setMappingTable(Map<Long, DirDO> mappingTable) {
        this.mappingTable = mappingTable;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(mappingTable == null) {
            mappingTable = new HashMap<Long, DirDO>();
        }
        List<DirDO> dirDOs = this.dirDAO.loadAllDir();
        for (DirDO dirDO : dirDOs) {
            mappingTable.put(dirDO.getId(), dirDO);
        }
    }

    /**
     * 获取目录和配置
     * @param did
     * @return
     */
    public DirDO getDir(Long did) {
        return mappingTable.get(did);
    }
}
