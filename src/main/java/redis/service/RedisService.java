package br.quarkus.redis.service;

import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;

public interface RedisService {

    void set(String key, String value);

    void setRange(String key, String value, String offset);

    String getRange(String key, String start, String end);

    String get(String key);

    Map<String, String> getAllFieldsFromHash(String hashKey);

    String getHashFieldValue(String hashKey, String field);

    void increment(String key);

    void delete(String key);

    void incrementBy(String key, long increment);

    void hmSet(String key, JsonObject fields);

    String hGetAll(String key);

    List<String> hGetAllFromListKeys(List<String> keys);

    void cacheRecentPosts();

    void hmsetField(String key, String value, String fieldName);

    void insertHashWithExpiration(String key, JsonObject fields, int expirationTimeInSeconds);

    void cleanRedisDatabase();

}