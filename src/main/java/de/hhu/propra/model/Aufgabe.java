package de.hhu.propra.model;

public class Aufgabe {
String Name;
String Beschreibung;
Inhalt[] Klassen;
Inhalt[] Interfaace;
Inhalt Test;
boolean valueBabysteps;

public Aufgabe(String name, String beschreibung, Inhalt[] klassen, Inhalt[] interface1, Inhalt test, boolean valueBabysteps) {
	
	Name = name;
	Beschreibung = beschreibung;
	Klassen = klassen;
	Interfaace = interface1;
	Test = test;
	this.valueBabysteps=valueBabysteps;
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
public boolean getValueBabysteps(){
	return valueBabysteps;
}

}
