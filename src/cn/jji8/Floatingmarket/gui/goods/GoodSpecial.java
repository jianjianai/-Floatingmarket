package cn.jji8.Floatingmarket.gui.goods;

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
import java.util.List;

/**
 * 负责特殊物品处理
 * */
public class GoodSpecial implements goods{
    /**
     * 获取用于显示的物品堆
     */
    @Override
    public ItemStack getxianshiwupin() {
        ItemStack ItemStack;
        if(物品==null){
            ItemStack = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        }else {
            ItemStack = new ItemStack(物品);
        }
        物品.setAmount(1);
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
        ItemMeta ItemMeta = ItemStack.getItemMeta();
        List<String> ArrayList = null;
        if(ItemMeta!=null){
            ArrayList = ItemMeta.getLore();
        }
        if(ArrayList==null){
            ArrayList = new ArrayList();
        }
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
     *
     * @param P
     * @param 数量
     */
    @Override
    public void goumai(Player P, int 数量) {
        if(!money.kouqian(P,价格*数量)){
            return;
        }
        购买数量 += 数量;
        for(;数量>0;数量--){
            HashMap HashMap = P.getInventory().addItem(物品);
            for(Object a:HashMap.values()){
                if(a instanceof ItemStack){
                    money.jiaqian(P,数量*价格);
                    购买数量 -= 数量;
                    P.sendMessage("背包已满，多购部分已退回");
                }
            }
        }
        baocun(10);
        tiaojiasuaxing();
    }

    /**
     * 调用此方法代表玩家出售此物品
     *
     * @param P
     * @param 数量
     */
    @Override
    public void chushou(Player P, int 数量) {
        if(!kouwupin(P,数量,物品)){
            P.sendMessage("你没有足够的物品");
            return;
        }
        if(!money.jiaqian(P,价格*数量)){
            P.sendMessage("操作失败");
            for(int i = 0;i<数量;i++){
                P.getInventory().addItem(物品);
            }
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
     * 调价刷新
     * */
    public void tiaojiasuaxing(){
        tiaojia();
        main.getMain().event.shuaxin();
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
     */
    @Override
    public void baocun() {
        //完全异步保存
        Thread T = new Thread(){
            @Override
            public void run() {
                File F = new File(main.getMain().getDataFolder(),"special/"+文件名字);
                YamlConfiguration wenjian = YamlConfiguration.loadConfiguration(F);
                wenjian.set("价格",价格);
                wenjian.set("购买数量",购买数量);
                wenjian.set("单独最高价格",单独最高价格);
                wenjian.set("单独最低价格",单独最低价格);
                wenjian.set("物品",物品);
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
     */
    String 文件名字;
    @Override
    public void jiazai() {
        YamlConfiguration wenjian = YamlConfiguration.loadConfiguration(new File(main.getMain().getDataFolder(),"special/"+文件名字));
        if(wenjian.contains("购买数量")){
            购买数量 = wenjian.getLong("购买数量");
        }else {
            购买数量 = 0;
        }
        if(wenjian.contains("价格")){
            价格 = wenjian.getDouble("价格");
        }else {
            价格 = main.getMain().getConfig().getDouble("默认价格");
        }
        if(wenjian.contains("单独最高价格")){
            单独最高价格 = wenjian.getDouble("单独最高价格");
        }
        if(wenjian.contains("单独最低价格")){
            单独最低价格 = wenjian.getDouble("单独最低价格");
        }
        if(wenjian.contains("物品")){
            物品 = wenjian.getItemStack("物品");
        }
        baocun();
    }


    /**
     * 调价
     * 用于调整物品价格
     * */
    public ItemStack 物品;
    public double 价格;
    long 购买数量 = 0;
    double 最高价格 = main.getMain().getConfig().getDouble("全局最高价格");
    double 最低价格 = main.getMain().getConfig().getDouble("全局最低价格");
    double 涨跌幅度 = main.getMain().getConfig().getDouble("涨跌价格");
    double 涨跌指数 = main.getMain().getConfig().getDouble("涨跌指数");

    public double 单独最高价格 = -1;
    public double 单独最低价格 = -1;
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
     * 用于获取原商品
     */
    @Override
    public ItemStack getshangping() {
        物品.setAmount(1);
        return 物品;
    }
    /**
     * 用于设置物品价格
     *
     * @param 价格
     */
    @Override
    public void setjiage(double 价格) {
        this.价格 = 价格;
    }

    /**
     * 用于设置物品最高价格
     *
     * @param 价格
     */
    @Override
    public void setgaojiage(double 价格) {
        this.单独最高价格 = 价格;
    }

    /**
     * 用于设置物品最低价格
     *
     * @param 价格
     */
    @Override
    public void setdijiage(double 价格) {
        this.单独最低价格 = 价格;
    }

    /**
     * 获取物品的名字
     */
    @Override
    public String getname() {
        return 文件名字;
    }

    /**
     * 构造方法一个
     * ItemStack可以null
     * */
    public GoodSpecial(String 文件名字,ItemStack a){
        this.文件名字 = 文件名字;
        jiazai();
        if(a==null){
            return;
        }
        物品=a;
        baocun();
    }
    /**
     * 用于获取物品价格
     */
    @Override
    public double getjiage() {
        return 价格;
    }
}
