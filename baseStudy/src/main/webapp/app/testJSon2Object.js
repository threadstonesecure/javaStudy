/**
 * 
 */
//对象字面量表示法
var o = {
	"xlid" : "cxh",
	"xldigitid" : 123456,
	"topscore" : 2000,
	"topplaytime" : "2009-08-20"
};
alert(o.xlid);

function displayInfo(args){
	var output = "";
	if (typeof args.name == "string"){
		output += "Name:" + args.name + "\n";
	}
	if (typeof args.age == "number"){
		output += "Age:"+ args.age + "\n";
	}
	alert(output);
}

displayInfo({
	name : "denglt",
	age  : 38
});

displayInfo({
	name : "zyy"
});
//定义数组对象(数组字面量表示法)
var jsonranklist = [ {
	"xlid" : "cxh",
	"xldigitid" : 123456,
	"topscore" : 2000,
	"topplaytime" : "2009-08-20"
}, {
	"xlid" : "zd",
	"xldigitid" : 123456,
	"topscore" : 1500,
	"topplaytime" : "2009-11-20"
} ];

alert(jsonranklist[0].xlid);

//
var colors = new Array(); // new Array(20); new Array("red","blue","green");


//JSON字符串:
var str_people = '{ "name": "denglt", "sex": "man" }';

var people = eval('('+str_people+')');//

alert(people.name);

//JSON数组字符串
var str_array = '[{"xlid" : "cxh","xldigitid" : 123456,"topscore" : 2000,	"topplaytime" : "2009-08-20"}, {"xlid" : "zd","xldigitid" : 123456,"topscore" : 1500,"topplaytime" : "2009-11-20"}]';

var objArray = eval(str_array);

for (var i =0 ; i < objArray.length; i++){
	alert(objArray[i].xlid);
}



var t3="[['<a href=# onclick=openLink(14113295100,社旗县国税局桥头税务所,14113295100,d6d223892dc94f5bb501d4408a68333d,swjg_dm);>14113295100</a>','社旗县国税局桥头税务所','社旗县城郊乡长江路西段']]";  
//通过eval() 函数可以将JSON字符串转化为对象  (二为数组 )
var obj = eval(t3);
for (var i = 0; i < obj.length; i++) {
	for (var j = 0; j < obj[i].length; j++) {
		alert(obj[i][j]);
	}  

} 

