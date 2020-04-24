import io.restassured.http.Method;
import models.Task;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TaskTest extends BaseTest {

    static String taskInboxId = "";
    static String taskSaturdayId = "";

    @Test
    public void ShouldCreateTask() {

        Task taskObject = given()
                .spec(requestSpec)
                .body("{\"content\":\"Create New Task in Inbox\"}")
                .when()
                .request(Method.POST, "tasks?token=" + System.getenv("TODOIS_TOKEN"))
                .then()
                .assertThat()
                .spec(responseSuccessSpec)
                .body("priority", equalTo(1))
                .body("section_id", equalTo(0))
                .body("comment_count", equalTo(0))
                .body("completed", equalTo(false))
                .extract()
                .as(Task.class);
        taskInboxId = taskObject.id;
        Assert.assertThat(taskObject.project_id, equalTo(inboxProjectId));
        Assert.assertThat(taskObject.url, containsString(taskObject.id));
    }

    @Test
    public void ShouldCreateTaskInProject() {

        Task taskObject = given()
                .spec(requestSpec)
                .body("{\"content\":\"Create New Task in SaturdayProject\"," +
                        "\"project_id\":" + saturdayProjectId + "}")
                .when()
                .request(Method.POST, "tasks?token=" + System.getenv("TODOIS_TOKEN"))
                .then()
                .assertThat()
                .spec(responseSuccessSpec)
                .extract()
                .as(Task.class);
        taskSaturdayId = taskObject.id;
        Assert.assertThat(taskObject.project_id, equalTo(saturdayProjectId));
    }

    @Test
    public void ShouldNotCreateTaskWithTwoDue() {

        given()
                .spec(requestSpec)
                .body("{\"content\":\"Create New Task with due_string & due_date\"," +
                        "\"due_string\":\"2020-07-01 12:00\"" +
                        "\"due_date\":\"2020-09-01\"}")
                .when()
                .request(Method.POST, "tasks?token=" + System.getenv("TODOIS_TOKEN"))
                .then()
                .assertThat()
                .spec(responseBadRequestSpec);
    }

    @AfterClass
    public static void DeleteCreatedTask() {
        if (!taskInboxId.isEmpty()) {
            given()
                    .spec(requestSpec)
                    .when()
                    .pathParam("taskId", taskInboxId)
                    .request(Method.DELETE, "tasks/{taskId}?token=" + System.getenv("TODOIS_TOKEN"))
                    .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
        }
        if (!taskSaturdayId.isEmpty()) {
            given()
                    .spec(requestSpec)
                    .when()
                    .pathParam("taskId", taskSaturdayId)
                    .request(Method.DELETE, "tasks/{taskId}?token=" + System.getenv("TODOIS_TOKEN"))
                    .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
        }
    }
}
