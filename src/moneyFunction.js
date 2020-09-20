//————————————————————————————
//	JavaScript脚本文件
//	用于计算税收
//      variable.getTransactionAmount() 可以获取玩家交易的钱数
//————————————————————————————
//默认的税收计算公式
function tax(variable){
	var qian = variable.getTransactionAmount();//获取玩家交易的钱数
	if(qian<50){
		return -1;//如果钱小于10就告诉插件扣-1元的税函数结束，任意一个负数代表不扣税
	}
	if(qian<100){
		return (qian-50)*0.01;//如果交易小于100，超过50元的部分扣税1%
	}
	return 50*0.01+(qian-100)*0.05;//如果上面if{}里的return都没有执行代表交易金额大于100，超过50元的部分扣税1%，超过100的扣税5%
}