package com.github.thyago.ifood.restaurant;

import org.approvaltests.Approvals;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(RestaurantTestLifecycleManager.class)
public class RestaurantResourceTest {

    @Test
    public void testListRestaurants() {
        String result = RestAssured.given()
                .when().get("/restaurant")
                .then()
                .statusCode(200)
                .extract().asString();
        Approvals.verifyJson(result);
    }
}
