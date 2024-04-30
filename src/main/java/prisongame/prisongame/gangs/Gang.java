package prisongame.prisongame.gangs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.nbt.OfflinePlayerHolder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Gang {
    public String name;
    public UUID owner;
    public String ownerName;
    public ArrayList<UUID> members;
    public ArrayList<UUID> officials;
    public double bank;

    public Gang(
            String name,
            UUID owner,
            String ownerName,
            ArrayList<UUID> members,
            ArrayList<UUID> officials,
            double bank
    ) {
        this.name = name;
        this.owner = owner;
        this.ownerName = ownerName;
        this.members = members;
        this.officials = officials;
        this.bank = bank;
    }

    public void disband() throws SQLException {
        for (var uuid : (ArrayList<UUID>) members.clone())
            remove(uuid);

        Gangs.remove(this);
    }

    public void add(OfflinePlayer player) throws SQLException {
        add(player.getUniqueId());
    }

    public void remove(OfflinePlayer player) throws SQLException {
        remove(player.getUniqueId());
    }

    public void add(UUID uuid) throws SQLException {
        var pdc = new OfflinePlayerHolder(uuid);

        members.add(uuid);
        Keys.GANG.set(pdc, name);
        Keys.GANG_CONTRIBUTION.set(pdc, 0.0);
        Keys.GANG_ROLE.set(pdc, GangRole.MEMBER.ordinal());

        Gangs.update(this);
        pdc.close();
    }

    public void remove(UUID uuid) throws SQLException {
        var pdc = new OfflinePlayerHolder(uuid);

        members.remove(uuid);
        Keys.GANG.remove(pdc);
        Keys.GANG_CONTRIBUTION.remove(pdc);
        Keys.GANG_ROLE.remove(pdc);

        Gangs.update(this);
        pdc.close();
    }

    public void promote(OfflinePlayer player, GangRole role) throws SQLException {
        promote(player.getUniqueId(), role);
    }

    public void promote(UUID uuid, GangRole role) throws SQLException {
        var pdc = new OfflinePlayerHolder(uuid);

        officials.add(uuid);
        Keys.GANG_ROLE.set(pdc, role.ordinal());

        Gangs.update(this);
        pdc.close();
    }

    public void broadcast(GangRole requiredRole, Consumer<Player> consumer) {
        for (var member : Bukkit.getOnlinePlayers()) {
            var memberGang = Keys.GANG.get(member);

            if (memberGang == null || !memberGang.equalsIgnoreCase(name))
                continue;

            var role = GangRole.values()[Keys.GANG_ROLE.get(member, 0)];

            if (!role.isAtLeast(requiredRole))
                continue;

            consumer.accept(member);
        }
    }

    @Override
    public String toString() {
        return "Gang{name=" + name + "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Gang && ((Gang) obj).name.equalsIgnoreCase(name);
    }
}
