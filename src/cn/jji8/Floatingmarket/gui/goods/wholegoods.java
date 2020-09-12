package cn.jji8.Floatingmarket.gui.goods;

import cn.jji8.Floatingmarket.main;
import cn.jji8.Floatingmarket.account.money;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class wholegoods{
    long 购买数量 = 0;
    double 涨跌幅度 = main.getMain().getConfig().getDouble("涨跌价格");
    double 涨跌指数 = main.getMain().getConfig().getDouble("涨跌指数");
    public double 单独最高价格 = -1;
    public double 单独最低价格 = -1;
    public ItemStack 物品;
    double 手续费 = main.getMain().getConfig().getDouble("手续费");
    /**
     * 获取当前价格
     * */
    public double getPrice(){
        if(价格>0){
            return 价格;
        }
        if(单独最高价格>0){
            if(main.getMain().formulalist.calculation(购买数量,getMapvariable())>单独最高价格){
                return 单独最高价格;
            }
        }
        if(单独最低价格>0){
            if(main.getMain().formulalist.calculation(购买数量,getMapvariable())<单独最低价格){
                return 单独最低价格;
            }
        }
        return main.getMain().formulalist.calculation(购买数量,getMapvariable());
    }
    /**
     * 获取变量map
     * */
    public Map getMapvariable(){
        Map<String,Double> sss = new HashMap<>();
        sss.put("%购买数量%", (double) 购买数量);
        return sss;
    }
    /**
     * 调用此方法代表玩家出售此物品一组
     */
    public void chushouyizu(Player P){
        chushou(P,物品.getMaxStackSize());
    }
    /**
     * 调用此方法代表玩家出售此物品
     */
    String 没物品消息 = main.getMain().getConfig().getString("没物品消息");
    String 操作失败消息 = main.getMain().getConfig().getString("操作失败消息");
    String 手续费不足 = main.getMain().getConfig().getString("手续费不足");
    String 扣除手续费 = main.getMain().getConfig().getString("扣除手续费");
    public void chushou(Player P, int 数量) {
        if(!kouwupin(P,数量,物品)){
            P.sendMessage(没物品消息);
            return;
        }
        if(手续费>0){
            if(!money.kouqian(P,手续费,false)){
                P.sendMessage(手续费不足.replaceAll("%钱%",Double.toString(手续费)));
                return;
            }else {
                P.sendMessage(扣除手续费.replaceAll("%钱%",Double.toString(手续费)));
            }
        }
        double 钱 = 0;
        for(int i = 0;i<数量;i++){
            购买数量--;
            钱 += getPrice();
        }
        if(!money.jiaqian(P,钱)){
            for(int i = 0;i<数量;i++){
                购买数量++;
                P.getInventory().addItem(物品);
            }
            return;
        }
        shuaxin();
        baocun(10);
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
            执行时间 = System.currentTimeMillis()+时间秒*1000;
            T.start();
        }else {
            执行时间 = System.currentTimeMillis()+时间秒*1000;
        }
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
            try {
                ItemStack = new ItemStack(Material.getMaterial(错误物品));
            }catch (Throwable a){
                main.getMain().getLogger().warning("config.yml文件中“错误物品”错误，请检查配置文件");
                return new ItemStack(Material.BEDROCK);
            }
            错误 = true;
        }else {
            物品.setAmount(1);
            ItemStack = new ItemStack(物品);
        }
        String 价格字符舍 = money.XianShiZiFu(getPrice());
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
        String 服务器账户余额字符舍 = money.XianShiZiFu(main.getMain().getServermoney().getmoney());
        for(String S:价格显示){
            if(购买数量>=0){
                S = S.replaceAll("%价格%",价格字符舍)
                        .replaceAll("%购买数量%","+"+购买数量)
                        .replaceAll("%服务器账户余额%",服务器账户余额字符舍);
            }else {
                S = S.replaceAll("%价格%",价格字符舍)
                        .replaceAll("%购买数量%",Long.toString(购买数量))
                        .replaceAll("%服务器账户余额%",服务器账户余额字符舍);
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
    /**public void tiaojia(){
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
    }*/

    /**
     * 用于获取物品价格
     */
    public double getjiage() {
        return getPrice();
    }

    /**
     * 用于设置物品价格
     */
    double 价格 = -1;
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
    public void shuaxin(){
        main.getMain().event.shuaxin();
    }
    /**
     * 调用此方法代表玩家购买一组物品
     * */
    public void goumaiyizu(Player P){
        goumai(P,物品.getMaxStackSize());
    }
    /**
     * 调用此方法代表玩家购买了此商品
     */
    String 背包满消息 = main.getMain().getConfig().getString("背包满消息");
    String 增加手续费 = main.getMain().getConfig().getString("增加手续费");
    public void goumai(Player P, int 数量) {
        long 前购买数量 = 购买数量;
        double 钱数 = 0;
        for(int i = 0;i<数量;i++){
            购买数量++;
            钱数 += getPrice();
        }
        double 总价格 = 0;
        if(手续费>0){
            P.sendMessage(增加手续费.replaceAll("%钱%",Double.toString(手续费)));
            总价格 = 钱数+手续费;
        }else {
            总价格 = 钱数;
        }
        if(!money.kouqian(P,总价格)){
            购买数量 = 前购买数量;
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
            double 钱 = 0;
            for(int i = 0;i<退回数量;i++){
                购买数量--;
                钱 += getPrice();
            }
            money.jiaqian(P,钱,true,false);
            P.sendMessage(背包满消息);
        }
        shuaxin();
        baocun(10);
    }
}
