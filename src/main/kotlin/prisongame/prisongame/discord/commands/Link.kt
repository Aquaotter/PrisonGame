package prisongame.prisongame.discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import prisongame.prisongame.PrisonGame
import prisongame.prisongame.discord.guild
import prisongame.prisongame.discord.linkedRole
import prisongame.prisongame.keys.Keys
import prisongame.prisongame.nbt.OfflinePlayerHolder

fun link(event: SlashCommandInteractionEvent) {
    if (!event.isFromGuild) {
        event.reply("This can only be ran in a guild.").setEphemeral(true).queue()
        return
    }

    val member = event.member!!
    val code = event.getOption("code")!!.asInt
    val player = PrisonGame.linkCodes[code]

    if (player == null) {
        event.reply("That isn't a valid link code.").setEphemeral(true).queue()
        return
    }

    val holder = OfflinePlayerHolder(player)

    if (Keys.LINK.has(holder)) {
        event.reply("That player is already linked.").setEphemeral(true).queue()
        return
    }

    Keys.LINK.set(holder, member.id)

    guild.addRoleToMember(member, linkedRole).queue();
    PrisonGame.linkCodes.remove(code)
    event.reply("You are now linked as `${player.name}`.").setEphemeral(true).queue()
}