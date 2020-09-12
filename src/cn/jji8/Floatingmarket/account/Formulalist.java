package cn.jji8.Floatingmarket.account;

import cn.jji8.Floatingmarket.main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于加载表达试，并且调用
 * */
public class Formulalist {
    YamlConfiguration peizhi;
    /**
     * 加载公式list
     * */
    public void setFormulalist() {
        main.getMain().saveResource("formula.yml",false);
        File F = new File(main.getMain().getDataFolder(),"formula.yml");
        setFormulalist(F);
    }
    public void setFormulalist(File F) {
        peizhi = YamlConfiguration.loadConfiguration(F);
    }
    /**
     * 使用默认计算方式计算价格
     * */
    public double calculation(long 数量,Map<String,Double> 变量列表){
        return calculation(数量,变量列表,"默认");
    }
    /**
     * 使用自定义计算方式计算价格
     * */
    public double calculation(long 数量,Map<String,Double> 变量列表,String 计算方式){
        List<Map<?,?>> 公式列表 = peizhi.getMapList(计算方式);
        Map map = getMap(数量,公式列表);
        if(map==null){
            System.out.println("没有找到符合条件的公式，请检查公式条件，已使用价格999");
            return 999;
        }
        return new formula((String) map.get("公式")).calculation(变量列表);
    }
    /**
     * 返回符合条件得map
     * @return null 没有符合的
     * */
    public Map getMap(long 数量,List<Map<?,?>> list){
        for(Map a:list){
            if(数量<=(long)a.get("最高个数")&数量>=(long)a.get("最低个数")){
                return a;
            }
        }
        return null;
    }

}
