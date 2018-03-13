package cz.pavelzelenka.maze;

import javafx.util.Pair;

/**
 * Bludiste
 * @author Pavel Zelenka A16B0176P
 * @version 2018-03-10
 */
public class Maze {

	/** Vychozi velikost matice */
	public static final int IMPLICIT_MATRIX_SIZE = 10;
	
	/** Pocet radku a sloupcu */
	private int rows, columns;
	
	/** Plocha bludiste */
	private Cell[][] surface;
	
	/** Pocatecni a cilova pozice */
	private Cell start, target;
	
	/**
	 * Vytvori instanci bludiste
	 */
	public Maze() {
		this(IMPLICIT_MATRIX_SIZE, IMPLICIT_MATRIX_SIZE);
	}
	
	/**
	 * Vytvori instanci bludiste
	 * @param rows pocet radku
	 * @param columns pocet sloupcu
	 */
	public Maze(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		generateSurface();
		this.start = detectStart();
		this.target = detectTarget();
	}

	/**
	 * Generovani bludiste
	 */
	private void generateSurface() {
		this.surface = new Cell[getRows()][getColumns()];
		initEnvironment(getRows(), getColumns());
		createPaths(this.surface[0][0]);
	}
	
	/**
	 * Inicializace prostredi
	 * @param gen matice
	 * @param rows radky
	 * @param columns sloupce
	 */
    private void initEnvironment(int rows, int columns) {
        // Pro kazde policko [i,j] matice inicializuj novou bunku
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
            	surface[i][j] = new Cell();
            	surface[i][j].setX(i);
            	surface[i][j].setY(j);
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                addNeighbors(surface[i][j], i, j);
            }
        }
    }
	
	/**
	 * Prida do seznamu sousedu bunky jeji sousedy
	 * @param cell bunka
	 * @param row radek
	 * @param column sloupec
	 */
    private void addNeighbors(Cell cell, int row, int column) {
    	// Nastaveni leveho souseda
    	if(row != 0) {
    		cell.getNeighbors().add(new Pair<>(3, surface[row-1][column  ]));
    	}
    	// Nastaveni spodniho souseda
    	if(column != getColumns()-1) {
    		cell.getNeighbors().add(new Pair<>(2,surface[row]  [column+1]));
    	}
    	// Nastaveni praveho souseda
    	if(row != getRows()-1) {
    		cell.getNeighbors().add(new Pair<>(1,surface[row+1][column  ]));
    	}
    	// Nastaveni horniho souseda
    	if(column != 0) {
    		cell.getNeighbors().add(new Pair<>(0,surface[row]  [column-1]));
    	}
    }
    
    /**
     * Vytvoreni cest bludiste
     * @param currentCell soucasna bunka
     */
    private void createPaths(Cell cell) {
    	// Nastaveni bunky jako pripojene
    	cell.setConnected(true);
    	// Pripojeni sousednich bunek
    	while(cell.isUnconnectedNeighbor()) {
        	Cell nextCell = cell.getUnconnectedNeighbor().getValue();
        	cell.connect(nextCell);
        	nextCell.connect(cell);
        	nextCell.setDistance(cell.getDistance() + 1);
        	createPaths(nextCell);
    	} 
    }
    
    /**
     * Urci pocatecni pozici
     * @return bunka na pocatecni pozici
     */
    private Cell detectStart() {
    	for(int i = 0; i < getRows(); i++) {
        	for(int j = 0; j < getColumns(); j++) {
        		Cell cell = getCell(i,j);
        		if(cell.getDistance() == 0) {
        			return cell;
        		}
        	}
    	}
    	throw new IllegalStateException("Start position does not exist.");
    }
    
    /**
     * Urci cilovou pozici
     * @return bunka na cilove pozici
     */
    private Cell detectTarget() {
    	Cell target = null;
    	for(int i = 0; i < getRows(); i++) {
        	for(int j = 0; j < getColumns(); j++) {
        		Cell cell = getCell(i,j);
        		if(target == null) {
        			target = cell;
        		}
        		if(cell.getDistance() > target.getDistance()) {
        			target = cell;
        		}
        	}
    	}    	
    	if(target == null) {
    		throw new IllegalStateException("Target position does not exist.");
    	} else {
    		// statistika
        	Statistics.targetDistance.set(target.getDistance());
        	System.out.println(Statistics.targetDistance.get());
        	// vraceni cilove bunky
    		return target;
    	}
    }
    
	/**
	 * Vrati pocet radku matice 
	 * @return pocet radku matice 
	 */
	public int getRows() {
		return this.rows;
	}

	/**
	 * Vrati pocet sloupcu matice
	 * @return pocet sloupcu matice
	 */
	public int getColumns() {
		return this.columns;
	}

	/**
	 * Vrati bunku na pozici
	 * @param i radek
	 * @param j sloupec
	 * @return bunka
	 */
	public Cell getCell(int i, int j) {
		return this.surface[i][j];
	}
	
	/**
	 * Vrati plochu bludiste
	 * @return plocha bludiste
	 */
	protected Cell[][] getSurface() {
		return this.surface;
	}

    /**
     * Vrati pocatecni pozici
     * @return bunka na pocatecni pozici
     */
	public Cell getStart() {
		return start;
	}

    /**
     * Vrati cilovou pozici
     * @return bunka na cilove pozici
     */
	public Cell getTarget() {
		return target;
	}

}
