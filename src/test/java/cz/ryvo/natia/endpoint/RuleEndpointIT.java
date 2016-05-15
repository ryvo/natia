package cz.ryvo.natia.endpoint;

import com.google.common.collect.ImmutableMap;
import cz.ryvo.natia.IntegrationTest;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static com.google.common.collect.ImmutableMap.of;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static java.util.Collections.singletonMap;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class RuleEndpointIT extends IntegrationTest {

    @Test
    public void testCreateRule() throws IOException {
        String ruleName = "Test rule 1";

        // Create rule
        Long id = given()
                .accept(JSON)
                .contentType(JSON)
                .content(getRequest("create-new-rule.json", singletonMap("ruleName", ruleName)))
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
                .body("name", equalTo(ruleName))
                .body("inputArticles.size()", is(2))
                .body("inputArticles.code", Matchers.hasItems("00001/01", "00001/02"))
                .body("inputArticles.find {p -> p.code == '00001/01'}.description", equalTo("Input article 1"))
                .body("inputArticles.find {p -> p.code == '00001/01'}.amount", equalTo(11))
                .body("inputArticles.find {p -> p.code == '00001/02'}.description", equalTo("Input article 2"))
                .body("inputArticles.find {p -> p.code == '00001/02'}.amount", equalTo(12))
                .body("outputArticles.code", Matchers.hasItems("00002/01", "00002/02"))
                .body("outputArticles.find {p -> p.code == '00002/01'}.description", equalTo("Output article 1"))
                .body("outputArticles.find {p -> p.code == '00002/01'}.amount", equalTo(21))
                .body("outputArticles.find {p -> p.code == '00002/02'}.description", equalTo("Output article 2"))
                .body("outputArticles.find {p -> p.code == '00002/02'}.amount", equalTo(22));
    }

    @Test
    public void testUpdateRule() throws IOException {
        String ruleName = "Test rule 2";
        String updatedRuleName = "Updated test rule 2";

        // Create rule
        Long id = given()
                .accept(JSON)
                .contentType(JSON)
                .content(getRequest("create-new-rule.json", singletonMap("ruleName", ruleName)))
                .log().all()

                .when()
                .post("/api/v1/rules")

                .then()
                .log().all()
                .statusCode(SC_CREATED)
                .extract().body().jsonPath().getLong("id");

        // Update rule
        given()
                .accept(JSON)
                .contentType(JSON)
                .content(getRequest("update-rule.json", singletonMap("ruleName", updatedRuleName)))
                .log().all()

                .when()
                .put("/api/v1/rules/{ruleId}", id)

                .then()
                .log().all()
                .statusCode(SC_OK);

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
                .body("name", equalTo(updatedRuleName))
                .body("inputArticles.size()", is(3))
                .body("inputArticles.code", Matchers.hasItems("00003/01", "00003/02", "00003/03"))
                .body("inputArticles.find {p -> p.code == '00003/01'}.description", equalTo("Updated input article 1"))
                .body("inputArticles.find {p -> p.code == '00003/01'}.amount", equalTo(31))
                .body("inputArticles.find {p -> p.code == '00003/02'}.description", equalTo("Updated input article 2"))
                .body("inputArticles.find {p -> p.code == '00003/02'}.amount", equalTo(32))
                .body("inputArticles.find {p -> p.code == '00003/03'}.description", equalTo("Updated input article 3"))
                .body("inputArticles.find {p -> p.code == '00003/03'}.amount", equalTo(33))
                .body("outputArticles.code", Matchers.hasItems("00004/01"))
                .body("outputArticles.find {p -> p.code == '00004/01'}.description", equalTo("Updated output article 1"))
                .body("outputArticles.find {p -> p.code == '00004/01'}.amount", equalTo(41));
    }

    @Test
    public void testCreateRuleWithRank() throws IOException {
            String ruleName1 = "Rule 1";
            String ruleName2 = "Rule 2";

            // Create rule 1
            given()
                    .accept(JSON)
                    .contentType(JSON)
                    .content(getRequest("create-new-rule.json", singletonMap("ruleName", ruleName1)))
                    .log().all()

                    .when()
                    .post("/api/v1/rules")

                    .then()
                    .log().all()
                    .statusCode(SC_CREATED)
                    .extract().body().jsonPath().getLong("id");

            // Create rule 2 with rank 0
            given()
                    .accept(JSON)
                    .contentType(JSON)
                    .content(getRequest("create-new-rule-with-rank.json", of("ruleName", ruleName2, "rank", 0)))
                    .log().all()

                    .when()
                    .post("/api/v1/rules")

                    .then()
                    .log().all()
                    .statusCode(SC_CREATED)
                    .extract().body().jsonPath().getLong("id");

            // Check rank of all rules
            given()
                    .accept(JSON)
                    .log().all()

                    .when()
                    .get("/api/v1/rules")

                    .then()
                    .log().all()
                    .statusCode(SC_OK)
                    .body("$.size()", equalTo(3))
                    .body("[0].name", equalTo(ruleName2))
                    .body("[0].rank", equalTo(0))
                    .body("[1].name", equalTo("Default rule"))
                    .body("[1].rank", equalTo(1))
                    .body("[2].name", equalTo(ruleName1))
                    .body("[2].rank", equalTo(2));
    }

    @Test
    public void testSetRuleRank() throws IOException {
            String ruleName1 = "Rule 1";
            String ruleName2 = "Rule 2";

            // Create rule 1
            Long id1 = given()
                    .accept(JSON)
                    .contentType(JSON)
                    .content(getRequest("create-new-rule.json", singletonMap("ruleName", ruleName1)))
                    .log().all()
                    .when()
                    .post("/api/v1/rules")
                    .then()
                    .log().all()
                    .statusCode(SC_CREATED)
                    .extract().body().jsonPath().getLong("id");

            // Create rule 2
            Long id2 = given()
                    .accept(JSON)
                    .contentType(JSON)
                    .content(getRequest("create-new-rule.json", singletonMap("ruleName", ruleName2)))
                    .log().all()
                    .when()
                    .post("/api/v1/rules")
                    .then()
                    .log().all()
                    .statusCode(SC_CREATED)
                    .extract().body().jsonPath().getLong("id");

            // Move rule 2 at the top
            given()
                    .accept(JSON)
                    .log().all()
                    .when()
                    .put("/api/v1/rules/{ruleId}/rank/{rank}", id2, 0)
                    .then()
                    .log().all()
                    .statusCode(SC_OK);

            // Move rule 0 at the bottom
            given()
                    .accept(JSON)
                    .log().all()
                    .when()
                    .put("/api/v1/rules/{ruleId}/rank/{rank}", 1, 2)
                    .then()
                    .log().all()
                    .statusCode(SC_OK);

            // Check rank of all rules
            given()
                    .accept(JSON)
                    .log().all()

                    .when()
                    .get("/api/v1/rules")

                    .then()
                    .log().all()
                    .statusCode(SC_OK)
                    .body("$.size()", equalTo(3))
                    .body("[0].name", equalTo(ruleName2))
                    .body("[0].rank", equalTo(0))
                    .body("[1].name", equalTo(ruleName1))
                    .body("[1].rank", equalTo(1))
                    .body("[2].name", equalTo("Default rule"))
                    .body("[2].rank", equalTo(2));
    }
}
