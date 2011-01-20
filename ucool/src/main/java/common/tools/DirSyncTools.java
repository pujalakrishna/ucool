package common.tools;

import biz.file.FileEditor;
import common.DirMapping;
import common.PersonConfig;
import dao.DirDAO;
import dao.UserDAO;

/**
 * Created by IntelliJ IDEA.
 * User: zhangting
 * Date: 11-1-20
 * Time: 上午11:14
 * To change this template use File | Settings | File Templates.
 */
public class DirSyncTools {

    private FileEditor fileEditor;

    private UserDAO userDAO;

    private DirDAO dirDAO;

    private DirMapping dirMapping;

    public void setDirMapping(DirMapping dirMapping) {
        this.dirMapping = dirMapping;
    }

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setDirDAO(DirDAO dirDAO) {
        this.dirDAO = dirDAO;
    }

    /**
     * Method sync ...
     *
     * @param dirId of type Long 需要同步的目录id
     * @param dirPath of type String 需要同步的目录路径
     * @param personConfig of type PersonConfig
     * @return boolean
     */
    public boolean sync(Long dirId, String dirPath, PersonConfig personConfig) {
        /**
         * 页面没刷新，目录被删除，同步有2种可能
         * 1、当前目录存在，目标目录不存在
         * 2、当前目录不存在，切换配置
         */

        if (!fileEditor.findFile(dirPath)) {
            //删除目录
            if (dirDAO.deleteDir(dirId)) {
                dirMapping.removeDir(dirId);
                Long srcId = personConfig.getDirId();
                personConfig.getUserDO().setDirId(-1L);
                userDAO.updateDir(personConfig.getUserDO().getId(), personConfig.getDirId(), srcId);
                return true;
            }
        }
        return false;
    }
}
