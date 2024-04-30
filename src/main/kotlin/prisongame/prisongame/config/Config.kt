package prisongame.prisongame.config

import cc.ekblad.toml.decode
import cc.ekblad.toml.model.TomlException
import cc.ekblad.toml.tomlMapper
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import prisongame.prisongame.PrisonGame
import prisongame.prisongame.config.filter.Filter
import prisongame.prisongame.config.filter.reloadFilter
import prisongame.prisongame.instance
import prisongame.prisongame.logger

private val file = instance.dataFolder.resolve("config.toml")
var config = reloadConfig()

data class Config(
    val dev: Boolean,
    val general: General,
    val warden: Warden,
    val discord: Discord
) {
    lateinit var prisonConfig: Prisons
    lateinit var defaultPrison: Prison
    lateinit var prisons: Map<String, Prison>
    lateinit var filter: Map<String, Filter>

//    private val prisonConfig = reloadPrisons()
//    var prisons = prisonConfig.prisons
//    var defaultPrison = prisons[prisonConfig.defaultPrison]
//    var filter = reloadFilter()

    data class General(
        val discordInvite: String,
        val rules: List<Rule>
    ) {
        data class Rule(
            private val rawName: String,
            private val rawPages: List<String>
        ) {
            val name = PrisonGame.mm.deserialize(rawName)
            val pages = rawPages.map { PrisonGame.mm.deserialize(it, Placeholder.component(
                "inclusion",
                PrisonGame.mm.deserialize("This includes <b>but is not limited to</b>:")
            )) }
        }
    }

    data class Warden(
        val help: Map<String, Help>,
        val prefix: Prefix
    ) {
        data class Help(
            val args: List<String>,
            val description: String
        )

        data class Prefix(
            val bannedContainers: List<String>
        )
    }

    data class Discord(
        val token: String,
        val guild: String,
        val chatChannel: String,
        val commandsChannel: String,
        val punshmentChannel: String,
        val filterChannel: String,
        val linkedRole: String,
        val canSpeakRole: String
    )
}

fun reloadConfig(): Config {
    val mapper = tomlMapper {
        mapping<Config.General>("discord-invite" to "discordInvite")
        mapping<Config.Warden.Prefix>("banned-containers" to "bannedContainers")
        mapping<Config.General.Rule>("name" to "rawName", "pages" to "rawPages")
        mapping<Config.Discord>(
            "chat-channel" to "chatChannel",
            "commands-channel" to "commandsChannel",
            "punshment-channel" to "punshmentChannel",
            "filter-channel" to "filterChannel",
            "linked-role" to "linkedRole",
            "can-speak-role" to "canSpeakRole"
        )
    }

    if (!file.exists())
        instance.saveResource(file.name, false)

    var config = try {
        val config = mapper.decode<Config>(file.toPath())
        config.prisonConfig = reloadPrisons()
        config.prisons = config.prisonConfig.prisons
        config.defaultPrison = config.prisons[config.prisonConfig.defaultPrison] ?: fallbackPrisons.prisons["jail"]!!
        config.filter = reloadFilter()
        config
    } catch (exception: TomlException) {
        handleTOMLException(exception)
        fallbackConfig
    }

    // If any configs have failed to decode, return the fallback config for everything.
    if (config == fallbackConfig || config.prisons == fallbackPrisons.prisons || config.filter == fallbackFilters.filters) {
        config = fallbackConfig
        config.prisons = fallbackPrisons.prisons
        config.defaultPrison = config.prisons[fallbackPrisons.defaultPrison]!!
        config.filter = fallbackFilters.filters
    }

    return config
}

fun handleTOMLException(exception: TomlException) {
    logger.severe(when (exception::class.java) {
        TomlException.DecodingError::class.java -> (exception as TomlException.DecodingError).reason
        else -> exception.message
    })
}