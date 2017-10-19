var userStore = new Ext.data.Store( {// 定义数据集对象
        proxy : new Ext.data.HttpProxy( {
            url : 'user.do?method=getUserList×tamp=' + new Date()// 设置代理请求的url
        }),
        
//        reader : new Ext.data.XmlReader( {// 创建xml数据解析器
//            totalRecords : "results",
//            record : "UserForm",// 对应的Form,下面的字段名与此Form中的相同,数量可以不同
//            id : "userId"
//        }, Ext.data.Record.create( [ {
//            name : 'userId'
//        }, {
//            name : 'userName'
//        }, {
//            name : 'realName'
//        }, {
//            name : 'roleId'
//        }, {
//            name : 'telephone'
//        }, {
//            name : 'email'
//        }, {
//            name : 'address'
//        }, {
//            name : 'remark'
//        } ]))
        
         reader: new Ext.data.JsonReader({
             totalProperty : 'results',
             root : 'items',
             id: 'userId'
             },
             ['userId','userName','realName','roleId','telephone','email','address','remark'])
    });
    function addUser(){
    
    };
    
    function deleteUser(){
    
    };
    
    function updateUser(){
    };
    

    var toolbar = new Ext.Toolbar( [ {// 创建GridPanel的工具栏组件
        text : '新增员工信息',
        iconCls : 'add',
        handler : addUser
    }, {
        text : '删除员工信息',
        iconCls : 'remove',
        handler : deleteUser
    }, {
        text : '修改员工信息',
        iconCls : 'plugin',
        handler : updateUser

    }
    , '-','查询：',' ', new Ext.ux.form.SearchField( {
        store : userStore,
        width : 110
    })

    ]);
    
    var sm = new Ext.grid.CheckboxSelectionModel();// 创建复选择模式对象
    var cm = new Ext.grid.ColumnModel( [// 创建表格列模型
    new Ext.grid.RowNumberer(), sm, {
        header : "编号",
        width : 30,
        dataIndex : 'userId',
        hidden : true,
        sortable : true
    }, {
        header : "用户名",
        width : 60,
        dataIndex : 'userName',
        sortable : true
    }, {
        header : "真实姓名",
        width : 60,
        dataIndex : 'realName',
        sortable : true
    }, {
        header : "用户角色",
        width : 60,
        dataIndex : 'roleId',
        sortable : true
    }, {
        header : "联系电话",
        width : 60,
        dataIndex : 'telephone',
        sortable : true
    }, {
        header : "电子邮箱",
        width : 80,
        dataIndex : 'email',
        sortable : true
    }, {
        header : "住址",
        width : 80,
        dataIndex : 'address',
        sortable : true
    }, {
        header : "备注",
        width : 60,
        dataIndex : 'remark'
    } ]);

    var userGrid = new Ext.grid.GridPanel( {// 创建Grid表格组件
        applyTo : 'user-grid-div',// 设置表格现实位置
        frame : true,// 渲染表格面板
        tbar : toolbar,// 设置顶端工具栏
        stripeRows : true,// 显示斑马线
        autoScroll : true,// 当数据查过表格宽度时，显示滚动条
        store : userStore,// 设置表格对应的数据集
        viewConfig : {// 自动充满表格
            autoFill : true
        },
        sm : sm,// 设置表格复选框
        cm : cm,// 设置表格的列
        bbar : new Ext.PagingToolbar( {
            pageSize : 25,
            store : userStore,
            displayInfo : true,
            displayMsg : '显示第{0}条到{1}条记录,一共{2}条记录',
            emptyMsg : '没有记录'
        //    ,items:['-',new Ext.app.SearchField({store:userStore})]
        })
    });
    userStore.load( {// 加载数据集
        params : {
            start : 0,
            limit : 25
        }
    });