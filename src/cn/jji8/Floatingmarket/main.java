package cn.jji8.Floatingmarket;

import cn.jji8.Floatingmarket.Basics.config;
import cn.jji8.Floatingmarket.account.Formulalist;
import cn.jji8.Floatingmarket.account.function;
import cn.jji8.Floatingmarket.account.money;
import cn.jji8.Floatingmarket.account.server;
import cn.jji8.Floatingmarket.command.completion;
import cn.jji8.Floatingmarket.command.implement;
import cn.jji8.Floatingmarket.gui.event;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 一个主类
 * */
public class main extends JavaPlugin {
    static main main;
    public event event;
    Metrics Metrics;
    server servermoney;
    function function;
    public cn.jji8.Floatingmarket.account.function getFunction() {
        return function;
    }

    /**
     * 插件启动时会被调用
     * */
    @Override
    public void onEnable() {
        Metrics = new Metrics(this,8676);
        main = this;
        event = new event();
        servermoney = new server();
        getLogger().info("开始加载...");
        money.setupEconomy();//加载经济
        saveDefaultConfig();
        saveResource("commodity.yml",false);
        saveResource("function.js",false);
        Bukkit.getPluginCommand("Floatingmarket").setExecutor(new implement());
        Bukkit.getPluginCommand("Floatingmarket").setTabCompleter(new completion());
        Bukkit.getPluginManager().registerEvents(new eventlisteners(),this);
        function = new function(new File(getDataFolder(),"function.js"));
        event.jiazai();
    }

    public cn.jji8.Floatingmarket.account.server getServermoney() {
        return servermoney;
    }
    /**
     * 获取插件main
     * */
    public static cn.jji8.Floatingmarket.main getMain() {
        return main;
    }
    /**
     * 重新加载全部插件配置
     * */
    public void reload(){
        function = new function(new File(getDataFolder(),"function.js"));
        money.setupEconomy();
        servermoney = new server();
        main.getMain().event = new event();
        main.getMain().event.jiazai();
    }
}
