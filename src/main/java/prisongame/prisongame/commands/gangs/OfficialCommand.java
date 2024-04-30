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
import java.util.Objects;

public class OfficialCommand implements IGangCommand {
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

            var joins = PrisonGame.gangJoinRequest.get(gang.name);

            if (joins != null && joins.contains(target)) {
                gang.add(target);
                gang.promote(target, GangRole.OFFICIAL);
                joins.remove(target);
                target.sendMessage(PrisonGame.mm.deserialize("<gray>You are now a official of the gang."));
                return true;
            }

            if (!Keys.GANG.has(target) || !Keys.GANG.get(target).equals(gang.name)) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>That player isn't in the gang."));
                return true;
            }

            PrisonGame.gangInvites.put(target, new Pair<>(gang.name, GangRole.OFFICIAL));
            target.sendMessage(PrisonGame.mm.deserialize(
                    """
                    
                    <gray>You've been invited to become an official of the <gang> gang.
                    <accept>
                    """,
                    Placeholder.component("gang", Component
                            .text(gang.name)
                            .color(NamedTextColor.WHITE)),
                    Placeholder.component("accept", Component
                            .text("Accept")
                            .color(NamedTextColor.WHITE)
                            .clickEvent(ClickEvent.runCommand("/gangs accept")))
            ));
            player.sendMessage(PrisonGame.mm.deserialize(
                    "<gray><player> has been invited.",
                    Placeholder.component("player", Component
                            .text(target.getName())
                            .color(NamedTextColor.WHITE))
            ));
        } catch (SQLException exception) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }

    @Override
    public GangRole getRole() {
        return GangRole.OWNER;
    }
}
