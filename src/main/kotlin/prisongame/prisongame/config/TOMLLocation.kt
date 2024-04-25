package prisongame.prisongame.config

import org.bukkit.Bukkit
import org.bukkit.Location

data class TOMLLocation(
    private val rawWorld: String = "world",
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
    val yaw: Float = 0f,
    val pitch: Float = 0f
) {
    val world = Bukkit.getWorld(rawWorld)
        ?: throw IllegalArgumentException("Invalid world provided.")

    fun getLocation(): Location {
        return Location(world, x, y, z, yaw, pitch)
    }
}
