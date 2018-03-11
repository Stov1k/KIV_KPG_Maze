package cz.pavelzelenka.maze;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Rozvrzeni okna aplikace
 * @author Pavel Zelenka A16B0176P
 * @version 2018-03-09
 */
public class WindowLayout {

	/** Hlavni stage aplikace */
	private final Stage stage;
	
	/** Zakladni rozvrzeni okna */
	private BorderPane borderPane;

	
	
	/**
	 * Konstruktor
	 * @param stage (hlavni) stage aplikace
	 */
	public WindowLayout(Stage stage) {
		this.stage = stage;
	}
	
	/**
	 * Vrati zakladni rozvrzeni okna aplikace
	 * @return zakladni rozvrzeni okna aplikace
	 */
	public Parent get() {
		borderPane = new BorderPane();
		borderPane.setCenter(getCanvasPane());
		return borderPane;
	}
	
	/**
	 * Vrati panel kresby
	 * @return panel kresby
	 */
	public Parent getCanvasPane() {
		AnchorPane anchorPane = new AnchorPane();

        Canvas canvas = new Canvas(borderPane.getWidth(), borderPane.getHeight());
        
        borderPane.widthProperty().addListener(event -> canvas.setWidth(borderPane.getWidth()));
        borderPane.heightProperty().addListener(event -> canvas.setHeight(borderPane.getHeight()));
        
        Drawing drawing = new Drawing(canvas);
        drawing.draw();
        
		anchorPane.getChildren().add(canvas);
		return anchorPane;
	}
	
}
