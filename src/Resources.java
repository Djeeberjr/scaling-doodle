import javax.swing.*;
import java.util.Base64;

public class Resources {
    public static final String SPRITES[] = {
        "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"
    };

    public static ImageIcon getSprite(int i){
        return new ImageIcon(Base64.getDecoder().decode(Resources.SPRITES[i]));
    }

}
