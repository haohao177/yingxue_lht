<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<head>
    <meta charset="utf-8">
    <!-- 引入 ECharts 文件 -->
    <script src="${path}/bootstrap/js/echarts.min.js"></script>
    <%--引入 GoEasy.js文件网络路径--%>
    <script type="text/javascript" src="https://cdn.goeasy.io/goeasy-1.0.6.js"></script>
    <%--<script src="${path}/bootstrap/js/jquery.min.js"></script>--%>
    <script type="text/javascript">

        /*初始化GoEasy对象*/
        var goEasy = new GoEasy({
            host: 'hangzhou.goeasy.io', //应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
            appkey: "BC-1d32917956554f3698de28365aa54183", //替换为您的应用appkey
        });

        $(function () {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            /*浏览器接收消息*/
            goEasy.subscribe({
                channel: "190channel", //替换为您自己的channel
                onMessage: function (message) {
                    //alert("Channel:" + message.channel + " content:" + message.content);

                    var datas = message.content;
                    //将json字符串转为JavaScript对象
                    var data = JSON.parse(datas);

                    // 指定图表的配置项和数据
                    var option = {
                        title: {
                            text: '用户注册量统计图', //标题
                            show: true,
                            link: "${path}/main/main.jsp",
                            subtext: "纯属虚构",
                        },
                        tooltip: {
                            show: true,
                        },  //鼠标提示
                        legend: {
                            data: ['男', '女']  //选项卡
                        },
                        xAxis: {
                            data: data.woman
                        },
                        yAxis: {},
                        series: [{
                            name: '男',
                            type: 'bar', //bar 柱状图  line  折线图
                            data: data.men
                        }, {
                            name: '女',
                            type: 'line',
                            data: data.woman
                        }]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
                }
            });
        });
    </script>


    <script type="text/javascript">
        $(function () {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            $.get("${path}/user/dataMonth", function (data) {
                console.log(data);
                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: '用户注册量统计图', //标题
                        show: true,
                        link: "${path}/main/main.jsp",
                        subtext: "纯属虚构",
                    },
                    tooltip: {
                        show: true,
                    },  //鼠标提示
                    legend: {
                        data: ['男', '女']  //选项卡
                    },
                    xAxis: {

                        data: [$.each(data.woman, function (a, b) {
                            b.month
                        })]

                    },
                    yAxis: {},
                    series: [{
                        name: '男',
                        type: 'bar', //bar 柱状图  line  折线图
                        data: data.men
                    }, {
                        name: '女',
                        type: 'line',
                        data: data.woman
                    }]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);

            }, "JSON");
        });
    </script>


</head>

<body>
<div align="center">
    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="main" style="width: 600px;height:400px;"></div>
</div>
</body>
