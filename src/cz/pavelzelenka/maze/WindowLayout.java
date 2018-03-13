package cz.pavelzelenka.maze;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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

	/** Kresba */
	private Drawing drawing;
	
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
		borderPane.setBottom(getBottomPane());
		return borderPane;
	}
	
	/**
	 * Vrati spodni panel
	 * @return spodni panel
	 */
	public Parent getBottomPane() {
		String scount = "Steps: ";
		String tardis = "Target distance: ";
		
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(5D, 5D, 5D, 5D));
		hBox.setMinHeight(40D);
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(10D);
		Text steps = new Text(scount + Statistics.steps.get());
		Text targetDistance = new Text(tardis + Statistics.targetDistance.get());
		Pane pane = new Pane();
		HBox.setHgrow(pane, Priority.ALWAYS);
		Button easier = new Button("←");
		Button harder = new Button("→");
		hBox.getChildren().addAll(steps, targetDistance, pane, easier, harder);
	
		easier.setOnAction(action -> {
			if(drawing != null) {
				drawing.prevMaze();
			}
		});
		
		harder.setOnAction(action -> {
			if(drawing != null) {
				drawing.nextMaze();
			}
		});
		
		Statistics.steps.addListener((observable, oldValue, newValue) -> {
			steps.setText(scount + newValue);
		});
		
		Statistics.targetDistance.addListener((observable, oldValue, newValue) -> {
			targetDistance.setText(tardis + newValue);
		});
		
		return hBox;
	}
	
	/**
	 * Vrati panel kresby
	 * @return panel kresby
	 */
	public Parent getCanvasPane() {
		BorderPane pane = new BorderPane();
		
        Canvas canvas = new Canvas(pane.getWidth(), pane.getHeight());
        
        pane.widthProperty().addListener(event -> canvas.setWidth(pane.getWidth()));
        pane.heightProperty().addListener(event -> canvas.setHeight(pane.getHeight()));
        
        drawing = new Drawing(canvas);
        drawing.draw();
        
        pane.getChildren().add(canvas);
        
		return pane;
	}

}
