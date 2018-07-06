var denglt = denglt || {};
denglt._starttime = new Date().getTime();
denglt._name = '邓隆通'; //动态增加属性
denglt.start = function() {
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
    
};
denglt.start();
window.alert(denglt._name);
alert(denglt._starttime);