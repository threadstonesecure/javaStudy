Ext.require('Ext.container.Viewport');

Ext.application({
    name: 'HelloExt',
    launch: function() {
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            items: [
                {
                    title: 'Hello Ext by 邓隆通',
                    html : 'Hello! Welcome to Ext JS.'
                }
            ]
        });
    }
});

var Mystrjson="{Name:'邓隆通',Sex:'男',Age:26,Married:false}";//第一json字符串

var Myobjson=Ext.decode(Mystrjson);//将json字符串转换为对象

alert(Myobjson.Name);//使用json对象属性


var Mystrjson2=Ext.encode(Myobjson);//将json对象转换为json字符串 ，Mystrjson=Mystrjson2
alert(Mystrjson2);


/*
Ext.define('MyApp.app.Application', {
			extend : 'Ext.app.Application',
			name : 'MyApp',
			launch : function() {
				Ext.create('Ext.container.Viewport', {

							items : {
								html : 'My App <h2>Hello World!</h2>'
							}
						});
			}
		});
		
Ext.application('MyApp.app.Application');

*/
		
		
		