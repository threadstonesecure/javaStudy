/**
 * 每个函数都是Funtion类型的实例，函数名称实际上是一个指向Funtion类型的实例对象的指针。
 * 每个函数都有一个prototype(原型)属性，这个属性是一个指针，指向一个原型对象。
 * 所有该函数的实例都共享这个prototype，他们有个内部属性指向函数的prototype。
 * 同理函数也是一个对象(Function类型的实例)，他也有一个内部属性指向Function.prototype
 * prototype.constructor 指向函数
 */
Object.prototype.basename="basename";
/*function Person(){

}*/
Person = function (){
	
}
Person["extend"]="denglt";
alert(Person.extend);
alert(Person.prototype);
alert(Function.prototype.constructor == Function); // true
alert(Function.prototype);
alert(Object.getPrototypeOf(Person)==Function.prototype); // true
Function.prototype.$hello = "hello by dlt";
alert(Person.$hello);

alert(Person.constructor == Function);  // true
alert(Person.prototype.constructor == Person); // true

Person.prototype.name="denglt";
//Person.prototype.basename="personbasename";
Person.prototype.age=38;
Person.prototype.job="IT";
Person.prototype.sayName=function(){
	alert(this.name);
}

alert(Person.sayName())



var person1 = new Person();
var person2 = new Person();
alert(person1.basename); //"basename"

person1.basename="ssssss";

alert(person2.basename); //"basename"

alert(person1.constructor==Person) ; // true
alert(Person.prototype.isPrototypeOf(person1)); // true
alert(Object.getPrototypeOf(person1) == Person.prototype) ; // true

person1.sayName(); // "denglt"
person2.sayName(); // "denglt"

alert(person1.sayName == person2.sayName);

person1.name="zyy";  //实际上在person1上新定义了一个name的属性,该属性屏蔽了prototype.name
person1.sayName();  // "zyy"
person2.sayName();  // "denglt"

Person.prototype.name="denglt_zyy";
person1.sayName();  // "zyy"
person2.sayName();  // "denglt_zyy"

Person.prototype = {
		constructor : Person,   //注意这儿
		name : "denglt",
		age  : 38,
		wife : "zyy"
};  //  这将重写了原型 ,原先生成 的person1和person2的prototype还是指向旧的原型；后面再new Person()出来的实例将指向新的原型

person3 = new Person();
alert("注意：");
alert(Object.getPrototypeOf(person1) == Person.prototype) ; // false
alert(Object.getPrototypeOf(person3) == Person.prototype) ; // true

person3.sayName();  // error
