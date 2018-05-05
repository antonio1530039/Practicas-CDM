package dispositivos_moviles_may_ago_2018.upvictoria.com.p01_molina_de_la_fuente_jose_antonio;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("dispositivos_moviles_may_ago_2018.upvictoria.com.p01_molina_de_la_fuente_jose_antonio", appContext.getPackageName());
    }
}
