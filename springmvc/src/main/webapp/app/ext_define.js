
Ext.define('My.sample.Person', {
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


   
var aaron = Ext.create('My.sample.Person', 'Aaron');

Ext.apply(aaron,{
     eat2:function(foodType){
     	alert(this.name + " is eating: " + foodType);
     }
   });
   
aaron.eat("Salad"); 
aaron.eat2("Salad too"); 

var aaron2 = Ext.create('My.sample.Person', 'Aaron2');
aaron2.eat("Salad");
//aaron2.eat2("xxxxx");

