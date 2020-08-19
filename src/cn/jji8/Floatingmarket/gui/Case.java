package cn.jji8.Floatingmarket.gui;

import cn.jji8.Floatingmarket.main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * 箱子类,主要负责箱子界面的处理
 * */
public class Case{
    public int 页数;
    goods 物品[] = new goods[45];
    Inventory 箱子;
    Case(int 页数){
        this.页数 = 页数;
        箱子 = org.bukkit.Bukkit.createInventory(null,6*9, main.getconfig().getString("箱子标题").replaceAll("%页数%",String.valueOf(页数)));
    }
    /**
     * 添加，添加箱子中的物品
     * true 添加成功
     * false 满了，或添加失败
     * */
    public boolean tianjia(Material 商品){
        int 空位 = kongw();
        if(空位==-1){
            return false;
        }
        物品[空位] = new goods(商品,true);
        return true;
    }
    /**
     * 返回一个 物品 数组空位，没有返回-1
     * */
    int kongw(){
        for(int i=0;i<物品.length;i++){
            if(物品[i]==null){
                return i;
            }
        }
        return -1;
    }
    /**
     * 用于加载或刷新商店页面
     * */
    public void shuaxin(){
        ItemStack ItemStack = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta ItemMeta = ItemStack.getItemMeta();
        ItemMeta.setDisplayName("§7§l此处无商品");
        ItemStack.setItemMeta(ItemMeta);
        for(int i=0;i<54;i++){
            箱子.setItem(i,ItemStack);
        }
        for(int i=0;i<物品.length;i++){
            if(物品[i]!=null){
                箱子.setItem(i,物品[i].getxianshiwupin());
            }
        }
        //上一页物品
        ItemStack = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta = ItemStack.getItemMeta();
        ItemMeta.setDisplayName("§a§l上一页");
        ItemStack.setItemMeta(ItemMeta);
        箱子.setItem(45,ItemStack);
        //下一页物品
        ItemStack = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta = ItemStack.getItemMeta();
        ItemMeta.setDisplayName("§a§l下一页");
        ItemStack.setItemMeta(ItemMeta);
        箱子.setItem(53,ItemStack);
    }
    /**
     * 玩家点击格子时触发
     * */
    public void dianji(InventoryClickEvent a){
        //判断点击的是不是gui
        if(!箱子.equals(a.getClickedInventory())){
            return;
        }
        a.setCancelled(true);
        int 格子 = a.getRawSlot();
        //判断点击的是不是需要的格子
        if(格子>=45|格子<0){
            return;
        }
        //判断点击的格子是不是没有物品
        if(物品[格子]==null){
            return;
        }
        if(ClickType.SHIFT_LEFT.equals(a.getClick())){//Shift+鼠标左键. 购买一组
            物品[格子].goumai((Player) a.getWhoClicked(),64);
        }else if(ClickType.SHIFT_RIGHT.equals(a.getClick())){//Shift+鼠标右键. 出售一组
            物品[格子].chushou((Player) a.getWhoClicked(),64);
        }else if(ClickType.LEFT.equals(a.getClick())){//鼠标左键. 购买一
            物品[格子].goumai((Player) a.getWhoClicked(),1);
        }else if(ClickType.RIGHT.equals(a.getClick())){//鼠标右键. 出售一
            物品[格子].chushou((Player) a.getWhoClicked(),1);
        }
    }
    /**
     * 用于给玩家打开gui
     * */
    public void dakai(Player a){
        a.openInventory(箱子);
    }
    /**
     * 用于添加物品
     * true成功 fales满了
     * */
    public boolean add(Material 物品){
        for(int i=0;i<this.物品.length;i++){
            if(this.物品[i]==null){
                this.物品[i]=new goods(物品,true);
                return true;
            }
        }
        return false;
    }
}
