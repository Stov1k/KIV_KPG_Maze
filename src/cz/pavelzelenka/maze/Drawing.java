package cz.pavelzelenka.maze;
import java.util.Random;
import java.util.Timer;

import com.sun.prism.paint.Paint;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Kresba
 * @author Pavel Zelenka A16B0176P
 * @version 2018-03-09
 */
public class Drawing {

	/** Sirka pera */
	public static final int MAX_PEN_WIDTH = 5;
	/** Ovladaci prvek kresleni */
	private GraphicsContext g;
	/** Platno */
	private Canvas activeCanvas;
	/** Bludiste */
	private Maze maze = new Maze(10,10);
	/** Animovany pohyb */
	private boolean animated = true;
	/** Cas animace */
	private int time = 0;
	/** Bezici animace */
	private boolean isRunning = false;
	/** Hrac */
	private Player player;
	
	/** Velikost jedne bunky */
	float cellSize;
	/** Levy horni roh bunecne mrizky */
	Point2D leftUpperCorner;
	/** Pravy spodni roh bunecne mrizky */
	Point2D rightBottomCorner;
	
	/**
	 * Vytvoreni instance kresby
	 * @param canvas platno
	 */
	public Drawing(Canvas canvas) {
		this.activeCanvas = canvas;
		g = canvas.getGraphicsContext2D();
		observeCanvasSize();
		createPlayer();
	}
	
	/**
	 * Vytvoreni hrace
	 */
	private void createPlayer() {
		player = new Player(maze.getStart());
		PlayerController.INSTANCE.setDrawing(this);
		PlayerController.INSTANCE.setPlayer(player);
	}
	
	/**
	 * Pozorovani zmen velikosti platna
	 */
	private void observeCanvasSize() {
		activeCanvas.widthProperty().addListener(event -> {
			clear();
			draw();
		});
		
		activeCanvas.heightProperty().addListener(event -> {
			clear();
			draw();
		});
	}
		
	/**
	 * Vycisti plochu
	 */
	public void clear() {
		g.clearRect(0, 0, activeCanvas.getWidth(), activeCanvas.getWidth());
	}
	
	/**
	 * Vykresleni obrazu
	 */
	public void draw() {
		drawGrid();
		drawPlayer();
	}
	
	/**
	 * Vykresleni mrizky
	 */
	public void drawGrid() {
        // nastavi hodnotu mensi strany okna (tzn. vyska nebo sirka)
		int min = (int) Math.min(activeCanvas.getWidth(), activeCanvas.getHeight());
		
		// nastavi stetec
        g.setLineWidth(MAX_PEN_WIDTH);
        g.setStroke(Color.BLACK);
        
        // spocte pozici leveho horniho rohu mrizky a praveho spodniho spodniho rohu
        float startX = 0.5f * (float)activeCanvas.getWidth() - 0.5f * min;
        float startY = 0.5f * (float)activeCanvas.getHeight() - 0.5f * min;

        // spocti velikost jedne bunky
        cellSize = ((float)(min)/(float)maze.getRows());
        
        // Vykresli bunecnou mrizku zvolenou velikosti a barvou pera
        for(int i = 0; i < maze.getRows(); i++) {
        	for(int j = 0; j < maze.getColumns(); j++) {
        		Cell currentCell = maze.getCell(i,j);
        		g.setStroke(Color.BLACK);
        		if(currentCell.equals(maze.getStart())) {
        			g.setFill(Color.rgb(230, 126, 34));
        			g.fillRect(i * cellSize + startX, j * cellSize + startY, cellSize, cellSize);
        		}
        		if(currentCell.equals(maze.getTarget())) {
        			g.setFill(Color.rgb(46, 204, 113));
        			g.fillRect(i * cellSize + startX, j * cellSize + startY, cellSize, cellSize);
        		}
        		if(!currentCell.getConnectivity()[0]) {
        			g.strokeLine(i * cellSize + startX, j * cellSize + startY, (i+1) * cellSize + startX, j * cellSize + startY);
        		}
        		if(!currentCell.getConnectivity()[3]) {
        			g.strokeLine(i * cellSize + startX, j * cellSize + startY, i * cellSize + startX, (j+1) * cellSize + startY);
        		}
        		if(!currentCell.getConnectivity()[2]) {
        			g.strokeLine(i * cellSize + startX, (j+1) * cellSize + startY, (i+1) * cellSize + startX, (j+1) * cellSize + startY);
        		}
        		if(!currentCell.getConnectivity()[1]) {
        			g.strokeLine((i+1) * cellSize + startX, j * cellSize + startY, (i+1) * cellSize + startX, (j+1) * cellSize + startY);
        		}        			
        	}
        }
    }
	
	/**
	 * Vykresleni hrace
	 */
	public void drawPlayer() {
		drawPlayer(player.getPosition().getX(), player.getPosition().getY());
	}
	
	/**
	 * Vykresleni hrace na pozici
	 * @param x souradnice
	 * @param y souradnice
	 */
	public void drawPlayer(double x, double y) {
		int min = (int) Math.min(activeCanvas.getWidth(), activeCanvas.getHeight());
        float startX = 0.5f * (float)activeCanvas.getWidth() - 0.5f * min;
        float startY = 0.5f * (float)activeCanvas.getHeight() - 0.5f * min;
		g.setFill(Color.rgb(231, 76, 60));
		g.setStroke(Color.rgb(44, 62, 80));
		g.fillOval(x * cellSize + startX + cellSize/4, y * cellSize + startY + cellSize/4, cellSize/2, cellSize/2);
		g.strokeOval(x * cellSize + startX + cellSize/4, y * cellSize + startY + cellSize/4, cellSize/2, cellSize/2);
	}
	
	/**
	 * Animace pohybu
	 * @param player hrac
	 * @param from pocatecni pozice
	 * @param to konecna pozice
	 */
	public void move(Player player, Cell from, Cell to) {
		if(animated) {
			new AnimationTimer() {
				@Override
				public void handle(long now) {
					isRunning = true;
					int steps = 10;
					clear();
					drawGrid();
					double x = ((double)from.getX()+((double)time*((double)to.getX()-(double)from.getX())/(double)steps));
					double y = ((double)from.getY()+((double)time*((double)to.getY()-(double)from.getY())/(double)steps));	
					drawPlayer(x,y);
					time += 1;
					// Ukonceni animace
					if(time >= steps) {
						// Prekresli
						clear();
						draw();
						// Nastav cas zpet na nulu
						time = 0;
						// Ukoci animaci */
						stop();
				        isRunning = false;
				        afterChangePositionAction();
					}	
				}
			}.start();
		} else {
			clear();
			draw();
			afterChangePositionAction();
		}
		
	}
	
	/**
	 * Akce po zmene pozice
	 */
	private void afterChangePositionAction() {
		if(player.getPosition().equals(maze.getTarget())) {
			nextMaze();
		} else {
			// Zpracuj dalsi klavesu v zasobniku
			PlayerController.INSTANCE.processNextKeyFromQueue();
		}
	}
	
	/**
	 * Zmena bludiste
	 */
	private void nextMaze() {
		maze = new Maze(maze.getRows()+1,maze.getColumns()+1);
		createPlayer();
		clear();
		draw();
	}
	
	/**
	 * Vrati, zdali bezi animace
	 * @return vrati true, bezi-li v tento moment animace
	 */
    public boolean isRunning() {
		return isRunning;
	}
	
}
