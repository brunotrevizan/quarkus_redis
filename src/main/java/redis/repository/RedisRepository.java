//package br.quarkus.redis.repository;
//
//import br.senai.sc.service.redis.RedisService;
//import br.senai.sc.service.redis.model.RedisKeys;
//import br.senai.sc.service.v2.PostagemServiceV2;
//import io.vertx.codegen.annotations.Nullable;
//import io.vertx.core.AsyncResult;
//import io.vertx.core.Handler;
//import io.vertx.core.json.JsonObject;
//import io.vertx.redis.client.Command;
//import io.vertx.redis.client.RedisAPI;
//import io.vertx.redis.client.Response;
//import org.jboss.logging.Logger;
//
//import javax.inject.Inject;
//import javax.inject.Singleton;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//@Singleton
//public class RedisRepository implements RedisService {
//
//    private static final int DELAY_REDIS_COMMAND = 1;
//
//    @Inject
//    RedisAPI redisAPI;
//
//    @Inject
//    PostagemServiceV2 postagemServiceV2;
//
//    private static final Logger LOG = Logger.getLogger(RedisRepository.class);
//
//    public void set(String key, String value) {
//        redisAPI.set(Arrays.asList(key, value), res -> {
//            if (res.failed()) {
//                LOG.error(res.cause().getMessage());
//            }
//        });
//    }
//
//    public void setRange(String key, String value, String offset) {
//        redisAPI.setrange(key, offset, value, res -> {
//            if (res.failed()) {
//                LOG.error(res.cause().getMessage());
//            }
//        });
//    }
//
//    public String getRange(String key, String start, String end) {
//        try {
//            CompletableFuture<String> future = new CompletableFuture<>();
//
//            redisAPI.getrange(key, String.valueOf(start), String.valueOf(end), res -> handleResponse(future, res));
//
//            return future.get();
//        } catch (ExecutionException | InterruptedException e) {
//            LOG.error(e.getMessage());
//            Thread.currentThread().interrupt();
//        }
//        return null;
//    }
//
//    public String get(String key) {
//        try {
//            CompletableFuture<String> future = new CompletableFuture<>();
//
//            redisAPI.get(key, res -> handleResponse(future, res));
//
//            return future.get();
//        } catch (ExecutionException | InterruptedException e) {
//            LOG.error(e.getMessage());
//            Thread.currentThread().interrupt();
//        }
//        return null;
//    }
//
//    private void handleResponse(CompletableFuture<String> future, AsyncResult<@Nullable Response> res) {
//        if (res.succeeded()) {
//            Response response = res.result();
//            if (response == null || response.toString().isEmpty()) {
//                future.complete(null);
//            } else {
//                future.complete(response.toString());
//            }
//        } else {
//            future.completeExceptionally(res.cause());
//        }
//    }
//
//    public Map<String, String> getAllFieldsFromHash(String hashKey) {
//        try {
//            CompletableFuture<Map<String, String>> future = new CompletableFuture<>();
//
//            redisAPI.hgetall(hashKey, res -> {
//                if (res.succeeded()) {
//                    Response response = res.result();
//                    Map<String, String> fields = new HashMap<>();
//
//                    if (response != null) {
//                        for (int i = 0; i < response.size(); i += 2) {
//                            String field = response.get(i).toString();
//                            String value = response.get(i + 1).toString();
//                            fields.put(field, value);
//                        }
//                    }
//
//                    future.complete(fields);
//                } else {
//                    future.completeExceptionally(res.cause());
//                }
//            });
//
//            return future.get();
//        } catch (ExecutionException | InterruptedException e) {
//            LOG.error(e.getMessage());
//            Thread.currentThread().interrupt();
//        }
//        return Collections.emptyMap();
//    }
//
//    public String getHashFieldValue(String hashKey, String field) {
//        try {
//            CompletableFuture<String> future = new CompletableFuture<>();
//
//            redisAPI.hget(hashKey, field, res -> handleResponse(future, res));
//
//            return future.get();
//        } catch (ExecutionException | InterruptedException e) {
//            LOG.error(e.getMessage());
//            Thread.currentThread().interrupt();
//        }
//        return null;
//    }
//
//    public void increment(String key) {
//        redisAPI.incr(key, res -> {
//            if (res.failed()) {
//                LOG.error(res.cause().getMessage());
//            }
//        });
//    }
//
//    public void delete(String key) {
//        redisAPI.del(Arrays.asList(key), res -> {
//            if (res.failed()) {
//                LOG.error(res.cause().getMessage());
//            }
//        });
//    }
//
//    public void incrementBy(String key, long increment) {
//        redisAPI.incrby(key, String.valueOf(increment), res -> {
//            if (res.failed()) {
//                LOG.error(res.cause().getMessage());
//            }
//        });
//    }
//
//    public void hmSet(String key, JsonObject fields) {
//        List<String> arguments = new ArrayList<>();
//        arguments.add(key);
//        fields.forEach(entry -> {
//            arguments.add(entry.getKey());
//            arguments.add(entry.getValue().toString());
//        });
//
//        redisAPI.hmset(arguments, res -> {
//            if (res.failed()) {
//                LOG.error(res.cause().getMessage());
//            }
//        });
//    }
//
//    public String hGetAll(String key) {
//        try {
//            CompletableFuture<String> future = new CompletableFuture<>();
//
//            redisAPI.hgetall(key, res -> handleResponse(future, res));
//            return future.get();
//        } catch (ExecutionException | InterruptedException e) {
//            LOG.error(e.getMessage());
//            Thread.currentThread().interrupt();
//        }
//        return null;
//    }
//
//    public List<String> hGetAllFromListKeys(List<String> keys) {
//        List<String> values = new ArrayList<>();
//        try {
//            for (String key : keys) {
//                CompletableFuture<String> future = new CompletableFuture<>();
//
//                redisAPI.hgetall(key, res -> handleResponse(future, res));
//                String result = future.get();
//                if (!result.equals(RedisKeys.EMPTY_REDIS_REGISTER.getValue()))
//                    values.add(future.get());
//            }
//
//        } catch (ExecutionException | InterruptedException e) {
//            LOG.error(e.getMessage());
//            Thread.currentThread().interrupt();
//        }
//        return values;
//    }
//
//    public void cacheRecentPosts() {
//        try {
//            cleanRedisDatabase();
//            Map<Long, JsonObject> postagens = postagemServiceV2.obterPostagensRecentes();
//            for (Map.Entry<Long, JsonObject> postagem : postagens.entrySet()) {
//                hmSet(RedisKeys.getKeyPost(postagem.getKey()), postagem.getValue());
//                Thread.sleep(DELAY_REDIS_COMMAND);
//            }
//
//            Map<Long, JsonObject> informacoesPostagens = postagemServiceV2.obterInformacoesPostagensRecentes();
//
//            for (Map.Entry<Long, JsonObject> informacoesPostagem : informacoesPostagens.entrySet()) {
//                hmSet(RedisKeys.getKeyInfosPost(informacoesPostagem.getKey()), informacoesPostagem.getValue());
//                Thread.sleep(DELAY_REDIS_COMMAND);
//            }
//        } catch (InterruptedException e) {
//            LOG.error(e.getMessage());
//            Thread.currentThread().interrupt();
//        }
//    }
//
//    public void hmsetField(String key, String value, String fieldName) {
//        redisAPI.exists(Collections.singletonList(key), existsRes -> {
//            if (existsRes.succeeded() && existsRes.result().toInteger() > 0) {
//                List<String> arguments = new ArrayList<>();
//                arguments.add(key);
//                arguments.add(fieldName);
//                arguments.add(value);
//
//                redisAPI.hmset(arguments, hmsetRes -> {
//                    if (hmsetRes.failed()) {
//                        LOG.error(hmsetRes.cause().getMessage());
//                    }
//                });
//            }
//        });
//    }
//
//    public void insertHashWithExpiration(String key, JsonObject fields, int expirationTimeInSeconds) {
//        List<String> hmsetArgs = new ArrayList<>();
//        hmsetArgs.add(key);
//        fields.forEach(entry -> {
//            hmsetArgs.add(entry.getKey());
//            hmsetArgs.add(entry.getValue().toString());
//        });
//
//
//        redisAPI.hmset(hmsetArgs, hmsetRes -> {
//            if (hmsetRes.succeeded()) {
//                List<String> expireArgs = new ArrayList<>();
//                expireArgs.add(key);
//                expireArgs.add(String.valueOf(expirationTimeInSeconds));    // TTL in seconds
//
//                expire(expireArgs, expireRes -> {
//                    if (expireRes.failed()) {
//                        LOG.error(hmsetRes.cause().getMessage());
//                    }
//                });
//            } else {
//                LOG.error(hmsetRes.cause().getMessage());
//            }
//        });
//    }
//
//    public void cleanRedisDatabase() {
//        redisAPI.flushall(Collections.singletonList("ASYNC")).onComplete(res -> {
//            if (res.failed()) {
//                LOG.error(res.cause());
//            }
//        });
//    }
//
//    private void expire(List<String> args, Handler<AsyncResult<Response>> handler) {
//        redisAPI.send(Command.EXPIRE, args.toArray(new String[0])).onComplete(handler);
//    }
//
//}