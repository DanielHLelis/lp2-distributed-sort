package br.cefetmg.parallelSort.IO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class FileInputParserTest {
    @Test
    public void shortFileReadTestSuccess() throws IOException {
        List<String> expected = Arrays.asList(
                "-31745",
                "-55297",
                "74752",
                "-78852",
                "3075",
                "-51205",
                "18438",
                "-88072",
                "66568",
                "43019",
                "-95243",
                "-54285",
                "95245",
                "52236",
                "-90128",
                "-94226",
                "-75796",
                "-4120",
                "-97306",
                "47128"
        ); // Expected list

        FileInputParser parser = new FileInputParser(this.getClass().getResource("/inputs/short_input.txt").getFile());
        List<String> result = parser.parse();

        assertLinesMatch(expected, result);
    }
}
