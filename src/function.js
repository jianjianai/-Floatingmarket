//————————————————————————————
//	JavaScript脚本文件
//	插件调用对应的函数返回价格  可以使用/fg setformula 函数名字 来设置物品的计算函数，所有物品默认使用Price函数
//		Price———默认的计算价格的函数
//  	stock———默认的计算库存变量的函数
//      variable.getNumberOfItems()   获取物品的数量
//————————————————————————————
//默认的计算价格的方法
function Price(variable){
	var shuliang = variable.getNumberOfItems();//获取物品的数量
	var jiage = shuliang*0.01+10;//使用shuliang*0.01+10的值
	if(jiage<0.01){//如果价格小于0.01就执行{}里面的语句
		jiage = 0.01;//让价格等于0.01
	}
	if(jiage>20){//和上面一个原理，只是小于变成大于了
		jiage = 20;
	}
	//下面是判断是否还有库存的
	var stock = -shuliang;//库存和购买数量是反的所以加个-号就可以了。
	if(stock<-20){//如果库存小于-20就执行{}里面的语句
		jiage = -1//设置价格为-1，这样插件就不会出售或购买物品了
	}
	if(stock>20){//和上面一样，只是小于变成了大于
		jiage = -1
	}

	return jiage;//把计算好的价格告诉插件，如果返回一个负数，插件将不会出售或购买此物品
}
//默认的计算库存变量的方法
function stock(variable){
	var stock = -variable.getNumberOfItems();//库存和购买数量是反的所以加个-号就可以了。
	var xianshi = stock;//让显示值等于stock（库存）
	if(stock<=-20){//如果库存小于-20就执行{}里面的语句
		xianshi = "缺货，不再出售"+stock//设置显示为缺货，不再出售
	}
	if(stock>=20){//和上面一样，只是小于变成了大于
		xianshi = "已满，不再收购"+stock
	}
	return xianshi;//把需要显示的值告诉插件。
}