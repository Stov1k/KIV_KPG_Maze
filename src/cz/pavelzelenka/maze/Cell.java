package cz.pavelzelenka.maze;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.util.Pair;

/**
 * Bunka
 * @author Pavel Zelenka A16B0176P
 * @version 2018-03-10
 */
public class Cell {
	
	/** Napojeno */
	private boolean connected = false;
	
	/** Napojeni */
	private boolean [] connectivity = new boolean[4];
	
	/** Seznam sousedu bunky */
    private ObservableList<Pair<Integer, Cell>> neighbors;
    
	/** Vzdalenost od zacatku */
	private int distance = 0;
    
	/** Pozice */
	private int x,y;
	
    /** 
     * Konstruktor bunky 
     */
    public Cell() {
        neighbors = FXCollections.observableArrayList();
        connectivity[0] = false;	// horni
        connectivity[1] = false;	// prava
        connectivity[2] = false;	// dolni
        connectivity[3] = false;	// leva
    }

    /**
     * Konstruktor bunky 
     * @param cell bunka
     */
    public Cell(Cell cell) {
        neighbors = FXCollections.observableArrayList(cell.neighbors);
    }
    
    /**
     * Vrati, zdali existuje nepripojena sousedni bunka
     * @return vraci true, kdyz existuje nepripojena sousedni bunka
     */
    public boolean isUnconnectedNeighbor() {
    	for(Pair<Integer, Cell> neighbor : neighbors) {
    		if(neighbor.getValue().connected == false) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Vrati pocet nepripojenych sousednich bunek
     * @return pocet nepripojenych sousednich bunek
     */
    public int getUnconnectedNeighborCount() {
    	int count = 0;
    	for(Pair<Integer, Cell> neighbor : neighbors) {
    		if(neighbor.getValue().connected == false) {
    			count++;
    		}
    	}
    	return count;
    }
    
    /**
     * Vrati nahodne zvolenou nepripojenou sousedni bunku.
     * V pripade, ze takova bunka neexistuje, vraci null.
     * @return nepripojena sousedni bunka
     */
    public Pair<Integer, Cell> getUnconnectedNeighbor() {
    	// Seznam nepripojenych sousednich bunek
    	List<Pair<Integer, Cell>> unconnectedNeighbors = new ArrayList<>();
    	for(Pair<Integer, Cell> neighbor : neighbors) {
    		if(neighbor.getValue().connected == false) {
    			unconnectedNeighbors.add(neighbor);
    		}
    	}
    	// Vraceni nahodne bunky ze seznamu
    	if(!unconnectedNeighbors.isEmpty()) {
    		Random random = new Random();
    		int size = unconnectedNeighbors.size();
        	int next = random.nextInt(size);
        	return unconnectedNeighbors.get(next);
    	}
    	// Neexistuje nepripojena sousedni bunka
    	return null;
    }
    
    /**
     * Propojeni bunky s bunkou sousedni
     * @param cell sousedni bunka
     */
    public void connect(Cell cell) {
    	// strana (vychozi -1 -> nezvoleno)
    	int side = -1;
    	
    	// nastaveni strany, kde dojde k propojeni
    	for(Pair<Integer, Cell> neighbor : neighbors) {
    		if(neighbor.getValue().equals(cell)) {
    			side = neighbor.getKey();
    		}
    	}
    	
    	// nezvolena strana
    	if(side == -1) {
    		System.err.println("Cells cannot be connected!");
    		return;
    	}
    	
    	// propojeni
    	connectivity[side] = true;
    	switch(side) {
    		case 0: cell.connectivity[2] = true; break;
    		case 1: cell.connectivity[3] = true; break;
    		case 2: cell.connectivity[0] = true; break;
    		case 3: cell.connectivity[1] = true; break;
    		default: return;
    	}
    	
    	// nastaveni bunky jako pripojene
    	cell.connected = true;
    }

    /**
     * Vrati, zdali je bunka pripojena
     * @return vrati true, kdyz je bunka pripojena
     */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Nastavi, zdali je bunka pripojena
	 * @param connected pripojeno
	 */
	protected void setConnected(boolean connected) {
		this.connected = connected;
	}

	/**
	 * Vrati propojeni s okolnimi bunkami v poradi horni, prava, dolni, leva
	 * @return propojeni s okolnimi bunkami
	 */
	public boolean[] getConnectivity() {
		return connectivity;
	}

	/**
	 * Vrati vzdalenost od startu
	 * @return vzdalenost od startu
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * Nastavi vzdalenost od startu
	 * @param distance vzdalenost od startu
	 */
	protected void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * Sousedni bunky (hodnota) a zpusob pripojeni (klic)
	 * @return seznam sousednich bunek a zpusobu pripojeni
	 */
	public ObservableList<Pair<Integer, Cell>> getNeighbors() {
		return neighbors;
	}

	/**
	 * Napojene sousedni bunky (hodnota) a zpusob pripojeni (klic)
	 * @return seznam napojenych sousednich bunek a zpusobu pripojeni
	 */
	public List<Pair<Integer, Cell>> getConnectedNeighbors() {
		List<Pair<Integer, Cell>> list = new ArrayList<>();
		FilteredList<Pair<Integer, Cell>> filteredData = new FilteredList<>(neighbors, p -> true);
		filteredData.setPredicate(cell -> {
			if(cell.getValue().isConnected()) {
				return true;
			}
			return false;
			});
		list.addAll(filteredData);
		return list;
	}
	
	/**
	 * Nenapojene sousedni bunky (hodnota) a zpusob pripojeni (klic)
	 * @return seznam nenapojenych sousednich bunek a zpusobu pripojeni
	 */
	public List<Pair<Integer, Cell>> getUnconnectedNeighbors() {
		List<Pair<Integer, Cell>> list = new ArrayList<>();
		FilteredList<Pair<Integer, Cell>> filteredData = new FilteredList<>(neighbors, p -> true);
		filteredData.setPredicate(cell -> {
			if(!cell.getValue().isConnected()) {
				return true;
			}
			return false;
			});
		list.addAll(filteredData);
		return list;
	}
	
	/**
	 * Vrati pozici na ose X
	 * @return pozice X
	 */
	public int getX() {
		return x;
	}

	/**
	 * Nastavi pozici na ose X
	 * @param x pozice X
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Vrati pozici na ose Y
	 * @return pozice Y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Nastavi pozici na ose Y
	 * @param y pozice Y
	 */
	public void setY(int y) {
		this.y = y;
	}

}
