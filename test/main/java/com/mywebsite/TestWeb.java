
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.com.mywebsite.main.Web;

class TestWeb
{
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
        fail("Not yet implemented");
    }

}
