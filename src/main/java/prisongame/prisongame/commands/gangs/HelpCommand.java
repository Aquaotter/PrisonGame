package prisongame.prisongame.commands.gangs;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.gangs.GangRole;

public class HelpCommand implements IGangCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(PrisonGame.mm.deserialize("""
                <dark_blue>-=-=-=-=-=-=-=-<blue>
                /gangs list <dark_gray>- <gray>List all gangs.</dark_gray>
                /gangs create (name) <dark_gray>- <gray>Create a gang.</dark_gray>
                /gangs bank <dark_gray>- <gray>View bank information.</dark_gray>
                /gangs bank deposit (amount) <dark_gray>- <gray>Deposit cash into the bank.</dark_gray>
                /gangs bank withdraw (amount) <dark_gray>- <gray>Withdraw cash from the bank.</dark_gray>
                /gangs leaderboard <dark_gray>- <gray>View a leaderboard of gangs.</dark_gray>
                /gangs info [gang] <dark_gray>- <gray>View information about a gang.</dark_gray>
                /gangs invite (player) <dark_gray>- <gray>Invite a player to the gang.</dark_gray>
                /gangs official (player) <dark_gray>- <gray>Promote a player to an official.</dark_gray>
                /gangs fire (player) <dark_gray>- <gray>Fire a player.</dark_gray>
                /gangs kick (player) <dark_gray>- <gray>Kick a player from the gang.</dark_gray>
                /gangs resign <dark_gray>- <gray>Resign as an official.</dark_gray>
                /gangs accept <dark_gray>- <gray>Accept an invitation to be a member or official.</dark_gray>
                /gangs leave <dark_gray>- <gray>Leave the gang.</dark_gray>
                /gangs join (gang) <dark_gray>- <gray>Request to join a gang.</dark_gray>
                /gangs disband <dark_gray>- <gray>Disband the gang.</dark_gray>
                </blue>-=-=-=-=-=-=-=-</dark_blue>
                """));
        return true;
    }

    @Override
    public GangRole getRole() {
        return GangRole.NONE;
    }
}
