package prisongame.prisongame.gangs;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.lib.SQL;
import prisongame.prisongame.nbt.OfflinePlayerHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Gangs {
    public static Gang get(String name) throws SQLException {
        var rs = SQL.query("""
                SELECT * FROM gangs WHERE (lower(name)) = ?;
                """, name.toLowerCase());

        if (!rs.next())
            return null;

        return getGangFromResultSet(rs);
    }

    public static Gang get(OfflinePlayer player) throws SQLException {
        var pdc = new OfflinePlayerHolder(player);

        if (!Keys.GANG.has(pdc))
            return null;

        var rs = SQL.query("""
                SELECT * FROM gangs WHERE name = ?;
                """, Keys.GANG.get(pdc));

        if (!rs.next())
            return null;

        return getGangFromResultSet(rs);
    }

    public static void remove(Gang gang) throws SQLException {
        SQL.execute("DELETE FROM gangs WHERE (lower(name)) = ?", gang.name.toLowerCase());
    }

    public static List<Gang> list() throws SQLException {
        var rs = SQL.query("""
                SELECT * FROM gangs;
                """);

        var gangs = new ArrayList<Gang>();

        while (rs.next())
            gangs.add(getGangFromResultSet(rs));

        return gangs;
    }

    public static int count() throws SQLException {
        return SQL.query("SELECT COUNT(*) FROM gangs;").getInt(1);
    }

    public static boolean exists(String name) throws SQLException {
        return SQL.query("SELECT name FROM gangs WHERE (lower(name)) = ?;", name.toLowerCase()).next();
    }

    public static void create(Player owner, String name) throws SQLException {
        var uuid = owner.getUniqueId();
        var gang = new Gang(name, uuid, owner.getName(), new ArrayList<>(), new ArrayList<>(), 0.0);
        gang.members.add(uuid);
        gang.officials.add(uuid);
        update(gang);
    }

    public static void update(Gang gang) throws SQLException {
        SQL.execute("""
                INSERT OR REPLACE INTO gangs(name, owner, ownerName, members, officials, bank)
                VALUES (?, ?, ?, ?, ?, ?);
                """,
                gang.name,
                gang.owner.toString(),
                gang.ownerName,
                serializeMembers(gang.members),
                serializeMembers(gang.officials),
                gang.bank);
    }

    private static Gang getGangFromResultSet(ResultSet rs) throws SQLException {
        return new Gang(
                rs.getString("name"),
                UUID.fromString(rs.getString("owner")),
                rs.getString("ownerName"),
                deserializeMembers(rs.getString("members")),
                deserializeMembers(rs.getString("officials")),
                rs.getDouble("bank")
        );
    }

    private static ArrayList<UUID> deserializeMembers(String data) {
        if (data.equals(""))
            return new ArrayList<>();

        return Arrays
                .stream(data.split(","))
                .map(UUID::fromString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static String serializeMembers(List<UUID> members) {
        var uuids = members.stream().map(UUID::toString).toArray(String[]::new);
        return String.join(",", uuids);
    }
}
