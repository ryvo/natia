package cz.ryvo.natia;

import com.jayway.restassured.RestAssured;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.util.Collections.emptyMap;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = NatiaApplication.class)
@TestPropertySource("classpath:application-test.properties")
@Sql(scripts = "classpath:db/data.sql")
public abstract class IntegrationTest {

    public static final String REQUEST_FILE_DIR = "requests";

    @Value("${local.server.port}")
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    public String getRequest(String fileName, Map<String, Object> params) throws IOException {
        if (params == null) {
            params = emptyMap();
        }
        File file = getRequestFile(fileName);
        String content = FileUtils.readFileToString(file, "UTF-8");
        return evaluateRequestContent(content, params);
    }

    private String evaluateRequestContent(String content, Map<String, Object> params) {
        return new StrSubstitutor(params, "${", "}").replace(content);
    }

    private File getRequestFile(String fileName) throws IOException {
        return new ClassPathResource(new File(REQUEST_FILE_DIR, fileName).getPath()).getFile();
    }
}
