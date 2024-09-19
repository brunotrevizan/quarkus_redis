package br.com.redis.service;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;

import java.util.Map;

public interface RedisService {

    void set(String key, String value);

    void setRange(String key, String value, int offset);

    Uni<String> getRange(String key, int start, int end);

    Uni<String> get(String key);

    Uni<Map<String, String>> getAllFieldsFromHash(String key);

    Uni<String> getHashFieldValue(String key, String field);

    void delete(String key);

    void hmSet(String key, JsonObject fields);

    void hmsetField(String key, String value, String fieldName);

    void insertHashWithExpiration(String key, JsonObject fields, int expirationTimeInSeconds);

}