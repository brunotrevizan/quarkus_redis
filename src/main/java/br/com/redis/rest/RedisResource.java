package br.com.redis.rest;

import br.com.redis.qualifiers.HighLevelAPI;
import br.com.redis.qualifiers.LowLevelAPI;
import br.com.redis.service.RedisService;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/redis")
public class RedisResource {

    private RedisService highLevelAPI;
    private RedisService lowLevelApi;

    @Inject
    public RedisResource(@HighLevelAPI RedisService highLevelAPI, @LowLevelAPI RedisService lowLevelApi){
        this.highLevelAPI = highLevelAPI;
        this.lowLevelApi = lowLevelApi;
    }

    @POST
    @Path("/set/{key}/{value}/{lowlevel}")
    public Response set(@PathParam("lowlevel") boolean lowLevel, @PathParam("key") String key, @PathParam("value") String value) {
        if (lowLevel) {
            lowLevelApi.set(key, value);
        } else {
            highLevelAPI.set(key, value);
        }
        return Response.ok().build();
    }

    @PUT
    @Path("/setRange/{key}/{value}/{offset}/{lowlevel}")
    public Response setRange(@PathParam("lowlevel") boolean lowLevel, @PathParam("key") String key, @PathParam("value") String value, @PathParam("offset") int offset) {
        if (lowLevel) {
            lowLevelApi.setRange(key, value, offset);
        } else {
            highLevelAPI.setRange(key, value, offset);
        }
        return Response.ok().build();
    }

    @GET
    @Path("/get/{key}/{lowlevel}")
    public Response get(@PathParam("lowlevel") boolean lowLevel, @PathParam("key") String key) {
        if (lowLevel) {
            return Response.ok(lowLevelApi.get(key).await().indefinitely()).build();
        }
        return Response.ok(highLevelAPI.get(key).await().indefinitely()).build();
    }

    @GET
    @Path("/getRange/{key}/{start}/{end}/{lowlevel}")
    public Response getRange(@PathParam("lowlevel") boolean lowLevel, @PathParam("key") String key, @PathParam("start") int start, @PathParam("end") int end) {
        if (lowLevel) {
            return Response.ok(lowLevelApi.getRange(key, start, end).await().indefinitely()).build();
        }
        return Response.ok(highLevelAPI.getRange(key, start, end).await().indefinitely()).build();
    }

    @POST
    @Path("/setHash/{key}/{lowlevel}")
    public Response setHash(@PathParam("lowlevel") boolean lowLevel, @PathParam("key") String key, Map<String, Object> json) {
        if (lowLevel) {
            lowLevelApi.hmSet(key, new JsonObject(json));
        } else {
            highLevelAPI.hmSet(key, new JsonObject(json));
        }
        return Response.ok().build();
    }

    @GET
    @Path("/getHash/{key}/{lowlevel}")
    public Response getHash(@PathParam("lowlevel") boolean lowLevel, @PathParam("key") String key) {
        if (lowLevel) {
            return Response.ok(lowLevelApi.getAllFieldsFromHash(key).await().indefinitely()).build();
        }
        return Response.ok(highLevelAPI.getAllFieldsFromHash(key).await().indefinitely()).build();
    }

    @GET
    @Path("/getHashFieldValue/{key}/{field}/{lowlevel}")
    public Response getHashFieldValue(@PathParam("lowlevel") boolean lowLevel, @PathParam("key") String key, @PathParam("field") String field) {
        if (lowLevel) {
            return Response.ok(lowLevelApi.getHashFieldValue(key, field).await().indefinitely()).build();
        }
        return Response.ok(highLevelAPI.getHashFieldValue(key, field).await().indefinitely()).build();
    }

    @PUT
    @Path("/setHashField/{key}/{field}/{value}/{lowlevel}")
    public Response setHashField(@PathParam("lowlevel") boolean lowLevel, @PathParam("key") String key, @PathParam("field") String field, @PathParam("value") String value) {
        if (lowLevel) {
            lowLevelApi.hmsetField(key, value, field);
        } else {
            highLevelAPI.hmsetField(key, value, field);
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/{key}/{lowlevel}")
    public Response delete(@PathParam("lowlevel") boolean lowLevel, @PathParam("key") String key) {
        if (lowLevel) {
            lowLevelApi.delete(key);
        } else {
            highLevelAPI.delete(key);
        }
        return Response.ok().build();
    }

    @POST
    @Path("/setHashWithExpiration/{key}/{expiration}/{lowlevel}")
    public Response setHashWithExpiration(@PathParam("lowlevel") boolean lowLevel, @PathParam("key") String key, @PathParam("expiration") int expiration, Map<String, Object> json) {
        if (lowLevel) {
            lowLevelApi.insertHashWithExpiration(key, new JsonObject(json), expiration);
        } else {
            highLevelAPI.insertHashWithExpiration(key, new JsonObject(json), expiration);
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/flushall")
    public Response flushall() {
        highLevelAPI.flushAll();
        return Response.ok().build();
    }

}
