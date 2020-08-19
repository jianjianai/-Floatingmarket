package cn.jji8.Floatingmarket;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * 主要负责操控玩家金钱
 * */
public class money {
    static Economy econ = null;
    /**
     * 插件启动时被调用,用于加载经济
     * */
    public static boolean setupEconomy() {
        if (main.main.getServer().getPluginManager().getPlugin("Vault") == null) {
            System.out.println("没有找到Vault依赖");
            return false;
        }
        RegisteredServiceProvider< Economy > rsp = main.main.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            System.out.println("请安装ess");
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    /**
     * 给指定玩家加钱
     * 加钱成功返回true
     * 加钱失败返回false
     * */
    public static boolean jiaqian(Player P, double qian){
        if(econ==null){
            return false;
        }
        synchronized(P){
            EconomyResponse EconomyResponse = econ.depositPlayer(P,qian);//尝试加钱
            if(EconomyResponse.transactionSuccess()){//判断操作是否成功
                String 价格字符 =  String.valueOf(qian);
                int 小数点位置 = 价格字符.indexOf(".");
                String 价格字符舍;
                if(价格字符.length()>小数点位置+5){
                    价格字符舍 = 价格字符.substring(0,小数点位置+5);
                }else {
                    价格字符舍 = 价格字符;
                }
                if(qian<=0.0001){
                    价格字符舍 = "0.0001";
                }
                P.sendMessage("你赚了："+价格字符舍);
                return true;
            }else {
                P.sendMessage("操作失败");
                return false;
            }
        }
    }
    /**
     * 扣除玩家一定钱
     * 扣除成功返回true
     * 扣除失败或没钱返回false
     * */
    public static boolean kouqian(Player P, double qian){
        if(econ==null){
            P.sendMessage("没有经济前置，无法处理");
            return false;
        }
        synchronized (P){
            if(!econ.has(P,qian)){//检查玩家是否有足够的钱
                P.sendMessage("你没有足够的钱，需要："+qian);
                return false;
            }
            EconomyResponse EconomyResponse = econ.withdrawPlayer(P,qian);//尝试扣钱
            if(EconomyResponse.transactionSuccess()){//判断操作是否成功
                String 价格字符 =  String.valueOf(qian);
                int 小数点位置 = 价格字符.indexOf(".");
                String 价格字符舍;
                if(价格字符.length()>小数点位置+5){
                    价格字符舍 = 价格字符.substring(0,小数点位置+5);
                }else {
                    价格字符舍 = 价格字符;
                }
                if(qian<=0.0001){
                    价格字符舍 = "0.0001";
                }
                P.sendMessage("你花费了："+价格字符舍);
                return true;
            }else {
                P.sendMessage("操作失败");
                return false;
            }
        }
    }
}
