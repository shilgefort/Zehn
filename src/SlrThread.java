import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import io.SearchLineReader;

public class SlrThread extends Thread {
	
	private String regex;
	private File file;
	private StringBuilder str;
	private SearchLineReader slr;
	private Collection que;
	
	public SlrThread(String regex, File file, Collection que) {
	
		this.regex=regex;
		this.file=file;	// TODO Auto-generated constructor stub
		this.que=que;
	}
	/**
	 * Wird ausgefuehrt, wenn Thread startet; baut String aus allen Zeilen mit Treffern in der Datei mit Anzahl der Treffer
	 */
	public void run(){
		str=new StringBuilder();
		str.append(""+file+":%n");
		str.append("%n");
		try {
			FileReader fr=new FileReader(file);
			slr=new SearchLineReader(fr, regex);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line;
		try{
			while((line=slr.readLine())!=null){
			
				if(slr.getAmountOfMatches()!=0){
					str.append("  Zeile: "+line+" Anzahl der Treffer: "+slr.getAmountOfMatches()+"%n");
				}
			
			}
			str.append("%n");
		
		}
		catch(IOException e){
			e.printStackTrace();
		}
		synchronized(que){
			que.add(str.toString());
			try {
				que.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
}
