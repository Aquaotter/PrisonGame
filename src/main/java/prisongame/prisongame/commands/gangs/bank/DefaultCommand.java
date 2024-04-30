package prisongame.prisongame.commands.gangs.bank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.commands.gangs.IGangCommand;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.gangs.Gangs;
import prisongame.prisongame.keys.Keys;

import java.sql.SQLException;

public class DefaultCommand implements IGangCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var player = (Player) sender;

        try {
            var gang = Gangs.get(player);
            var contribution = Keys.GANG_CONTRIBUTION.get(player, 0.0);

            player.sendMessage(PrisonGame.mm.deserialize(
                    """
                    
                    <gray>Bank Total: <total>
                    Your Contribution: <contribution>
                    """,
                    Placeholder.component("total", Component
                            .text(PrisonGame.formatBalance(gang.bank) + "$")
                            .color(NamedTextColor.GREEN)),
                    Placeholder.component("contribution", Component
                            .text(PrisonGame.formatBalance(contribution) + "$")
                            .color(NamedTextColor.GREEN))
            ));
        } catch (SQLException exception) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }

    @Override
    public GangRole getRole() {
        return GangRole.MEMBER;
    }
}
