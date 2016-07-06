package main.xmlparser;

public class Aufgabe {
String Name;
String Beschreibung;
Inhalt[] Klassen;
Inhalt[] Interfaace;
Inhalt Test;

public Aufgabe(String name, String beschreibung, Inhalt[] klassen, Inhalt[] interface1, Inhalt test) {
	
	Name = name;
	Beschreibung = beschreibung;
	Klassen = klassen;
	Interfaace = interface1;
	Test = test;
}

public String getName() {
	return Name;
}

public String getBeschreibung() {
	return Beschreibung;
}

public Inhalt[] getKlassen() {
	return Klassen;
}

public Inhalt[] getInterfaace() {
	return Interfaace;
}

public Inhalt getTest() {
	return Test;
}


}
