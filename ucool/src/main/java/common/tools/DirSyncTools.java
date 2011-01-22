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
 * Time: ����11:14
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
     * @param dirPath of type String ��Ҫͬ����Ŀ¼·��
     * @param personConfig of type PersonConfig
     * @return boolean
     */
    public boolean sync(String dirPath, PersonConfig personConfig) {
        /**
         * ҳ��ûˢ�£�Ŀ¼��ɾ����ͬ����2�ֿ���
         * 1����ǰĿ¼���ڣ�Ŀ��Ŀ¼������
         * 2����ǰĿ¼�����ڣ��л�����
         */

        if (!fileEditor.findFile(dirPath)) {
            //ɾ��Ŀ¼
            userDAO.updateDir(personConfig.getUserDO().getId(), "", personConfig.getUserDO().getName());
            return true;
        }
        return false;
    }
}
