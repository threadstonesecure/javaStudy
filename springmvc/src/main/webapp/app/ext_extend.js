function S() {
}

S.prototype.s = "s";
S.prototype.s1 = "s1";

var ss = new S();

alert("hello is " + ss.s);

function C() {
	this.c = "c";
	this.c1 = "c1";
}
var CC = Ext.extend(C, S, {
	s1 : "by c overload"
});

var c = new CC();
alert(c.s); // s
alert(c.s1); // by c overload
var c2 = new C();
alert(c2.s); // s
alert(c2.s1); // by c overload



C = Ext.extend(S,{s1:"by c overload"});   
var c = new C();   
alert(c.s); //s   
alert(c.s1); //by c overload   

Ext.ns('My.cool');
My.cool.Penson = Ext.extend(Object, {
    name: 'Unknown',

    constructor: function(name) {
        if (name) {
            this.name = name;
        }
    },

    eat: function(foodType) {
        alert(this.name + " is eating: " + foodType);
    }
});

var aaron = new My.cool.Penson("denglt");

aaron.eat("Salad"); 


// var aaron = Ext.create('My.sample.Person', 'Aaron');