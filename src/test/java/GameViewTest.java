import Logic.GameController;
import View.GameView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameViewTest {

    @Test
    public void GameViewTest_windowWillBeInstatiatedTwice_TheTwoInstancesShouldBeTheSameObject() {
        GameController gameController = new GameController();

        GameView gameView1 = GameView.getInstance(gameController);
        GameView gameView2 = GameView.getInstance(gameController);

        assertEquals(gameView1.hashCode(), gameView2.hashCode());
    }
}
