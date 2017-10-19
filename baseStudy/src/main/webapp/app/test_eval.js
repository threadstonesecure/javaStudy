/**
 * Global的方法eval
 */

var cmd="alert('hello world!\')";
eval(cmd);
window.eval(cmd); //默认浏览器都将Global对象作为window对象的一部分加以实现。

cmd="function sayHi(){ alert('hi'); }";
eval(cmd);
sayHi(); // "hi"

function syaHi2(){
	eval(cmd);
	sayHi();  //  "hi"
}
syaHi2();