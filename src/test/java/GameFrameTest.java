import View.GameFrame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameFrameTest {

    @Test
    public void GameFrameTest_windowWillBeInstatiatedTwice_TheTwoInstancesShouldBeTheSameObject() {
        int dimension = 400;
        GameFrame gameFrame1 = GameFrame.getInstance(dimension);
        GameFrame gameFrame2 = GameFrame.getInstance(dimension);

        assertEquals(gameFrame1.hashCode(), gameFrame2.hashCode());
    }
}
