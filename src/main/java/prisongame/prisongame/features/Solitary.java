package prisongame.prisongame.features;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.MyListener;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.lib.Role;

import java.text.DecimalFormat;

/**
 * Handles the solitary timer.
 */
public class Solitary implements Feature {
    @Override
    public void schedule() {
        scheduler.scheduleSyncRepeatingTask(plugin, this::execute, 0L, 1L);
    }

    @Override
    public void execute() {
        var numberFormat = new DecimalFormat("#0.0");
        var whole = new DecimalFormat("#");

        PrisonGame.solitcooldown--;

        for (var player : Bukkit.getOnlinePlayers()) {
            if (!PrisonGame.solittime.containsKey(player))
                PrisonGame.solittime.put(player, 0);

            PrisonGame.solittime.put(player, PrisonGame.solittime.get(player) - 1);

            if (PrisonGame.solittime.get(player) <= 0 && player.getDisplayName().contains("SOLITARY")) {
                PrisonGame.tptoBed(player);
                MyListener.playerJoin(player, false);
                player.removePotionEffect(PotionEffectType.WATER_BREATHING);
                player.sendMessage( "You were released from solitary.");
            }

            if (!player.getGameMode().equals(GameMode.SPECTATOR) && PrisonGame.warden != null) {
                if (player != PrisonGame.warden) {
                    Role role = PrisonGame.roles.get(player);
                    String acbar = "";
                    if (player.getDisplayName().contains("SOLITARY"))
                        acbar = " ||" + ChatColor.DARK_GRAY + " SOLITARY [ " + numberFormat.format(PrisonGame.solittime.get(player) / 20) + " seconds left. ]";
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + numberFormat.format(Keys.MONEY.get(player, 0.0)) + "$" + ChatColor.GRAY + acbar + ChatColor.GRAY + " || Current Warden: " + ChatColor.DARK_RED + PrisonGame.warden.getName()));
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + numberFormat.format(Keys.MONEY.get(player, 0.0)) + "$" + ChatColor.GRAY + " || " + ChatColor.GRAY + "Current Warden: " + ChatColor.GREEN + "You! Use \"/warden help\"!"));
                }
            }
        }
    }
}
