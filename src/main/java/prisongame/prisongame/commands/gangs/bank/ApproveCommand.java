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

public class ApproveCommand implements IGangCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var player = (Player) sender;
        var target = args.length >= 1 ? Bukkit.getPlayer(args[0]) : null;

        if (target == null) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a player."));
            return true;
        }

        var amount = PrisonGame.gangWithdrawRequest.get(target.getUniqueId());

        try {
            var gang = Gangs.get(player);

            if (amount == null || !gang.equals(Gangs.get(target))) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>That player hasn't requested any money."));
                return true;
            }

            if (gang.bank < amount) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>There isn't enough money in the bank."));
                return true;
            }

            PrisonGame.gangWithdrawRequest.remove(target);
            gang.bank -= amount;
            Gangs.update(gang);
            Keys.MONEY.set(target, Keys.MONEY.get(target, 0.0) + amount);
            Keys.GANG_CONTRIBUTION.set(target, 0.0);

            for (var member : Bukkit.getOnlinePlayers()) {
                var memberGang = Keys.GANG.get(member);

                if (memberGang == null || !memberGang.equalsIgnoreCase(Keys.GANG.get(player)))
                    continue;

                member.sendMessage(PrisonGame.mm.deserialize(
                        "<gray><player> has withdrawn <amount> (now <total>, transaction approved by <official>)",
                        Placeholder.component("player", Component
                                .text(target.getName())
                                .color(NamedTextColor.WHITE)),
                        Placeholder.component("amount", Component
                                .text(PrisonGame.formatBalance(amount) + "$")
                                .color(NamedTextColor.GREEN)),
                        Placeholder.component("total", Component
                                .text(PrisonGame.formatBalance(gang.bank) + "$")
                                .color(NamedTextColor.GREEN)),
                        Placeholder.component("official", Component
                                .text(player.getName())
                                .color(NamedTextColor.WHITE))
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
