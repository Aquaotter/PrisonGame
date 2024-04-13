package prisongame.prisongame.discord.listeners

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.advancement.Advancement
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
import prisongame.prisongame.PrisonGame
import prisongame.prisongame.config.config
import prisongame.prisongame.discord.chatChannel
import prisongame.prisongame.discord.jda

object Messages : ListenerAdapter() {
    @Suppress("NAME_SHADOWING")
    fun onDeath(victim: Player, attacker: Entity?, message: String) {
        var message = message.replace(victim.name, "**${victim.name}**")

        if (attacker != null)
            message = message.replace(attacker.name, "**${attacker.name}**")
            message = message.replace("§", " ")
        chatChannel.sendMessage(message).queue();
    }

    fun onGrantAdvancement(player: Player, advancement: Advancement) {
        chatChannel.sendMessage(String.format(
            "**%s** has made the advancement **%s**",
            player.name,
            PlainTextComponentSerializer.plainText().serialize(advancement.displayName())
        )).queue();
    }

    fun onJoin(player: Player) {
        val solitary = player.hasPotionEffect(PotionEffectType.WATER_BREATHING)

        chatChannel.sendMessage(String.format(
            "**%s** was caught and sent %s!",
            player.name.replace("_", "\\_"),
            if (solitary) "back to solitary"
            else "to prison"
        )).queue();
    }

    fun onLeave(player: Player) {
        chatChannel.sendMessage(String.format(
            "**%s** ran off somewhere else...",
            player.name.replace("_", "\\_")
        )).queue();
    }

    fun onChat(player: Player, message: String) {
        chatChannel.sendMessage(String.format(
            "**%s**: %s",
            PlainTextComponentSerializer.plainText()
                .serialize(player.displayName())
                .replace("_", "\\_"),
            message
                .replace("@", "`@`")
                .replace("_", "\\_")
        )).queue();
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val content = event.message.contentDisplay
        val channel = event.channel
        val user = event.author

        if (channel.id != config.discord.chatChannel || user.id == jda.selfUser.id)
            return

        Bukkit.getServer().sendMessage(PrisonGame.mm.deserialize(
            "<name>: <message>",
            Placeholder.component("name", Component
                .text(user.name)
                .color(TextColor.fromHexString("#b3b9fc"))),
            Placeholder.component("message", Component
                .text(content.replace("\n", " ").ifEmpty { "(Image)" })
                .color(TextColor.fromHexString("#d6daff")))
        ))
    }
}