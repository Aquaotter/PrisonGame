package prisongame.prisongame.features;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Keys;

import java.text.DecimalFormat;

/**
 * Handles various aspects such as the warden timer, invulnerability, cooldowns, etc.
 */
public class Warden implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        var whole = new DecimalFormat("#");

        if (PrisonGame.warden == null) {
            String wardentime = ChatColor.RED + "None! Use '/warden' to become the prison warden!";

            if (PrisonGame.wardenCooldown > 0) {
                wardentime = ChatColor.RED + "None! Warden command is on cooldown! " + net.md_5.bungee.api.ChatColor.GRAY + "[" + Math.round((float) PrisonGame.wardenCooldown / 20f) + "]";
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.getDisplayName().contains("ASCENDING")) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + PrisonGame.formatBalance(Keys.MONEY.get(p, 0.0)) + "$" + ChatColor.GRAY + " || " + ChatColor.GRAY + "Current Warden: " + wardentime));
                } else {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent((ChatColor.AQUA + whole.format(Keys.ASCENSION_COINS.get(p, 0)) + " ascension coins.")));
                }
            }
        } else if (!PrisonGame.warden.isOnline())
            PrisonGame.warden = null;

        for (var player : Bukkit.getOnlinePlayers()) {
            // Reset the player's warden timer if they aren't the warden.
            // todo(performance): Don't use a hash map for warden timer. There's only one warden. Just have a global variable.
            if (!PrisonGame.wardentime.containsKey(player) || player != PrisonGame.warden)
                PrisonGame.wardentime.put(player, 0);

            // Increase the player's warden timer if they are a warden.
            if (PrisonGame.warden == player) {
                var time = PrisonGame.wardentime.get(player) + 1;
                PrisonGame.wardentime.put(player, time);
            }
        }

        PrisonGame.swapcool -= 1;
        PrisonGame.lockdowncool -= 1;
        PrisonGame.wardenCooldown -= 1;
    }
}
