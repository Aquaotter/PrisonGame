package prisongame.prisongame.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Keys;

import static prisongame.prisongame.discord.DiscordKt.*;

public class UnlinkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>You aren't a player."));
            return true;
        }

        if (!Keys.LINK.has(player)) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>You aren't linked."));
            return true;
        }

        var member = guild.getMemberById(Keys.LINK.get(player));
        Keys.LINK.remove(player);

        if (member != null) {
            guild.removeRoleFromMember(member, linkedRole).queue();
            guild.removeRoleFromMember(member, canSpeakRole).queue();
        }

        player.sendMessage(PrisonGame.mm.deserialize("<gray>You are no longer linked."));
        return true;
    }
}
