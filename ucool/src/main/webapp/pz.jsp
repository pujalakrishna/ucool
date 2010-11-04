<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="common.ConfigCenter" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="gbk"/>
    <title>ucool ����ҳ</title>
    <style type="text/css">
        body {
            background-color: #E2E2E2;
            font-size: 14px;
        }

        li {
            height: 24px;
            line-height: 24px;
        }

        #content {
            width: 950px;
            margin: 0 auto;
        }

        .open {
            color: #00cc66;
            font-weight: bold;
        }

        .closed {
            color: #ff3333;
            font-weight: bold;
        }

        .circle-hidden {
            list-style: none;
        }

        #switch input {
            float: right;
        }

        input {
            margin: 0;
        }
    </style>
</head>
<body>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
%>
<div id="content">
    <h3>ucool v2.4 ����ҳ</h3>
    <fieldset>
        <legend>����</legend>
        <ul id="switch">
            <li><label>js/css����ģʽ������js��css����ʾԴ�룩</label>
                <input type="button" id="assetsdebugswitch"
                       value="<%=configCenter.getStateOper(configCenter.getUcoolAssetsDebug())%>"/>
                <span class="<%=configCenter.getStateStyle(configCenter.getUcoolAssetsDebug())%>" id="assetsdebugstate">
                    <%=configCenter.getCurState(configCenter.getUcoolAssetsDebug())%>
                </span>
            </li>
            <li><label>�ֶ��������ϻ���</label>
                <input type="button" id="cleanOnlineCache"
                       value="��������"/><span></span></li>
            <li><label>�ֶ�����daily����</label>
                <input type="button" id="cleanDailyCache"
                       value="��������"/><span></span></li>
            <li><label>��Ԥ��</label>
                <input type="checkbox" id="bindPrepub"/><span></span></li>
        </ul>
    </fieldset>

    <fieldset>
        <legend>��ǰ����</legend>

        <ul>
            <%--<li>��ǰ������<%=configCenter.getUcoolEnv()%></li>--%>
            <li>�ճ�������<%=configCenter.getUcoolDailyDomain()%>
            </li>
            <li>����������<%=configCenter.getUcoolOnlineDomain()%>
            </li>
            <li>�ճ�ip��<%=configCenter.getUcoolDailyIp()%>
            </li>
            <li>����ip��<%=configCenter.getUcoolOnlineIp()%>
            </li>
            <li>Ԥ��ip��<%=configCenter.getUcoolPrepubIp()%>
            </li>
            <li>combo�ָ�����<%=configCenter.getUcoolComboDecollator()%>
            </li>
            <li class="circle-hidden"></li>
            <%--<li>�Ƿ���assetsĿ¼����<%=configCenter.getUcoolAssetsAutoClean()%></li>--%>
            <%--<li>assetsĿ¼�������ڣ�Сʱ����<%=configCenter.getUcoolAssetsCleanPeriod()%></li>--%>
            <li>�Ƿ���cacheĿ¼����<%=configCenter.getUcoolCacheAutoClean()%>
            </li>
            <li>cacheĿ¼�������ڣ�Сʱ����<%=configCenter.getUcoolCacheCleanPeriod()%>
            </li>
            <li>assetsĿ¼��<%=configCenter.getUcoolAssetsRoot()%>
            </li>
            <li>cacheĿ¼��<%=configCenter.getUcoolCacheRoot()%>
            </li>
            <li>daily������Ŀ¼��<%=configCenter.getUcoolCacheRootDaily()%>
            </li>
            <li>������Ŀ¼��<%=configCenter.getUcoolCacheRootOnline()%>
            </li>
            <li>Ԥ��������Ŀ¼��<%=configCenter.getUcoolCacheRootPrepub()%>
            </li>
            <li>debug״̬���Ƿ�ʹ��cache��<%=configCenter.getUcoolAssetsDebugCache()%>
            </li>
        </ul>
    </fieldset>
    <p style="text-align:center">author��zhangting@taobao.com</p>
</div>
<script type="text/javascript" src="http://a.tbcdn.cn/s/kissy/1.1.5/kissy-min.js"></script>
<script type="text/javascript">
    KISSY.ready(function(S) {
        var Event = S.Event;
        var DOM = S.DOM;
        S.app('UCOOL');
        S.namespace('Pz');

        UCOOL.Pz = function() {

            var _change = function(pid, success, curState) {
                if (success === 'ok') {
                    S.get('#' + pid + 'switch').value = curState === 'true' ? '����ر�' : '�����';
                    S.get('#' + pid + 'state').innerHTML = curState === 'true' ? '�Ѵ�' : '�ѹر�';
                    S.get('#' + pid + 'state').className = curState === 'true' ? 'open' : 'closed';
                }
            };

            var _doOnce = function(pid, success, time) {
                var el = S.get('#' + pid);
                DOM.next(el).innerHTML = '��' + time + (success === 'ok' ? '����ɹ�' : '����ʧ��');
                DOM.next(el).className = success === 'ok' ? 'open' : 'closed';
            }

            return{
                init:function() {
                    Event.on('#assetsdebugswitch', 'click', function(e) {
                        S.getScript("pzbg.jsp?" + "pid=assetsdebug&callback=UCOOL.Pz.change&t=" + new Date());
                    });
                    Event.on('#cleanOnlineCache', 'click', function(e) {
                        S.getScript("pzbg.jsp?" + "pid=cleanOnlineCache&callback=UCOOL.Pz.doOnce&t=" + new Date());
                    });
                    Event.on('#cleanDailyCache', 'click', function(e) {
                        S.getScript("pzbg.jsp?" + "pid=cleanDailyCache&callback=UCOOL.Pz.doOnce&t=" + new Date());
                    });
                },

                change:function(pid, success, curState) {
                    _change(pid, success, curState);
                },
                doOnce:function(pid, success, time) {
                    _doOnce(pid, success, time);
                }
            }
        }();

        UCOOL.Pz.init();
    });
</script>
</body>
</html>