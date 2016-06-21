import io.FileSystem;
import java.io.File;
import java.util.concurrent.*;

public class Search {
	private static final String USAGE="java Search [-r] regex path";
	private static boolean r=false;
	private static FileSystem fileSystem;
	private static File file;
	private static String regex;
	private static Collection que;
	
	public static void main(String[]args){
		
		if(args.length==0){
			System.out.println(USAGE);
			System.exit(1);
		}
		
		if(args[0]=="-r"){
			r=true;
			file=new File(args[2]);
			if(!file.isFile() &&!file.isDirectory()){
				
				System.out.println("Pfad/Datei existiert nicht");
				System.exit(1);
			}
			regex=args[1];
		}
		else{
			file=new File(args[1]);
			if(!file.isFile() && !file.isDirectory()){
				System.out.println("Pfad/Datei existiert nicht");
				System.exit(1);
			}
			regex=args[0];
		}
		fileSystem=new FileSystem(file);
		que=new Collection();
		que.start();
		MyFileVisitor fv=new MyFileVisitor(r, regex, file, que);
		fileSystem.accept(fv);
		ConcurrentLinkedDeque<String> deque=que.getQue();
		
		
		
		while(!deque.isEmpty()){
			
			System.out.printf("%s", deque.element());
			deque.remove();
		}
		
		}
	}


