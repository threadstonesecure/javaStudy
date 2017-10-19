/**
 * this 引用的是函数据以执行的环境对象。
 * 当在网页作用域中调用函数时，this对象引用的就是window
 */
alert(this); //"object window"
var global = function(){return this;}();
alert(global); //"object window"
alert(global===this); // true
alert(global===window); // true

function Person(username, password) {
    alert(this);
	this.username = username;
	this.password = password;
	this.getInfo = function() {
		alert(this.username + ", " + this.password);
	}
}

Person("denglt","password");  // alert object window
window.getInfo(); //"denglt,password"

var person = new Person("denglt","password");// alert [object Object]
person.getInfo(); //"denglt,password"

Object.defineProperty(person, "wifename", {
	writable : false,
	value : "zyy"
});

person.wifename = "zyf";
alert("person.wifename = " + person.wifename);  // "zyy"  //修改为"zyf"没有成功

Object.defineProperty(person,"password",{
	  writable :false,
	  value    : "11111111111111"
});

alert("person.password=" + person.password); // "11111111111111"


/*
 * this 与  Global对象 (默认浏览器都将Global对象作为window)
 */
this.Date = "dddd";
alert(typeof Date);

var d = new Date(); // 这个会报错，说明优先使用了this.Date(即window.Date)，而屏蔽了Global对象的Date函数
alert(d);  


/*
window.color = "red";
var o = {color: "blue"};

function sayColor(){
	alert(this);
	alert(this.color);
}

sayColor(); // "red"

o.sayColor = sayColor;
o.sayColor(); // "blue"
*/

/*
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
obj.printName.call(window,'ggggg');  //一种特殊的调用方法
obj.printName.apply(window,['ggggg']);

*/




