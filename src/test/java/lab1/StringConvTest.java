package lab1;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringConvTest {
    static StringConv strConv;

    @BeforeAll
    static void init() {
        strConv = new StringConv();
    }

    @Disabled
    @Test
    void testDisabledAnnotation() {
        assertTrue(false);
    }

    @Nested
    @DisplayName("Test strToInt() method from StringConv class")
    class TestStrToInt {
        @Test
        void testOneDigit() {
            assertEquals(0, strConv.strToInt("0"));
            assertEquals(8, strConv.strToInt("8"));
            assertEquals(3, strConv.strToInt("3"));
        }
    }
}
