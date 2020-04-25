import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.config;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class BaseTest {
    protected static RestAssuredConfig conf;
    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSuccessSpec;
    protected static ResponseSpecification responseBadRequestSpec;
    protected String inboxProjectId = "2234262640";
    protected String saturdayProjectId = "2234318358";

    @BeforeClass
    public static void Setup() {
        conf = config.encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://api.todoist.com")
                .setBasePath("rest/v1")
                .setContentType(ContentType.JSON)
                .setConfig(conf)
                .log(LogDetail.ALL)
                .build();
        responseSuccessSpec = new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .log(LogDetail.ALL)
                .build();
        responseBadRequestSpec = new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .expectContentType(ContentType.TEXT)
                .log(LogDetail.ALL)
                .build();
    }
}
