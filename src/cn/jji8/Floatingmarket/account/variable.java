package cn.jji8.Floatingmarket.account;
/**
 * 用作处理变量
 * */
public class variable {
    long NumberOfItems;//库存数量
    double TransactionAmount;//交易金融

    public variable setTransactionAmount(double transactionAmount) {
        TransactionAmount = transactionAmount;
        return this;
    }
    public double getTransactionAmount() {
        return TransactionAmount;
    }
    public variable setNumberOfItems(long numberOfItems) {
        NumberOfItems = numberOfItems;
        return this;
    }
    public long getNumberOfItems() {
        return NumberOfItems;
    }
}
