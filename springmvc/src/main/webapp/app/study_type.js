
if (0.1+0.2 == 0.3){
	alert("you got 0.3");
}else{
	alert("you does not get 0.3");
}

alert(0.1+0.2);
alert(0.15+0.15);

alert(Number.MIN_VALUE);	
alert(Number.MAX_VALUE);

var result = Number.MAX_VALUE + Number.MAX_VALUE;
alert(isFinite(result));

alert(NaN == NaN);// false

var text="denglt邓隆通";
alert(text.length);  //9 双字节长度不对
alert(typeof text);  // "string"

var message;
alert(message==undefined); //true
alert(message);  //"undefined"
alert(typeof message); "undefined"


alert(typeof null) ;//"object"

alert(null == undefined); // true
//var age;
alert(age); // error



