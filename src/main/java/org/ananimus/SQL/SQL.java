package org.ananimus.SQL;

import java.sql.*;

public final class SQL implements AutoCloseable {

    public Connection connection;

    public SQL(String folder){
        create(folder);
    }

    private void create(String folder){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + folder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //uuid PRIMARY KEY, name TEXT, code TEXT, gc TEXT
    public void createTable(String tables){
        String sql = "CREATE TABLE IF NOT EXISTS players (" + tables + ");";

        try (Statement statement = connection.createStatement()){
            statement.execute(sql);
        } catch (SQLException e){
            throw new RuntimeException("Ошибка при создании таблицы.");
        }
    }

    //INSERT INTO players(uuid, name, balance) VALUES(?, ?, ?)
    public boolean insertData(String sql, Object... objects){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                preparedStatement.setString(i + 1, objects[i].toString());
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e){
            throw new RuntimeException("Ошибка подключения.");
        }
    }


    // SELECT name, balance FROM players WHERE uuid = ?
    public String getData(String sql, String where, String get){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, where);

            try(ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()){
                    return rs.getString(get);
                }
            } catch (SQLException e){
                throw new RuntimeException("Ошибка получение данных из базы.");
            }
        } catch (SQLException e){
            throw new RuntimeException("Ошибка подключения.");
        }
        return null;
    }


    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()){
            connection.close();
        }
    }
}
