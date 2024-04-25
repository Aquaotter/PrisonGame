package prisongame.prisongame.commands.warden;

import me.coralise.spigot.API.CBPAPI;
import me.coralise.spigot.CustomBansPlus;
import me.coralise.spigot.players.CBPlayer;
import me.coralise.spigot.players.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.FilteredWords;
import prisongame.prisongame.MyListener;
import prisongame.prisongame.PrisonGame;

import java.time.Instant;

public class IntercomCommand implements CommandExecutor {
    public static Instant lastUse = null;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>You aren't a player."));
            return true;
        }

        var api = CBPAPI.getApi();

        if (api != null) {
            var cbp = CustomBansPlus.getInstance();
            var playerManager = cbp.plm;
            var cbpPlayer = playerManager.getCBPlayer(player.getUniqueId());

            if (api.isPlayerMuted(cbpPlayer)) {
                player.sendMessage(ChatColor.RED + "You cannot use the intercom while you are muted.");
                return true;
            }
        }

        var now = Instant.now();

        if (lastUse != null) {
            var lastSec = lastUse.getEpochSecond();
            var nowSec = now.getEpochSecond();
            var diff = nowSec - lastSec;

            if (diff < 5) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Please wait " + (5 - diff) + " seconds before using the intercom again."));
                return true;
            }
        }

        StringBuilder b = new StringBuilder(ChatColor.BLUE + "INTERCOM >> " + ChatColor.RED);
        for (String a : args) {
            if (a.toLowerCase().contains("&k")) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>You cannot use obfuscation."));
                return true;
            }

            b.append(a + " ");
        }
        var filter = FilteredWords.isClean(b.toString());
        if (filter != null) {
            FilteredWords.alert(player, b.toString().substring(16), filter.getFirst(), "intercom");
            return true;
        }

        lastUse = now;

        String be = ChatColor.translateAlternateColorCodes('&', b.toString());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p, Sound.ENTITY_BEE_LOOP_AGGRESSIVE, 1, 0.75f);
            p.sendTitle(ChatColor.BLUE + "" ,be, 20, 40, 20);
        }
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(be);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("");
        return true;
    }
}