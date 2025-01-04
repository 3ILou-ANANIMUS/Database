package org.ananimus.Redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.List;

public final class Redis {

    private RedisCommands<String, String> redisCommands;

    public Redis() {
        create();
    }

    private void create() {
        RedisClient redisClient = RedisClient.create("redis://localhost:6379");
        redisCommands = redisClient.connect().sync();
    }

    // Устанавливаем значения в хеш
    public void hashSet(Request request) {
        try {
            redisCommands.hset(request.key(), request.object(), request.value());
        } catch (Exception e) {
            System.err.println("Ошибка при установке хеша: " + e.getMessage());
        }
    }

    // Получаем данные из хеша
    public String hashGet(String key, String field) {
        try {
            return redisCommands.hget(key, field);
        } catch (Exception e) {
            System.err.println("Ошибка при получении данных из хеша: " + e.getMessage());
            return null;
        }
    }

    // Кеширование данных с TTL
    public void setCached(Request request) {
        try {
            long ttl = Long.parseLong(request.object());
            redisCommands.setex(request.key(), ttl, request.value());
        } catch (NumberFormatException e) {
            System.err.println("Некорректное значение TTL: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка при кешировании: " + e.getMessage());
        }
    }

    // добавление элементов в список
    public void listAdd(String key, String value) {
        try {
            redisCommands.lpush(key, value);
        } catch (Exception e) {
            System.err.println("Ошибка при добавлении в список: " + e.getMessage());
        }
    }

    // получение всех элементов из списка
    public List<String> listGet(String key) {
        try {
            return redisCommands.lrange(key, 0, -1);
        } catch (Exception e) {
            System.err.println("Ошибка при получении данных из списка: " + e.getMessage());
            return null;
        }
    }

    public void setString(String key, String value) {
        try {
            redisCommands.set(key, value);
        } catch (Exception e) {
            System.err.println("Ошибка при установке строки: " + e.getMessage());
        }
    }

    public String getString(String key) {
        try {
            return redisCommands.get(key);
        } catch (Exception e) {
            System.err.println("Ошибка при получении строки: " + e.getMessage());
            return null;
        }
    }


}
