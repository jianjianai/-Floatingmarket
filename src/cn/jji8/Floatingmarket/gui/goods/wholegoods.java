package cn.jji8.Floatingmarket.gui.goods;

import cn.jji8.Floatingmarket.main;
import cn.jji8.Floatingmarket.money;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class wholegoods{
    long 购买数量 = 0;
    public double 价格;
    double 最高价格 = main.getMain().getConfig().getDouble("全局最高价格");
    double 最低价格 = main.getMain().getConfig().getDouble("全局最低价格");
    double 涨跌幅度 = main.getMain().getConfig().getDouble("涨跌价格");
    double 涨跌指数 = main.getMain().getConfig().getDouble("涨跌指数");
    public double 单独最高价格 = -1;
    public double 单独最低价格 = -1;
    public ItemStack 物品;
    /**
     * 调用此方法代表玩家出售此物品
     */
    String 没物品消息 = main.getMain().getConfig().getString("没物品消息");
    String 操作失败消息 = main.getMain().getConfig().getString("操作失败消息");
    public void chushou(Player P, int 数量) {
        if(!kouwupin(P,数量,物品)){
            P.sendMessage(没物品消息);
            return;
        }
        购买数量 -= 数量;
        tiaojiasuaxing();
        if(!money.jiaqian(P,数量*价格)){
            P.sendMessage(操作失败消息);
            for(int i = 0;i<数量;i++){
                P.getInventory().addItem(物品);
            }
            return;
        }else {
            baocun(10);
        }
    }
    /**
     * 保存数据，但不会频繁重复保存
     * 在指定时间多次调用此方法，前面的调用无效
     * 时间/秒；
     * */
    long 执行时间 = -1;
    public void baocun(int 时间秒){
        if(执行时间==-1){
            Thread T = new Thread(){
                @Override
                public void run() {
                    while (true){
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(System.currentTimeMillis()>执行时间){
                            baocun();
                            执行时间 = -1;
                            return;
                        }
                    }
                }
            };
            T.start();
        }
        执行时间 = System.currentTimeMillis()+时间秒*1000;
    }
    public void baocun(){}
    /**
     * 获取用于显示的物品堆
     */
    String 错误物品 = main.getMain().getConfig().getString("错误物品");
    List<String> 价格显示 = main.getMain().getConfig().getStringList("价格显示");
    public ItemStack getxianshiwupin() {
        boolean 错误 = false;
        ItemStack ItemStack;
        if(物品==null){
            ItemStack = new ItemStack(Material.getMaterial(错误物品));
            错误 = true;
        }else {
            物品.setAmount(1);
            ItemStack = new ItemStack(物品);
        }
        String 价格字符舍 = money.XianShiZiFu(价格);
        ItemMeta ItemMeta = ItemStack.getItemMeta();
        if(错误){
            ItemMeta.setDisplayName("§7§l此物品配置错误");
        }
        List<String> ArrayList = null;
        if(ItemMeta!=null){
            ArrayList = ItemMeta.getLore();
        }
        if(ArrayList==null){
            ArrayList = new ArrayList();
        }
        for(String S:价格显示){
            if(购买数量>=0){
                S = S.replaceAll("%价格%",价格字符舍).replaceAll("%购买数量%","+"+购买数量);
            }else {
                S = S.replaceAll("%价格%",价格字符舍).replaceAll("%购买数量%",Long.toString(购买数量));
            }
            ArrayList.add(S);
        }
        ItemMeta.setLore(ArrayList);
        ItemStack.setItemMeta(ItemMeta);
        return ItemStack;
    }
    /**
     * 给玩家扣除指定数量得指定物品
     * */
    boolean kouwupin(Player P,int 数量,Material 物品){
        if(物品==null){
            P.sendMessage("执行此命令出错");
        }
        return kouwupin(P,数量,new ItemStack(物品));
    }
    /**
     * 给玩家扣除指定数量得指定物品
     * */
    boolean kouwupin(Player P,int 数量,ItemStack 物品){
        int 物品数量 = 0;
        ItemStack[] 全部物品 = P.getInventory().getContents();
        for (ItemStack a:全部物品){
            if(a!=null){
                if(a.isSimilar(物品)){
                    物品数量 += a.getAmount();
                    a.setAmount(0);
                }
            }
        }
        if(物品数量<数量){
            for(int i = 0;i<物品数量;i++){
                P.getInventory().addItem(物品);
            }
            return false;
        }
        for(int i = 0;i<物品数量-数量;i++){
            P.getInventory().addItem(物品);
        }
        return true;
    }

    /**
     * 调价
     * 用于调整物品价格
     * */
    public void tiaojia(){
        while (购买数量>涨跌指数){
            if(单独最高价格>0){
                最高价格 = 单独最高价格;
            }
            if(单独最低价格>0){
                最低价格 = 单独最低价格;
            }
            价格 += 涨跌幅度;
            购买数量-=涨跌指数;
            if(价格>=最高价格){
                购买数量-=涨跌指数;
                价格=最高价格;
                break;
            }
        }
        while(购买数量<-涨跌指数){
            价格 -= 涨跌幅度;
            购买数量+=涨跌指数;
            if(价格<=最低价格){
                购买数量-=涨跌指数;
                价格=最低价格;
                break;
            }
        }
    }

    /**
     * 用于获取物品价格
     */
    public double getjiage() {
        return 价格;
    }

    /**
     * 用于设置物品价格
     */
    public void setjiage(double 价格) {
        this.价格 = 价格;
    }

    /**
     * 用于设置物品最高价格
     *
     * @param 价格
     */
    public void setgaojiage(double 价格) {
        this.单独最高价格 = 价格;
    }

    /**
     * 用于设置物品最低价格
     *
     * @param 价格
     */
    public void setdijiage(double 价格) {
        this.单独最低价格 = 价格;
    }
    /**
     * 调价刷新
     * */
    public void tiaojiasuaxing(){
        tiaojia();
        main.getMain().event.shuaxin();
    }
    /**
     * 调用此方法代表玩家购买了此商品
     */
    String 背包满消息 = main.getMain().getConfig().getString("背包满消息");
    public void goumai(Player P, int 数量) {
        购买数量 += 数量;
        tiaojia();
        if(!money.kouqian(P,价格*数量)){
            return;
        }
        int 退回数量 = 0;
        for(;数量>0;数量--){
            HashMap HashMap = P.getInventory().addItem(物品);
            for(Object a:HashMap.values()){
                if(a instanceof ItemStack){
                    退回数量++;
                }
            }
        }
        if(退回数量!=0){
            购买数量 -= 退回数量;
            money.jiaqian(P,退回数量*价格);
            P.sendMessage(背包满消息);
        }
        tiaojiasuaxing();
        baocun(10);
    }
}
