import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import packfinder.ClassFinder;
import static packfinder.ClassFinder.check;
import static packfinder.ClassFinder.compare;
import static packfinder.ClassFinder.create;

@DisplayName("Tests are about ClassFinder\n")
public class ClassFinderTest {

    @Test
    @DisplayName("Test #11\n")
    public void test11() {

        assertTrue( compare("FB",    "FooBarBaz"));
        assertTrue( compare("FBar",  "FooBarBaz"));
        assertTrue( compare("FBar",  "FooBar"));
        assertTrue( compare("FBaz",  "FooBarBaz"));
        assertTrue( compare("FBaBz", "FooBarBaz"));
        assertTrue( compare("FBB",   "FooBarBaz"));
        assertTrue( compare("FoBa",  "FooBar"));
        assertTrue( compare("FBa",   "FooBar"));
    }

    @Test
    @DisplayName("Test #12\n")
    public void test12() {
        assertTrue(!compare("FZ",    "FooBar"));
        assertTrue(!compare("BF ",   "FooBar"));
    }

    @Test
    @DisplayName("Test #13\n")
    public void test13() {

        assertTrue( compare("fbb", "FooBarBaz"));
        assertTrue(!compare("fBb", "FooBarBaz"));
    }

    @Test
    @DisplayName("Test #14\n")
    public void test14() {

        assertTrue(!compare("FBar ", "FooBarBaz"));
        assertTrue( compare("FBaz ", "FooBarBaz"));
    }

    @Test
    @DisplayName("Test #15\n")
    public void test15() {

        assertTrue( compare("F*Ba",    "FooBar"));
        assertTrue( compare("B*rBaz",  "FooBarBaz"));
        assertTrue(!compare("B*rBaz",  "BrBaz"));
        assertTrue(!compare("B**rBaz", "BrBaz"));
    }

    @Test
    @DisplayName("Test #21\n")
    public void test21() {
        ClassFinder finder = create(Arrays.asList(
                "a.b.FooBarBaz",
                "a.b.AltFooBarBaz",
                "b.c.AltFooBar",
                "a.b.something",
                "c.d.FooBar",
                "c.d.simpleClass",
                "e.f.BarBaz",
                "e.f.AltBarBaz"));

        List<String> solution1 = finder.findClass("AFBB");
        List<String> solution2 = finder.findClass("FBB");
        List<String> solution3 = finder.findClass("fb");
        List<String> solution4 = finder.findClass("some");
        List<String> solution5 = finder.findClass("AZ");

        assertTrue( check(solution1, Arrays.asList("a.b.AltFooBarBaz")));

        assertTrue( check(solution2, Arrays.asList("a.b.AltFooBarBaz",
                "a.b.FooBarBaz")));

        assertTrue( check(solution3, Arrays.asList("b.c.AltFooBar",
                "a.b.AltFooBarBaz",
                "c.d.FooBar",
                "a.b.FooBarBaz")));

        assertTrue( check(solution4, Arrays.asList("a.b.something")));

        assertTrue( check(solution5, Arrays.asList()));
    }

    @Test
    @DisplayName("Test #22\n")
    public void test22() {
        ClassFinder finder = create(Arrays.asList(
                "a.b.FooBarBaz",
                "a.b.AltFooBarBaz",
                "b.c.AltFooBar",
                "a.b.something",
                "c.d.FooBar",
                "c.d.simpleClass",
                "e.f.BarBaz",
                "e.f.AltBarBaz"));

        List<String> solution1 = finder.findClass("*fb");
        List<String> solution2 = finder.findClass("***fb");
        List<String> solution3 = finder.findClass("*Class");

        assertTrue( check(solution1, Arrays.asList("b.c.AltFooBar",
                "a.b.AltFooBarBaz")));

        assertTrue( check(solution2, Arrays.asList("b.c.AltFooBar",
                "a.b.AltFooBarBaz")));

        assertTrue( check(solution3, Arrays.asList("c.d.simpleClass")));

    }

    @Test
    @DisplayName("Test #23\n")
    public void test23() {
        ClassFinder finder = create(Arrays.asList(
                "a.b.FooBarBaz",
                "a.b.AltFooBarBaz",
                "b.c.AltFooBar",
                "a.b.something",
                "c.d.FooBar",
                "c.d.simpleClass",
                "e.f.BarBaz",
                "e.f.AltBarBaz"));

        List<String> solution1 = finder.findClass("something ");
        List<String> solution2 = finder.findClass("*Bar ");

        assertTrue( check(solution1, Arrays.asList("a.b.something")));

        assertTrue( check(solution2, Arrays.asList("b.c.AltFooBar",
                "c.d.FooBar")));

    }
}
