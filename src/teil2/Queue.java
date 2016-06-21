package teil2;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


/**
 * An implementation of a Queue with a limited capacity.
 * 
 * @author Mathias Menninghaus (mathias.menninghaus@uos.de)
 * 
 * @param <E>
 */
public class Queue<E> {
	
	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Lock readLock = rwLock.readLock();
	private final Lock writeLock = rwLock.writeLock();

	private final Condition condition = writeLock.newCondition();

	/**
	 * Holds the objects stored by this {@code Queue}.
	 */
	private Object[] objects;
	/**
	 * index of the first instance stored by this {@code Queue}.
	 */
	private int first;
	/**
	 * number of elements contained in this {@code Queue}
	 */
	private int size;

	/**
	 * @param capacity
	 *            number of objects which may be hold in this {@code Queue}.
	 */
	public Queue(int capacity) {
		this.objects = new Object[capacity];
		this.first = 0;
		this.size = 0;
	}

	/**
	 * Inserts {@code o} at the first free position of this {@code Queue}
	 * 
	 * @param o
	 *            object to be inserted
	 * 
	 * @throws RuntimeException
	 *             if this {@code Queue} is already full
	 */
	public void enq(E o) throws InterruptedException {
		
		
		//Dieser Block wird durch eine lock instanz gesperrt
		//Wenn ein anderer thread schreibt oder liest wird gewartet bis kein thread mehr liest oder schreibt
		//Condition instanz um die verlangte Synchronisation zu gewährleisten
		this.writeLock.lock();
			try{ 
					if (this.full()) {
							this.condition.await();
					}
					objects[(first + size) % objects.length] = o;
					size++;
				}
				//Block wird wieder freigegeben
				finally { 
					this.condition.signalAll();
					this.writeLock.unlock(); 
				}
				}
			
			
			
	/**
	 * Removes the object at the first position of this {@code Queue}.
	 * 
	 * @return the removed object
	 * @throws NoSuchElementException
	 *             if this {@code Queue} is already empty
	 */
	@SuppressWarnings("unchecked")
	public E deq() throws InterruptedException {
		
			// Block wird durch Lock Instanz gesperrt
			// Wenn ein anderer thread schreibt oder liest wird gewartet bis kein thread mehr liest oder schreibt
			// Condition Instanz um die verlangte Synchronisation zu gewährleisten
			this.writeLock.lock();
			try {
					if (this.empty()) {
						this.condition.await();
					}

						E o = (E) objects[first];
						first = (first + 1) % objects.length;
						size--;
						return o;
			}
			//Block wird wieder freigegeben
			finally {
					this.condition.signalAll();
					this.writeLock.unlock();
			}
	}

	/**
	 * Returns the object at the first position of this {@code Queue}
	 * 
	 * @return the first element of this {@code Queue}
	 * @throws NoSuchElementException
	 *             if this {@code Queue} is already empty
	 */
	@SuppressWarnings ("unchecked")
	public E front() {
		//Dieser Block wird gesperrt mit readLock Instanz
		//Wenn ein anderer Thread gerade schreibt wird gewartet bis kein Thread mehr schreibt
		this.readLock.lock();
		try {
				if (this.empty()) {
				throw new NoSuchElementException();
		}
		return (E) objects[first];
	}
	//Block wird freigegeben
	finally {
			this.readLock.unlock();
	}
	}

		
		
		
	/**
	 * 
	 * @return {@code true} if this {@code Queue} is empty
	 */
	public boolean empty() {
			// siehe front
			this.readLock.lock();
	try {
				return this.size == 0;	
	}
	finally{ 
				this.readLock.unlock();
     	}
	}

	/**
	 * 
	 * @return {@code true} if this {@code Queue} is full
	 */
	public boolean full() {
			// siehe front, empty
			this.readLock.lock();
		try {
				return this.size == objects.length;
	
	}
	finally {
				this.readLock.unlock();
				
	}
  }
}
