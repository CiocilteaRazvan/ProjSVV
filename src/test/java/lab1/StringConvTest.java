package lab1;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

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

        @Test
        void testMultipleDigits() {
            assertEquals(11, strConv.strToInt("11"));
            assertEquals(3771, strConv.strToInt("3771"));
            assertEquals(919929999, strConv.strToInt("919929999"));
        }

        @Test
        void invalidInput() {
            assertThrows(NumberFormatException.class, () -> strConv.strToInt("^!@#"));
            assertThrows(NumberFormatException.class, () -> strConv.strToInt("number"));
            assertThrows(NumberFormatException.class, () -> strConv.strToInt("123.5"));
            assertThrows(NumberFormatException.class, () -> strConv.strToInt("123,5"));
            assertThrows(NumberFormatException.class, () -> strConv.strToInt(null));
            assertThrows(NumberFormatException.class, () -> strConv.strToInt("123alpha"));
        }
    }
}
