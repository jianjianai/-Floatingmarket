package cn.jji8.Floatingmarket.gui.goods;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface goods {
    /**
     * 获取用于显示的物品堆
     * */
    public ItemStack getxianshiwupin();
    /**
     * 调用此方法代表玩家购买了此商品
     * */
    public void goumai(Player P, int 数量);
    /**
     * 调用此方法代表玩家出售此物品
     * */
    public void chushou(Player P,int 数量);
    /**
     * 保存方法,用于保存数据
     * */
    public void baocun();
    /**
     * 删除时调用的方法
     * */
    public void delete();
    /**
     * 加载方法,用于加载数据
     * false没有相关数据使用默认值 true加载成功
     * */
    public void jiazai();
    /**
     * 用于获取原商品
     * */
    public ItemStack getshangping();
    /**
     * 用于获取物品价格
     * */
    public double getjiage();
    /**
     * 用于设置物品的固定价格
     * */
    public void setjiage(double 价格);
    /**
     * 用于设置物品最高价格
     * */
    public void setgaojiage(double 价格);
    /**
     * 用于设置物品最低价格
     * */
    public void setdijiage(double 价格);
    /**
     * 获取物品的名字
     * */
    public String getname();
    /**
     * 调用此方法代表玩家出售此物品一组
     */
    public void chushouyizu(Player P);
    /**
     * 调用此方法代表玩家购买一组物品
     * */
    public void goumaiyizu(Player P);
    /**
     * 设置物品的公式名字
     * */
    void setSetformula(String 公式名字);
}
