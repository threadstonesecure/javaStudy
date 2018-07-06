/**
 * 获取对象属性、原型
 */


var Dlt = Dlt || {};
Dlt.pros = {
	getProsHtml : function(obj) {
		var tables = this._toTable(obj, obj);
		if (typeof obj == "function") {
			tables += this._toTable(obj.prototype, "函数原型：" + obj.name
					+ ".prototype" + "  (函数生成的实例将有个内部属性指向该prototype)");
			tables += this._toTable(Object.getPrototypeOf(obj), "函数对象原型："
					+ Object.getPrototypeOf(obj).constructor.name
					+ ".prototype");
		} else {
			tables += this._toTable(Object.getPrototypeOf(obj), "对象原型："
					+ Object.getPrototypeOf(obj).constructor.name
					+ ".prototype");
		}
		return tables;
	},
	getPros : function(obj) {
		// var pros = Object.keys(obj); // 仅能显示可枚举的属性
		var pros = Object.getOwnPropertyNames(obj); // 可以显示不能枚举的属性
		/*
		 * var pros = new Array(); for (var proname in obj){ // 原型链上继承到的可枚举属性
		 * pros.push(proname); }
		 */
		return pros;
	},
	_toTable : function(obj, title) {
		var prosHtml = "<table border='1'>";
		prosHtml += "<tr align='left'><th colspan='3' >" + title + "</th>";
		prosHtml += "<tr><th>属性</th><th>值</th><th>属性性质</th></tr>";
		var pros = this.getPros(obj);
		if (pros instanceof Array) {
			for (var i = 0; i < pros.length; i++) {
				var prodesc = Object.getOwnPropertyDescriptor(obj, pros[i]);
				prodesc.toString = function() {
					return "configurable=" + this.configurable + ",enumerable="
							+ this.enumerable + ",writable=" + this.writable
					// + ",value=" + this.value
					;
				};
				prosHtml += "<tr><td>" + pros[i] + "</td><td>" + obj[pros[i]]
						+ "</td><td>" + prodesc + "</td></tr>";
			}
		}
		prosHtml += "</table>";
		return prosHtml;
	}

};


