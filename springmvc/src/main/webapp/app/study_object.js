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

for (var propName in person){
	alert(propName);
	//alert(typeof propName);
}
var  c = person.constructor;

alert(c);
alert(typeof c); // "function"

var person2 = new c("denglt","76");

alert(person2.username);

var f = Person;
alert(f);
alert(typeof f);  //"function"
alert(c==f); // true;
alert(c===f); // true;
person2 = new c("zyy","76");

alert(person2.username);