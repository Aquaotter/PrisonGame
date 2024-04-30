package prisongame.prisongame.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.gangs.GangRole;
import prisongame.prisongame.keys.Keys;

public class StatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /stats <player>");
            return false;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
        getStats(targetPlayer, sender);
        return true;
    }
    private void getStats(OfflinePlayer p, CommandSender sender){
        if (!p.hasPlayedBefore()) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The player has never played before!"));
            return;
        }
        if(!p.isOnline()){
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Sorry, Seeing Offline player stats are not coming soon!"));
            return;
        }
        var stats = PrisonGame.mm.deserialize("""
                <gray>=-=-= <gold>PrisonButBad <gray>=-=-=
                
                <gold>Playtime: <playtime> (HOURS)
                <gold>WardenPlaytime: <wardenplaytime> (HOURS)
                <gold>Kills: <kills>
                <gold>Deaths: <deaths>
                <gold>KDR: <kdr>
                <gold>Bells: <bells>
                <gold>Money: <money>
                <gold>Gang: <gang>
                <gold>GangContribution: <gangc>
                <gold>GangRole: <gangrole>
                <gold>EscapeCount: <escapecount>
                
                <gray>=-=-= <gold><username>'s stat <gray>=-=-=
                """,
                Placeholder.component("playtime", PrisonGame.mm.deserialize("" + (float) p.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60/60)),
                Placeholder.component("wardenplaytime", PrisonGame.mm.deserialize("" + (float) Keys.ALLWARDENTIME.get(p.getPlayer(), 0)/20/60/60)),
                Placeholder.component("kills", PrisonGame.mm.deserialize("" + p.getStatistic(Statistic.PLAYER_KILLS))),
                Placeholder.component("deaths", PrisonGame.mm.deserialize("" + p.getStatistic(Statistic.DEATHS))),
                Placeholder.component("kdr", PrisonGame.mm.deserialize("" + (float) p.getStatistic(Statistic.PLAYER_KILLS)/p.getStatistic(Statistic.DEATHS))),
                Placeholder.component("money", PrisonGame.mm.deserialize("" +  Keys.MONEY.get(p.getPlayer(), 0.0))),
                Placeholder.component("bells", PrisonGame.mm.deserialize("" + p.getStatistic(Statistic.BELL_RING))),
                Placeholder.component("gang", PrisonGame.mm.deserialize(" " + Keys.GANG.get(p.getPlayer(), "NONE"))),
                Placeholder.component("gangc", PrisonGame.mm.deserialize("" + Keys.GANG_CONTRIBUTION.get(p.getPlayer(), 0.0))),
                Placeholder.component("gangrole", PrisonGame.mm.deserialize(" " + getGangRoleByInt(Keys.GANG_ROLE.get(p.getPlayer(), 0)))),
                Placeholder.component("escapecount", PrisonGame.mm.deserialize(" " + Keys.ESCAPE_COUNT.get(p.getPlayer(), 0))),

                Placeholder.component("username", PrisonGame.mm.deserialize(" " + p.getName()))
        );
        sender.sendMessage(stats);
    }
    private String getGangRoleByInt(int gangroleid){
        if(gangroleid==1) return "MEMBER";
        if(gangroleid==2) return "OFFICIAL";
        if(gangroleid==3) return "OWNER";
        return "NONE";
    }
}
