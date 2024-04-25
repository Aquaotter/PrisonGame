package prisongame.prisongame.commands.gangs;

import org.bukkit.command.CommandExecutor;
import prisongame.prisongame.gangs.GangRole;

public interface IGangCommand extends CommandExecutor {
    GangRole getRole();
}