<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>

    $(function () {
        $(function () {
            pageInit();

            //点击发送验证码
            $("#sendMsg").click(function () {

                //1.获取手机号
                var phone = $("#phoneCode").val();

                //2.发送验证码
                $.post("${path}/user/getPhoneCode", {"phone": phone}, function (data) {
                    //设置警告信息
                    $("#messages").html(data.message);

                    //展示警告框，提示信息
                    $("#showMsg").show();

                    //5秒自动关闭
                    setTimeout(function () {
                        //关闭警告框
                        $("#showMsg").hide();
                    }, 3000);
                }, "JSON")
            });

        });


        pageInit();
    });

    function pageInit() {
        //初始化表单
        $("#userTable").jqGrid({
            url: "${path}/user/showAllPage",  //接收 page:当前页     rows：每页展示条数
            editurl: "${path}/user/edit",
            datatype: "json",                //返回   page:当前页   rows:数据（List） total:总页数  recoreds:总条数
            rowNum: 5,
            rowList: [2, 5, 20, 30],
            styleUI: "Bootstrap",
            autowidth: true,
            height: "auto",
            pager: '#userPager',  //工具栏
            viewrecords: true,  //是否展示总条数
            colNames: ['Id', '名称', '手机号', '头像', '描述', '微信', '状态', "注册时间"],
            colModel: [
                {name: 'id', width: 55},
                {name: 'username', editable: true, width: 90},
                {name: 'hone', editable: true, width: 100},
                {
                    name: 'headImg', editable: true, align: "center", edittype: "file",
                    formatter: function (cellvalue, options, rowObject) {
                        return "<img src='https://yingxue-lht.oss-cn-beijing.aliyuncs.com/headImage/" + cellvalue + "' width='200px' height='100px'  />";
                    }
                },
                {name: 'breif', editable: true, width: 80, align: "right"},
                {name: 'wechat', width: 80, align: "right"},
                {
                    name: 'status', width: 150, sortable: false, align: "center",
                    formatter: function (cellvalue, options, rowObject) {
                        //状态为1    正常     点击冻结
                        //状态为0    冻结     点击解除冻结
                        //   id    status
                        if (cellvalue == 1) {                        //id   修改的字段
                            return "<button class='btn btn-success' onclick='updateUserStatus(\"" + rowObject.id + "\",\"" + rowObject.status + "\")' >冻结</button>";
                        } else {
                            return "<button class='btn btn-danger' onclick='updateUserStatus(\"" + rowObject.id + "\",\"" + rowObject.status + "\")'>解除冻结</button>";
                        }
                    }
                },
                {name: 'createDate', width: 150, sortable: false}
            ]
        });

        //表单增删改查操作
        $("#userTable").jqGrid('navGrid', '#userPager',
            {edit: true, add: true, del: false, addtext: "添加", edittext: "编辑"},
            {
                closeAfterEdit: true,//修改之后关闭对话框

                //文件上传
                afterSubmit: function (reponse) {
                    console.log(reponse.responseText);
                    //异步上传
                    $.ajaxFileUpload({
                        url: "${path}/user/uploadUserCover",
                        type: "post",
                        data: {id: reponse.responseText},
                        fileElementId: "headImg",  //fileElementId　　　需要上传的文件域的ID，即<input type="file" id=  >的ID。
                        success: function () {
                            //刷新表单
                            //$("#userTable").trigger("reloadGrid");
                        }
                    });

                    //随便返回一个返回值
                    return "hello";
                }
            },  //修改之后额外的操作
            {
                closeAfterAdd: true,  //添加之后关闭对话框
                //文件上传
                afterSubmit: function (reponse) {
                    //异步上传
                    $.ajaxFileUpload({
                        url: "${path}/user/uploadUserCover",
                        type: "post",
                        data: {id: reponse.responseText},
                        fileElementId: "headImg",  //fileElementId　　　需要上传的文件域的ID，即<input type="file" id=  >的ID。
                        success: function () {
                            //刷新表单
                            $("#userTable").trigger("reloadGrid");
                        }
                    });

                    //随便返回一个返回值
                    return "hello";
                }
            },  //添加之后额外的操作
            {}  //删除之后额外的操作
        );
    }

    //修改状态
    function updateUserStatus(id, status) {
        //id   修改的字段

        if (status == 1) {
            $.ajax({
                url: "${path}/user/edit",
                type: "post",
                data: {"id": id, "status": "0", "oper": "edit"},
                success: function () {
                    //刷新表单
                    $("#userTable").trigger("reloadGrid")
                }
            });
        } else {
            $.ajax({
                url: "${path}/user/edit",
                type: "post",
                data: {"id": id, "status": "1", "oper": "edit"},
                success: function () {
                    //刷新表单
                    $("#userTable").trigger("reloadGrid")
                }
            });
        }
    }

    $("#ssssss").click(function () {
        $.post("${path}/user/downloadUser", function (data) {
            //设置警告信息
            $("#messages").html(data.message);

            //展示警告框，提示信息
            $("#showMsg").show();

            //5秒自动关闭
            setTimeout(function () {
                //关闭警告框
                $("#showMsg").hide();
            }, 3000);
        }, "JSON");
    });


</script>


<%--初始化面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>用户管理</h2>
    </div>

    <%--创建选项卡--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">用户展示</a></li>
    </div>
    <%--提示框--%>
    <div id="showMsg" class="alert alert-warning" style="width: 300px;height:50px;display: none">
        <span id="messages"/>
    </div>


    <%--手机验证码--%>
    <div class="input-group" style="width: 300px;height: auto;float: right">
        <input type="text" id="phoneCode" class="form-control" placeholder="请输入手机号" aria-describedby="basic-addon2">
        <span class="input-group-addon" id="sendMsg">发送验证码</span>
    </div>
</div>

<%--面板按钮--%>
<div class="panel panel-body">
    <button class="btn btn-info" id="ssssss">导出用户信息</button>
</div>

<%--初始化表单--%>
<table id="userTable"/>

<%--工具栏--%>
<div id="userPager"/>

</div>
