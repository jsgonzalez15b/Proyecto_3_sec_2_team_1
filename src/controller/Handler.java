package controller;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.vo.verticeInfo;
import model.data_structures.Arco;
import model.data_structures.Vertice;

public class Handler extends DefaultHandler {

	private ArrayList<Vertice<verticeInfo,Integer,Integer>> vertices= new ArrayList<>(); 

	private Vertice<verticeInfo, Integer, Integer> vertice; 

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		case "node": 
			break; 
		case "way": 
			break; 
		}
	}
	
	public ArrayList<Vertice<verticeInfo,Integer,Integer>> getVertices(){
		return vertices; 
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch(qName) {
		case "osm version":
			break; 
		case "note":
			break; 
		case "meta":
			break; 
		case "bounds":
			break; 
		case "node":
			verticeInfo info= new verticeInfo(Double.parseDouble(attributes.getValue("lat")),Double.parseDouble(attributes.getValue("lon")));
			
			vertice=new Vertice<>(Integer.parseInt(attributes.getValue("id")), info); 
			vertices.add(vertice); 
			break; 
			
		}
	}
}
