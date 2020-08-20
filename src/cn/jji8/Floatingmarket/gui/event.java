package cn.jji8.Floatingmarket.gui;

import cn.jji8.Floatingmarket.main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件,主要负责事件的处理
 * */
public class event{
    static ArrayList<Case> biao;
    /**
     * 用于刷新全部物品
     * */
    public static void shuaxin(){
        for(Case a:biao){
            a.shuaxin();
        }
    }
    /**
     * 玩家点击时被调用
     * */
    public static void dianji(InventoryClickEvent a){
        int 点击页数 = ifdianjiyemian(a.getClickedInventory());
        if(点击页数==-1){
            return;
        }
        if(a.getRawSlot()==45){
            dakai((Player) a.getWhoClicked(),点击页数);
        }else if(a.getRawSlot()==53){
            dakai((Player) a.getWhoClicked(), 点击页数 + 2);
        }
        biao.get(点击页数).dianji(a);
    }
    /**
     * 判断玩家点击的哪一页
     * 没有点击bug返回-1
     * */
    public static int ifdianjiyemian(Inventory 点击界面){
        for(int i=0;i<biao.size();i++){
            if(biao.get(i).箱子.equals(点击界面)){
                return i;
            }
        }
        return -1;
    }
    /**
     * 用于给玩家打开指定gui
     * */
    public static void dakai(Player 玩家,int 页数){
        if(biao.size()<页数|页数<1){
            玩家.sendMessage("没有第"+页数+"页哦！");
            return;
        }
        页数--;
        biao.get(页数).dakai(玩家);
    }
    /**
     * 加载方法，用于加载全部商品
     * */
    public static void jiazai(){
        biao = new ArrayList<Case>();
        List<String> 商品列表 = main.Configcommodity.getStringList("商品");
        for(String 商品:商品列表){
            Material material = Material.getMaterial(商品.toUpperCase().replaceAll(" ","_"));
            if(material==null){
                material = Material.LIGHT_GRAY_STAINED_GLASS_PANE;
            }
            tianjia(material);
        }
        for(Case i:biao){
            i.shuaxin();
        }
    }
    /**
     * 用于添加商品
     * */
    static int 页数 = 1;
    static void tianjia(Material 商品){
        if(biao.size()==0){
            biao.add(new Case(页数));
            页数++;
        }
        while (true){
            if(!biaotianjia(商品)){
                biao.add(new Case(页数));
                页数++;
            }else {
                return;
            }
        }
    }
    /**
     * 向现有case中添加商品
     * 添加成功返回true，已满返回false
     * */
    static boolean biaotianjia(Material 添加商品){
        for(Case 商品:biao){
            if(商品.add(添加商品)){
                return true;
            }
        }
        return false;
    }
}
