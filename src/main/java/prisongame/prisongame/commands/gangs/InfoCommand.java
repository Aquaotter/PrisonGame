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

import java.sql.SQLException;

public class InfoCommand implements IGangCommand {
    @Override
    public GangRole getRole() {
        return GangRole.NONE;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var player = (Player) sender;

        try {
            var gang = args.length == 0 ? Gangs.get(player) : Gangs.get(args[0]);

            if (gang == null) {
                player.sendMessage(PrisonGame.mm.deserialize(
                        args.length == 0 ? "<red>You aren't in a gang." : "<red>That gang doesn't exist."
                ));
                return true;
            }

            player.sendMessage(PrisonGame.mm.deserialize(
                    """
                    
                    <gray><u><gang> Info</u>
                    
                    Owner: <owner>
                    Members: <members>
                    Bank: <bank>
                    """,
                    Placeholder.component("gang", Component.text(gang.name)),
                    Placeholder.component("owner", Component
                            .text(gang.ownerName)
                            .color(NamedTextColor.WHITE)),
                    Placeholder.component("members", Component
                            .text(gang.members.size())
                            .color(NamedTextColor.WHITE)),
                    Placeholder.component("bank", Component
                            .text(PrisonGame.formatBalance(gang.bank) + "$")
                            .color(NamedTextColor.GREEN))
            ));
        } catch (SQLException exception) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }
}
