package model.data_structures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.vo.verticeInfo;

public class Grafo <K extends Comparable<K>,V extends Comparable<V>,A extends Comparable<A>> extends DefaultHandler 
{
	public static final double radio=6.371;  
	public static final Integer referencia=1; 
	//Atributos
	/**
	 * Numero de arcos en el grafo
	 */
	private int nArcos;

	/**
	 * Numero de vertices en el grafo
	 */
	private int nVertices;

	/**
	 * Arreglo de arcos y nodos
	 */
	private Vertice[] arreglo; 
	
	private IStack<Vertice> pila; 

	private Long anterior;
	private Long actual;

	//Constructor
	public Grafo()
	{
		nArcos=0;
		nVertices=0;
		arreglo= new Vertice[162999]; //implementacion de factor de carga 0.75
		pila=new Stack<>(); 
	}
	
	//Metodos
	/**
	 * Numero de vertices en el grafo
	 * @return nVertices numero de vertices en el grafo
	 */
	public int V()
	{
		return nVertices;
	}
	
	/**
	 * Numero de arcos en el grafo
	 * @return nVertices numero de vertices en el grafo
	 */
	public int E()
	{
		return nArcos;
	}
	
	/**
	 * Adiciona un vertice con un Id unico. El vertice tiene la informacion InfoVertex
	 */
	public void addVertex( K pIdVertex, V infoVertex) //PREGUNTAR SEGUNDO PARAMETRO
	{
		double factorActual= nVertices/arreglo.length;
		int hashCalculado = pIdVertex.hashCode()& 0x7fffffff %arreglo.length; 
		
		if(factorActual<0.75)
		{
			if(arreglo[hashCalculado]!=null)//no es requerido un else ya que los id son unicos
			{
				arreglo[hashCalculado]=new Vertice(pIdVertex,(Comparable) infoVertex);
				nVertices++;
			}
		}
		else
		{
			reHashGrafo();
			addVertex( pIdVertex, infoVertex);
		}
	}
	
	/**
	 * Retorna el objeto de tipo Vertice dado su id
	 * @param pIdVertex id que identifica el vertice a buscar
	 */
	public Vertice findVertice( K pIdVertex) 
	{
		int hashCalculado = pIdVertex.hashCode()& 0x7fffffff %arreglo.length; 
		return arreglo[hashCalculado];
	}
	
	/**
	 * Actualiza la posicion de todos los elementos del hashTable segun su llave y el nuevo tamano
	 */
	public void	reHashGrafo()
	{
		Vertice[] copiaHash= arreglo; //se crea un copia con los vertices actuales
		arreglo = new Vertice[arreglo.length*2];//se aumenta la tabla del HashTableLinear

		for ( int i = 0; i < copiaHash.length; i++)//se obtienen los nuevos indices 
		{
			if(copiaHash[i]!=null)
			{
				addVertex((K)copiaHash[i].darLlave(),(V)copiaHash[i].darArcos());
			}
		} 
	}
	/**
	 * Adiciona el arco No dirigido entre el vertice IdVertexIni y el vertice IdVertexFin. El arco tiene la informacion infoArc.
	 */
	public void addEdge(K idVertexIni, K idVertexFin, A infoArc )
	{
		int hashCalculado1 = idVertexIni.hashCode()& 0x7fffffff %arreglo.length; 
		int hashCalculado2 = idVertexFin.hashCode()& 0x7fffffff %arreglo.length; 
		
		//necesariamente ambos vertices deben existir? PREGUNTAR
		if(arreglo[hashCalculado1]!=null && arreglo[hashCalculado2]!=null)
		{
			arreglo[hashCalculado1].agregarArco((Comparable) infoArc, idVertexFin); //se agrega el peso del arco y el vertice Fin como conexion al primero
			arreglo[hashCalculado2].agregarArco((Comparable) infoArc, idVertexIni); //se agrega el peso del arco y el vertice Ini como conexion al segundo
			nArcos++;
		}
		//Caso en el que se crea el vertice pero este caso no aplica creo - igual lo dejo por si acaso
//		if(arreglo[hashCalculado1]==null || arreglo[hashCalculado2]==null) ASK XD
//		{
//			//se crean los vertices que no han sido creados
//			if(arreglo[hashCalculado1]==null)
//			{
//				arreglo[hashCalculado1]= new Vertice( idVertexIni);
//			}
//			if(arreglo[hashCalculado2]==null)
//			{
//				arreglo[hashCalculado2]= new Vertice( idVertexFin);
//			}
//			
//			arreglo[hashCalculado1].agregarArco(infoArc, idVertexFin); //se agrega el peso del arco y el vertice Fin como conexion al primero
//			arreglo[hashCalculado2].agregarArco(infoArc, idVertexIni); //se agrega el peso del arco y el vertice Ini como conexion al segundo
//		}
	}
	
	/**
	 * Obtener la informacion de un vertice
	 * @param pIdVertex id del vertice
	 * @return elValor informacion del vertice
	 */
	public V getInfoVertex(K pIdVertex)
	{
		int hashCalculado = pIdVertex.hashCode()& 0x7fffffff %arreglo.length; 
		V elValor =(V) arreglo[hashCalculado].darValor();
		return elValor;
	}
	
	/**
	 * Modificar la informacion del vertice idVertex
	 * @param pIdVertex información
	 * @param pInfoVertex nueva informacion del vertice
	 */
	public void setInfoVertex(K pIdVertex, V pInfoVertex)
	{
		int hashCalculado = pIdVertex.hashCode()& 0x7fffffff %arreglo.length; 
		arreglo[hashCalculado].setInfoVertex((Comparable) pInfoVertex);;
	}
	
	/**
	 * Obtener la informacion de un arco
	 * @return infoArco null si alguno de los indices no es encontrado
	 */
	public A getInfoArc(K idVertexIni, K idVertexFin) 
	{
		int hashCalculado1 = idVertexIni.hashCode()& 0x7fffffff %arreglo.length; 
		A infoArco = null; //informacion del arco a retornas
		if(arreglo[hashCalculado1]!=null)
		{
			infoArco = (A) arreglo[hashCalculado1].darArco(idVertexFin);
		}
		return infoArco;
	}
	
	/**
	 * Modificar la informacion del arco entre los vertices idVertexIni eidVertexFin
	 */
	public void setInfoArc(K idVertexIni, K idVertexFin,
			A infoArc)
	{
		//se obtienen los indices de los hash
		int hashCalculado1 = idVertexIni.hashCode()& 0x7fffffff %arreglo.length; 
		int hashCalculado2 = idVertexFin.hashCode()& 0x7fffffff %arreglo.length; 
		//si ambos existen su arco es actualizado
		if(arreglo[hashCalculado1]!=null && arreglo[hashCalculado2]!=null)
		{
			arreglo[hashCalculado1].setInfoArc(idVertexFin, infoArc);
			arreglo[hashCalculado2].setInfoArc(idVertexIni, infoArc);
		}
		
	}
	
	/**
	 * Retorna los identificadores de los vertices adyacentes a idVertex
	 */
	public Iterator<Arco> adj(K idVertex) 
	{
		int hashCalculado = idVertex.hashCode()& 0x7fffffff %arreglo.length; 
		Vertice elVerticeBuscado = arreglo[hashCalculado];
		Iterador<Arco> elIterador = (Iterador<Arco>) elVerticeBuscado.darArcos().iterator();
		return elIterador;
	}
	
	public Vertice[] darArreglo() {
		return arreglo; 
	}

	public ArrayList<Vertice<verticeInfo,Long,Double>> vertices; 

	private Vertice<verticeInfo, Long, Double> vertice; 

	@Override
	public void startDocument() throws SAXException {

		vertices=new ArrayList<>(); 
	}
	public boolean printedFirstNode=false;

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

	}

	public ArrayList<Vertice<verticeInfo,Long,Double>> getVertices(){
		return vertices; 
	}

	@Override
	public void endDocument() throws SAXException {
		System.out.println(vertices.size()+"");
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
			vertice=new Vertice<>(Long.parseLong(attributes.getValue("id")), info); 
			vertices.add(vertice);
			break; 

		case "way":
			anterior=null;  
			actual=null;  
			break; 
		case "nd": 
			actual=Long.parseLong(attributes.getValue("ref")); 
			if(anterior!=null) { 
				Vertice<verticeInfo, Long, Double> uno=getVertice(actual);
				Vertice<verticeInfo, Long, Double> dos=getVertice(anterior);
				double lat1=uno.darValor().darLatitud(); 
				double lat2=dos.darValor().darLatitud();
				double deltalat=lat2-lat1; 
				double long1=uno.darValor().darlongitud(); 
				double long2=dos.darValor().darlongitud(); 
				double deltalong=long2-long1; 
				double a = Math.pow(Math.sin(deltalat/2),2)+Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin(deltalong/2),2); 
				double b=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
				double dis=radio*b; 
				uno.agregarArco(dis, dos.darLlave());
				dos.agregarArco(dis, uno.darLlave());			
				nArcos++; 
			}
			anterior=actual;
			break; 
		}
	}
	public Vertice<verticeInfo, Long, Double> getVertice(Long pId) {
		Vertice<verticeInfo, Long, Double> buscado=null;
		for(int i=0;i<vertices.size(); i++) {
			Vertice<verticeInfo, Long, Double> idtemp=vertices.get(i);			
			if(vertices.get(i).darLlave().equals(pId)) {		
				buscado=vertices.get(i); 
			}
		}
		return buscado; 
	}
	public int darNumArcos() {
		return nArcos; 
		}
	public void JsonVertices() throws FileNotFoundException {
		PrintWriter pw= new PrintWriter(new File("."+File.separator+"data"+File.separator+"JsonVertices"));
		System.out.println("creo");
		boolean cerrado=true;
		String fin="},";
		pw.println("[");
		for(int i=0; i<vertices.size(); i++) {
			Vertice<verticeInfo, Long, Double> actual= vertices.get(i); 
			pw.println("{");
			pw.println("\"id\":"+vertices.get(i).darLlave()+",");
			pw.println("\"lat\":"+vertices.get(i).darValor().darLatitud()+",");
			pw.println("\"lon\":"+vertices.get(i).darValor().darlongitud()+",");
			String arcos=""; 
			for (int j=0;j<actual.getArcos().size();j++) {
				if(j==actual.getArcos().size()-1) {
					arcos+=actual.getArcos().get(j).darAdyacente(); 
				}else {
					arcos+=actual.getArcos().get(j).darAdyacente()+","; 
				}
			}
			pw.println("\"adj\":["+arcos+"]");
			if(i==vertices.size()-1) {
				fin="}"; 
			}
			pw.println(fin); 
		}
		pw.println("]");
		pw.close();
	}
}