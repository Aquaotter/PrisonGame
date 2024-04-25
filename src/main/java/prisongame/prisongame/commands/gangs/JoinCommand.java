package prisongame.prisongame.commands.gangs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.gangs.Gangs;
import prisongame.prisongame.keys.Keys;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class JoinCommand implements IGangCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var player = (Player) sender;

        if (Keys.GANG.has(player)) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>You're already in a gang."));
            return true;
        }

        try {
            var gang = args.length >= 1 ? Gangs.get(args[0]) : null;

            if (gang == null) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a gang."));
                return true;
            }

            var invite = PrisonGame.gangInvites.get(player);
            if (invite != null && invite.getFirst().equals(gang)) {
                var role = invite.getSecond();
                gang.add(player);

                if (role != GangRole.MEMBER)
                    gang.promote(player, role);

                player.sendMessage(PrisonGame.mm.deserialize(
                        "<gray>You are now a <role> of the gang.",
                        Placeholder.component("role", Component
                                .text(role.name().toLowerCase())
                                .color(NamedTextColor.WHITE))
                ));
                return true;
            }

            PrisonGame.gangJoinRequest.putIfAbsent(gang.name, new ArrayList<>());
            var requests = PrisonGame.gangJoinRequest.get(gang.name);

            if (requests.contains(player)) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>You've already requested to join."));
                return true;
            }

            requests.add(player);
            player.sendMessage(PrisonGame.mm.deserialize("<gray>You've requested to join."));

            for (var member : Bukkit.getOnlinePlayers()) {
                var memberGang = Keys.GANG.get(member);

                if (memberGang == null || !memberGang.equalsIgnoreCase(gang.name))
                    continue;

                var role = GangRole.values()[Keys.GANG_ROLE.get(member, 0)];

                if (!role.isAtLeast(GangRole.OFFICIAL))
                    continue;

                member.sendMessage(PrisonGame.mm.deserialize(
                        """
                        
                        <gray><player> has requested to join the gang.
                        <accept>?
                        """,
                        Placeholder.component("player", Component
                                .text(player.getName())
                                .color(NamedTextColor.WHITE)),
                        Placeholder.component("accept", Component
                                .text("Accept")
                                .color(NamedTextColor.WHITE)
                                .clickEvent(ClickEvent.runCommand("/gangs invite " + player.getName())))
                ));
            }
        } catch (SQLException exception) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }

    @Override
    public GangRole getRole() {
        return GangRole.NONE;
    }
}
