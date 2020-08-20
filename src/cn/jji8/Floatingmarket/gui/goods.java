package cn.jji8.Floatingmarket.gui;

import cn.jji8.Floatingmarket.main;
import cn.jji8.Floatingmarket.money;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 主要负责箱子中物品的处理
 * */
public class goods {
    double 价格;
    Material 物品;
    int 购买数量 = 0;
    /**
     * 获取用于显示的物品堆
     * */
    public ItemStack getxianshiwupin(){
        ItemStack ItemStack = new ItemStack(物品);
        ItemMeta ItemMeta = ItemStack.getItemMeta();
        String 价格字符 =  String.valueOf(价格);
        int 小数点位置 = 价格字符.indexOf(".");
        String 价格字符舍;
        if(价格字符.length()>小数点位置+5){
            价格字符舍 = 价格字符.substring(0,小数点位置+5);
        }else {
            价格字符舍 = 价格字符;
        }
        if(价格<=0.0001){
            价格字符舍 = "0.0001";
        }
        ArrayList<String> ArrayList = new ArrayList<String>();
        ArrayList.add("§f-------------------------------");
        if(购买数量>=0){
            ArrayList.add("§d物品价格：§e"+价格字符舍+"  §a|  "+"§d跌涨指数：§e+"+购买数量);
        }else {
            ArrayList.add("§d物品价格：§e"+价格字符舍+"  §a|  "+"§d跌涨指数：§b"+购买数量);
        }
        ArrayList.add("§f-------------------------------");
        ArrayList.add("§e左键购买，右键出售");
        ArrayList.add("§eShift+左键/右键，购买/出售一组");
        ArrayList.add("§f-------------------------------");
        ItemMeta.setLore(ArrayList);
        ItemStack.setItemMeta(ItemMeta);
        return ItemStack;
    }
    /**
     * 调用此方法代表玩家购买了此商品
     * */
    public void goumai(Player P, int 数量){
        if(!money.kouqian(P,价格*数量)){
            return;
        }
        购买数量 += 数量;
        HashMap HashMap = P.getInventory().addItem(new ItemStack(物品,数量));
        for(Object a:HashMap.values()){
            if(a instanceof ItemStack){
                int 退回数量 = ((ItemStack)a).getAmount();
                money.jiaqian(P,退回数量*价格);
                购买数量 -= 退回数量;
                P.sendMessage("背包已满，多购部分已退回");
            }
        }
        baocun(10);
        tiaojiasuaxing();
    }
    /**
     * 调用此方法代表玩家出售此物品
     * */
    public void chushou(Player P,int 数量){
        if(!P.getInventory().contains(物品,数量)){
            P.sendMessage("你没有足够的物品");
            return;
        }
        if(!kouwupin(P,数量,物品)){
            P.sendMessage("你没有足够的物品");
            return;
        }
        if(!money.jiaqian(P,价格*数量)){
            P.sendMessage("操作失败");
            P.getInventory().addItem(new ItemStack(物品,数量));
            return;
        }else {
            购买数量 -= 数量;
            baocun(10);
            tiaojiasuaxing();
        }
    }
    /**
     * 给玩家扣除指定数量得指定物品
     * */
    boolean kouwupin(Player P,int 数量,Material 物品){
        int 物品数量 = 0;
        while (物品数量<数量){
            int 物品位置 = P.getInventory().first(物品);
            if(物品位置==-1){
                P.getInventory().addItem(new ItemStack(物品,物品数量));
                return false;
            }
            ItemStack ItemStack = P.getInventory().getItem(物品位置);
            物品数量 += ItemStack.getAmount();
            P.getInventory().setItem(物品位置,null);
        }
        P.getInventory().addItem(new ItemStack(物品,物品数量-数量));
        return true;
    }
    /**
     * 调价
     * 用于调整物品价格
     * */
    double 最高价格 = main.getconfig().getDouble("全局最高价格");
    double 最低价格 = main.getconfig().getDouble("全局最低价格");
    double 涨跌幅度 = main.getconfig().getDouble("涨跌价格");
    double 涨跌指数 = main.getconfig().getDouble("涨跌指数");
    public void tiaojia(){
        while (购买数量>涨跌指数){
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
     * 调价刷新
     * */
    public void tiaojiasuaxing(){
        tiaojia();
        event.shuaxin();
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
    /**
     * 保存方法,用于保存数据
     * */
    public void baocun(){
        //完全异步保存
        Thread T = new Thread(){
            @Override
            public void run() {
                File F = new File(main.getMain().getDataFolder(),"Price/"+物品.toString());
                YamlConfiguration wenjian = YamlConfiguration.loadConfiguration(F);
                wenjian.set("价格",价格);
                wenjian.set("购买数量",购买数量);
                try {
                    wenjian.save(F);
                } catch (IOException e) {
                    e.printStackTrace();
                    main.getMain().getLogger().warning("数据文件保存失败");
                }
            }
        };
        T.start();
    }
    /**
     * 加载方法,用于加载数据
     * false没有相关数据使用默认值 true加载成功
     * */
    public boolean jiazai(){
        YamlConfiguration wenjian = YamlConfiguration.loadConfiguration(new File(main.getMain().getDataFolder(),"Price/"+物品.toString()));
        if(wenjian.contains("购买数量")){
            购买数量 = wenjian.getInt("购买数量");
        }else {
            购买数量 = 0;
        }
        if(wenjian.contains("价格")){
            价格 = wenjian.getDouble("价格");
            return true;
        }
        价格 = main.getconfig().getDouble("默认价格");
        baocun();
        return false;
    }
    /**
     * boolean 加载 true从配置文件中加载数据 false不加载数据,使用默认值
     * */
    goods(Material 物品,boolean 加载){
        this.物品 = 物品;
        if(加载){
            jiazai();
        } else {
            价格 = main.getconfig().getDouble("默认价格");
            购买数量 = 0;
        }
    }

    public double get价格() {
        return 价格;
    }
    public Material get物品() {
        return 物品;
    }
}
