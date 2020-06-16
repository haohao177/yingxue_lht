<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    $("#searchBtn").click(function () {

        //1.获取输入内容
        var content = $("#contentId").val();

        //2.向后台发起搜索请求
        $.ajax({
            url: "${path}/video/querySearch",
            type: "post",
            dataType: "JSON",
            data: {"content": content},
            success: function (data) {

                //清空table
                $("#showVideo").empty();

                //加入目录
                $("#showVideo").append("<tr>" +
                    "<td>标题</td>" +
                    "<td>描述</td>" +
                    "<td>封面</td" +
                    "><td>上传时间</td>" +
                    "</tr>");

                //3.获取搜索结果，渲染页面
                //data  集合
                //遍历集合  参数：要遍历的集合,function(集合数据下标,遍历集合里面的数据)
                $.each(data, function (index, video) {
                    $("#showVideo").append("<tr>" +
                        "<td>" + video.title + "</td>" +
                        "<td>" + video.brief + "</td>" +
                        "<td><img src='" + video.cover + "' width='200px' height='100px' /></td>" +
                        "<td>" + video.publishDate + "</td>" +
                        "</tr>");
                })
            }
        })
    });

</script>


<div align="center">
    <%--搜索输入框--%>
    <div class="input-group" style="width: 350px;height: auto">
        <input id="contentId" type="text" class="form-control" placeholder="请输入视频标题|描述" aria-describedby="basic-addon2">
        <span class="input-group-btn" id="basic-addon2">
            <button class="btn btn-info" id="searchBtn">百知一下</button>
        </span>
    </div>

    <hr>

    <%--搜索结果展示--%>
    <div class="panel panel-default">
        <!-- 面板头 -->
        <div class="panel-heading">搜索结果</div>
        <!-- 表格 -->
        <table class="table" id="showVideo"></table>
    </div>

</div>
