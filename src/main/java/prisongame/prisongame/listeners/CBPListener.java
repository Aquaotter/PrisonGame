package prisongame.prisongame.listeners;

import me.coralise.spigot.API.events.PostBanEvent;
import me.coralise.spigot.API.events.PostMuteEvent;
import me.coralise.spigot.API.events.UnbanEvent;
import me.coralise.spigot.API.events.UnmuteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.nbt.OfflinePlayerHolder;

import java.util.UUID;

import static prisongame.prisongame.discord.DiscordKt.*;

public class CBPListener implements Listener {
    @EventHandler
    public void onBan(PostBanEvent event) {
        mute(event.getPlayerUuid());
    }

    @EventHandler
    public void onMute(PostMuteEvent event) {
        mute(UUID.fromString(event.getPlayerUuid()));
    }

    @EventHandler
    public void onUnban(UnbanEvent event) {
        unmute(UUID.fromString(event.getPlayerUuid()));
    }

    @EventHandler
    public void onUnmute(UnmuteEvent event) {
        unmute(UUID.fromString(event.getPlayerUuid()));
    }

    private void unmute(UUID uuid) {
        var pdc = new OfflinePlayerHolder(uuid);

        if (!Keys.LINK.has(pdc))
            return;

        var member = guild.getMemberById(Keys.LINK.get(pdc));

        if (member == null)
            return;

        removeMuted(member);
    }

    private void mute(UUID uuid) {
        var pdc = new OfflinePlayerHolder(uuid);

        if (!Keys.LINK.has(pdc))
            return;

        var member = guild.getMemberById(Keys.LINK.get(pdc));

        if (member == null)
            return;

        addMuted(member);
    }
}
