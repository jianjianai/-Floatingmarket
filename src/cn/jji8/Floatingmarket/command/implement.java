package cn.jji8.Floatingmarket.command;

import cn.jji8.Floatingmarket.gui.event;
import cn.jji8.Floatingmarket.gui.goods.goods;
import cn.jji8.Floatingmarket.main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * 一个命令执行器,专用于执行命令
 * */
public class implement implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] 参数) {
        if(参数.length==1){
            if("reload".equals(参数[0])){
                if(!commandSender.hasPermission("Floatingmarket.reload")){
                    commandSender.sendMessage("你没有执行此命令的权限");
                    return true;
                }
                commandSender.sendMessage("开始重新加载");
                main.getMain().reloadConfig();
                for(Player P:org.bukkit.Bukkit.getOnlinePlayers()){
                    P.closeInventory();
                }
                main.getMain().event = new event();
                main.getMain().event.jiazai();
                commandSender.sendMessage("重新加载完成");
                return true;
            }
        }
        //下方命令必须玩家操作
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("操作失败");
            return true;
        }
        Player Player = (Player) commandSender;
        if(参数.length==0){
            main.getMain().event.dakai(Player,1);
            return true;
        }
        if(参数.length>=1){
            if("help".equals(参数[0])){
                if(!commandSender.hasPermission("Floatingmarket.help")){
                    commandSender.sendMessage("你没有执行此命令的权限");
                    return true;
                }
                commandSender.sendMessage("§6-------------------------帮助-----------------------");
                commandSender.sendMessage("§e/Floatingmarket help");
                commandSender.sendMessage("§7//获取帮助");
                commandSender.sendMessage("§e/Floatingmarket addspecial");
                commandSender.sendMessage("§7//添加特殊物品，将手上特殊物品添加到商店中，会保存物品中全部数据");
                commandSender.sendMessage("§e/Floatingmarket add");
                commandSender.sendMessage("§7//添加物品，将手上物品添加到商店中");
                commandSender.sendMessage("§e/Floatingmarket set 物品价格");
                commandSender.sendMessage("§e/Floatingmarket set 物品价格 最低价格 最高价格 ");
                commandSender.sendMessage("§7//设置手上物品商品价格");
                commandSender.sendMessage("§e/Floatingmarket delete");
                commandSender.sendMessage("§7//删除手上物品商品");
                commandSender.sendMessage("§e/Floatingmarket reload");
                commandSender.sendMessage("§7//重新加载插件配置文件");
                commandSender.sendMessage("§6---------------------------------------------------");
                return true;
            }
            if("addspecial".equals(参数[0])){
                if(!commandSender.hasPermission("Floatingmarket.addspecial")){
                    commandSender.sendMessage("你没有执行此命令的权限");
                    return true;
                }
                addspecial(Player);
                return true;
            }
            if("add".equals(参数[0])){
                if(!commandSender.hasPermission("Floatingmarket.add")){
                    commandSender.sendMessage("你没有执行此命令的权限");
                    return true;
                }
                add(Player);
                return true;
            }
            if("set".equals(参数[0])){
                if(!commandSender.hasPermission("Floatingmarket.set")){
                    commandSender.sendMessage("你没有执行此命令的权限");
                    return true;
                }
                if(参数.length!=4&参数.length!=2){
                    commandSender.sendMessage("set 物品价格");
                    commandSender.sendMessage("set 物品价格 最高价格 最低价格");
                    return true;
                }
                if(参数.length==4){
                    try {
                        tianjia(Player,Double.valueOf(参数[1]),Double.valueOf(参数[2]),Double.valueOf(参数[3]));
                    }catch (NumberFormatException a){
                        commandSender.sendMessage("你输入的数字不是一个有效数字");
                    }
                    return true;
                }
                if(参数.length==2){
                    try {
                        tianjia(Player,Double.valueOf(参数[1]));
                    }catch (NumberFormatException a){
                        commandSender.sendMessage("你输入的数字不是一个有效数字");
                    }
                    return true;
                }

            }
            if("delete".equals(参数[0])){
                if(!commandSender.hasPermission("Floatingmarket.delete")){
                    commandSender.sendMessage("你没有执行此命令的权限");
                    return true;
                }
                delete(Player);
                return true;
            }
        }
        commandSender.sendMessage("命令参数错误");
        return true;
    }
    /**
     * 添加方法
     * 添加玩家手上的物品
     * */
    public static void addspecial(Player 命令执行者){
        ItemStack ItemStack = 命令执行者.getInventory().getItemInMainHand();
        if(ItemStack.getType().isAir()){
            命令执行者.sendMessage("你不可以空手");
            return;
        }
        goods goods = main.getMain().event.shousuo(ItemStack);
        if(goods!=null){
            String 价格字符 =  String.valueOf(goods.getjiage());
            int 小数点位置 = 价格字符.indexOf(".");
            String 价格字符舍;
            if(价格字符.length()>小数点位置+5){
                价格字符舍 = 价格字符.substring(0,小数点位置+5);
            }else {
                价格字符舍 = 价格字符;
            }
            if(goods.getjiage()<=0.0001){
                价格字符舍 = "0.0001";
            }
            命令执行者.sendMessage("本商品已经在买了，现在价格是："+价格字符舍);
            return;
        }
        main.getMain().event.add(ItemStack);
        goods = main.getMain().event.shousuo(ItemStack);
        if(goods!=null){
            goods.baocun();
        }
        命令执行者.sendMessage("添加成功");
    }
    /**
     * 添加方法，添加指定物品
     * */
    public static void add(Player 命令执行者){
        Material Material = 命令执行者.getInventory().getItemInMainHand().getType();
        if(Material.isAir()){
            命令执行者.sendMessage("你不可以空手");
            return;
        }
        goods goods = main.getMain().event.shousuo(Material);
        if(goods!=null){
            String 价格字符 =  String.valueOf(goods.getjiage());
            int 小数点位置 = 价格字符.indexOf(".");
            String 价格字符舍;
            if(价格字符.length()>小数点位置+5){
                价格字符舍 = 价格字符.substring(0,小数点位置+5);
            }else {
                价格字符舍 = 价格字符;
            }
            if(goods.getjiage()<=0.0001){
                价格字符舍 = "0.0001";
            }
            命令执行者.sendMessage("本商品已经在买了，现在价格是："+价格字符舍);
            return;
        }
        main.getMain().event.add(Material);
        goods = main.getMain().event.shousuo(Material);
        if(goods!=null){
            goods.baocun();
        }
        命令执行者.sendMessage("添加成功");
    }
    /**
     * 修改方法，修改物品的价格
     * */
    public static void tianjia(Player Player, double 价格, double 最低价格, double 最高价格){
        ItemStack 物品堆 = Player.getInventory().getItemInMainHand();
        if(物品堆.getType().isAir()){
            Player.sendMessage("你不可以空手");
            return;
        }
        goods goods = main.getMain().event.shousuo(物品堆);
        if(goods==null){
            Player.sendMessage("此商品没有被上架");
            return;
        }
        goods.setjiage(价格);
        goods.setdijiage(最低价格);
        goods.setgaojiage(最高价格);
        main.getMain().event.shuaxin();
        Player.sendMessage("设置成功");
    }
    /**
     * 修改方法，修改物品的价格
     * */
    public static void tianjia(Player Player, double 价格){
        ItemStack 物品堆 = Player.getInventory().getItemInMainHand();
        if(物品堆.getType().isAir()){
            Player.sendMessage("你不可以空手");
            return;
        }
        goods goods = main.getMain().event.shousuo(物品堆);
        if(goods==null){
            Player.sendMessage("此商品没有被上架");
            return;
        }
        goods.setjiage(价格);
        main.getMain().event.shuaxin();
        Player.sendMessage("设置成功");
    }
    /**
     * 删除方法，用于删除商品
     * 删除成功返回true 失败返回false
     * */
    public static boolean delete(Player Player){
        ItemStack 物品堆 = Player.getInventory().getItemInMainHand();
        if(物品堆.getType().isAir()){
            Player.sendMessage("你不可以空手");
            return false;
        }
        goods goods = main.getMain().event.shousuo(物品堆);
        if(goods==null){
            Player.sendMessage("此商品没有被上架");
            return false;
        }
        if(main.getMain().event.delete(goods.getname())){
            Player.sendMessage("删除成功");
            main.getMain().event.baocun();
            for(Player P:org.bukkit.Bukkit.getOnlinePlayers()){
                P.closeInventory();
            }
            main.getMain().event = new event();
            main.getMain().event.jiazai();
            return true;
        }else {
            Player.sendMessage("错误？");
            return false;
        }

    }
}
