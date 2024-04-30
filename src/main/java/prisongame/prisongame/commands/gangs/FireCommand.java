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
import prisongame.prisongame.nbt.OfflinePlayerHolder;

import java.sql.SQLException;

public class FireCommand implements IGangCommand {
    @Override
    public GangRole getRole() {
        return GangRole.OWNER;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var player = (Player) sender;

        try {
            var gang = Gangs.get(player);
            var target = args.length >= 1 ? Bukkit.getOfflinePlayer(args[0]) : null;

            if (target == null) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a player."));
                return true;
            }

            var pdc = new OfflinePlayerHolder(target);

            if (!Keys.GANG.has(pdc) || !Keys.GANG.get(pdc).equals(gang.name)) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>That player is not in the gang."));
                return true;
            }

            gang.promote(target, GangRole.MEMBER);
            gang.broadcast(GangRole.MEMBER, (member) -> {
                member.sendMessage(PrisonGame.mm.deserialize(
                        "<gray><player> has been fired.",
                        Placeholder.component("player", Component
                                .text(target.getName())
                                .color(NamedTextColor.WHITE))
                ));
            });
        } catch (SQLException exception) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }
}
