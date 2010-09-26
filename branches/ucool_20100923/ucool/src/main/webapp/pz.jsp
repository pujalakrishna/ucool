<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="common.ConfigCenter" %>
<html>
<head>
    <title>ucool ����ҳ</title>
    <style type="text/css">
        #content {
            width: 950px;
            margin: 0 auto;
        }
        .open{
            color:#00cc66;
        }
        .closed{
            color:#ff3333;
        }
    </style>
</head>
<body>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
%>
<div id="content">
    ucool ����ҳ
    <fieldset>
        <legend>����</legend>
        <label for="assetsdebugswitch">js/css����ģʽ������js��css����ʾsource�ļ�,combo·����ʱ��֧�֣�</label>
        <input type="button" id="assetsdebugswitch" value="<%=configCenter.getStateOper(configCenter.getUcoolAssetsDebug())%>"/>
    </fieldset>

    <fieldset>
        <legend>��ǰ����</legend>

        <ul>
            <li>��ǰ������<%=configCenter.getUcoolEnv()%></li>
            <li>�ճ�������<%=configCenter.getUcoolDailyDomain()%></li>
            <li>����������<%=configCenter.getUcoolOnlineDomain()%></li>
            <li>�ճ�ip��<%=configCenter.getUcoolDailyIp()%></li>
            <li>����ip��<%=configCenter.getUcoolOnlineIp()%></li>
            <%--<li>�Ƿ���assetsĿ¼����<%=configCenter.getUcoolAssetsAutoClean()%></li>--%>
            <%--<li>assetsĿ¼�������ڣ�Сʱ����<%=configCenter.getUcoolAssetsCleanPeriod()%></li>--%>
            <%--<li>�Ƿ���cacheĿ¼����<%=configCenter.getUcoolCacheAutoClean()%></li>--%>
            <%--<li>cacheĿ¼�������ڣ�Сʱ����<%=configCenter.getUcoolCacheCleanPeriod()%></li>--%>
            <li>assets�ļ�������<%=configCenter.getUcoolAssetsRoot()%></li>
            <li>�����ļ�������<%=configCenter.getUcoolCacheRoot()%></li>
            <li>�Ƿ��ѿ���assets debugģʽ��
                <span class="<%=configCenter.getStateStyle(configCenter.getUcoolAssetsDebug())%>" id="assetsdebugstate">
                    <%=configCenter.getCurState(configCenter.getUcoolAssetsDebug())%>
                </span>
            </li>
        </ul>
    </fieldset>
</div>
<script type="text/javascript" src="http://a.tbcdn.cn/s/kissy/1.1.5/kissy-min.js"></script>
<script type="text/javascript">
    KISSY.ready(function(S) {
        var Event = S.Event;
        S.app('UCOOL');
        S.namespace('Pz');

        UCOOL.Pz = function() {

            var callbackOper = function(pid, data, curState) {
                if (data === 'ok') {
                    S.get('#'+pid+'switch').value = curState==='true'?'����ر�':'�����';
                    S.get('#'+pid+'state').innerHTML = curState==='true'?'�ѿ���':'�ѹر�';
                    S.get('#'+pid+'state').className = curState==='true'?'open':'closed';
                }
            };

            return{
                init:function() {
                    Event.on('#assetsdebugswitch', 'click', function(e) {
                        S.getScript("pzbg.jsp?" + "pid=assetsdebug&callback=UCOOL.Pz.change&t=" + new Date());
                    });
                },
                
                change:function(pid, data, curState){
                    callbackOper(pid, data, curState);
                }
            }
        }();

        UCOOL.Pz.init();
    });
</script>
</body>
</html>