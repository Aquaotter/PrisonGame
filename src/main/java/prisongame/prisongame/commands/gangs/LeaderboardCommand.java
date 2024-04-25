package prisongame.prisongame.commands.gangs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.gangs.Gangs;

import java.sql.SQLException;

public class LeaderboardCommand implements IGangCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            var gangs = Gangs.list();
            var leaderboard = gangs.stream().sorted((g1, g2) -> {
                if (g2.bank < g1.bank)
                    return -1;
                else if (g2.bank == g1.bank)
                    return 0;
                else return 1;
            }).toList();

            var position = 1;
            var listing = Component.empty();
            for (var i = 0; i < 10; i++) {
                if (leaderboard.size() == i)
                    break;

                var gang = leaderboard.get(i);

                if (i > 0 && leaderboard.get(i - 1).bank != gang.bank)
                    position++;

                if (!listing.equals(Component.empty()))
                    listing = listing.append(Component.newline());

                listing = listing.append(PrisonGame.mm.deserialize(
                        """
                        <white><position>.</white> Gang <gang> with <money>
                        """.trim(),
                        Placeholder.component("gang", Component
                                .text(gang.name)
                                .color(NamedTextColor.WHITE)),
                        Placeholder.component("money", Component
                                .text(gang.bank + "$")
                                .color(NamedTextColor.GREEN)),
                        Placeholder.component("position", Component.text(position))
                ));
            }

            sender.sendMessage(PrisonGame.mm.deserialize(
                    """
                    
                    <blue>-=- Gang Leaderboard -=-<gray>
                    <listing>
                    </gray>-=-=-=-=-=-=-=-=-=-=-=-</blue>
                    """,
                    Placeholder.component("listing", listing)
            ));
        } catch (SQLException exception) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }

    @Override
    public GangRole getRole() {
        return GangRole.NONE;
    }
}
