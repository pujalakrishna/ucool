package biz.file;

import common.ConfigCenter;

/**
 * @author <a href="mailto:zhangting@taobao.com">��ͦ</a>
 * @since 10-10-28 ����10:05
 */
public class AutoCleanDelegate {

    private ConfigCenter configCenter;

    private FileEditor fileEditor;

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void autoCleanOnline() {
        fileEditor.removeDirectory(configCenter.getWebRoot() + configCenter.getUcoolCacheRootOnline());
        fileEditor.removeDirectory(configCenter.getWebRoot() + configCenter.getUcoolCacheRootDaily());
    }

    public void autoCleanDaily() {
        fileEditor.removeDirectory(configCenter.getWebRoot() + configCenter.getUcoolCacheRootDaily());
    }
}
