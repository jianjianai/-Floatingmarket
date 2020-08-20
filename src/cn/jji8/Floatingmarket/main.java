package cn.jji8.Floatingmarket;

import cn.jji8.Floatingmarket.command.completion;
import cn.jji8.Floatingmarket.command.implement;
import cn.jji8.Floatingmarket.gui.event;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * 一个主类
 * */
public class main extends JavaPlugin {
    static main main;
    public static FileConfiguration Config;
    public static FileConfiguration Configcommodity;
    /**
     * 插件启动时会被调用
     * */
    @Override
    public void onEnable() {
        main = this;
        getLogger().info("开始加载...");
        money.setupEconomy();//加载经济
        saveDefaultConfig();
        Config = getConfig();
        saveResource("commodity.yml",false);
        Configcommodity = YamlConfiguration.loadConfiguration(new File(main.getMain().getDataFolder(),"commodity.yml"));
        Bukkit.getPluginCommand("Floatingmarket").setExecutor(new implement());
        Bukkit.getPluginCommand("Floatingmarket").setTabCompleter(new completion());
        Bukkit.getPluginManager().registerEvents(new eventlisteners(),this);
        event.jiazai();

    }
    /**
     * 获取插件main
     * */
    public static cn.jji8.Floatingmarket.main getMain() {
        return main;
    }
    /**
     * 获取插件配置
     * */
    public static FileConfiguration getconfig() {
        return Config;
    }
}
