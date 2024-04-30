package prisongame.prisongame.lib;

import prisongame.prisongame.PrisonGame;

import java.sql.*;

public class SQL {
    private static Connection connection;

    public static void initialize() throws SQLException {
        var dataFolder = PrisonGame.instance.getDataFolder().getAbsolutePath();
        var path = dataFolder + "/database.db";
        var url = "jdbc:sqlite:" + path;

        connection = DriverManager.getConnection(url);
        doInitialSetup();
    }

    public static void execute(String query, Object... values) throws SQLException {
        var statement = prepare(query, values);
        statement.execute();
        statement.close();
    }

    public static ResultSet query(String query, Object... values) throws SQLException {
        var statement = prepare(query, values);
        return statement.executeQuery();
    }

    private static PreparedStatement prepare(String query, Object[] values) throws SQLException {
        var statement = connection.prepareStatement(query);

        for (int i = 0; i < values.length; i++) {
            var value = values[i];
            statement.setObject(i + 1, value);
        }

        return statement;
    }

    public static void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            PrisonGame.instance.getLogger().severe("An error occurred closing SQL.\n" + exception.getMessage());
        }
    }

    private static void doInitialSetup() throws SQLException {
        execute("""
                CREATE TABLE IF NOT EXISTS gangs(
                    name TEXT NOT NULL PRIMARY KEY UNIQUE,
                    owner TEXT NOT NULL,
                    ownerName TEXT NOT NULL,
                    members TEXT NOT NULL,
                    officials TEXT NOT NULL,
                    bank REAL NOT NULL
                );
                """);
    }
}
