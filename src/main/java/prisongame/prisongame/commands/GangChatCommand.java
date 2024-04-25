package prisongame.prisongame.commands;

import me.coralise.spigot.API.CBPAPI;
import me.coralise.spigot.CustomBansPlus;
import me.coralise.spigot.players.CBPlayer;
import me.coralise.spigot.players.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.FilteredWords;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.config.filter.FilterAction;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.gangs.Gangs;
import prisongame.prisongame.keys.Keys;

import java.sql.SQLException;

public class GangChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>You aren't a player."));
            return true;
        }

        if (Keys.GANG_ROLE.get(player, 0) <= 0) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>You aren't in a gang."));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a message."));
            return true;
        }

        CBPAPI api = CBPAPI.getApi();

        if (api != null) {
            CustomBansPlus cbp = CustomBansPlus.getInstance();
            PlayerManager playerManager = cbp.plm;

            CBPlayer cbpPlayer = playerManager.getCBPlayer(player.getUniqueId());

            if (api.isPlayerMuted(cbpPlayer)) {
                player.sendMessage(ChatColor.RED + "You cannot use gang chat while you are muted.");
                return true;
            }
        }

        var message = String.join(" ", args);

        try {
            var gang = Gangs.get(player);

            var filter = FilteredWords.takeActionIfNotClean(player, message, "gang chat");

            if (filter != null && filter.getAction() == FilterAction.BLOCK_MESSAGE) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>Gangs are currently disabled."));
                return true;
            }

            gang.broadcast(GangRole.MEMBER, (member) -> {
                member.sendMessage(PrisonGame.mm.deserialize(
                        "<gray>[<red>GANG CHAT</red>] <player>: <message>",
                        Placeholder.component("player", Component.text(player.getName())),
                        Placeholder.component("message", Component.text(filter != null
                            ? FilteredWords.filterMessage
                            : message))
                ));
            });
        } catch (SQLException exception) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }
}
