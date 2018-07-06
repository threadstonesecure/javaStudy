//基于已有对象扩充其对象和方法(只适合于临时的生成一个对象)：
var obj = new Object();
obj.name = "userObject";
obj.printName = function(name) {
	
	alert(this.name);
	this.name = name + " is ok";  //this 指向的是调用者
	alert(name);  // 
	alert(this.name);
}
obj.printName("newUserObjec" );


window.name='window name';
obj.printName.call(window,'ggggg');

// var obj2 = new obj(); is error
// 工厂方式创建对象(

function createObject(username, password) {
	var obj = new Object();
	obj.username = username;
	obj.password = password;
	obj.get = function() {
		alert(this.username + ", " + this.password);
	}
	return obj;
}
var obj1 = createObject("zhangsan", "123456");
obj1.get();

//构造函数是传递参数
function Person(username, password) {
	this.username = username;
	this.password = password;
	this.getInfo = function() {
		alert(this.username + ", " + this.password);
	}
}
var person = new Person("zhangsan", "123");
person.getInfo();


function setName(obj){
	obj.name="denglt";
	obj = new Object();
	obj.name="zyy";
}
var person = new Object();
setName(person);
alert(person.name); //"denglt"