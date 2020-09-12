package cn.jji8.Floatingmarket.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个命令补全器,转用于命令补全
 * */
public class completion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length==1){
            ArrayList<String> ArrayList = new ArrayList<String>();
            ArrayList.add("reload");
            ArrayList.add("addspecial");
            ArrayList.add("add");
            ArrayList.add("help");
            ArrayList.add("delete");
            ArrayList.add("set");
            ArrayList.add("exchange");
            ArrayList.add("setservermoney");
            ArrayList.add("setformula");
            return ArrayList;
        }
        return null;
    }
}
