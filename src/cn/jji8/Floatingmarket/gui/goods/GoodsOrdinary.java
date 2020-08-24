package cn.jji8.Floatingmarket.gui.goods;

import cn.jji8.Floatingmarket.main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

/**
 * 主要负责普通商品的处理
 * */
public class GoodsOrdinary extends wholegoods implements  goods{
    /**
     * 保存方法,用于保存数据
     * */
    public void baocun(){
        //完全异步保存
        Thread T = new Thread(){
            @Override
            public void run() {
                File F = new File(main.getMain().getDataFolder(),"Price/"+getname());
                YamlConfiguration wenjian = YamlConfiguration.loadConfiguration(F);
                wenjian.set("价格",价格);
                wenjian.set("购买数量",购买数量);
                wenjian.set("单独最高价格",单独最高价格);
                wenjian.set("单独最低价格",单独最低价格);
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
    public void jiazai(){
        YamlConfiguration wenjian;
        wenjian = YamlConfiguration.loadConfiguration(new File(main.getMain().getDataFolder(),"Price/"+getname()));
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
        baocun();
    }

    /**
     * 用于获取原商品
     */
    @Override
    public ItemStack getshangping() {
        if(物品==null){
            return null;
        }
        return new ItemStack(物品);
    }
    /**
     * 获取物品的名字
     */
    @Override
    public String getname() {
        if(物品==null){
            return "错误";
        }
        return 物品.getType().toString();
    }

    /**
     * boolean 加载 true从配置文件中加载数据 false不加载数据,使用默认值
     * */
    public GoodsOrdinary(ItemStack 物品){
        this.物品 = 物品;
        价格 = main.getMain().getConfig().getDouble("默认价格");
        购买数量 = 0;
    }
    public ItemStack get物品() {
        return 物品;
    }
}
