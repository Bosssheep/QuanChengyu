<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import=""  %>
<%
    Server server = new Server();
    server.DowloadData();
%>
<%= server.blockChain.blockChainString() %>