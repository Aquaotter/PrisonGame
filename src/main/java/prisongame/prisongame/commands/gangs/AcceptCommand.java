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

public class AcceptCommand implements IGangCommand {
    @Override
    public GangRole getRole() {
        return GangRole.NONE;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var player = (Player) sender;
        var invite = PrisonGame.gangInvites.get(player);

        try {
            System.out.println(invite);
            var gang = Gangs.get(player);

            if (invite == null || (gang != null && !gang.name.equals(invite.getFirst()))) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>You haven't received an invite."));
                return true;
            }

            var inviteGang = Gangs.get(invite.getFirst());
            var role = invite.getSecond();

            if (Keys.GANG_ROLE.get(player, 0) == 0)
                inviteGang.add(player);

            inviteGang.promote(player, role);
            inviteGang.broadcast(GangRole.MEMBER, (member) -> {
                var isSelf = member.equals(player);

                member.sendMessage(PrisonGame.mm.deserialize(
                        "<gray><player> <verb> now <article> <role> of the gang.",
                        Placeholder.component("player", Component
                                .text(isSelf ? "You" : player.getName())
                                .color(isSelf ? NamedTextColor.GRAY : NamedTextColor.WHITE)),
                        Placeholder.component("verb", Component.text(isSelf ? "are" : "is")),
                        Placeholder.component("article", Component.text(role == GangRole.MEMBER ? "a" : "an")),
                        Placeholder.component("role", Component.text(role.name().toLowerCase()))
                ));
            });
        } catch (SQLException exception) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }
}
