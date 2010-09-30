<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="common.ConfigCenter" %>
<%@ page import="biz.file.FileEditor" %>
<html>
<head>
    <title>ucool ����ҳ</title>
    <style type="text/css">
        body{
            background-color: #E2E2E2;
        }
        li{
            height:24px;
            line-height:24px;
        }
        #content {
            width: 950px;
            margin: 0 auto;
        }
        .open{
            color:#00cc66;
            font-weight:bold;
        }
        .closed{
            color:#ff3333;
            font-weight: bold;
        }
        .circle-hidden {
            list-style:none;
        }
        #switch input {
            float:right;
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
        <ul id="switch">
            <li><label>js/css����ģʽ������js��css����ʾsource�ļ�,combo·����ʱ��֧�֣�</label>
                <input type="button" id="assetsdebugswitch"
                       value="<%=configCenter.getStateOper(configCenter.getUcoolAssetsDebug())%>"/></li>
            <li><label>�ֶ��������ϻ���</label>
                <input type="button" id="cleanOnlineCache"
                       value="��������"/><span></span></li>
            <li><label>�ֶ�����daily����</label>
                <input type="button" id="cleanDailyCache"
                       value="��������"/><span></span></li>
        </ul>
    </fieldset>

    <fieldset>
        <legend>��ǰ����</legend>

        <ul>
            <li>��ǰ������<%=configCenter.getUcoolEnv()%></li>
            <li>�ճ�������<%=configCenter.getUcoolDailyDomain()%></li>
            <li>����������<%=configCenter.getUcoolOnlineDomain()%></li>
            <li>�ճ�ip��<%=configCenter.getUcoolDailyIp()%></li>
            <li>����ip��<%=configCenter.getUcoolOnlineIp()%></li>
            <li class="circle-hidden"></li>
            <%--<li>�Ƿ���assetsĿ¼����<%=configCenter.getUcoolAssetsAutoClean()%></li>--%>
            <%--<li>assetsĿ¼�������ڣ�Сʱ����<%=configCenter.getUcoolAssetsCleanPeriod()%></li>--%>
            <%--<li>�Ƿ���cacheĿ¼����<%=configCenter.getUcoolCacheAutoClean()%></li>--%>
            <%--<li>cacheĿ¼�������ڣ�Сʱ����<%=configCenter.getUcoolCacheCleanPeriod()%></li>--%>
            <li>assets�ļ�������<%=configCenter.getUcoolAssetsRoot()%></li>
            <li>����daily�ļ�������<%=configCenter.getUcoolCacheRootDaily()%></li>
            <li>����online�ļ�������<%=configCenter.getUcoolCacheRootOnline()%></li>
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
        var DOM = S.DOM;
        S.app('UCOOL');
        S.namespace('Pz');

        UCOOL.Pz = function() {

            var _change = function(pid, success, curState) {
                if (success === 'ok') {
                    S.get('#'+pid+'switch').value = curState==='true'?'����ر�':'�����';
                    S.get('#'+pid+'state').innerHTML = curState==='true'?'�ѿ���':'�ѹر�';
                    S.get('#'+pid+'state').className = curState==='true'?'open':'closed';
                }
            };

            var _doOnce = function(pid, success, time) {
                var el = S.get('#' + pid);
                DOM.next(el).innerHTML = '��'+time+(success === 'ok' ? '����ɹ�': '����ʧ��');
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
                
                change:function(pid, success, curState){
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