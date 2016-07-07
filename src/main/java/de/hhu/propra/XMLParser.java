package de.hhu.propra;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.hhu.propra.model.Aufgabe;
import de.hhu.propra.model.Inhalt;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
	Element WurzelExercise;
	public XMLParser(String Filename){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		
		Document doc = builder.parse(new File(Filename)); //hier muss rein wo der Katalog zu finden ist
		WurzelExercise = doc.getDocumentElement();

	}catch (ParserConfigurationException | SAXException | IOException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	}
		}

	public Aufgabe[] getAufgaben(){
		
		
		
		NodeList ExerciseList = WurzelExercise.getElementsByTagName("exercise");
		Aufgabe[] aufgaben = new Aufgabe[ExerciseList.getLength()];
		for (int i = 0; i < ExerciseList.getLength(); i++) {
			Node node = ExerciseList.item(i);
			Element element = WurzelExercise;
			String Aufgabenname = new String("");
			String Beschreibung = new String ("");
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				element = (Element) node;
				Aufgabenname=element.getAttribute("Aufgabenname");
				Beschreibung = ((NodeList) element).item(1).getTextContent();

			}
			NodeList ClassList = element.getElementsByTagName("class");
			Inhalt[] klassen = new Inhalt[ClassList.getLength()];
			for (int j = 0; j<ClassList.getLength(); j++){
				Node node0 = ClassList.item(j);
				String Klassenname =new String("");
				String KlassenText = new String("");
				if (node0.getNodeType() == Node.ELEMENT_NODE) {
					Element element0 = (Element) node0;
					Klassenname=element0.getAttribute("Klassenname");
					KlassenText=element0.getTextContent();
				}
				klassen[j]=new Inhalt(Klassenname, KlassenText);
			}
			
			NodeList TestList = element.getElementsByTagName("test");
			Inhalt test=new Inhalt("", "");
			for (int j = 0; j<TestList.getLength(); j++){
				Node node1 = TestList.item(j);
				String Testname =new String("");
				String TestText = new String("");
				if (node1.getNodeType() == Node.ELEMENT_NODE) {
					Element element1 = (Element) node1;
					Testname=element1.getAttribute("Testname");
					TestText=element1.getTextContent();
				}
				test = new Inhalt(Testname, TestText);
			}
			
			NodeList InterfaceList = element.getElementsByTagName("interface");
			Inhalt[] interfaace = new Inhalt[InterfaceList.getLength()];
			for (int j = 0; j<InterfaceList.getLength(); j++){
				Node node2 = InterfaceList.item(j);
				String Interfacename =new String("");
				String InterfaceText = new String("");
				if (node2.getNodeType() == Node.ELEMENT_NODE) {
					Element element2 = (Element) node2;
					Interfacename=element2.getAttribute("name");
					InterfaceText=element2.getTextContent();
				}
				interfaace[j]= new Inhalt(Interfacename, InterfaceText);
			}
			aufgaben[i]=new Aufgabe(Aufgabenname, Beschreibung, klassen, interfaace, test);
		}
		
		
		
		return aufgaben;
	}
}
