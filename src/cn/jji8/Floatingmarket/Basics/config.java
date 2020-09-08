package cn.jji8.Floatingmarket.Basics;

import cn.jji8.Floatingmarket.main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * 简化配置文件的读取，和修复
 * 可以使用默认参数获取配置，配置文件中没有指定参数，会使用默认参数并且保存
 * */
//刚写完，其他类还没有使用
public class config {
    YamlConfiguration configfile;
    File file;
    /**
     * 通过文件路径构造一个配置
     * */
    config(File 配置路径){
        configfile = YamlConfiguration.loadConfiguration(配置路径);
        this.file = 配置路径;
    }
    public int getInt(String key,int 默认参数){
        if(configfile.contains(key)){
           return configfile.getInt(key);
        }
        configfile.set(key,默认参数);
        return 默认参数;
    }
    public String getString(String key,String 默认参数){
        if(configfile.contains(key)){
            return configfile.getString(key);
        }
        configfile.set(key,默认参数);
        return 默认参数;
    }
    public double getDouble(String key,double 默认参数){
        if(configfile.contains(key)){
            return configfile.getDouble(key);
        }
        configfile.set(key,默认参数);
        return 默认参数;
    }
    /**
     * 异步保存配置
     * */
    public void Threadserve(){
        Thread T = new Thread(()->{
                serve();
        });
        T.setName("配置保存线程");
        T.start();
    }
    /**
     * 线程安全的保存
     * */
    public synchronized void serve(){
        try {
            configfile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            main.getMain().getLogger().warning("配置文件保存失败！");
        }
    }
    /**
     * 重新加载配置
     * */
    public void reload(){
        configfile = YamlConfiguration.loadConfiguration(file);
    }
}
