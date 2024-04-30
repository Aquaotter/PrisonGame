package prisongame.prisongame.commands.gangs;

import kotlin.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.gangs.Gangs;
import prisongame.prisongame.keys.Keys;

import java.sql.SQLException;

public class KickCommand implements IGangCommand {
    @Override
    public GangRole getRole() {
        return GangRole.OFFICIAL;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var player = (Player) sender;

        try {
            var gang = Gangs.get(player);
            var target = args.length >= 1 ? Bukkit.getPlayer(args[0]) : null;

            if (target == null) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a player."));
                return true;
            }

            if (!Keys.GANG.has(target) || !Keys.GANG.get(target).equals(gang.name)) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>That player isn't in the gang."));
                return true;
            }

            if (Keys.GANG_ROLE.get(target, 0) >= Keys.GANG_ROLE.get(player, 0)) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>You can't kick that player."));
                return true;
            }

            gang.broadcast(GangRole.MEMBER, (member) -> {
                member.sendMessage(PrisonGame.mm.deserialize(
                        "<gray><player> has been kicked from the gang.",
                        Placeholder.component("player", Component
                                .text(target.getName())
                                .color(NamedTextColor.WHITE))
                ));
            });
            gang.remove(target);
        } catch (SQLException exception) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }
}
