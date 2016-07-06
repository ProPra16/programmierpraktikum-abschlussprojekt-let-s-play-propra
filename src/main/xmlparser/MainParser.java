package main.xmlparser;

public class MainParser {

	public static void main(String[] args) {
		XMLParser parser= new XMLParser("aufgaben.xml");
		Aufgabe[] aufgaben =parser.getAufgaben();
		for(int i=0; i<aufgaben.length; i++){
			System.out.println(aufgaben[i].getName());
			System.out.println(aufgaben[i].getBeschreibung());
			for(int j=0; j<aufgaben[i].getKlassen().length; j++){
				System.out.println(aufgaben[i].getKlassen()[j].getName());
				System.out.println(aufgaben[i].getKlassen()[j].getText());
			}
			for(int j=0; j<aufgaben[i].getInterfaace().length; j++){
				System.out.println(aufgaben[i].getInterfaace()[j].getName());
				System.out.println(aufgaben[i].getInterfaace()[j].getText());
			}
			System.out.println(aufgaben[i].getTest().getName());
			System.out.println(aufgaben[i].getTest().getText());
			System.out.println("");
		}
	}

}
