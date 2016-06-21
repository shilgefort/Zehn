//CONSUMER
/**
 * Simple {@code Thread} which continuously reads random values from the given
 * {@code Queue} and sleeps for as long as the currently read value determines.
 * 
 * @author Mathias Menninghaus (mathias.menninghaus@uos.de)
 * 
 */
public class Sleeper extends Thread {

   private Queue<Long> values;

   public Sleeper(Queue<Long> values) {
      this.values = values;
   }

   public void run() {
      try {
         while (true) {
            long value;
            //Dieser Block wird geschützt abgearbeitet
            synchronized (values) {
            	//wartet wenn Schlange leer
            	while(this.values.empty()) {
            		values.wait();
            }
            	
            
            value = values.deq();
            //Block wird freigegeben
         	values.notifyAll();
            System.out.println("Now sleeping for " + value + " ms");
      	}
            this.sleep(value);

         }
      }
      catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}
