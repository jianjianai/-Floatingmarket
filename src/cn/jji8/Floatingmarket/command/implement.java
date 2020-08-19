package cn.jji8.Floatingmarket.command;

import cn.jji8.Floatingmarket.gui.event;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 一个命令执行器,专用于执行命令
 * */
public class implement implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        event.dakai((Player) commandSender,1);
        return false;
    }
}
