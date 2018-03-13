package cz.pavelzelenka.maze;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Statistics {

	/** Pocet kroku */
	public static IntegerProperty steps = new SimpleIntegerProperty(0);
	
	/** Pocet kroku */
	public static IntegerProperty targetDistance = new SimpleIntegerProperty(0);
}
