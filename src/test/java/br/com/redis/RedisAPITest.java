package br.com.redis;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;

@QuarkusTest
public class RedisAPITest {

    @Test
    public void testRedisSetOperation() {
        given()
                .accept(ContentType.JSON)
                .when()
                .delete("redis/flushall")
                .then()
                .statusCode(200);

        given()
                .accept(ContentType.JSON)
                .when()
                .get("redis/get/test/false")
                .then()
                .statusCode(200)
                .body(equalTo(""));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("redis/set/test/123/false")
                .then()
                .statusCode(200);

        given()
                .accept(ContentType.JSON)
                .when()
                .get("redis/get/test/false")
                .then()
                .statusCode(200)
                .body(equalTo("123"));
    }
}