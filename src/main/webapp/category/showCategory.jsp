<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>

    //延迟加载
    $(function () {

        //初始化父表格
        $("#cateTable").jqGrid({
            url: "${path}/category/showOneCategory",
            editurl: "${path}/category/edit",
            datatype: "json",
            height: "auto",
            autowidth: true,
            rowNum: 5,
            rowList: [5, 8, 10, 20, 30],
            pager: '#catePager',
            viewrecords: true,
            styleUI: "Bootstrap",
            colNames: ['Id', '类别名'],
            colModel: [
                {name: 'id', index: 'id', width: 55},
                {name: 'cateName', editable: true, index: 'note', width: 150, sortable: false}
            ],
            subGrid: true,  //是否开启子表格
            //subgrid_id, 当点击一行时父表格会创建一个div来容纳子表格，  subGrid_id  就是这个div的id
            //subgrid_id是在创建表数据时创建的div标签的ID
            //row_id    点击行的id
            subGridRowExpanded: function (subgrid_id, row_id) {
                //调用子表格的函数
                addSubGrid(subgrid_id, row_id);
            }
        });

        //父表格增删改查操作   分页工具
        $("#cateTable").jqGrid('navGrid', '#catePager',
            {add: true, edit: true, del: true},
            {
                closeAfterEdit: true,
                afterSubmit: function (response) {

                    //设置警告信息
                    $("#messages").html(response.responseJSON.message);

                    //展示警告框，提示信息
                    $("#showMsg").show();

                    //5秒自动关闭
                    setTimeout(function () {
                        //关闭警告框
                        $("#showMsg").hide();
                    }, 3000);

                    return "hello";
                }
            }, //修改
            {
                closeAfterAdd: true,
                afterSubmit: function (response) {

                    //设置警告信息
                    $("#messages").html(response.responseJSON.message);

                    //展示警告框，提示信息
                    $("#showMsg").show();

                    //5秒自动关闭
                    setTimeout(function () {
                        //关闭警告框
                        $("#showMsg").hide();
                    }, 3000);

                    return "hello";
                }

            }, //添加
            {
                closeAfterDel: true,
                afterSubmit: function (response) {

                    //设置警告信息
                    $("#messages").html(response.responseJSON.message);

                    //展示警告框，提示信息
                    $("#showMsg").show();

                    //5秒自动关闭
                    setTimeout(function () {
                        //关闭警告框
                        $("#showMsg").hide();
                    }, 3000);

                    return "hello";
                }
            } //删除
        );
    });

    //构建子表格
    function addSubGrid(subgridId, rowId) {

        var subgridTableId = subgridId + "Table";  //拼接子表格id
        var pagerId = subgridId + "Pager";   //拼接子表格工具栏id

        //在子表格div容器中创建一个表格和一个工具栏的div
        $("#" + subgridId).html("<table id='" + subgridTableId + "' /><div id='" + pagerId + "' />");

        //初始化子表格
        $("#" + subgridTableId).jqGrid({
            url: "${path}/category/showTwoCategory?parentId=" + rowId,
            editurl: "${path}/category/edit?parentId=" + rowId,
            datatype: "json",
            rowNum: 20,
            pager: "#" + pagerId,
            height: 'auto',
            autowidth: true,
            rowNum: 5,
            rowList: [5, 8, 10, 20, 30],
            styleUI: "Bootstrap",
            viewrecords: true,
            colNames: ['ID', '类别名', '级别', '上级别id'],
            colModel: [
                {name: "id", index: "num", width: 80, key: true},
                {name: "cateName", editable: true, index: "item", width: 130},
                {name: "levels", index: "qty", width: 70, align: "right"},
                {name: "parentId", index: "total", width: 70, align: "right", sortable: false}
            ]
        });

        //子表格分页工具栏
        $("#" + subgridTableId).jqGrid('navGrid', "#" + pagerId,
            {edit: true, add: true, del: true},
            {closeAfterEdit: true},
            {closeAfterAdd: true}
        );
    }


</script>


<%--初始化面板--%>
<div class="panel panel-success">

    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>类别管理</h2>
    </div>

    <%--创建选项卡--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">类别展示</a></li>
    </div>

    <%--警告框--%>
    <div id="showMsg" class="alert alert-warning" style="width: 300px;height:50px;display: none">
        <span id="messages"/>
    </div>

    <%--初始化表单--%>
    <table id="cateTable"/>

    <%--工具栏--%>
    <div id="catePager"/>

</div>
