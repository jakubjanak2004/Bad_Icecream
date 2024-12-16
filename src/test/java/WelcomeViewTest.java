import View.WelcomeView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WelcomeViewTest {

    @Test
    public void WelcomeViewConstructorTest_windowWillBeInstatiatedTwice_TheTwoInstancesShouldBeTheSameObject() {
        int dimension = 400;
        WelcomeView welcomeView1 = WelcomeView.getInstance(dimension);
        WelcomeView welcomeView2 = WelcomeView.getInstance(dimension);

        assertEquals(welcomeView1.hashCode(), welcomeView2.hashCode());

        welcomeView1.dispose();
    }

}
