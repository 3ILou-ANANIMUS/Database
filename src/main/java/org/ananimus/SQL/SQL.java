package org.ananimus.SQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class SQL implements AutoCloseable {

    public Connection connection;

    /// params: {type= url/file}
    ///("jdbc:mysql://localhost:3306/dada?useSSL=false", "root", "");
    public SQL(Type type, String... folderOrUrl){
        create(type, folderOrUrl);
    }

    private void create(Type type, String... folderOrUrl){
        try {
            if (folderOrUrl.length > 1){
                connection = DriverManager.getConnection("jdbc:sqlite:" + folderOrUrl[0]);
            }
            connection = DriverManager.getConnection(folderOrUrl[0], folderOrUrl[1], folderOrUrl[2]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //uuid PRIMARY KEY, name TEXT NOTNULL, code TEXT NOTNULL, gc TEXT пераделать
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
    public List<Object> getData(String sql, String where, String... gets){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            List<Object> list = new ArrayList<>();

            if (where != null) {
                preparedStatement.setString(1, where);
            }

            try(ResultSet rs = preparedStatement.executeQuery()) {
                for (String obj : gets) {
                    if (rs.next()){
                        list.add(rs.getObject(obj));
                    }
                }
                return list;
            } catch (SQLException e){
                throw new RuntimeException("Ошибка получение данных из базы.");
            }
        } catch (SQLException e){
            throw new RuntimeException("Ошибка подключения.");
        }
    }


    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()){
            connection.close();
        }
    }
}
