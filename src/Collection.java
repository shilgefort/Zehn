import java.util.concurrent.*;
public class Collection extends Thread{
	private ConcurrentLinkedDeque<String> que;
	/**
	 * Custom-Konstruktor
	 */
	public Collection() {
		que=new ConcurrentLinkedDeque<String>();
	}

	public void add(String s){
		que.addLast(s);
	}
	
	public ConcurrentLinkedDeque<String> getQue(){
		return this.que;
	}
}
