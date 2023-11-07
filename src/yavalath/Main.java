package yavalath;


import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        if(args.length > 0) {
            switch(args[0]) {
                case "light":
                    FlatLightLaf.setup();
                    break;
                case "dark":
                    FlatDarkLaf.setup();
                    break;
                case "intellij":
                    FlatIntelliJLaf.setup();
                    break;
                case "darcula":
                    FlatDarculaLaf.setup();
                    break;
                default:
                    break;
            }
        }
        MainMenu.main(args);
    }
}
