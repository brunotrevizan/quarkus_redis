package br.com.redis.repository;

import br.com.redis.qualifiers.HighLevelAPI;
import br.com.redis.service.RedisService;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;

@ApplicationScoped
@HighLevelAPI
public class RedisHighLevelAPIRepository implements RedisService {

    private ValueCommands<String, String> valueCommands;
    private HashCommands<String, String, String> hashCommands;
    private KeyCommands<String> keyCommands;

    private RedisDataSource redisDataSource;

    @Inject
    public RedisHighLevelAPIRepository(RedisDataSource redisDataSource){
        this.redisDataSource = redisDataSource;
    }

    @PostConstruct
    public void init() {
        this.valueCommands = redisDataSource.value(String.class);
        this.hashCommands = redisDataSource.hash(String.class, String.class, String.class);
        this.keyCommands = redisDataSource.key(String.class);
    }

    @Override
    public void set(String key, String value) {
        valueCommands.set(key, value);
    }

    @Override
    public void setRange(String key, String value, int offset) {
        valueCommands.setrange(key, offset, value);
    }

    @Override
    public Uni<String> getRange(String key, int start, int end) {
        return Uni.createFrom().item(valueCommands.getrange(key, start, end));
    }

    @Override
    public Uni<String> get(String key) {
        return Uni.createFrom().item(valueCommands.get(key));
    }

    @Override
    public Uni<Map<String, String>> getAllFieldsFromHash(String key) {
        return Uni.createFrom().item(hashCommands.hgetall(key));
    }

    @Override
    public Uni<String> getHashFieldValue(String key, String field) {
        return Uni.createFrom().item(hashCommands.hget(key, field));
    }

    @Override
    public void delete(String key) {
        keyCommands.del(key);
    }

    @Override
    public void hmSet(String key, JsonObject fields) {
        fields.forEach(entry -> hashCommands.hset(key, entry.getKey(), entry.getValue().toString()));
    }

    @Override
    public void hmsetField(String key, String value, String fieldName) {
        hashCommands.hset(key, Map.of(fieldName, value));
    }

    @Override
    public void insertHashWithExpiration(String key, JsonObject fields, int expirationTimeInSeconds) {
        hmSet(key, fields);
        keyCommands.expire(key, expirationTimeInSeconds);
    }

    @Override
    public void flushAll() {
        redisDataSource.flushall();
    }

}