package test.main.java.com.mywebsite;

import main.java.com.mywebsite.Main;
import org.junit.jupiter.api.*;

class TestWeb
{
    String url = "localhost:4000";
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        Main web = new Main();
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
        /*given()
        .when().get(url+"?get=insert")
        .then().statusCode(200).statusLine("HTTP/1.1 200 OK")
//        .body("description", equalTo(""))
//        .body("hostURL", equalTo("172.18.65.85:8000"))
         */
        ;
    }

}
