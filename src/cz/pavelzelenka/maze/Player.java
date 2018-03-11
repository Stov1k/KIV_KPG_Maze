package cz.pavelzelenka.maze;

/**
 * Hrac
 * @author Pavel Zelenka A16B0176P
 * @version 2018-03-10
 */
public class Player {

	/** Pozice hrace */
	private Cell position;
	
	/**
	 * Vytvori instanci hrace
	 * @param position pozice
	 */
	public Player(Cell position) {
		this.position = position;
	}

	/**
	 * Vrati pozici hrace
	 * @return pozice hrace
	 */
	public Cell getPosition() {
		return position;
	}

	/**
	 * Nastavi pozici hrace
	 * @param position pozice hrace
	 */
	public void setPosition(Cell position) {
		this.position = position;
	}
	
}
