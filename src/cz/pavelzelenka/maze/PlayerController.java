package cz.pavelzelenka.maze;

import java.util.LinkedList;
import java.util.Queue;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

/**
 * Ovladani hrace
 * @author Pavel Zelenka A16B0176P
 * @version 2018-03-10
 */
public class PlayerController {

	/** Instance */
	public static final PlayerController INSTANCE = new PlayerController();
	
	/** Hrac */
	private Player player;

	/** Scena */
	private Scene scene;
	
	/** Kresba */
	private Drawing drawing;
	
	/** Aktivni ovladani */
	private boolean active;
	
	/** Klavesa */
	private Queue<KeyCode> keysQueue = new LinkedList<KeyCode>();
	
	/** Kontruktor */
	private PlayerController() {
		active = false;
	}
	
	/**
	 * Prida ovladani hrace
	 */
	private void addControls() {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
        		/** Ignorovani klaves behem animace */
            	if(drawing.isRunning()) {
            		keysQueue.add(event.getCode());
            		return;
            	} else {
            		keyAction(event.getCode());
            	}
            }
        });
	}

	private void keyAction(KeyCode keyCode) {
    	/** Pozice */
    	Cell position = player.getPosition();
    	/** Zmena pozice */
    	boolean positionChanged = false;
    	/** Sousedni bunky */
    	Cell up = null, right = null, left = null, down = null;
    	for(Pair<Integer, Cell> cell : position.getNeighbors()) {
    		int side = cell.getKey();
    		switch(side) {
    			case 0: up = cell.getValue(); break;
    			case 1: right = cell.getValue(); break;
    			case 3: left = cell.getValue(); break;
    			case 2: down = cell.getValue(); break;
    		}
    	}
    	/** Zmena pozice */
        switch (keyCode) {
            case UP:
            	if(up != null && position.getConnectivity()[0]) {
            		player.setPosition(up);
            		positionChanged = true;
            	} else {
            		keysQueue.clear();
            	}
            	break;
            case RIGHT:
            	if(right != null && position.getConnectivity()[1]) {
            		player.setPosition(right);
            		positionChanged = true;
            	} else {
            		keysQueue.clear();
            	}
            	break;
            case DOWN:
            	if(down != null && position.getConnectivity()[2]) {
            		player.setPosition(down);
            		positionChanged = true;
            	} else {
            		keysQueue.clear();
            	}
            	break;
            case LEFT:
            	if(left != null && position.getConnectivity()[3]) {
            		player.setPosition(left);
            		positionChanged = true;
            	} else {
            		keysQueue.clear();
            	}
            	break;
        }
        /** Vykresleni zmen */
        if(positionChanged) {
        	drawing.move(player, position, player.getPosition());
        }
	}
	
	/**
	 * zpeacovani dalsi klavesy z fronty
	 */
	public void processNextKeyFromQueue() {
		if(!keysQueue.isEmpty()) {
			keyAction(keysQueue.poll());
		}
	}
	
	/** 
	 * Aktivace ovladani hrace
	 */
	private void activation() {
		if(!this.active) {
			if(player != null && scene != null && drawing != null) {
				addControls();
				keysQueue.clear();
			}
		}
	}
	
	/**
	 * Vrati hrace
	 * @return hrac
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Nastavi hrace
	 * @param player hrac
	 */
	public void setPlayer(Player player) {
		this.player = player;
		activation();
	}

	/**
	 * Vrati scenu
	 * @return scena
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * Nastavi scenu
	 * @param scene scena
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
		activation();
	}

	/**
	 * Vrati kresbu
	 * @return kresba
	 */
	public Drawing getDrawing() {
		return drawing;
	}

	/**
	 * Nastavi kresbu
	 * @param drawing kresba
	 */
	public void setDrawing(Drawing drawing) {
		this.drawing = drawing;
		activation();
	}
	
}
