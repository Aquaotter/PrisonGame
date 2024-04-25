package prisongame.prisongame.cbp

import me.coralise.spigot.API.events.PostMuteEvent
import me.coralise.spigot.API.events.PreMuteEvent
import me.coralise.spigot.API.events.UnbanEvent
import me.coralise.spigot.API.events.UnmuteEvent
import me.coralise.spigot.AbstractAnnouncer
import me.coralise.spigot.enums.AnnouncementType
import me.coralise.spigot.enums.BanType
import me.coralise.spigot.enums.MuteType
import me.coralise.spigot.enums.Punishment
import org.bukkit.OfflinePlayer
import java.util.*

fun issueBan(player: OfflinePlayer, duration: String, reason: String) {
    val playerManager = cbp.plm
    val cbpPlayer = playerManager.getCBPlayer(player.uniqueId)
    val type = if (duration.startsWith("perm")) BanType.PERM_BAN else BanType.TEMP_BAN
    val punishment = Punishment.fromString(type.toString())

    cbp.database.updateHistoryStatus(player.uniqueId, "Ban", "Overwritten", null)
    cbp.bm.ban(
        cbpPlayer,
        type,
        reason,
        duration,
        null,
        false,
        null,
        cbp.u.determineBanAnnType(type, reason),
        false
    )
    cbp.database.addHistory(cbpPlayer, punishment, null, reason)
}

fun issueMute(player: OfflinePlayer, duration: String, reason: String) {
    val playerManager = cbp.plm
    val cbpPlayer = playerManager.getCBPlayer(player.uniqueId)
    val type = if (duration.startsWith("perm")) MuteType.PERM_MUTE else MuteType.TEMP_MUTE
    val announcementType = if (duration.startsWith("perm")) AnnouncementType.PERM_MUTE else AnnouncementType.TEMP_MUTE
    val punishment = Punishment.fromString(type.toString())
    cbp.u.callEvent(PreMuteEvent(
        cbpPlayer.getUuid(),
        cbpPlayer.getName(),
        cbp.getConfig().getString("console-name"),
        reason,
        duration,
        false))

    cbp.database.updateHistoryStatus(player.uniqueId, "Mute", "Overwritten", null)
    cbp.mm.setMute(
        player.uniqueId,
        type,
        Date(),
        reason,
        duration,
        null,
        true)
    cbp.database.addHistory(cbpPlayer, punishment, null, reason)
    AbstractAnnouncer.getAnnouncer(cbpPlayer, null, duration, reason, announcementType, false)

    cbp.u.callEvent(PostMuteEvent(cbp.mm.getMute(cbpPlayer.uuid), false))
}

fun issueUnban(player: OfflinePlayer) {
    val playerManager = cbp.plm
    val cbpPlayer = playerManager.getCBPlayer(player.uniqueId)

    cbp.database.updateHistoryStatus(player.uniqueId, "Ban", "Unbanned", null)
    cbp.bm.removeBan(cbp.bm.getBan(player.uniqueId), "Unbanned", null)
    cbp.u.removeOci(player.uniqueId)
    AbstractAnnouncer.getAnnouncer(cbpPlayer, null, null, null, AnnouncementType.UNBAN, false)

    cbp.u.callEvent(UnbanEvent(null, cbpPlayer, false))
}

fun issueUnmute(player: OfflinePlayer) {
    val playerManager = cbp.plm
    val cbpPlayer = playerManager.getCBPlayer(player.uniqueId)

    cbp.mm.removeMute(player.uniqueId, "Unmuted", null)
    AbstractAnnouncer.getAnnouncer(cbpPlayer, null, null, null, AnnouncementType.UNMUTE, false)
    cbp.u.callEvent(UnmuteEvent(null, cbpPlayer, false))
}