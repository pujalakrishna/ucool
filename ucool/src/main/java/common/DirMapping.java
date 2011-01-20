package common;

import dao.DirDAO;
import dao.entity.DirDO;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ����Ŀ¼�����õĴ�ȡ
 * Created by IntelliJ IDEA.
 * User: zhangting
 * Date: 11-1-14
 * Time: ����2:59
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
     * ��ȡĿ¼������
     * @param did
     * @return
     */
    public DirDO getDir(Long did) {
        return mappingTable.get(did);
    }

    public void addDir(DirDO dirDO) {
        mappingTable.put(dirDO.getId(), dirDO);
    }

    public void removeDir(Long id) {
        mappingTable.remove(id);
    }

    /**
     * ����Ŀ¼������do�� Ч�ʲ��ߵķ���
     *
     * @param dirName of type String
     * @return DirDO
     */
    public DirDO getDirByName(String dirName) {
        for (DirDO dirDO : mappingTable.values()) {
            if(dirDO.getName().equals(dirName)) {
                return dirDO;
            }
        }
        return null;
    }
}
