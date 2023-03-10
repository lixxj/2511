package random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

public class GameTest {
    @Test
    public void testBattle1() {
        Game g = new Game(1);
        Random random = new Random(1);
        assertEquals(random.nextInt(100) < 50, g.battle());
    }

    @Test
    public void testBattle2() {
        Game g = new Game(2);
        Random random = new Random(2);
        assertEquals(random.nextInt(100) < 50, g.battle());
    }

    @Test
    public void testBattle3() {
        Game g = new Game(3);
        Random random = new Random(3);
        assertEquals(random.nextInt(100) < 50, g.battle());
    }

    @Test
    public void testBattle4() {
        Game g = new Game(4);
        Random random = new Random(4);
        assertEquals(random.nextInt(100) < 50, g.battle());
    }

    @Test
    public void testBattle5() {
        Game g = new Game(5);
        Random random = new Random(5);
        assertEquals(random.nextInt(100) < 50, g.battle());
    }

    @Test
    public void testBattle6() {
        long CTM = System.currentTimeMillis();
        Game g = new Game(CTM);
        Random random = new Random(CTM);
        assertEquals(random.nextInt(100) < 50, g.battle());
    }
}
