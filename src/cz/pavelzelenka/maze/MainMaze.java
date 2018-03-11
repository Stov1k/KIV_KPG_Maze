package cz.pavelzelenka.maze;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hlavni trida aplikace
 * @author Pavel Zelenka A16B0176P
 * @version 2018-03-09
 */
public class MainMaze extends Application {

	/** Popisek okna aplikace */
	public static final String WINDOW_TITLE = "Maze";
	/** Minimalni sirka okna aplikace */
	public static final double MIN_WINDOW_WIDTH = 600D;
	/** Minimalni vyska okna aplikace */
	public static final double MIN_WINDOW_HEIGHT = 600D;
	
	public static Scene scene;
	
	/** Vychozi rozvrzeni okna */
	private WindowLayout wl;
	
	/** 
	 * Hlavni metoda aplikace
	 * @param args argumenty pri spusteni
	 */
    public static void main(String[] args) {
        launch(args);
    }
	
    /**
     * Spusteni GUI aplikace
     */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(WINDOW_TITLE);
		primaryStage.setMinWidth(MIN_WINDOW_WIDTH);
		primaryStage.setMinHeight(MIN_WINDOW_HEIGHT);
		wl = new WindowLayout(primaryStage);
		scene = new Scene(wl.get());
        primaryStage.setScene(scene);
        PlayerController.INSTANCE.setScene(scene);
        primaryStage.show();
	}
	
}
