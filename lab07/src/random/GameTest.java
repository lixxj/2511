package random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

public class GameTest {
    @Test
    public void testBattle() {
        Game g = new Game(1);
        Random random = new Random(1);
        assertEquals(random.nextInt(100) < 50, g.battle());

        g = new Game(5);
        random = new Random(5);
        assertEquals(random.nextInt(100) < 50, g.battle());
    }
}