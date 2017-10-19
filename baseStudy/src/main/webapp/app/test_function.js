/**
 * 每个函数都是Funtion类型的实例，函数名称实际上是一个指向Funtion类型的实例对象的指针
 */

// 构造函数是传递参数
function Person(username, password) {
	this.username = username;
	this.password = password;
	this.getInfo = function() {
		alert(this.username + ", " + this.password);
	}
}
alert(typeof window.Person); // "function"
alert(typeof Person); // "function"
alert(Person instanceof Function); // true

var person = new Person("zhangsan", "123");
alert(typeof person); // "object"
alert(person instanceof Person); // true
alert(person instanceof Function); // false

Person.staticfun = function()
{
	alert("这像个静态函数吧");
};

Person.staticfun();
/*
 * alert(typeof Date); // "function" alert(Date); alert(typeof encodeURI); //
 * "function"
 * 
 * this.f= function(){ }
 * 
 * alert(typeof f); // "function" alert(f);
 */
