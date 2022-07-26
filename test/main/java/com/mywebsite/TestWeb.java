
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.com.mywebsite.main.Web;

class TestWeb
{
    String url = "localhost:4000";
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        Web web = new Web();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception
    {
    }

    @BeforeEach
    void setUp() throws Exception
    {
    }

    @AfterEach
    void tearDown() throws Exception
    {
    }

    @Test
    void test()
    {
        given()
        .when().get(url+"?get=insert")
        .then().statusCode(200).statusLine("HTTP/1.1 200 OK")
//        .body("description", equalTo(""))
//        .body("hostURL", equalTo("172.18.65.85:8000"))
        ;
    }

}
