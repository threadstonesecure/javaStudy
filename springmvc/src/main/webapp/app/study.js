
//构造函数是传递参数
function Person(username, password) {
	//"use strict"  //严格模式
	this.username = username;
	this.password = password;
	this.age      = 070; //严格模式 不支持八进制
	this.getInfo = function() {
		alert(this.username + ", " + this.password);
	}
}
var person = new Person("zhangsan", "123");
//person.prototype.s = "dddd"; is error   //prototype是函数的属性
alert(person.toString());
alert(person.age);
alert(typeof null);  // object
alert(typeof doSomething);  //undefined
alert(typeof(Person));     //function
alert(typeof(person));     //object

var message;
alert(message == undefined);


var car = null;

if (car == undefined){
	alert("car == undefined");
}

if (car == null){
	alert("car == null");
}