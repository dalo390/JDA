import net.dv8tion.jda.api.EmbedBuilder;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.awt.*;


/*
 * Class Mockito Test
 * Simple test suit for selected mocking 
 */

public class MockitoTest {
    @Mock
    Color color;
    int col;
    EmbedBuilder mockEmbed;

    @BeforeEach
    public void setup() {
        color = mock(Color.class);
        col = (int)Math.random();
        mockEmbed = mock(EmbedBuilder.class);
    }

    @Test
    public void mockitoTestColor() throws Exception {
        assertNotNull(color);
        mockEmbed.setColor(color);
        verify(mockEmbed).setColor(color);
    }

    @Test
    public void mockitoTestColor1() throws Exception {
        assertNotNull(color);
        EmbedBuilder realEmbed = new EmbedBuilder();
    }


    @Test
    public void mockitoTestColorInt() throws Exception {
        assertNotNull(col);
        mockEmbed.setColor(col);
        verify(mockEmbed).setColor(col);
    }

    @Test
    public void mockitoTestColorInt1() throws Exception {
        assertNotNull(col);
        EmbedBuilder realEmbed = new EmbedBuilder();
        //assertEquals(realEmbed.setColor(col), mockEmbed.setColor(col));
    }


}
