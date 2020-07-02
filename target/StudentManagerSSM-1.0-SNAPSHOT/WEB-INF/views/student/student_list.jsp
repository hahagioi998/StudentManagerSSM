<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>学生列表</title>
    <link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="../../easyui/css/demo.css">
    <script type="text/javascript" src="../../easyui/jquery.min.js"></script>
    <script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../easyui/js/validateExtends.js"></script>
    <script type="text/javascript" src="../../easyui/js/jquery.form.js"></script>
    <script type="text/javascript">
        var clazzList = ${clazzListJson};
        $(function () {
            //datagrid初始化
            $('#dataList').datagrid({
                title: '学生列表',
                iconCls: 'icon-more',//图标
                border: true,
                collapsible: false,//是否可折叠的
                fit: true,//自动大小
                method: "post",
                url: "/student/get_list?t=" + new Date().getTime(),
                idField: 'id',
                singleSelect: false,//是否单选
                pagination: true,//分页控件
                rownumbers: true,//行号
                sortName: 'id',
                sortOrder: 'DESC',
                remoteSort: false,
                columns: [[
                    {field:'chk',checkbox: true,width:50},
                    {field:'id',title:'ID',width:50, sortable: true},
                    {field:'photo',title:'头像',width:100,
                        formatter:function(value,index,row){
                            return '<img src='+value+' width="100px" />';
                        }
                    },
                    {field:'username',title:'姓名',width:150, sortable: true},
                    {field:'sn',title:'学号',width:150, sortable: true},
                    {field:'sex',title:'性别',width:150, sortable: true},
                    {field:'clazzId',title:'所属班级',width:150, sortable: true,
                        formatter:function(value,index,row){
                            for(var i=0;i<clazzList.length;i++){
                                if(clazzList[i].id == value){
                                    return clazzList[i].name;
                                }
                            }
                            return value;
                        }
                    },
                    {field:'password',title:'密码',width:150},
                    {field:'remark',title:'备注',width:200},
                ]],
                toolbar: "#toolbar"
            });
            //设置分页控件
            var p = $('#dataList').datagrid('getPager');
            $(p).pagination({
                pageSize: 10,//每页显示的记录条数，默认为10
                pageList: [10, 20, 30, 50, 100],//可以设置每页记录条数的列表
                beforePageText: '第',//页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
            });
            //设置工具类按钮
            $("#add").click(function () {
                $("#addDialog").dialog("open");
            });


            //修改
            $("#edit").click(function () {
                table = $("#editTable");
                var selectRows = $("#dataList").datagrid("getSelections");
                if (selectRows.length != 1) {
                    $.messager.alert("消息提醒", "请选择一条数据进行操作!", "warning");
                } else {
                    $("#editDialog").dialog("open");
                }
            });
            //删除
            $("#delete").click(function () {
                var selectRows = $("#dataList").datagrid("getSelections");
                var selectLength = selectRows.length;
                if (selectLength == 0) {
                    $.messager.alert("消息提醒", "请选择数据进行删除!", "warning");
                } else {
                    var ids = [];
                    $(selectRows).each(function (i, row) {
                        ids[i] = row.id;
                    });

                    $.messager.confirm("消息提醒", "信息删除无法回滚，确认继续？", function (r) {
                        if (r) {
                            $.ajax({
                                type: "post",
                                url: "/student/delete",
                                data: {ids: ids},
                                dataType: 'json',
                                success: function (data) {
                                    if (data.type == "success") {
                                        $.messager.alert("消息提醒", "删除成功!", "info");
                                        //刷新表格
                                        $("#dataList").datagrid("reload");
                                        $("#dataList").datagrid("uncheckAll");
                                    } else {
                                        $.messager.alert("消息提醒", data.msg, "warning");
                                        return;
                                    }
                                }
                            });
                        }
                    });
                }
            });

            //设置添加窗口
            $("#addDialog").dialog({
                title: "添加学生",
                width: 430,
                height: 530,
                iconCls: "icon-add",
                modal: true,
                collapsible: false,
                minimizable: false,
                maximizable: false,
                draggable: true,
                closed: true,
                buttons: [
                    {
                        text: '添加',
                        plain: true,
                        iconCls: 'icon-user_add',
                        handler: function () {
                            var validate = $("#addForm").form("validate");
                            if (!validate) {
                                $.messager.alert("消息提醒", "请检查你输入的数据!", "warning");
                                return;
                            } else {
                                var data = $("#addForm").serialize();
                                $.ajax({
                                    type: "post",
                                    url: "/student/add",
                                    data: data,
                                    dataType: "json",
                                    success: function (data) {
                                        if (data.type == "success") {
                                            $.messager.alert("消息提醒", "添加成功!", "info");
                                            //关闭窗口
                                            $("#addDialog").dialog("close");
                                            //清空原表格数据
                                            $("#add_username").textbox('setValue', "");
                                            $("#add_password").textbox('setValue', "");
                                            $("#add_remark").textbox('setValue', "");
                                            //重新刷新页面数据
                                            $('#dataList').datagrid("reload");
                                        } else {
                                            $.messager.alert("消息提醒", data.msg, "warning");
                                            return;
                                        }
                                    }
                                });
                            }
                        }
                    },
                ],
                onClose: function () {
                    $("#add_username").textbox('setValue', "");
                    $("#add_password").textbox('setValue', "");
                    $("#add_remark").textbox('setValue', "");
                }
            });

            //编辑学生信息
            $("#editDialog").dialog({
                title: "修改学生信息",
                width: 450,
                height: 400,
                iconCls: "icon-edit",
                modal: true,
                collapsible: false,
                minimizable: false,
                maximizable: false,
                draggable: true,
                closed: true,
                buttons: [
                    {
                        text: '提交',
                        plain: true,
                        iconCls: 'icon-edit',
                        handler: function () {
                            var validate = $("#editForm").form("validate");
                            if (!validate) {
                                $.messager.alert("消息提醒", "请检查你输入的数据!", "warning");
                                return;
                            } else {
                                var data = $("#editForm").serialize();
                                $.ajax({
                                    type: "post",
                                    url: "/student/edit",
                                    data: data,
                                    dataType: 'json',
                                    success: function (data) {
                                        if (data.type == "success") {
                                            $.messager.alert("消息提醒", "修改成功!", "info");
                                            //关闭窗口
                                            $("#editDialog").dialog("close");
                                            //重新刷新页面数据
                                            $('#dataList').datagrid("reload");
                                            $('#dataList').datagrid("uncheckAll");

                                        } else {
                                            $.messager.alert("消息提醒", data.msg, "warning");
                                            return;
                                        }
                                    }
                                });
                            }
                        }
                    },
                ],
                onBeforeOpen: function () {
                    var selectRow = $("#dataList").datagrid("getSelected");
                    //设置值
                    $("#edit-id").val(selectRow.id);
                    $("#edit_username").textbox('setValue', selectRow.username);
                    $("#edit_clazzId").combobox('setValue', selectRow.clazzId);
                    $("#edit_sex").combobox('setValue', selectRow.sex);
                    $("#edit_password").textbox('setValue', selectRow.password);
                    $("#edit_remark").textbox('setValue', selectRow.remark);
                    $("#edit-photo-preview").attr("src",selectRow.photo);
                    $("#edit_photo").val(selectRow.photo);
                },
            });


            /*查询*/
            $("#search-btn").click(function (){
                $('#dataList').datagrid('reload', {
                    name: $("#search-name").textbox('getValue'),
                    clazzId: $("#search-clazz-id").combobox('getValue')
                });
            });


            /*图片上传*/
            $("#upload-btn").click(function () {
               if($("#add-upload-photo").filebox("getValue")==''){
                   $.messager.alert("消息提醒","请选择图片文件!", "warning");
                   return;
               }else {
                   $("#photoForm").ajaxSubmit({
                       url: "/student/upload_photo",
                       data: "pic",
                       dataType:"json",
                       success: function (data) {
                           if(data.type == "success"){
                               $.messager.alert("消息提醒","图片上传成功!","info");
                               $("#photo-preview").attr("src",data.src);
                               $("#edit-photo-preview").attr("src",data.src);
                               $("#add_photo").val(data.src);
                               $("#edit_photo").val(data.src);
                           }else{
                               $.messager.alert("消息提醒",data.msg,"warning");
                           }
                       }
                   });
               }
            });


            /*图片修改*/
            $("#edit-upload-btn").click(function () {
               if($("#edit-upload-photo").filebox("getValue")==''){
                   $.messager.alert("消息提醒","请选择图片文件!", "warning");
                   return;
               }else {
                   $("#editPhotoForm").ajaxSubmit({
                       url: "/student/upload_photo",
                       data: "pic",
                       dataType:"json",
                       success: function (data) {
                           if(data.type == "success"){
                               $.messager.alert("消息提醒","图片上传成功!","info");
                               $("#edit-photo-preview").attr("src",data.src);
                               $("#edit_photo").val(data.src);
                           }else{
                               $.messager.alert("消息提醒",data.msg,"warning");
                           }
                       }
                   });
               }
            });

        });
    </script>
</head>
<body>
<!-- 数据列表 -->
<table id="dataList" cellspacing="0" cellpadding="0">
</table>
<!-- 工具栏 -->
<div id="toolbar">
    <c:if test="${usertype==1}">
        <div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a></div>
    </c:if>
    <div style="float: left;" class="datagrid-btn-separator"></div>
    <div style="float: left;"><a id="edit" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></div>
    <div style="float: left;" class="datagrid-btn-separator"></div>
    <div>
        <c:if test="${usertype==1}">
        <a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a>
        学生名：<input id="search-name" class="easyui-textbox" />
        所属班级：
        <select id="search-clazz-id" class="easyui-combobox" style="width: 150px;">
            <option value="">全部</option>
            <c:forEach items="${clazzList}" var="clazz">
                <option value="${clazz.id}">${clazz.name}</option>
            </c:forEach>
        </select>
        <a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
        </c:if>
    </div>
</div>
<!-- 添加窗口 -->
<div id="addDialog">
    <form id="photoForm" method="post" enctype="multipart/form-data" action="/student/upload_photo">
        <table id="addTable1" cellpadding="8">
            <tr >
                <td>预览头像:</td>
                <td>
                    <img id="photo-preview" alt="照片" title="照片" src="../../../photo/1.jpg" style="max-width: 100px; max-height: 100px;" />
                </td>
            </tr>
            <tr >
                <td>学生头像:</td>
                <td>
                    <input id="add-upload-photo" class="easyui-filebox" name="photo" data-options="prompt:'选择照片'" style="width:200px;">
                    <a id="upload-btn" href="javascript:;" class="easyui-linkbutton">上传图片</a>
                </td>
            </tr>
        </table>
    </form>
    <form id="addForm" method="post">
        <table id="addTable2" cellpadding="8">
            <input id="add_photo" type="hidden" name="photo" value="../../../photo/1.jpg"  />
            <tr >
                <td>学生姓名:</td>
                <td>
                    <input id="add_username"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="username" data-options="required:true, missingMessage:'请填写学生姓名'"  />
                </td>
            </tr>
            <tr >
                <td>登录密码:</td>
                <td>
                    <input id="add_password"  class="easyui-textbox" style="width: 200px; height: 30px;" type="password" name="password" data-options="required:true, missingMessage:'请填写登录密码'"  />
                </td>
            </tr>
            <tr >
                <td>所属班级:</td>
                <td>
                    <select id="add_clazzId"  class="easyui-combobox" style="width: 200px;" name="clazzId" data-options="required:true, missingMessage:'请选择所属班级'">
                        <c:forEach items="${clazzList}" var="clazz">
                            <option value="${clazz.id}">${clazz.name}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr >
                <td>学生性别:</td>
                <td>
                    <select id="add_sex"  class="easyui-combobox" style="width: 200px;" name="sex" data-options="required:true, missingMessage:'请选择学生性别'">
                        <option value="男">男</option>
                        <option value="女">女</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>备注:</td>
                <td><input id="add_remark" style="width: 256px; height: 180px;" class="easyui-textbox" type="text" name="remark" data-options="multiline:true"  /></td>
            </tr>
        </table>
    </form>
</div>
<!-- 修改窗口 -->
<div id="editDialog" style="padding: 10px">
    <form id="editPhotoForm" method="post" enctype="multipart/form-data" action="upload_photo" target="photo_target">
        <table id="editTable1" cellpadding="8">
            <tr >
                <td>预览头像:</td>
                <td>
                    <img id="edit-photo-preview" alt="照片" style="max-width: 100px; max-height: 100px;" title="照片" src="../../../photo/1.jpg" />
                </td>
            </tr>
            <tr >
                <td>学生头像:</td>
                <td>
                    <input id="edit-upload-photo" class="easyui-filebox" name="photo" data-options="prompt:'选择照片'" style="width:200px;">
                    <a id="edit-upload-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">上传图片</a>
                </td>
            </tr>
        </table>
    </form>
    <form id="editForm" method="post">
        <input type="hidden" name="id" id="edit-id">
        <table id="editTable2" cellpadding="8">
            <input id="edit_photo" type="hidden" name="photo" value="../../../photo/1.jpg"  />
            <tr >
                <td>学生姓名:</td>
                <td>
                    <input id="edit_username"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="username" data-options="required:true, missingMessage:'请填写学生姓名'" <c:if test="${usertype==2}">readonly="readonly"</c:if>/>
                </td>
            </tr>
            <tr >
                <td>登录密码:</td>
                <td>
                    <input id="edit_password"  class="easyui-textbox" style="width: 200px; height: 30px;" type="password" name="password" data-options="required:true, missingMessage:'请填写登录密码'"  />
                </td>
            </tr>
            <tr >
                <td>所属班级:</td>
                <td>
                    <select id="edit_clazzId"  class="easyui-combobox" style="width: 200px;" name="clazzId" data-options="required:true, missingMessage:'请选择所属班级'">
                        <c:forEach items="${clazzList}" var="clazz">
                            <option value="${clazz.id}">${clazz.name}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr >
                <td>学生性别:</td>
                <td>
                    <select id="edit_sex"  class="easyui-combobox" style="width: 200px;" name="sex" data-options="required:true, missingMessage:'请选择学生性别'">
                        <option value="男">男</option>
                        <option value="女">女</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>备注:</td>
                <td><input id="edit_remark" style="width: 256px; height: 180px;" class="easyui-textbox" type="text" name="remark" data-options="multiline:true"  /></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>