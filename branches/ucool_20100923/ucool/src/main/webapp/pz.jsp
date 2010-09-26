<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="common.ConfigCenter" %>
<html>
<head>
    <title>ucool 配置页</title>
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
    ucool 配置页
    <fieldset>
        <legend>开关</legend>
        <label for="assetsdebugswitch">js/css调试模式（所有js和css都显示source文件,combo路径暂时不支持）</label>
        <input type="button" id="assetsdebugswitch" value="<%=configCenter.getStateOper(configCenter.getUcoolAssetsDebug())%>"/>
    </fieldset>

    <fieldset>
        <legend>当前配置</legend>

        <ul>
            <li>当前环境：<%=configCenter.getUcoolEnv()%></li>
            <li>日常域名：<%=configCenter.getUcoolDailyDomain()%></li>
            <li>线上域名：<%=configCenter.getUcoolOnlineDomain()%></li>
            <li>日常ip：<%=configCenter.getUcoolDailyIp()%></li>
            <li>线上ip：<%=configCenter.getUcoolOnlineIp()%></li>
            <%--<li>是否开启assets目录清理：<%=configCenter.getUcoolAssetsAutoClean()%></li>--%>
            <%--<li>assets目录清理周期（小时）：<%=configCenter.getUcoolAssetsCleanPeriod()%></li>--%>
            <%--<li>是否开启cache目录清理：<%=configCenter.getUcoolCacheAutoClean()%></li>--%>
            <%--<li>cache目录清理周期（小时）：<%=configCenter.getUcoolCacheCleanPeriod()%></li>--%>
            <li>assets文件夹名：<%=configCenter.getUcoolAssetsRoot()%></li>
            <li>缓存文件夹名：<%=configCenter.getUcoolCacheRoot()%></li>
            <li>是否已开启assets debug模式：
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
                    S.get('#'+pid+'switch').value = curState==='true'?'点击关闭':'点击打开';
                    S.get('#'+pid+'state').innerHTML = curState==='true'?'已开启':'已关闭';
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