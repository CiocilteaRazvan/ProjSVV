package lab1;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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

    @Test
    void testStrToIntOneDigit() {
        assertEquals(0, strConv.strToInt("0"));
    }
}
