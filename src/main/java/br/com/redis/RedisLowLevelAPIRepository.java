package br.com.redis;

import br.com.redis.qualifiers.LowLevelAPI;
import br.com.redis.service.RedisService;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.redis.client.RedisAPI;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

@ApplicationScoped
@LowLevelAPI
public class RedisLowLevelAPIRepository implements RedisService {

    private static final Logger LOG = Logger.getLogger(RedisLowLevelAPIRepository.class);

    @Inject
    RedisAPI redisAPI;

    @Override
    public void set(String key, String value) {
        redisAPI.set(Arrays.asList(key, value))
                .subscribe().with(
                        success -> LOG.info("Key '" + key + "' with value '" + value + "' is saved on Redis"),
                        failure -> LOG.error("Error on creating key: " + key + ": " + failure.getMessage())
                );
    }

    @Override
    public void setRange(String key, String value, int offset) {
        redisAPI.setrange(key, String.valueOf(offset), value)
                .subscribe().with(
                        success -> LOG.info("setRange executed successfully"),
                        failure -> LOG.error("Error on executing setRange to key: " + key + ": " + failure.getMessage())
                );
    }

    @Override
    public Uni<String> getRange(String key, int start, int end) {
        return redisAPI.getrange(key, String.valueOf(start), String.valueOf(end))
                .map(value -> value.toString())
                .onFailure().invoke(err -> {
                    LOG.error("Error on getRange: " + err.getMessage(), err);
                });
    }

    @Override
    public Uni<String> get(String key) {
        return redisAPI.get(key)
                .onItem().transform(response -> response != null ? response.toString() : null)
                .onFailure().recoverWithItem("Error on retrieving value of key: " + key);
    }

    @Override
    public Uni<Map<String, String>> getAllFieldsFromHash(String key) {
        return redisAPI.hgetall(key)
                .map(response -> {
                    if (response == null || response.size() == 0) {
                        return null;
                    }

                    Map<String, String> hash = new HashMap<>();
                    for (String field : response.getKeys()) {
                        hash.put(field, response.get(field).toString());
                    }
                    return hash;
                })
                .onFailure().invoke(err -> {
                    LOG.error("Error on hGetAll: " + err.getMessage(), err);
                });
    }

    @Override
    public Uni<String> getHashFieldValue(String key, String field) {
        return redisAPI.hget(key, field)
                .map(buffer -> {
                    if (buffer == null) {
                        return null;
                    }
                    String value = buffer.toString();
                    return value;
                })
                .onFailure().invoke(err -> {
                    LOG.error("Error on hget: " + err.getMessage(), err);
                });
    }

    @Override
    public void delete(String key) {
        redisAPI.del(Arrays.asList(key))
                .subscribe().with(
                success -> LOG.info("Hash removed successfully"),
                failure -> LOG.error("Error to remove hash: " + key)
        );
    }

    @Override
    public void hmSet(String key, JsonObject fields) {
        List<String> args = new ArrayList<>(Arrays.asList(key));
        fields.forEach(entry -> {
            args.add(entry.getKey());
            args.add(entry.getValue().toString());
        });
        redisAPI.hmset(args)
                .subscribe().with(
                        success -> LOG.info("Hash created successfully"),
                        failure -> LOG.error("Error to create hash " + failure.getMessage())
                );
    }

    @Override
    public void hmsetField(String key, String value, String fieldName) {
        List<String> args = new ArrayList<>(Arrays.asList(key, fieldName, value));
        redisAPI.hmset(args)
                .subscribe().with(
                        success -> LOG.info("Hash field updated successfully"),
                        failure -> LOG.error("Error to update field on hash: " + failure.getMessage())
                );
    }

    @Override
    public void insertHashWithExpiration(String key, JsonObject fields, int expirationTimeInSeconds) {
        hmSet(key, fields);
        redisAPI.expire(Arrays.asList(key, String.valueOf(expirationTimeInSeconds)))
                .subscribe().with(
                success -> LOG.info("Hash expiration defined successfully"),
                failure -> LOG.error("Error on defining hash expiration")
        );;
    }

    @Override
    public void flushAll() {
        throw new UnsupportedOperationException("Not implemented");
    }
}