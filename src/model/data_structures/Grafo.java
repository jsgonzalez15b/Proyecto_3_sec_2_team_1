package model.data_structures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.text.TabableView;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.vo.verticeInfo;

public class Grafo <V extends Comparable<V>,K extends Comparable<K>,A extends Comparable<A>> extends DefaultHandler 
{
	//ATRIBUTOS
	//	private Long anterior;
	//	private Long actual;
	public static final double radio=6.371;  
	public static final Integer referencia=1; 
	/**
	 * Tabla de Hash de vertices del grafo
	 */
	private LinearProbingHashST<K,Vertice<V,K,A>> TablaVertices;
	/**
	 * Pila de arcos del grafo
	 */
	private IStack<Arco<K,A>> pilaArcos; 

	//CONSTRUCTOR
	public Grafo()
	{
		TablaVertices= new LinearProbingHashST<K, Vertice<V,K,A>>(); 
		pilaArcos= new Stack<>(); 
	}

	//METODOS
	/**
	 * Numero de vertices en el grafo
	 * @return nVertices numero de vertices en el grafo
	 */
	public int numVertices()
	{
		return TablaVertices.size();
	}
	/**
	 * Numero de arcos en el grafo
	 * @return nVertices numero de vertices en el grafo
	 */
	public int numArcos()
	{
		return pilaArcos.size(); 
	}
	/**
	 * Tabla Hash de vertices
	 * @return tablaVertices tabla con los vertices asociados al grafo
	 */
	public LinearProbingHashST<K, Vertice<V,K,A>> darTablaVertices()
	{
		return TablaVertices; 
	}
	/**
	 * Adiciona un vertice con un Id unico.
	 * @param Vertex objeto Vertice con parametros V,K,A genericos
	 */
	public void addVertex( K pIdVertex, Vertice<V,K,A> Vertex) 
	{
		TablaVertices.put(pIdVertex,Vertex);
	}
	/**
	 * Obtener el vertice asociado a la llave recibida por parametro
	 * @param pId llave generica K del vertice con parametros V,K,A a buscar
	 */
	public Vertice<V,K,A> getVertice(K pId)
	{
		return TablaVertices.get(pId); 
	}
	/**
	 * Obtener la informacion de un vertice
	 * @param pIdVertex id del vertice
	 * @return elValor Valor te tipo generico V del vertice
	 */
	public V getInfoVertex(K pIdVertex)
	{
		return TablaVertices.get(pIdVertex).darValor(); 
	}
	/**
	 * Modificar la informacion del vertice idVertex
	 * @param pIdVertex información
	 * @param pInfoVertex nueva informacion del vertice
	 */
	public void setInfoVertex(K pIdVertex, V pInfoVertex)
	{
		if(pIdVertex!=null&&pInfoVertex!=null&&TablaVertices.get(pIdVertex)!=null)
		{
			TablaVertices.get(pIdVertex).setInfoVertex(pInfoVertex);
		}
	}
	/**
	 * Adiciona el arco No dirigido entre el vertice IdVertexIni y el vertice IdVertexFin. El arco tiene la informacion infoArc.
	 */
	public void addEdge(K idVertexIni, K idVertexFin,Arco<K,A> arco)throws Exception
	{
		Vertice<V,K,A> inicio=TablaVertices.get(idVertexIni); 
		Vertice<V,K,A> fin=TablaVertices.get(idVertexFin); 
		if (inicio!=null&&fin!=null){
			inicio.agregarArco(arco);
			fin.agregarArco(arco);
			pilaArcos.push(arco);
		}else{
			throw new Exception("Alguno de los vertices es nulo"); 
		}
	}
	/**
	 * Obtener el arco asociado a un par de vertices
	 * @param pIdVertex id del vertice
	 * @return elValor Valor te tipo generico V del vertice
	 */
	public Arco<K,A> getArc(K idVertexIni, K idVertexFin)
	{
		return TablaVertices.get(idVertexIni).darArcoPorAdyacente(idVertexFin); 
	}
	/**
	 * Obtener la informacion de un arco
	 * @return infoArco null si alguno de los indices no es encontrado
	 */
	public A getInfoArc(K idVertexIni, K idVertexFin) 
	{
		return TablaVertices.get(idVertexIni).darArcoPorAdyacente(idVertexFin).darPeso();
	}
	/**
	 * Modificar la informacion del arco entre los vertices idVertexIni e idVertexFin
	 */
	public void setInfoArc(K idVertexIni, K idVertexFin,A infoArc)
	{
		TablaVertices.get(idVertexIni).darArcoPorAdyacente(idVertexFin).setInfoArc(infoArc);
	}
	/**
	 * Retorna los identificadores de los vertices adyacentes a idVertex
	 */
	public Iterator<K> adj(K idVertex) 
	{
		return TablaVertices.get(idVertex).darAdyacentes().iterator();  
	}

	public IStack<Arco<K,A>> darArcosGrafo(){
		return pilaArcos; 
	}
	public void setPilaArcos(Stack<Arco<K,A>> plista)
	{
		pilaArcos=plista; 
	}
	
	public void setListaVertices(LinearProbingHashST<K,Vertice<V,K,A>> a){
		TablaVertices=a; 
	}
	//	public ArrayList<Vertice<verticeInfo,Long,Double>> vertices; 
	//
	//	private Vertice<verticeInfo, Long, Double> vertice; 
	//
	//	@Override
	//	public void startDocument() throws SAXException {
	//
	//		vertices=new ArrayList<>(); 
	//	}
	//	public boolean printedFirstNode=false;
	//
	//	@Override
	//	public void endElement(String uri, String localName, String qName) throws SAXException {
	//
	//	}
	//
	//	public ArrayList<Vertice<verticeInfo,Long,Double>> getVertices(){
	//		return vertices; 
	//	}
	//
	//	@Override
	//	public void endDocument() throws SAXException {
	//		System.out.println(vertices.size()+"");
	//	}


	//	@Override
	//	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	//		switch(qName) {
	//		case "osm version":
	//			break; 
	//		case "note":
	//			break; 
	//		case "meta":
	//			break; 
	//		case "bounds":
	//			break; 
	//		case "node":
	//			verticeInfo info= new verticeInfo(Double.parseDouble(attributes.getValue("lat")),Double.parseDouble(attributes.getValue("lon")));
	//			vertice=new Vertice<>(Long.parseLong(attributes.getValue("id")), info); 
	//			vertices.add(vertice);
	//			break; 
	//
	//		case "way":
	//			anterior=null;  
	//			actual=null;  
	//			break; 
	//		case "nd": 
	//			actual=Long.parseLong(attributes.getValue("ref")); 
	//			if(anterior!=null) { 
	//				Vertice<verticeInfo, Long, Double> uno=getVertice(actual);
	//				Vertice<verticeInfo, Long, Double> dos=getVertice(anterior);
	//				double lat1=uno.darValor().darLatitud(); 
	//				double lat2=dos.darValor().darLatitud();
	//				double deltalat=lat2-lat1; 
	//				double long1=uno.darValor().darlongitud(); 
	//				double long2=dos.darValor().darlongitud(); 
	//				double deltalong=long2-long1; 
	//				double a = Math.pow(Math.sin(deltalat/2),2)+Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin(deltalong/2),2); 
	//				double b=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
	//				double dis=radio*b; 
	//				uno.agregarArco(new Arco<Long,Double>(dis,uno.darLlave(), dos.darLlave()));
	//				dos.agregarArco(new Arco<Long, Double>(dis, dos.darLlave(),uno.darLlave()));			
	//				nArcos++; 
	//			}
	//			anterior=actual;
	//			break; 
	//		}
	//	}
	//	public Vertice<V,K,A> getVertice(K pId) {
	//		return TablaVertices.get(pId); 
	//	}

	//	public void JsonVertices() throws FileNotFoundException {
	//		PrintWriter pw= new PrintWriter(new File("."+File.separator+"data"+File.separator+"JsonVertices"));
	//		System.out.println("creo");
	//		boolean cerrado=true;
	//		String fin="},";
	//		pw.println("[");
	//		for(int i=0; i<vertices.size(); i++) {
	//			Vertice<verticeInfo, Long, Double> actual= vertices.get(i); 
	//			pw.println("{");
	//			pw.println("\"id\":"+vertices.get(i).darLlave()+",");
	//			pw.println("\"lat\":"+vertices.get(i).darValor().darLatitud()+",");
	//			pw.println("\"lon\":"+vertices.get(i).darValor().darlongitud()+",");
	//			String arcos=""; 
	//			for (int j=0;j<actual.getArcos().size();j++) {
	//				if(j==actual.getArcos().size()-1) {
	//					arcos+=actual.getArcos().get(j).darAdyacente(); 
	//				}else {
	//					arcos+=actual.getArcos().get(j).darAdyacente()+","; 
	//				}
	//			}
	//			pw.println("\"adj\":["+arcos+"]");
	//			if(i==vertices.size()-1) {
	//				fin="}"; 
	//			}
	//			pw.println(fin); 
	//		}
	//		pw.println("]");
	//		pw.close();
	//	}
}