package prisongame.prisongame.lib;

import org.bukkit.ChatColor;

public enum Role {
    PRISONER(ChatColor.GOLD),
    NURSE(ChatColor.LIGHT_PURPLE),
    GUARD(ChatColor.BLUE),
    SWAT(ChatColor.DARK_GRAY),
    WARDEN(ChatColor.RED);

    public ChatColor color;

    Role(ChatColor color) {
        this.color = color;
    }
}
