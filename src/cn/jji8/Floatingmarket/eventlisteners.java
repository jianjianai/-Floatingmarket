package cn.jji8.Floatingmarket;

import cn.jji8.Floatingmarket.gui.event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class eventlisteners implements Listener {
    @EventHandler
    public void wanjia(InventoryClickEvent a) {//玩家点击物品栏格子时触发
        main.getMain().event.dianji(a);
    }
}
