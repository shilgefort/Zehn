import java.io.*;
import io.FileVisitResult;
import io.FileVisitor;
public class MyFileVisitor implements FileVisitor {
	
	private boolean r=false;
	private  String regex;
	private Collection que;
	
	/**
	 * Custom-Konstruktor
	 * @param r	 wenn true, durchlaufe auch alle Unterverzeichnisse
	 * @param regex	gesuchter regex
	 * @param file	Datei, die besucht wird
	 * @param que	
	 */
	public MyFileVisitor(boolean r, String regex, File file, Collection que) {
		
		this.r=r;
		this.regex=regex;
		this.que=que;
		
	}
	
	@Override
	public FileVisitResult postVisitDirectory(File dir) {
		// TODO Auto-generated method stub
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(File dir) {
		// TODO Auto-generated method stub
		if(r==false){
			return FileVisitResult.SKIP_SUBTREE;
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFailed(File dir) {
		// TODO Auto-generated method stub
		return FileVisitResult.TERMINATE;
	}

	@Override
	public FileVisitResult visitFile(File file) {
		
		Thread slrThread=new SlrThread(regex, file, que);
		slrThread.start();
		
		return FileVisitResult.CONTINUE;
	}
	
	
}
