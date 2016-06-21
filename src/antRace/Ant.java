package antRace;


/**
 * An {@code Ant} is created at a specific position of an {@link AntField} with
 * an initial {@code stepCount}. When running an Ant, it will lookup the values
 * on the current and all surrounding {@link AntField.Field}
 * (Moore-neighborhood) instances and test if the position is free, i.e. has a
 * value of {@code 0}, or if the value is greater than the {@code stepCount} of
 * this Ant. For both cases, the Ant will set the value of the {@code Field} at
 * the visited position to its own {@code stepCount+1}. After an {@code Ant} has
 * successfully visited one field, it will create new {@code Ant} instances with
 * an incremented {@code stepCount} to visit the other available {@code Field}
 * elements. The Ant will run until it finds no more {@code Field} elements in
 * its neighborhood to be altered.
 * 
 * @author Mathias Menninghaus (mathias.menninghaus@uos.de)
 * 
 */
public class Ant extends Thread implements Runnable{
	private AntField fields;
	private int x;
	private int y;
	private int stepCount;

   /**
    * 
    * @param fields
    *           the {@code AntField} on which this {@code Ant} operates
    * @param x
    *           x-axis value of the starting position
    * @param y
    *           y-axis value of the starting position
    * @param stepCount
    *           initial stepCount of this {@code Ant}.
    * 
    * @throws IllegalArgumentException
    *            If the {@code Field} at position {@code x,y} does not exist, or
    *            if its value is < 0
    */
   public Ant(AntField fields, int x, int y, int stepCount) {
	   this.fields=fields;
	   this.x=x;
	   this.y=y;
	   this.stepCount=stepCount;
	   AntField.Field spielfeld = fields.getField(x, y);
	    if (spielfeld == null) {
	      throw new IllegalArgumentException("Spielfeld konnte nicht erstellt werden");
	    }
   }

   public void run() {
	   boolean found;
	    int tempX;
	    int tempY;
	    AntField.Field feld;
	  
	    do {

	      found = false;
	      tempX = this.x;
	      tempY = this.y;
	     
	     //Durchsuche angrenzende Felder
	      for (int i = tempX - 1; i <= tempX + 1; i++) {
	        for (int j = tempY - 1; j <= tempY + 1; j++) {

	     //Feld gueltig?
	          feld = fields.getField(i, j);

	          if (feld != null) {

	            synchronized (feld) {
	            	
	              int value = feld.getValue();

	              //Wenn Feld frei oder Wert groesser als aktuelle Ameise
	              if (value == AntField.FREE || value > this.stepCount + 1) {
	              //Wenn kein neuer Weg
	                if (!found) {
	                
	                  feld.setValue(stepCount + 1);
	                  found = true;
	                  this.x = i;
	                  this.y = j;
	                //Wenn neuer Weg, starte neue Ameise  
	                } else {
	                  Ant ant = new Ant(fields, i, j, stepCount + 1);
	                  ant.start();
	                }
	              }
	            }
	          }
	        }
	      }
	      stepCount++;
	    //solange wie Weg nicht gefunden
	    } while (found);
	  
	  }
	
   }


