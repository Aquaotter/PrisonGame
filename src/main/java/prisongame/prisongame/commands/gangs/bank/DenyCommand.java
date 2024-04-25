package prisongame.prisongame.commands.gangs.bank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.commands.gangs.IGangCommand;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.gangs.Gangs;
import prisongame.prisongame.keys.Keys;

import java.sql.SQLException;

public class DenyCommand implements IGangCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var player = (Player) sender;
        var target = args.length >= 1 ? Bukkit.getPlayer(args[0]) : null;

        if (target == null) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a player."));
            return true;
        }

        try {
            var gang = Gangs.get(player);

            if (PrisonGame.gangWithdrawRequest.get(target.getUniqueId()) == null || !gang.equals(Gangs.get(target))) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>That player hasn't requested any money."));
                return true;
            }

            PrisonGame.gangWithdrawRequest.remove(target.getUniqueId());

            for (var member : Bukkit.getOnlinePlayers()) {
                var memberGang = Keys.GANG.get(member);

                if (memberGang == null || !memberGang.equalsIgnoreCase(Keys.GANG.get(player)))
                    continue;

                var role = GangRole.values()[Keys.GANG_ROLE.get(member)];

                if (!member.equals(target) && !role.isAtLeast(GangRole.OFFICIAL))
                    continue;

                member.sendMessage(PrisonGame.mm.deserialize(
                        "<gray><player> transaction has been denied.",
                        Placeholder.component("player", member.equals(target) ?
                                Component
                                        .text("Your") :
                                Component
                                        .text(target.getName())
                                        .color(NamedTextColor.WHITE)
                                        .append(Component.text("'s").color(NamedTextColor.GRAY)))
                ));
            }
        } catch (SQLException exception) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            return true;
        }

        return true;
    }

    @Override
    public GangRole getRole() {
        return GangRole.OFFICIAL;
    }
}
