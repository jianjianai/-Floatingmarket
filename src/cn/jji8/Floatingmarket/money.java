package cn.jji8.Floatingmarket;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;

/**
 * 主要负责操控玩家金钱
 * */
public class money {
    static Economy econ = null;
    static String 赚钱消息;
    static String 花钱消息;
    static String 没钱消息;
    static int 保留小数位数;
    static double 个人所得税;
    /**
     * 插件启动时被调用,用于加载经济
     * */
    public static boolean setupEconomy() {
        main.getMain().saveResource("money.yml",false);
        File F = new File(main.getMain().getDataFolder(),"money.yml");
        YamlConfiguration peizhi = YamlConfiguration.loadConfiguration(F);
        赚钱消息 = peizhi.getString("赚钱消息");
        花钱消息 = peizhi.getString("花钱消息");
        没钱消息 = peizhi.getString("没钱消息");
        保留小数位数 = peizhi.getInt("保留小数位数");
        个人所得税 = peizhi.getDouble("个人所得税");
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
        return jiaqian(P, qian,true,true);
    }
    public static boolean jiaqian(Player P, double qian,boolean 显示消息,boolean 是否扣税){
        if(econ==null){
            P.sendMessage("没有经济前置，无法处理");
            return false;
        }
        double 扣税 = 0;
        double 剩余 = qian;
        if(个人所得税<=0){
            是否扣税 = false;
        }
        if(是否扣税){
            扣税 = qian*个人所得税;
            剩余 = qian-扣税;
        }
        if(econ==null){
            return false;
        }
        EconomyResponse EconomyResponse = econ.depositPlayer(P,剩余);//尝试加钱
        if(EconomyResponse.transactionSuccess()){//判断操作是否成功
            String 价格字符舍 = XianShiZiFu(qian);
            String 扣税字符舍 = "无扣税";
            if(是否扣税){
                扣税字符舍 = XianShiZiFu(扣税);
            }
            String 剩余字符舍 = XianShiZiFu(剩余);
            if(显示消息){
                P.sendMessage(赚钱消息.replaceAll("%钱%",价格字符舍).replaceAll("%税%",扣税字符舍).replaceAll("%剩余%",剩余字符舍));
            }
            return true;
        }else {
            P.sendMessage("操作失败");
            return false;
        }
    }
    /**
     * 扣除玩家一定钱
     * 扣除成功返回true
     * 扣除失败或没钱返回false
     * */
    public static boolean kouqian(Player P, double qian){
        return kouqian(P,qian,true);
    }
    public static boolean kouqian(Player P, double qian,boolean 显示消息){
        if(econ==null){
            P.sendMessage("没有经济前置，无法处理");
            return false;
        }
        String 价格字符舍 = XianShiZiFu(qian);
        if(!econ.has(P,qian)){//检查玩家是否有足够的钱
            if(显示消息){
                P.sendMessage(没钱消息.replaceAll("%钱%",价格字符舍));
            }
            return false;
        }
        EconomyResponse EconomyResponse = econ.withdrawPlayer(P,qian);//尝试扣钱
        if(EconomyResponse.transactionSuccess()){//判断操作是否成功
            if(显示消息){
                P.sendMessage(花钱消息.replaceAll("%钱%",价格字符舍));
            }
            return true;
        }else {
            P.sendMessage("操作失败");
            return false;
        }
    }
    /**
     * 用来处理保留小数的显示字符串
     * */
    public static String XianShiZiFu(double qian){
        String 价格字符 =  String.valueOf(qian);
        int 小数点位置 = 价格字符.indexOf(".");
        String 价格字符舍;
        if(价格字符.length()>小数点位置+保留小数位数+1){
            价格字符舍 = 价格字符.substring(0,小数点位置+保留小数位数+1);
        }else {
            价格字符舍 = 价格字符;
        }
        double 小数 = 1;
        for(int i = 0;i<保留小数位数;i++){
            小数 /= 10;
        }
        if(qian<=小数){
            StringBuffer  S = new StringBuffer("0.");
            for(int i = 1;i<保留小数位数;i++){
                S.append("0");
            }
            S.append("1");
            价格字符舍 = S.toString();
        }
        return 价格字符舍;
    }
}
