package prisongame.prisongame.commands.gangs;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.gangs.Gang;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.gangs.Gangs;

import java.sql.SQLException;

public class DisbandCommand implements IGangCommand {
    @Override
    public GangRole getRole() {
        return GangRole.OWNER;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var player = (Player) sender;

        try {
            var gang = Gangs.get(player);

            player.sendMessage(PrisonGame.mm.deserialize(
                    """
                    
                    <red>This is a very dangerous command, which will result in your gang being deleted!
                    Are you sure you want to disband?
                    <confirm>
                    """,
                    Placeholder.component("confirm", Component
                            .text("Confirm")
                            .color(NamedTextColor.DARK_RED)
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.callback((_audience) -> disband(player, gang))))
            ));
        } catch (SQLException exception) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
            PrisonGame.instance.getLogger().severe(exception.getMessage());
        }

        return true;
    }

    private void disband(Player player, Gang gang) {
        player.sendMessage(PrisonGame.mm.deserialize("<gray>Disbanding gang..."));

        new Thread(() -> {
            try {
                gang.disband();
                player.sendMessage(PrisonGame.mm.deserialize("<gray>The gang has been disbanded."));
            } catch (SQLException exception) {
                player.sendMessage(PrisonGame.mm.deserialize("<red>An error occurred."));
                PrisonGame.instance.getLogger().severe(exception.getMessage());
            }
        }).start();
    }
}
