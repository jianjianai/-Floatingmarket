package cn.jji8.Floatingmarket.gui.goods;

import cn.jji8.Floatingmarket.main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;


/**
 * 负责特殊物品处理
 * */
public class GoodSpecial extends wholegoods implements  goods{
    /**
     * 保存方法,用于保存数据
     */
    @Override
    public void baocun() {
        //完全异步保存
        Thread T = new Thread(){
            @Override
            public void run() {
                wenjian.set("价格",价格);
                wenjian.set("购买数量",购买数量);
                wenjian.set("单独最高价格",单独最高价格);
                wenjian.set("单独最低价格",单独最低价格);
                wenjian.set("物品",物品);
                try {
                    wenjian.save(文件);
                } catch (IOException e) {
                    e.printStackTrace();
                    main.getMain().getLogger().warning("数据文件保存失败");
                }
            }
        };
        T.start();
    }

    /**
     * 删除时调用的方法
     */
    @Override
    public void delete() {
        文件.delete();
    }

    /**
     * 加载方法,用于加载数据
     * false没有相关数据使用默认值 true加载成功
     */
    String 文件名字;
    File 文件;
    YamlConfiguration wenjian;
    @Override
    public void jiazai() {
        wenjian = YamlConfiguration.loadConfiguration(文件 = new File(main.getMain().getDataFolder(),"special/"+getname()));
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
     * 用于获取原商品
     */
    @Override
    public ItemStack getshangping() {
        物品.setAmount(1);
        return 物品;
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
}
