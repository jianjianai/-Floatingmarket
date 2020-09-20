package cn.jji8.Floatingmarket.account;

import cn.jji8.Floatingmarket.main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * 服务器的经济账户
 * */
public class server {
    double 余额;
    File 路径;
    YamlConfiguration wenjian;
    public server(){
        路径 = new File(main.getMain().getDataFolder(),"ServerAccount");
        wenjian = YamlConfiguration.loadConfiguration(路径);
        余额 = wenjian.getDouble("余额");
    }
    /**
     * 设置服务器余额
     * */
    public void set余额(double 余额) {
        this.余额 = 余额;
    }
    /**
     * 在服务器账户上扣钱，成功返回true，没钱返回false
     * */
    public boolean reduce(double 钱){
        if(余额<钱){
            return false;
        }
        余额 -= 钱;
        baocun(10);
        return true;
    }
    /**
     * 加钱
     * @return 成功或失败
     * @param 钱 加的钱数
     * */
    public boolean increase(double 钱){
        余额 += 钱;
        baocun(10);
        return true;
    }
    /**
     * 保存数据，但不会频繁重复保存
     * 在指定时间多次调用此方法，前面的调用无效
     * 时间/秒；
     * */
    long 执行时间 = -1;
    public void baocun(int 时间秒){
        if(执行时间==-1){
            Thread T = new Thread(() -> {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(System.currentTimeMillis()>执行时间){
                        baocun();
                        执行时间 = -1;
                        return;
                    }
                }
            });
            执行时间 = System.currentTimeMillis()+时间秒*1000;
            T.start();
        }else {
            执行时间 = System.currentTimeMillis()+时间秒*1000;
        }
    }
    /**
     * 保存数据
     * */
    public void baocun(){
        wenjian.set("余额",余额);
        try {
            wenjian.save(路径);
        } catch (IOException e) {
            e.printStackTrace();
            main.getMain().getLogger().warning("服务器经济保存失败");
        }
    }

    public double getmoney() {
        return 余额;
    }
}
