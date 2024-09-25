package br.com.redis;

import br.com.redis.qualifiers.HighLevelAPI;
import br.com.redis.service.RedisService;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.string.StringCommands;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Map;

@ApplicationScoped
@HighLevelAPI
public class RedisHighLevelAPIRepository implements RedisService {

    private static final Logger LOG = Logger.getLogger(RedisHighLevelAPIRepository.class);

    private StringCommands<String, String> stringCommands;
    private HashCommands<String, String, String> hashCommands;
    private KeyCommands<String> keyCommands;

    @Inject
    RedisDataSource redisDataSource;

    @PostConstruct
    public void init() {
        this.stringCommands = redisDataSource.string(String.class);
        this.hashCommands = redisDataSource.hash(String.class, String.class, String.class);
        this.keyCommands = redisDataSource.key(String.class);
    }

    @Override
    public void set(String key, String value) {
        stringCommands.set(key, value);
    }

    @Override
    public void setRange(String key, String value, int offset) {
        stringCommands.setrange(key, offset, value);
    }

    @Override
    public Uni<String> getRange(String key, int start, int end) {
        return Uni.createFrom().item(stringCommands.getrange(key, start, end));
    }

    @Override
    public Uni<String> get(String key) {
        return Uni.createFrom().item(stringCommands.get(key));
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
        fields.forEach(entry -> {
            hashCommands.hset(key, entry.getKey(), entry.getValue().toString());
        });
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