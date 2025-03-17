package com.test.assignment12;

import com.shpp.p2p.cs.vfranchuk.assignment12.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class Assignment12Part1Test {

    @Test
    public void testFindSilhouettesWithoutParameter() {
        Assignment12Part1 assignment12Part1 = new Assignment12Part1();
        String[] args = {};
        Assignment12Part1.main(args);
        assertEquals(4, assignment12Part1.getSilhouettesCount());
    }

    @ParameterizedTest
    @CsvSource({
            "'assets/test.png', '4'",
            "'assets/8.png', '8'",
            "'assets/9.png', '1'",
            "'assets/test3.jpg', '1'",
            "'assets/test4.jpg', '12'",
            "'assets/test5.png', '11'",
            "'assets/12.jpg', '4'",
            "'assets/11.jpg', '6'",
            "'assets/10.png', '4'",
            "'assets/3.jpg', '7'",
            "'assets/test7.jpg', '2'"
    })
    public void testFindSilhouettesBigObjects(String path, String expected) {
        Assignment12Part1 assignment12Part1 = new Assignment12Part1();
        String[] args = {path};
        Assignment12Part1.main(args);
        assertEquals(Integer.parseInt(expected), assignment12Part1.getSilhouettesCount());
    }
}
