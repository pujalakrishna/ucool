<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="common.ConfigCenter" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="gbk"/>
    <title>ucool 配置页</title>
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
    <h3>ucool v2.4 配置页</h3>
    <fieldset>
        <legend>开关</legend>
        <ul id="switch">
            <li><label>js/css调试模式（所有js和css都显示源码）</label>
                <input type="button" id="assetsdebugswitch"
                       value="<%=configCenter.getStateOper(configCenter.getUcoolAssetsDebug())%>"/>
                <span class="<%=configCenter.getStateStyle(configCenter.getUcoolAssetsDebug())%>" id="assetsdebugstate">
                    <%=configCenter.getCurState(configCenter.getUcoolAssetsDebug())%>
                </span>
            </li>
            <li><label>手动清理线上缓存</label>
                <input type="button" id="cleanOnlineCache"
                       value="立即清理"/><span></span></li>
            <li><label>手动清理daily缓存</label>
                <input type="button" id="cleanDailyCache"
                       value="立即清理"/><span></span></li>
            <li><label>绑定预发</label>
                <input type="checkbox" id="bindPrepub"/><span></span></li>
        </ul>
    </fieldset>

    <fieldset>
        <legend>当前配置</legend>

        <ul>
            <%--<li>当前环境：<%=configCenter.getUcoolEnv()%></li>--%>
            <li>日常域名：<%=configCenter.getUcoolDailyDomain()%>
            </li>
            <li>线上域名：<%=configCenter.getUcoolOnlineDomain()%>
            </li>
            <li>日常ip：<%=configCenter.getUcoolDailyIp()%>
            </li>
            <li>线上ip：<%=configCenter.getUcoolOnlineIp()%>
            </li>
            <li>预发ip：<%=configCenter.getUcoolPrepubIp()%>
            </li>
            <li>combo分隔符：<%=configCenter.getUcoolComboDecollator()%>
            </li>
            <li class="circle-hidden"></li>
            <%--<li>是否开启assets目录清理：<%=configCenter.getUcoolAssetsAutoClean()%></li>--%>
            <%--<li>assets目录清理周期（小时）：<%=configCenter.getUcoolAssetsCleanPeriod()%></li>--%>
            <li>是否开启cache目录清理：<%=configCenter.getUcoolCacheAutoClean()%>
            </li>
            <li>cache目录清理周期（小时）：<%=configCenter.getUcoolCacheCleanPeriod()%>
            </li>
            <li>assets目录：<%=configCenter.getUcoolAssetsRoot()%>
            </li>
            <li>cache目录：<%=configCenter.getUcoolCacheRoot()%>
            </li>
            <li>daily缓存子目录：<%=configCenter.getUcoolCacheRootDaily()%>
            </li>
            <li>线上子目录：<%=configCenter.getUcoolCacheRootOnline()%>
            </li>
            <li>预发缓存子目录：<%=configCenter.getUcoolCacheRootPrepub()%>
            </li>
            <li>debug状态下是否使用cache：<%=configCenter.getUcoolAssetsDebugCache()%>
            </li>
        </ul>
    </fieldset>
    <p style="text-align:center">author：zhangting@taobao.com</p>
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
                    S.get('#' + pid + 'switch').value = curState === 'true' ? '点击关闭' : '点击打开';
                    S.get('#' + pid + 'state').innerHTML = curState === 'true' ? '已打开' : '已关闭';
                    S.get('#' + pid + 'state').className = curState === 'true' ? 'open' : 'closed';
                }
            };

            var _doOnce = function(pid, success, time) {
                var el = S.get('#' + pid);
                DOM.next(el).innerHTML = '在' + time + (success === 'ok' ? '清理成功' : '清理失败');
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