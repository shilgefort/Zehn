package antRace;


public class AntRace implements AntFields {

	public static void main(String[] args) {

		AntField field = new AntField(FIELD2);

		Ant ant = new Ant(field, 2, 4, 1);

		new Thread(ant).start();

		System.out.println(field.toString());
	}
}
