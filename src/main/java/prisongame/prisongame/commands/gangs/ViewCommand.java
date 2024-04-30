package prisongame.prisongame.commands.gangs;

import net.kyori.adventure.text.Component;
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

import java.sql.SQLException;

public class ViewCommand implements IGangCommand {
    @Override
    public GangRole getRole() {
        return GangRole.NONE;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(PrisonGame.instance, () -> {
            var target = Bukkit.getOfflinePlayer(args[0]);

            try {
                var gang = Gangs.get(target);

                if (gang == null) {
                    sender.sendMessage(PrisonGame.mm.deserialize("<red>That player isn't in a gang."));
                    return;
                }

                sender.sendMessage(PrisonGame.mm.deserialize(
                        "<gray><name> is in the <gang> gang.",
                        Placeholder.component("name", Component
                                .text(target.getName())
                                .color(NamedTextColor.WHITE)),
                        Placeholder.component("gang", Component
                                .text(gang.name)
                                .color(NamedTextColor.WHITE))
                ));
            } catch (SQLException exception) {
                sender.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
                PrisonGame.instance.getLogger().severe(exception.getMessage());
            }
        });

        return true;
    }
}
