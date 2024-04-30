package prisongame.prisongame.config

import cc.ekblad.toml.decode
import cc.ekblad.toml.model.TomlException
import cc.ekblad.toml.tomlMapper
import org.bukkit.ChatColor
import org.bukkit.Material
import prisongame.prisongame.PrisonGame
import prisongame.prisongame.instance

private val file = instance.dataFolder.resolve("prisons.toml")

data class Prisons(
    val defaultPrison: String,
    val prisons: Map<String, Prison>
)

data class Prison(
    val priority: Int,
    private val rawMaterial: String,
    val showInSelector: Boolean,
    private val rawDisplayName: String,
    val name: String,
    val firstTrack: TOMLLocation,
    val secondTrack: TOMLLocation,
    val nurse: TOMLLocation,
    val warden: TOMLLocation,
    val prisoner: TOMLLocation,
    val blackMarketIn: TOMLLocation,
    val blackMarketOut: TOMLLocation,
    val solitary: TOMLLocation,
    val bertrude: TOMLLocation
) {
    val material = Material.matchMaterial(rawMaterial)
    val displayName = ChatColor.translateAlternateColorCodes('&', rawDisplayName)
}

fun reloadPrisons(): Prisons {
    val mapper = tomlMapper {
        mapping<Prisons>("default-prison" to "defaultPrison")
        mapping<Prison>(
            "material" to "rawMaterial",
            "display-name" to "rawDisplayName",
            "show-in-selector" to "showInSelector",
            "track-1" to "firstTrack",
            "track-2" to "secondTrack",
            "nurse-2" to "nurse", // todo: remove nurse-1 (unused field)
            "bm-in" to "blackMarketIn",
            "bm-out" to "blackMarketOut"
        )
    }

    if (!file.exists())
        instance.saveResource(file.name, false)

    return try {
        mapper.decode<Prisons>(file.toPath())
    } catch (exception: TomlException) {
        handleTOMLException(exception)
        fallbackPrisons
    }
}