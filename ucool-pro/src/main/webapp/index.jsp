<%--
  Created by IntelliJ IDEA.
  User: czy
  Date: 2010-9-26
  Time: 22:46:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="biz.file.FileEditor" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.List" %>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    FileEditor fileEditor = (FileEditor) wac.getBean("fileEditor");
%>
<!DOCTYPE HTML>
<html>
<head><title>choose your directory</title></head>
<body>
<h1>directory list</h1>
<%
    List<String> assetsSubDirs = fileEditor.getAssetsSubDirs();
    for (String assetsSubDir : assetsSubDirs) {
        out.println(assetsSubDir + "<br/>");
    }
%>
</body>
</html>