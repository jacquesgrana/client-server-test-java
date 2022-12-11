import managers.MainManager;
import vues.MainWindowApp;

import java.awt.*;

public class RunApp {

    public static void main(String[] args) {
        System.out.println("lancement app");
        MainManager manager = new MainManager();
        //mainWindowApp.setVisible(true);
        /*
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainManager manager = new MainManager();
            }
        });*/
    }
}
