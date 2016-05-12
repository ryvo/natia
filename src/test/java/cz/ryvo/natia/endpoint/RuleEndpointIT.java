package cz.ryvo.natia.endpoint;

import cz.ryvo.natia.IntegrationTest;
import org.junit.Test;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static java.util.Collections.singletonMap;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;

public class RuleEndpointIT extends IntegrationTest {

    @Test
    public void testCreateRule() throws IOException {
        // Create rule
        Long id = given()
                .accept(JSON)
                .contentType(JSON)
                .content(getRequest("create-new-rule.json", singletonMap("ruleName", "Test rule")))
                .log().all()

                .when()
                .post("/api/v1/rules")

                .then()
                .log().all()
                .statusCode(SC_CREATED)
                .extract().body().jsonPath().getLong("id");

        // Get rule
        given()
                .accept(JSON)
                .contentType(JSON)
                .log().all()

                .when()
                .get("/api/v1/rules/{ruleId}", id)

                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("id", notNullValue())
                .body("name", equalTo("ruleName"))
                .body("inputArticles.size()", is(2))
                .body("inputArticles.code", containsInAnyOrder("00001/01", "00001/02"))
                .body("inputArticles find { it.code = '00001/01'}.description", equalTo("Input article 1"))
                .body("inputArticles find { it.code = '00001/01'}.amount", equalTo(11))
                .body("inputArticles find { it.code = '00001/02'}.description", equalTo("Input article 1"))
                .body("inputArticles find { it.code = '00001/02'}.amount", equalTo(12))
                .body("outputArticles find { it.code = '00002/01'}.description", equalTo("Output article 1"))
                .body("outputArticles find { it.code = '00002/01'}.amount", equalTo(21))
                .body("outputArticles find { it.code = '00002/02'}.description", equalTo("Output article 1"))
                .body("outputArticles find { it.code = '00002/02'}.amount", equalTo(22));
    }
}
