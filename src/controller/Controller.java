package controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;

import com.opencsv.CSVReader;
import com.sun.corba.se.impl.orbutil.graph.Node;
import com.sun.org.apache.xerces.internal.util.IntStack;

import mapa.Mapa;
import model.data_structures.Arco;
import model.data_structures.Dupla;
import model.data_structures.Grafo;
import model.data_structures.HashTableChaining;
import model.data_structures.IMaxColaPrioridad;
import model.data_structures.IQueue;
import model.data_structures.IStack;
import model.data_structures.Iterador;
import model.data_structures.LinearProbingHashST;
import model.data_structures.MaxColaPrioridad;
import model.data_structures.Nodo;
import model.data_structures.Queue;
import model.data_structures.RedBlackBST;
import model.data_structures.Stack;
import model.data_structures.Tupla;
import model.data_structures.Vertice;
import model.vo.VODaylyStatistic;
import model.vo.VOMovingViolations;
import model.vo.VOranking;
import model.vo.verticeInfo;
import sun.awt.image.VolatileSurfaceManager;
import sun.nio.cs.ext.ISCII91;
import view.MovingViolationsManagerView;

public class Controller
{
	public static final double radio=6.371;
	public static final Integer INFINITY=10000000;
	/**
	 * View para interaccion con usuario
	 */
	private MovingViolationsManagerView view;
	/**
	 * Pila donde se van a cargar los datos de los archivos
	 */ 
	private IStack<VOMovingViolations> movingViolationsStack;
	/**
	 * Instancia de grafo con la informacion de vertices y arcos entre si
	 */
	private Grafo<verticeInfo,Long, Double> grafo;
	/**
	 * Tabla de hash de infracciones asociadas a los vertices mas cercanos
	 */
	private LinearProbingHashST<Integer, VOMovingViolations> tablainfracciones;
	/**
	 * Mapa que permite la ilustracion de vertices y arcos del atributo grafo
	 */
	private Mapa mapa; 
	Stack<Vertice<verticeInfo,Long,Double>> resultadomatriz; 

	//CONSTRUCTOR
	public Controller()
	{
		view = new MovingViolationsManagerView();
		grafo=new Grafo<>(); 
		movingViolationsStack = new Stack<>(); 
		tablainfracciones= new LinearProbingHashST<Integer, VOMovingViolations>(); 
		resultadomatriz=null; 
	}

	public void run()
	{	
		Scanner sc= new Scanner(System.in);
		boolean fin=false; 
		while(!fin) {
			view.printMenu();
			int option=sc.nextInt();
			switch(option) {
			case 1:
				cargarDatosJson();
				System.out.println(grafo.numArcos()+" arcos y " +grafo.numVertices()+" vertices cargados");
				break;
			case 2:

				Vertice<verticeInfo, Long, Double> aleatorio1 = darAleatorio(); //obtencion de infracciones aleatorias
				Vertice<verticeInfo, Long, Double> aleatorio2 = darAleatorio();
				String latlon1=aleatorio1.darValor().darLatitud()+", "+aleatorio1.darValor().darlongitud();
				String latlon2=aleatorio2.darValor().darLatitud()+", "+aleatorio2.darValor().darlongitud();

				System.out.println("El par de vertices escogidos aleatoriamente tienen coordenadas"+latlon1+" y " +latlon2);
				Stack<Arco<Long, Double>> pilaArcosMinInfrac = caminoMenorCosto(aleatorio1,aleatorio2);
				int n = 2;//PROVISIONAL
				int d = 10;//PROVISIONAL
				System.out.println("Con un costo minimo de "+n+" infracciones, y una distancia de "+d+"km, el camino que une los nodos es:");
				break; 
			case 3:
				System.out.println("Ingrese el numero de vertices con maximo numero de infracciones que desea consultar");
				int numeroMaximos = Integer.parseInt(sc.next());

				break; 
			case 4: 
				Stack<Arco<Long, Double>> arcos=requerimiento4();
				break; 
			case 5: 
				System.out.println("Ingrese los 6 datos en el siguiente formato: el intervalo de latitud (latMin,latMax,longMin, longMax,numfilas,numcolumnas)");
				String datos=sc.next(); 
				double latMin=Double.parseDouble(datos.split(",")[0]);
				double latMax=Double.parseDouble(datos.split(",")[1]); 
				double longMin=Double.parseDouble(datos.split(",")[2]); 
				double longMax=Double.parseDouble(datos.split(",")[3]);
				int filas=Integer.parseInt(datos.split(",")[4]); 
				int colum=Integer.parseInt(datos.split(",")[5]);
				resultadomatriz=requerimiento5(latMin, latMax, longMin, longMax, filas, colum);
				break; 
			case 8: 
				if(resultadomatriz!=null){
					double inicio=System.currentTimeMillis(); 
				LinearProbingHashST< Integer, Stack<Arco<Long,Double>>> caminos=requerimiento8(resultadomatriz);
				double fintiempo=System.currentTimeMillis()-inicio; 
				System.out.println("el algoritmo se demora "+fintiempo+" en ejecución");
				view.printreq8(caminos); 
				}else{
					System.out.println("No se ha realizado la aproximación de la matriz");
				}
				break; 
			case 9:
				double inicio=System.currentTimeMillis(); 
				Stack<Long> resultado=requerimiento9(); 
				double f=System.currentTimeMillis()-inicio; 
				System.out.println("se tardó "+f+" milisegundos en ejecutar");
				view.printreq9(resultado,grafo); 
				
			case 12:	
				fin=true;
				sc.close();
				break;
			}
		}
	}

	/**
	 * Metodo que carga los vertices, arcos e infracciones del arhivo JSON finalGraph.json
	 */
	public void cargarDatosJson()
	{
		Arco<Long,Double> agregar; 
		String file="."+File.separator+"data"+File.separator+"finalGraph.json";  
		int arcosagregados=0; 
		int verticesagregados=0; 
		try{
			JsonParser parser= new JsonParser(); 
			JsonArray arr= (JsonArray)parser.parse(new FileReader(file));
			for (int i=0; i<arr.size() && arr!=null; i++)
			{
				JsonObject obj=(JsonObject)arr.get(i); //lectura de objeto tipo JsonObject actual
				Long id=Long.parseLong(obj.get("id").getAsString());
				Double lat=Double.parseDouble(obj.get("lat").getAsString()); 
				Double log=Double.parseDouble(obj.get("lon").getAsString()); 
				grafo.addVertex(id, new Vertice<verticeInfo, Long, Double>(id, new verticeInfo(lat,log)));//creacion de nuevo vertice	

				JsonArray arcos=obj.get("adj").getAsJsonArray(); //arreglo de arcos asociados a archivo JSON
				arcosagregados+=arcos.size(); 
				for (int j=0; j<arcos.size(); j++)
				{
					Long llegada= arcos.get(j).getAsLong(); 
					grafo.getVertice(id).agregarLongAdyacente(llegada);
				}
				JsonArray infracciones=obj.get("infractions").getAsJsonArray(); //infracciones asociadas a vertice
				for(int k=0; k<infracciones.size(); k++)
				{
					grafo.getVertice(id).setInfraccion(infracciones.get(k).getAsInt());
				}
			}
			Iterator<Vertice<verticeInfo, Long, Double>> iter = grafo.darTablaVertices().keys().iterator(); 
			Vertice<verticeInfo, Long, Double> actual; 
			double dis=0; 
			while(iter.hasNext())
			{
				actual=iter.next(); 
				Iterator<Long> iter2=actual.darLongAdyacente().iterator();
				Long pId;
				while (iter2.hasNext())
				{
					pId=iter2.next(); 
					Vertice<verticeInfo, Long, Double> actual2=grafo.darTablaVertices().get(pId);
					dis=calcularHarvesine(actual,actual2); 
					grafo.addEdge(actual.darLlave(), pId, new Arco<Long, Double>(dis, actual.darLlave(),pId));
				}
			}
			mapa=new Mapa(grafo,1,null); 

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public double calcularHarvesine(Vertice<verticeInfo, Long, Double> uno, Vertice<verticeInfo, Long, Double> dos){

		double deltalat=dos.darValor().darLatitud()-uno.darValor().darLatitud();
		double deltalog=dos.darValor().darlongitud()-uno.darValor().darlongitud(); 
		double a=Math.pow(Math.sin(deltalat/2), 2)+ Math.cos(uno.darValor().darLatitud())*Math.cos(dos.darValor().darLatitud())*Math.pow(Math.sin(deltalog), 2);
		double c=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a)); 
		return radio*c; 
	}
	/**
	 * Metodo que retorna un vertice aleatorio del atributo grafo
	 * @return retornar vertice de tipo <verticeInfo, Long, Double>
	 */
	public Vertice<verticeInfo, Long, Double> darAleatorio()
	{
		Vertice<verticeInfo, Long, Double> retornar=null; 
		int a; 
		while(retornar==null)
		{
			int contador=0; 
			a=(int)(Math.random()*grafo.darTablaVertices().size()); 
			Iterator<Vertice<verticeInfo, Long, Double>> iter = grafo.darTablaVertices().keys().iterator(); 
			while(iter.hasNext()&&contador!=a)
			{
				retornar=iter.next(); 
				contador++; 
			}
		}
		return retornar; 
	}
	/**
	 * Requerimiento 2:
	 * Metodo que retorna una pila de arcos correspondiente al trayecto de menor costo entre los vertices recibidos por parametro
	 * @return theWay pila de arcos tipo  
	 */
	public Stack<Arco<Long, Double>> caminoMenorCosto(Vertice<verticeInfo, Long, Double> vertice1, Vertice<verticeInfo, Long, Double> vertice2)
	{ 
		//idea: utilizar la implementacion con distTo() y edgeTo() para el algoritmo de Djistra con una tabla de Hash Linear
		//con esta lista se relajan los caminos entre vertices hasta llegar al vertice deseado
		
		Stack<Arco<Long, Double>> MST=BFS(vertice1, vertice2); //arcos disponibles para estimar el mejor camino
		Iterator<Arco<Long, Double>> iteradorArcos = MST.iterator(); //Iterador
		Stack<Arco<Long, Double>> pilafinal= new Stack<>(); //pila a retornar
		
		LinearProbingHashST<Long,Integer> distTo= new LinearProbingHashST<>(); //distancia a vertices desde vertice 1
		LinearProbingHashST<Long,Long> edgeTo= new LinearProbingHashST<>(); //ultimo vertice desde el cual se llego
		LinearProbingHashST<Long,Vertice<verticeInfo,Long, Double>> marcados= new LinearProbingHashST<>(); //Tabla de marcados
		Queue<Long> verticesRestantes=new Queue<>();//vertices a recorrer optimizando distancias restantes
		
		marcados.put(vertice1.darLlave(), vertice1);
		distTo.put(vertice1.darLlave(), 0);//se inicializa la distancia en cero
		edgeTo.put(vertice1.darLlave(), vertice1.darLlave());
		
		Long cero =(long) 0; //necesario para inicializar el edgeTo
		Arco<Long,Double> arcoActual=null;
		while(iteradorArcos.hasNext())//llenado de distTo y edgeTo de todos los nodos en MST
		{
			arcoActual=iteradorArcos.next();
			if(distTo.get(arcoActual.darInicio())==null)
			{
				distTo.put(arcoActual.darInicio(), INFINITY);
				edgeTo.put(arcoActual.darInicio(), cero);
			}
			if(distTo.get(arcoActual.darAdyacente())==null)
			{
				distTo.put(arcoActual.darAdyacente(), INFINITY);
				edgeTo.put(arcoActual.darAdyacente(), cero);
			}
		}
		//Hasta aqui la primera iteracion, ahora a actualizar iterativamente
		
		Iterador<Long> adjActuales=(Iterador<Long>) vertice1.darAdyacentes().iterator();//1 iteracion
		Vertice<verticeInfo,Long,Double> verticeIteracion=null;
		Vertice<verticeInfo,Long,Double> verticeMinIteracion=null;
		Integer minIteracion=INFINITY;
		
		while(iteradorArcos.hasNext())
		{
			while(adjActuales.hasNext())
			{
				verticeIteracion=grafo.getVertice(adjActuales.next());
				if(verticeIteracion.darAdyacentes().size()==0) //marcacion de vertice si este no tiene adyacentes
				{
					marcados.put(verticeIteracion.darLlave(), verticeIteracion);
				}
				else //guardado de vertice por terminar si este tiene adyacentes
				{
					verticesRestantes.enqueue(verticeIteracion.darLlave());
				}
				
				if(verticeIteracion.darNInfracciones()<minIteracion)//obtencion de minimo
				{
					minIteracion=verticeIteracion.darNInfracciones();
					verticeMinIteracion = verticeIteracion;
				}
				if(distTo.get(verticeIteracion.darLlave()) > verticeIteracion.darNInfracciones())
				{
					distTo.put(verticeIteracion.darLlave(), verticeIteracion.darNInfracciones());
				}
			}
			distTo.put(verticeMinIteracion.darLlave(), minIteracion);
			minIteracion=INFINITY;
			verticeMinIteracion=null;			
		}
		
		return null;
	}

	public Stack<Arco<Long, Double>> requerimiento4()
	{ 
		Vertice<verticeInfo, Long, Double> inicio= darAleatorio(); 
		Vertice<verticeInfo, Long, Double> fin=darAleatorio(); 
		Stack<Arco<Long, Double>> MST=BFS(inicio, fin); 
		Stack<Arco<Long, Double>> pilafinal= new Stack<>(); 
		Arco<Long, Double> primero=MST.pop(); 
		Long inicioanterior=primero.darInicio(); 
		pilafinal.push(primero);
		while(!MST.isEmpty()) {
			Arco<Long, Double> actual=MST.pop(); 
			if(actual.darAdyacente().equals(inicioanterior)) {
				pilafinal.push(actual);
				inicioanterior=actual.darInicio(); 
			}

		}
		view.printreq4(pilafinal,grafo); 
		grafo.setPilaArcos(pilafinal);
		mapa=new Mapa(grafo,1,null);
		return pilafinal; 
	}
	
	/**
	 * Metodo que retorna la pila de arcos asociada al BFS entre dos vertices recibidos por parametro
	 * @return retornar pila de arcos tipo Long, Double 
	 */
	public Stack<Arco<Long, Double>> BFS(Vertice<verticeInfo, Long, Double> inicio, Vertice<verticeInfo,Long, Double> fin)
	{
		Stack<Arco<Long, Double>> retornar= new Stack<>(); //pila a retornar
		boolean encontro=false; 
		LinearProbingHashST<Long,Vertice<verticeInfo,Long, Double>> marcados= new LinearProbingHashST<>(); //tabla de marcacion
		
		Queue<Long> cola= new Queue<>(); 
		marcados.put(inicio.darLlave(), inicio);
		cola.enqueue(inicio.darLlave());
		
		while(!cola.isEmpty()&&!encontro)
		{
			Long actual=cola.dequeue();  
			Iterator<Long> iter = grafo.getVertice(actual).darAdyacentes().iterator();
			Long longactual;
			while(iter.hasNext()&&!encontro) //iteracion sobre adyacentes actuales
			{
				longactual=iter.next();
				if(marcados.get(longactual)==null)
				{
					marcados.put(longactual, grafo.getVertice(longactual));
					cola.enqueue(longactual);
					retornar.push(grafo.getArc(actual,longactual));
					if(longactual.equals(fin.darLlave())) 
					{
						encontro=true; 
					}
				} 
			}
		}
		return retornar; 
	}

	public Stack<Vertice<verticeInfo, Long, Double>>  requerimiento5(Double latMin, Double latMax, Double longMin, Double longMax, int m, int n) {
		double deltalat=Math.abs(latMin)+Math.abs(latMax); 
		double deltalog=Math.abs(longMin)+Math.abs(longMax);
		double avancelat=deltalat/(m-1); 
		double avancelong=deltalog/(n-1); 
		Stack<verticeInfo> pila= new Stack<>(); 
		for(double i=latMin; i<=latMax; i+=avancelat) {
			for(double j=longMin; j<=longMax; j+=avancelong) {
				pila.push(new verticeInfo(i,j));
			}
		}
		Stack<Vertice<verticeInfo, Long, Double>> retornar= new Stack<>(); 
		while(!pila.isEmpty()) {
			verticeInfo info=pila.pop(); 
			Vertice<verticeInfo,Long, Double> mascercano=null; 
			double minima=1000;
			double disactual; 
			Iterator<Vertice<verticeInfo, Long, Double>> iter = grafo.darTablaVertices().keys().iterator();
			Vertice<verticeInfo, Long, Double> actual;
			while(iter.hasNext()) {
				actual=iter.next(); 
				double latActual=actual.darValor().darLatitud(); 
				double longActual=actual.darValor().darlongitud(); 
				if(latActual>=latMin&&latActual<=latMax&&longActual>=longMin&&longActual<=longMax){
					disactual=HarvesianaInfo(info, actual.darValor()); 
					if(disactual<minima) {
						minima=disactual; 
						mascercano=actual; 
					}
				}		
			}
			retornar.push(mascercano);
		}
		view.printreq5(retornar); 
		mapa= new Mapa(grafo, 2, retornar); 
		return retornar; 

	}

	public double HarvesianaInfo(verticeInfo uno, verticeInfo dos){

		double deltalat=dos.darLatitud()-uno.darLatitud();
		double deltalog=dos.darlongitud()-uno.darlongitud(); 
		double a=Math.pow(Math.sin(deltalat/2), 2)+ Math.cos(uno.darLatitud())*Math.cos(dos.darLatitud())*Math.pow(Math.sin(deltalog), 2);
		double c=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a)); 
		return radio*c; 
	}

	public Stack<Long> requerimiento9(){
		Vertice<verticeInfo, Long, Double> inicio=darAleatorio();
		Vertice<verticeInfo, Long, Double> fin=darAleatorio(); 
		LinearProbingHashST<Integer, Stack<Long>> caminos=new Caminos(grafo, inicio, fin).darCaminosTodos(); 
		Iterator<Stack<Long>> itercaminos = caminos.keys().iterator(); 
		int verticesnumero=1000;
		int infracciones=0; 
		Stack<Long> retornar=new Stack<>(); 
		while(itercaminos.hasNext()) {
			Stack<Long> caminoactual=itercaminos.next(); 
			if(caminoactual.size()<verticesnumero) {
				verticesnumero=caminoactual.size(); 
				retornar=caminoactual; 
				infracciones=calcularnumerodeinfracciones(caminoactual); 
			}else if(caminoactual.size()==verticesnumero) {
				int infraccionesactual=calcularnumerodeinfracciones(caminoactual); 
				if(infraccionesactual<infracciones) {
					retornar=caminoactual; 
					infracciones=infraccionesactual; 
				}
			}
		}
		System.out.println("El camino calculado tiene " + infracciones+ " infracciones");
		return retornar; 
		
	} 
	public int calcularnumerodeinfracciones (Stack<Long> vertices) {
		if(vertices.isEmpty()) {
			return 0; 
		}else {
			int infracciones=0; 
			Iterator<Long> iter = vertices.iterator(); 
			while(iter.hasNext()) {
			Vertice<verticeInfo,Long, Double>actual=grafo.getVertice(iter.next()); 
			if(actual!=null) {
				infracciones+=actual.darNInfracciones(); 
			}
		}
			return infracciones; 
		}
	}
	public LinearProbingHashST<Integer,Stack<Arco<Long, Double>>> requerimiento8(Stack<Vertice<verticeInfo, Long, Double>> vertices){
		LinearProbingHashST<Integer,Stack<Arco<Long, Double>>> retornar=new LinearProbingHashST<Integer, Stack<Arco<Long,Double>>>(); 
		Iterator<Vertice<verticeInfo, Long, Double>> iter = vertices.iterator(); 
		Vertice<verticeInfo, Long, Double> inicio=vertices.darPrimero().darElemento();
		int i=0; 
		while(iter.hasNext()&&i<vertices.size()) {
			Vertice<verticeInfo,Long, Double>fin=iter.next(); 
			Stack<Arco<Long,Double>> camino=Djikstra(inicio, fin, grafo.darTablaVertices().size()); 
			retornar.put(i, camino); 
			inicio=fin; 
		}	
		return retornar; 

	}

	public Stack<Arco<Long, Double>> Djikstra(Vertice<verticeInfo, Long, Double> inicio, Vertice<verticeInfo, Long, Double> fin, int n){
		Stack<Arco<Long, Double>> retornar=new Stack<>();
		Stack<Arco<Long, Double>> camino=new Stack<>();
		boolean encontro=false; 
		LinearProbingHashST<Long,Tupla<Arco<Long, Double>,Double>> tabla= new LinearProbingHashST<>();
		LinearProbingHashST<Long, Double> distancias=new LinearProbingHashST<>(); 
		Tupla<Arco<Long, Double>, Double> elementosacado=new Tupla<Arco<Long,Double>, Double>(null, 0.0);
		Long sacar=inicio.darLlave(); 
		tabla.put(inicio.darLlave(),elementosacado);
		distancias.put(sacar, elementosacado.darValor());
		int i=0; 
		while(i<n&&!encontro) {
			Vertice<verticeInfo, Long, Double> vertice=grafo.getVertice(sacar); 
			Iterator<Long> iter = vertice.darAdyacentes().iterator(); 
			while(iter.hasNext()&&!encontro) {
				Long adyacenteactual=iter.next(); 
				Arco<Long, Double> arco=grafo.getArc(sacar,adyacenteactual); 
				Double disto=distancias.get(sacar)+arco.darPeso(); 
				if(distancias.get(adyacenteactual).equals(null)) {
					if(tabla.get(adyacenteactual)==null) {
						tabla.put(adyacenteactual, new Tupla<Arco<Long,Double>, Double>(arco, disto));
					}else {
						Tupla<Arco<Long, Double>,Double> tupla= tabla.get(adyacenteactual); 
						if(disto<tupla.darValor()) {
							tupla.setLlave(arco);
							tupla.setValor(disto);
						}
					}

				}
			}
			sacar=darMenor(tabla);
			elementosacado=tabla.get(sacar);
			camino.push(elementosacado.darllave());
			tabla.delete(sacar);
			distancias.put(sacar, elementosacado.darValor());
			if(elementosacado.darllave().darAdyacente().equals(fin.darLlave())) {
				encontro=true;
			}
			i++; 
		}
		Arco<Long,Double> actual=camino.pop();
		retornar.push(actual);
		Long inicioanterior=actual.darInicio();
		while(!camino.isEmpty()){
			actual=camino.pop(); 
			if(actual.darAdyacente().equals(inicioanterior)){
				retornar.push(actual);
				inicioanterior=actual.darInicio(); 
			}
		}
		return retornar; 
	}

	private Long darMenor(LinearProbingHashST<Long, Tupla<Arco<Long, Double>, Double>> tabla) {
		Double menor=1000.0;
		Long retornar=null; 
		Iterator<Tupla<Arco<Long, Double>, Double>> iter = tabla.keys().iterator(); 
		while(iter.hasNext()) {
			Tupla<Arco<Long, Double>, Double> tupla=iter.next(); 
			if(tupla.darValor()<menor) {
				retornar=tupla.darllave().darAdyacente(); 
				menor=tupla.darValor(); 
			}
		}
		return retornar;
	}
	//	/**
	//	 * Metodo para carga de archivos segun semestre de seleccion
	//	 * @param num Semestre a cargar datos (1 para primer semestre, cualquier otro numero para segundo semestre)
	//	 */
	//	@SuppressWarnings("deprecation")
	//	public int[] loadMovingViolations(int num)
	//	{
	//		//estructuras de almacenamiento de infracciones
	//		movingViolationsStack= new Stack<VOMovingViolations>();
	//		//creacion e inicializacion de arreglo con nombre de los archivos de infracciones por mes 
	//		String[] nombresArchivos=new String[12];
	//		nombresArchivos[0]="."+File.separator+"data"+File.separator+"January_wgs84.csv";
	//		nombresArchivos[1]="."+File.separator+"data"+File.separator+"February_wgs84.csv";
	//		nombresArchivos[2]="."+File.separator+"data"+File.separator+"March_wgs84.csv";
	//		nombresArchivos[3]="."+File.separator+"data"+File.separator+"April_wgs84.csv";
	//		nombresArchivos[4]="."+File.separator+"data"+File.separator+"May_wgs84.csv";
	//		nombresArchivos[5]="."+File.separator+"data"+File.separator+"June_wgs84.csv";
	//		nombresArchivos[6]="."+File.separator+"data"+File.separator+"July_wgs84.csv";
	//		nombresArchivos[7]="."+File.separator+"data"+File.separator+"August_wgs84.csv";
	//		nombresArchivos[8]="."+File.separator+"data"+File.separator+"September_wgs84.csv";
	//		nombresArchivos[9]="."+File.separator+"data"+File.separator+"October_wgs84.csv";
	//		nombresArchivos[10]="."+File.separator+"data"+File.separator+"November_wgs84.csv";
	//		nombresArchivos[11]="."+File.separator+"data"+File.separator+"December_wgs84.csv";
	//		int previo=0;
	//		int[] pormes=new int[6];
	//		int pos=0; 
	//		CSVReader reader=null;
	//		int inicio=-1; 
	//		if(num==1)
	//		{
	//			inicio=0; //lectura de archivos a partir del primer mes.
	//		}
	//		else
	//		{
	//			inicio=6; //lectura de archivos a partir del septimo mes.
	//		}
	//		for(int i=inicio; i<inicio+6;i++)//ciclo para lectura de semestre seleccionado
	//		{		
	//			try
	//			{
	//				//Lector de archivos para la posicion i-esima 
	//				reader=new CSVReader(new FileReader(nombresArchivos[i]));
	//				String[] linea=reader.readNext();
	//				linea=reader.readNext();
	//				while(linea!=null)
	//				{
	//					int tres=linea[3].equals("")?0:Integer.parseInt(linea[3]);
	//					//separacion de coordenadas X y Y
	//					double cinco=linea[5].equals("")?0:Double.parseDouble(linea[5]);
	//					double seis=linea[6].equals("")?0:Double.parseDouble(linea[6]);
	//
	//					//StreetSegID
	//					int cuatro=linea[4].equals("")?0:Integer.parseInt(linea[4]);
	//
	//					double diez=linea[10].equals("")?0: Double.parseDouble(linea[10]);
	//					double once=linea[11].equals("")?0:Double.parseDouble(linea[11]);
	//
	//
	//					//creacion de infraccion en estructura de datos para campos definidos
	//					movingViolationsStack.push(new VOMovingViolations(Integer.parseInt(linea[0]), linea[2], linea[13], Double.parseDouble(linea[9]), linea[12], linea[15], linea[14], Double.parseDouble(linea[8]),tres,diez,once,cinco,seis,cuatro));
	//					linea=reader.readNext();
	//				}
	//				pormes[pos]=movingViolationsStack.size()-previo;
	//				previo=movingViolationsStack.size();
	//				pos++;
	//			}
	//			catch(Exception e)
	//			{
	//				e.printStackTrace();
	//			}
	//			finally
	//			{
	//				if(reader!=null)
	//				{
	//					try
	//					{
	//						reader.close();
	//					}
	//					catch(IOException e)
	//					{
	//						e.printStackTrace();	
	//					}
	//				}
	//			}
	//		}
	//
	//		return pormes; 
	//	}
	//
	//	/**
	//	 * 1A Metodo para obtener las N franjas horarias con el mayor numero de infracciones
	//	 * @param nFranjas numero de franjas a retornar (nFranjas<24)
	//	 */
	//	public String[] nFranjasHorarias(int nFranjas)
	//	{
	//		//IDEA: registrar toda la informacion promedio de las infracciones en un VOranking por hora y anadirla a una cola de prioridad
	//		//el VOranking es inicializado en su id con el numero de infracciones para utilizar compareTo
	//		//location es inicializado con la franja horaria, streetsegid como 0
	//
	//		IStack<VOMovingViolations> copiaViolationsStack =  movingViolationsStack; //copia de stack de infracciones
	//		MaxColaPrioridad<VOranking> estadisticasNInfracciones = null; //cola de prioridad con VOranking
	//		VOMovingViolations violacionActual=null; //violacion de recorrido
	//		VOranking rankingActual=null; //estadistica de recorrido
	//
	//		int indice = 0; //indice de franja horaria
	//		boolean acc = false; //indicador de accidente
	//
	//		//inicializacion de VOranking como bloques de informacion promedio por rango de hora
	//		VOranking[] franjas = new VOranking[24];
	//		for (int i = 0; i<10;i++)
	//		{
	//			franjas[i]= new VOranking("", 0, 100, 100, 0, "0"+i+":00:00 - 0"+i+":59:59",0);
	//		}
	//		for (int j = 10; j<24;j++)
	//		{
	//			franjas[j]= new VOranking("", 0, 100, 100, 0, j+":00:00 - "+j+":59:59",0);
	//		}
	//		while(!copiaViolationsStack.isEmpty()) //se vacia la pila copia para actualizar la informacion por violacion
	//		{
	//			violacionActual = copiaViolationsStack.pop();
	//			indice = Integer.parseInt(violacionActual.getTicketIssueDate().split("T")[1].split(":")[0]);
	//			indice = indice==24? 0:indice;//condicion de caso especial de franja horario
	//			acc = violacionActual.getAccidentIndicator().equals("Yes");
	//			franjas[indice].actualizarInfo(acc, (int)(violacionActual.getFINEAMT()+violacionActual.getPenalty1()+violacionActual.getPenalty2()-violacionActual.getTotalPaid()));
	//		}
	//		for(int k = 0; k<24; k++)
	//		{
	//			estadisticasNInfracciones.agregar(franjas[k]);
	//		}
	//
	//		String[] mensaje =new String[nFranjas]; //arreglo de Strings a retornar
	//		for(int conteoFinal = 0; conteoFinal<nFranjas; conteoFinal++)
	//		{
	//			rankingActual = estadisticasNInfracciones.delMax();
	//			//mensaje requerido para infracciones
	//			mensaje[conteoFinal]="Franja horaria:"+ rankingActual.darLocation() +" Numero de infracciones:"+ rankingActual.darnumInfracciones()+"Porcentaje sin accidentes:"+rankingActual.darPorcentajeSinAccidentes()+"% Porcentaje con accidentes:"+rankingActual.porPorcentajeAccidentes()+"% Deuda Total:"+rankingActual.darTotalDeuda(); 
	//		}
	//		return mensaje;
	//	}
	//
	//	/**
	//	 * 2A Metodo para ordenar infracciones geograficamente, Xcoord es la desigualdad principal y Ycoord la secundaria
	//	 */
	//	public HashTableChaining<Tupla,VOMovingViolations> ordenarGeograficamente()
	//	{
	//		//idea: Utilizar separate Chaining para entregar todas las infracciones en esa ubicacion geografica
	//		// el valor de la dupla con VOMovingViolations y la llave son la tupla "XCoord,YCoord"
	//
	//		IStack<VOMovingViolations> copiaViolationsStack =  movingViolationsStack; //copia de stack de infracciones
	//		HashTableChaining<Tupla,VOMovingViolations> tablaGeografica = new HashTableChaining(); //tabla de ordenamiento hash separate Chaining
	//		VOMovingViolations violacionActual=null; //violacion de recorrido
	//
	//		while(!copiaViolationsStack.isEmpty()) //se vacia la pila copia para actualizar la informacion por violacion
	//		{
	//			violacionActual=copiaViolationsStack.pop();
	//			tablaGeografica.put(new Tupla(violacionActual.getX(),violacionActual.getY()), violacionActual);
	//		}
	//
	//		return tablaGeografica;
	//	}
	//
	//	/**
	//	 * 2A Metodo para obtener informacion principal de infracciones en un par de coordenadas
	//	 */
	//	public String infoInfraccionesGeograficas(Dupla<Tupla,VOMovingViolations> pDupla)
	//	{
	//		int tamano = pDupla.chain.darTamano(); //tamano de arreglo dinamico de duplas que corresponden a las mismas coordenadas x,y
	//		System.out.println("Las " + tamano +"infracciones tienen la siguiente informacion:");
	//		VOMovingViolations violacionActual =null;//violacion de recorrido
	//		String mensaje= ""; //mensaje a retornar
	//		VOranking rankingActual=null; //estadistica de recorrido
	//		boolean acc = false;//indicador de accidente
	//		int pDeuda = 0; 
	//
	//		for(int i = 0; i<tamano; i++) //actualizacion de toda la informacion
	//		{
	//			violacionActual = (VOMovingViolations) pDupla.chain.darElemento(i).getValue();
	//			acc = violacionActual.getAccidentIndicator().equals("Yes");
	//			pDeuda = (int)(violacionActual.getFINEAMT()+violacionActual.getPenalty1()+violacionActual.getPenalty2()-violacionActual.getTotalPaid());
	//			rankingActual.actualizarInfo(acc,pDeuda );
	//		}
	//		mensaje= " Numero de infracciones:"+ rankingActual.darnumInfracciones()+"Porcentaje sin accidentes:"+rankingActual.darPorcentajeSinAccidentes()+"% Porcentaje con accidentes:"+rankingActual.porPorcentajeAccidentes()+"% Deuda Total:"+rankingActual.darTotalDeuda(); 
	//		return mensaje;
	//	}
	//
	//
	//	/**
	//	 * 3A Metodo para obtener las infracciones dentro de un rango determinado en un arbol balanceado
	//	 * @param pRango formato AAAA-MM-DD/AAAA-MM-DD de fechas iniciales y finales
	//	 */
	//	public String[] infraccionesFecha(String pRango)
	//	{
	//		//idea: Inscribir las fechas en el arbol que esten en el rango ingresado con llave fecha y valor VOranking, para no modificar el codigo
	//		//del arbol rojo negro se verifica que la fecha a inscribir no existe todavia
	//		try
	//		{
	//			//Separacion de fechas parametro
	//			String fechaInicial = (pRango.split("/"))[0];
	//			String fechaFinal = (pRango.split("/"))[1];
	//			String fechaActual = ""; //fecha de infraccion
	//
	//			IStack<VOMovingViolations> copiaViolationsStack =  movingViolationsStack; //copia de stack de infracciones
	//			IStack<String> copiaFechas = new Stack<String>(); //copia de fechas encontradas para retorno de mensajes
	//			RedBlackBST<String,VOranking> arbolBalanceado = new RedBlackBST(); //arbol balanceado de estadisticas por fecha
	//			VOMovingViolations violacionActual=null; //violacion de recorrido
	//			VOranking estadisticaActual=null; //estadistica de recorrido
	//			int n = 0;// numero de fechas distintivas
	//
	//			boolean acc = false;//indicador de accidente
	//			int pDeuda = 0; 
	//
	//			while(!copiaViolationsStack.isEmpty()) //se vacia la pila copia para actualizar la informacion por fechas
	//			{
	//				violacionActual=copiaViolationsStack.pop();
	//				fechaActual=(violacionActual.getTicketIssueDate().split("T"))[0];
	//				if(fechaActual.compareTo(fechaInicial)>0&&fechaActual.compareTo(fechaFinal)<0)
	//				{
	//					acc = violacionActual.getAccidentIndicator().equals("Yes");
	//					pDeuda = (int)(violacionActual.getFINEAMT()+violacionActual.getPenalty1()+violacionActual.getPenalty2()-violacionActual.getTotalPaid());
	//					if(arbolBalanceado.contains(fechaActual))
	//					{
	//						arbolBalanceado.get(fechaActual).actualizarInfo(acc, pDeuda);
	//					}
	//					else
	//					{
	//						//id como fecha, 1 infraccion, porcentaje inicial, porcentaje con inicial, deuda infraccion, no hay location comun, no hay streetsegid comun
	//						estadisticaActual=new VOranking(fechaActual,1,100,100,pDeuda,"",0); 
	//						arbolBalanceado.put(fechaActual,estadisticaActual );
	//						copiaFechas.push(fechaActual); //se anade la nueva fecha al arreglo
	//						n++;
	//					}
	//				}
	//			}
	//			//construccion del mensaje por fecha
	//			String[] mensaje = new String[n];
	//			for(int conteoFinal = 0; conteoFinal<n && !copiaFechas.isEmpty(); conteoFinal++)
	//			{
	//				estadisticaActual = arbolBalanceado.get(copiaFechas.pop());
	//				//mensaje requerido para infracciones
	//				mensaje[conteoFinal]= " Numero de infracciones:"+ estadisticaActual.darnumInfracciones()+"Porcentaje sin accidentes:"+estadisticaActual.darPorcentajeSinAccidentes()+"% Porcentaje con accidentes:"+estadisticaActual.porPorcentajeAccidentes()+"% Deuda Total:"+estadisticaActual.darTotalDeuda();
	//			}
	//			return mensaje;
	//
	//		}
	//		catch(Exception e)
	//		{
	//			e.printStackTrace();
	//		}
	//		return null;
	//	}
	//
	//	public double[] calcularMiniMax(){
	//		double[] coordenadas= new double[4];
	//		double xmin,xmax,ymin,ymax; 
	//		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
	//		VOMovingViolations actual= iter.next();
	//		xmin=actual.getX(); 
	//		xmax=actual.getX();
	//		ymin=actual.getY(); 
	//		ymax=actual.getY();
	//		while(iter.hasNext()){
	//			actual=iter.next(); 
	//			if(actual.getX()<xmin){
	//				xmin=actual.getX(); 
	//			}
	//			if(actual.getX()>xmax){
	//				xmax=actual.getX(); 
	//			}
	//			if(actual.getY()<ymin){
	//				ymin=actual.getY(); 
	//			}
	//			if(actual.getY()>ymax){
	//				ymax=actual.getY(); 
	//			}
	//		}
	//		coordenadas[0]=xmin; 
	//		coordenadas[1]=xmax; 
	//		coordenadas[2]=ymin; 
	//		coordenadas[3]=ymax;
	//		return coordenadas; 
	//	}
	//
	//
	//	public MaxColaPrioridad<VOranking> darRankingInfracciones (int N){
	//
	//		MaxColaPrioridad<VOranking> retornar= new MaxColaPrioridad<>(); 
	//		ArrayList<String> agregadas= new ArrayList<>();  
	//		while(agregadas.size()<N) {
	//			Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
	//			VOMovingViolations actual=iter.next(); 
	//			int masveces=0; 
	//			double accidente=0;
	//			double deuda=0;
	//			VOMovingViolations repetido=actual; 
	//			while (iter.hasNext()) { 
	//				if(!revisar(agregadas, actual.getViolationCode())) {
	//					int veces=0;
	//					Iterador<VOMovingViolations> iter2= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
	//					VOMovingViolations actual2=iter2.next();
	//					while(iter2.hasNext()) {
	//						if(actual2.getViolationCode().equals(actual.getViolationCode())) {
	//							veces++; 
	//							deuda+=actual2.getTotalPaid(); 
	//							if(actual2.getAccidentIndicator().equals("Yes")) {
	//								accidente++; 
	//							}
	//						}
	//						actual2=iter2.next(); 
	//					}
	//					if(veces>masveces) {
	//						masveces=veces; 
	//						repetido=actual; 
	//					}else {
	//						accidente=0;
	//						deuda=0; 
	//					}
	//					actual=iter.next(); 
	//				}else {
	//					actual=iter.next(); 
	//				}
	//			}
	//			agregadas.add(repetido.getViolationCode()); 
	//			double pPorcenacc=(accidente*100)/masveces; 
	//			double pPorcensinacc=100-pPorcenacc; 
	//			retornar.agregar(new VOranking(repetido.getViolationCode(),masveces, pPorcenacc, pPorcensinacc, deuda,null, 0)); 
	//		}
	//		return retornar; 
	//	}
	//
	//	public boolean revisar(ArrayList<String> arreglo, String codigo) {
	//		boolean respuesta=false;
	//		for(int i=0; i<arreglo.size() && !respuesta; i++) {
	//			if(arreglo.get(i).equals(codigo)) {
	//				respuesta=true; 
	//			}
	//		}
	//
	//		return respuesta;
	//	}
	//
	//	public boolean revisarTupla(ArrayList<Tupla> arreglo, Tupla num) {
	//		boolean respuesta=false; 
	//		for (int i=0; i<arreglo.size() &&!respuesta; i++) {
	//			if(arreglo.get(i).compareTo(num)==0) {
	//				respuesta=true; 
	//			}
	//		}
	//		return respuesta; 
	//	}
	//
	//	public VOranking ordenarPorlocalizacion(double x, double y) throws Exception 
	//	{
	//		Tupla buscada= new Tupla(x,y); 
	//		// Crear el arbol ordenado por las llaves(Tuplas)
	//		RedBlackBST<Tupla,IStack<VOMovingViolations>> arbol= new RedBlackBST<>();
	//		ArrayList<Tupla> agregadas= new ArrayList<>(); 
	//		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
	//		VOMovingViolations actual=iter.next(); 
	//		Tupla llave2=new Tupla(0,0); 
	//		while(iter.hasNext()) {
	//			Tupla llave=new Tupla(actual.getX(),actual.getY()); 
	//			if(!revisarTupla(agregadas, llave)) {
	//				IStack<VOMovingViolations> porAgregar= new Stack<>();
	//				Iterador<VOMovingViolations> iter2= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
	//				VOMovingViolations actual2=iter2.next(); 
	//				while (iter2.hasNext()) {
	//					llave2.setCoord(actual2.getX(),actual2.getY());
	//					if(llave.compareTo(llave2)==0) {
	//						porAgregar.push(actual2);
	//					}
	//				}
	//				arbol.put(llave, porAgregar);				
	//			}
	//			actual=iter.next(); 
	//		}
	//		IStack<VOMovingViolations> buscar=arbol.get(buscada);
	//		Iterador<VOMovingViolations> iter3=(Iterador<VOMovingViolations>) buscar.iterator();
	//		VOMovingViolations actual3=iter3.next(); 
	//		int acc=0; 
	//		double deuda=0; 
	//		while(iter3.hasNext()) {
	//			if(actual3.getAccidentIndicator().equals("Yes")) {
	//				acc++; 
	//				deuda+=actual3.getTotalPaid(); 
	//			}
	//		}
	//		double conacc=(acc*100)/buscar.size(); 
	//		double sinacc=100-conacc; 
	//
	//		return new VOranking(null, buscar.size(),conacc, sinacc,deuda,buscar.darPrimero().darElemento().getLocation(),buscar.darPrimero().darElemento().getStreetId());
	//	}
	//
	//
	//
	//	public void franjaFechaHora (double valorinicial, double valorfinal) {
	//		//PENDIENTE
	//	}
	//
	//	public VOranking getInformacionloc( int pId){
	//		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
	//		VOMovingViolations actual=iter.next();
	//		int total=0; 
	//		int accidentes=0; 
	//		double deuda=0;
	//		int streetId=0; 
	//		while(iter.hasNext()) {
	//			if(actual.getAdressId()==pId) {
	//				total++;
	//				streetId=actual.getStreetId(); 
	//				if(actual.getAccidentIndicator().equals("Yes")) {
	//					accidentes++; 
	//				}
	//				deuda+=actual.getTotalPaid(); 
	//			}
	//			actual=iter.next(); 
	//		}
	//		double pPorcenacc=(accidentes*100)/total; 
	//		double pPorcensinacc=100-pPorcenacc; 
	//		VOranking retornar= new VOranking(null, total, pPorcenacc, pPorcensinacc, deuda, null,streetId ); 
	//		return retornar; 
	//	}
	//
	//	public RedBlackBST<Double, String>  arbolRango (double inicial, double fin) throws Exception{
	//		RedBlackBST<Double, String> retornar= new RedBlackBST<>(); 
	//		String[] horas= new String[24]; 
	//		horas[0]="00";
	//		horas[1]="01";
	//		horas[2]="02"; 
	//		horas[3]="03"; 
	//		horas[4]="04"; 
	//		horas[5]="05"; 
	//		horas[6]="06"; 
	//		horas[7]="07"; 
	//		horas[8]="08"; 
	//		horas[9]="09" ; 
	//		horas[10]="10"; 
	//		horas[11]="11";
	//		horas[12]="12";
	//		horas[13]="13";
	//		horas[14]="14";
	//		horas[15]="15";
	//		horas[16]="16";
	//		horas[17]="17";
	//		horas[18]="18";
	//		horas[19]="19";
	//		horas[20]="20";
	//		horas[21]="21";
	//		horas[22]="22";
	//		horas[23]="23";
	//
	//		for (int i=0; i<horas.length; i++) {
	//			Iterador<VOMovingViolations> iter=(Iterador<VOMovingViolations>) movingViolationsStack.iterator(); 
	//			VOMovingViolations actual=iter.next();
	//			double acumulado=0; 
	//			while(iter.hasNext()) {
	//				if(actual.getTicketIssueDate().split("T")[1].split(":")[0].equals(horas[i])) {
	//					acumulado+=actual.getTotalPaid(); 
	//				}
	//				actual=iter.next(); 
	//			}
	//			if(acumulado<fin&&acumulado>inicial) {
	//				retornar.put(acumulado,horas[i]);
	//			}
	//		}		
	//		return retornar;
	//	}
	//
	//	public IStack<Dupla<String,Double>> tablaASCII(){
	//		IStack<Dupla<String,Double>> retornar= new Stack<>(); 
	//		Iterador<VOMovingViolations> iter=(Iterador<VOMovingViolations>) movingViolationsStack.iterator(); 
	//		VOMovingViolations actual=iter.next(); 
	//		ArrayList<String> revisadas= new ArrayList<>(); 
	//		while(iter.hasNext()) {
	//			double num=0; 
	//			if(!revisar(revisadas, actual.getViolationCode())) {
	//				Iterador<VOMovingViolations> iter2=(Iterador<VOMovingViolations>) movingViolationsStack.iterator(); 
	//				VOMovingViolations actual2=iter.next();
	//				if(actual2.getViolationCode().equals(actual.getViolationCode())) {
	//					num++; 
	//				}
	//			}
	//			Dupla<String, Double> agregar= new Dupla<>(actual.getViolationCode(),num); 
	//			actual=iter.next(); 
	//		}
	//
	//		return retornar; 
	//	}
	//	/**
	//	 * 2C Retorna un arreglo String con la informacion de las infracciones que estan dentro del rango y las que estan fuera del rango de horas recibido
	//	 */
	//	public String[] infoInfraccionesHora(String pHoras)
	//	{
	//		//Idea: obtener las estadisticas de las infracciones dentro del rango y ordenar las estadisticas de las infracciones restantes en una Hash Table para su clasificacion
	//		try
	//		{
	//			//Separacion de fechas parametro
	//			String horaInicial = (pHoras.split("/"))[0];
	//			String horaFinal = (pHoras.split("/"))[1];
	//			String horaActual = ""; //fecha de infraccion
	//
	//			IStack<VOMovingViolations> copiaViolationsStack =  movingViolationsStack; //copia de stack de infracciones
	//			VOMovingViolations violacionActual=null; //violacion de recorrido
	//			String codigoActual =""; //codigo de la infraccion
	//			HashTableChaining<String,VOranking> tablaViolationCode = new HashTableChaining(); //tabla de ordenamiento hash separate Chaining 
	//			IStack<String> copiaViolationCode = new Stack<String>(); //copia de violaciones encontradas para retorno de mensajes
	//
	//			//id inicializada, ninfracciones inicializadas, porcentaje inicial, porcentaje con inicial, deuda infraccion, no hay location comun, no hay streetsegid comun
	//			VOranking estadisticaRango=new VOranking("", 0, 100, 100, 0, "",0); //estadistica de recorrido
	//			//VOranking estadisticaFuera=new VOranking("", 0, 100, 100, 0, "",0); //estadistica de recorrido para Duplas en hash table
	//			int cantidad=0;
	//
	//			boolean acc = false;//indicador de accidente
	//			int pDeuda = 0; 
	//
	//			while(!copiaViolationsStack.isEmpty()) //se vacia la pila copia para actualizar la informacion por fechas
	//			{
	//				violacionActual=copiaViolationsStack.pop();
	//				horaActual=(violacionActual.getTicketIssueDate().split("T"))[1].split(".")[0]; //HH:MM:SS
	//				if(horaActual.compareTo(horaInicial)>0&&horaActual.compareTo(horaFinal)<0)
	//				{
	//					acc = violacionActual.getAccidentIndicator().equals("Yes");
	//					pDeuda = (int)(violacionActual.getFINEAMT()+violacionActual.getPenalty1()+violacionActual.getPenalty2()-violacionActual.getTotalPaid());
	//					estadisticaRango.actualizarInfo(acc, pDeuda); //se actualiza la informacion de las estadisticas dentro del rango
	//				}
	//				else
	//				{
	//					if(tablaViolationCode.get(violacionActual.getViolationCode())!=null)
	//					{
	//						tablaViolationCode.get(violacionActual.getViolationCode()).getValue().actualizarInfo(acc, pDeuda);
	//					}
	//					else
	//					{
	//						tablaViolationCode.put(violacionActual.getViolationCode(), new VOranking(violacionActual.getViolationCode(),1,100,100,0,"",0));
	//						copiaViolationCode.push(violacionActual.getViolationCode());
	//						cantidad++;
	//					}
	//
	//
	//				}
	//			}
	//			//construccion del mensaje por fecha
	//			String[] mensaje = new String[cantidad+1];
	//			mensaje[0]= " Numero de infracciones dentro de rango:"+ estadisticaRango.darnumInfracciones()+", Porcentaje sin accidentes:"+estadisticaRango.darPorcentajeSinAccidentes()+"%, Porcentaje con accidentes:"+estadisticaRango.porPorcentajeAccidentes()+"%, Deuda Total:"+estadisticaRango.darTotalDeuda();
	//			int conteoXD=1;
	//			//informacion de infracciones por fuera de ese rango
	//			while(!copiaViolationCode.isEmpty()&&conteoXD<cantidad+1)
	//			{
	//				mensaje[conteoXD]= "Violation Code por fuera de rango:"+tablaViolationCode.get(copiaViolationCode.pop()).getKey()+", numero de infracciones:"+tablaViolationCode.get(copiaViolationCode.pop()).getValue().darnumInfracciones();
	//				conteoXD++;
	//			}
	//			return mensaje;
	//
	//		}
	//		catch(Exception e)
	//		{
	//			e.printStackTrace();
	//		}
	//		return null;
	//	}
	//	/**
	//	 * 3C Retorna un arreglo String de tamano NLocalizaciones con los pares de coordenadas con mayor numero de infracciones
	//	 */
	//	public String[] infoNLocalizaciones(int NLocalizaciones)
	//	{
	//		//idea: recorrer el numero de elementos en el HashTableChaining utilizada en ordenarGeograficamente() con VOranking para actualizar una segunda MaxCola
	//		HashTableChaining<Tupla,VOMovingViolations> tablaOrdenadaGeo= ordenarGeograficamente();
	//		HashTableChaining<Tupla,VOranking> tablaEstadisticas= new HashTableChaining();
	//		Iterador<Tupla> recorrido = tablaOrdenadaGeo.keys(); //obtengo las llaves para realizar
	//
	//		VOMovingViolations violacionActual=null; //violacion Actual
	//		VOranking rankingActual=null; //estadistica actual
	//		Tupla tuplaActual=null; //tupla de recorrido
	//
	//		int contador = 0;
	//		int infraccion = 0;
	//		int pDeuda=0;
	//		boolean acc=false;
	//		while(recorrido.hasNext() && contador<NLocalizaciones) //paso de informacion a tabla de hash con VOranking
	//		{
	//			tuplaActual = recorrido.next();
	//			violacionActual= tablaOrdenadaGeo.get(tuplaActual).getValue();
	//			pDeuda = (int)(violacionActual.getFINEAMT()+violacionActual.getPenalty1()+violacionActual.getPenalty2()-violacionActual.getTotalPaid());
	//			acc = violacionActual.getAccidentIndicator().equals("Yes");
	//			infraccion= acc? 100:0;
	//			if(tablaEstadisticas.get(tuplaActual).getValue()==null)
	//			{
	//				rankingActual=new VOranking(""+violacionActual.getX()+","+violacionActual.getY(),1,100-infraccion,infraccion,pDeuda,violacionActual.getLocation(),violacionActual.getStreetId());
	//				tablaEstadisticas.put(tuplaActual, rankingActual);
	//			}
	//			else
	//			{
	//				tablaEstadisticas.get(tuplaActual).getValue().actualizarInfo(acc, pDeuda);
	//			}
	//			contador++; //registro de iteracion para break de while
	//		}
	//
	//		String[] mensaje = new String[NLocalizaciones];
	//		for(int i=0;i<NLocalizaciones;i++)
	//		{
	//			mensaje[i]= "En el par de coordenadas:"+rankingActual.darCode()+", Numero de infracciones:"+ rankingActual.darnumInfracciones()+",Porcentaje sin accidentes:"+rankingActual.darPorcentajeSinAccidentes()+"%, Porcentaje con accidentes:"+rankingActual.porPorcentajeAccidentes()+"%, Deuda Total:"+rankingActual.darTotalDeuda()+", En location: "+rankingActual.darLocation()+" y StreetSegID:"+rankingActual.darSreetiId(); 
	//		}
	//		return null;
	//	}
	//
	//	public int[] cargarDatos(int num) {
	//		int[] retornar=new int[2]; 
	//		String file=num==1?"Central-WashingtonDC-OpenStreetMap.xml":"exampleMap.xml";
	//		try {
	//			SAXParserFactory spf= SAXParserFactory.newInstance(); 
	//			spf.setNamespaceAware(true); 
	//			SAXParser saxParser= spf.newSAXParser(); 
	//			XMLReader xmlReader=saxParser.getXMLReader(); 
	//			xmlReader.setContentHandler(grafo);
	//			xmlReader.parse("."+File.separator+"data"+File.separator+file);
	//
	//		}catch(Exception e) {
	//			System.out.println("Ocurrio un problema leyendo los datos"+e.getStackTrace());
	//			e.printStackTrace();
	//		}
	//
	//		return retornar; 
	//	}
}















