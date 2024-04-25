package prisongame.prisongame.commands.gangs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.gangs.Gangs;
import prisongame.prisongame.keys.Keys;

import java.sql.SQLException;

public class LeaveCommand implements IGangCommand {
    @Override
    public GangRole getRole() {
        return GangRole.MEMBER;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var player = (Player) sender;

        try {
            var gang = Gangs.get(player);
            var role = Keys.GANG_ROLE.get(player, 0);

            if (gang == null) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>You aren't in a gang."));
                return true;
            }

            if (role == GangRole.OWNER.ordinal()) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>You can't leave the gang."));
                return true;
            }

            gang.broadcast(GangRole.MEMBER, (member) -> {
                member.sendMessage(PrisonGame.mm.deserialize(
                        "<gray><player> has left the gang.",
                        Placeholder.component("player", Component
                                .text(player.getName())
                                .color(NamedTextColor.WHITE))
                ));
            });
            gang.remove(player);
        } catch (SQLException exception) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }
}
