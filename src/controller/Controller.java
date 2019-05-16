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
	/**
	 * View para interaccion con usuario
	 */
	private MovingViolationsManagerView view;

	/**
	 * Pila donde se van a cargar los datos de los archivos
	 */ 
	private IStack<VOMovingViolations> movingViolationsStack;
	private Grafo<Long, verticeInfo, Double> grafo;  
	private LinearProbingHashST<Integer, VOMovingViolations> tablainfracciones;
	private Mapa mapa; 
	public Controller()
	{
		view = new MovingViolationsManagerView();
		grafo=new Grafo<>(); 
		movingViolationsStack = new Stack<>(); 
		tablainfracciones= new LinearProbingHashST<Integer, VOMovingViolations>(); 
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
			case 12:	
				fin=true;
				sc.close();
				break;
			}
		}
	}


	/**
	 * Metodo para carga de archivos segun semestre de seleccion
	 * @param num Semestre a cargar datos (1 para primer semestre, cualquier otro numero para segundo semestre)
	 */
	@SuppressWarnings("deprecation")
	public int[] loadMovingViolations(int num)
	{
		//estructuras de almacenamiento de infracciones
		movingViolationsStack= new Stack<VOMovingViolations>();
		//creacion e inicializacion de arreglo con nombre de los archivos de infracciones por mes 
		String[] nombresArchivos=new String[12];
		nombresArchivos[0]="."+File.separator+"data"+File.separator+"January_wgs84.csv";
		nombresArchivos[1]="."+File.separator+"data"+File.separator+"February_wgs84.csv";
		nombresArchivos[2]="."+File.separator+"data"+File.separator+"March_wgs84.csv";
		nombresArchivos[3]="."+File.separator+"data"+File.separator+"April_wgs84.csv";
		nombresArchivos[4]="."+File.separator+"data"+File.separator+"May_wgs84.csv";
		nombresArchivos[5]="."+File.separator+"data"+File.separator+"June_wgs84.csv";
		nombresArchivos[6]="."+File.separator+"data"+File.separator+"July_wgs84.csv";
		nombresArchivos[7]="."+File.separator+"data"+File.separator+"August_wgs84.csv";
		nombresArchivos[8]="."+File.separator+"data"+File.separator+"September_wgs84.csv";
		nombresArchivos[9]="."+File.separator+"data"+File.separator+"October_wgs84.csv";
		nombresArchivos[10]="."+File.separator+"data"+File.separator+"November_wgs84.csv";
		nombresArchivos[11]="."+File.separator+"data"+File.separator+"December_wgs84.csv";
		int previo=0;
		int[] pormes=new int[6];
		int pos=0; 
		CSVReader reader=null;
		int inicio=-1; 
		if(num==1)
		{
			inicio=0; //lectura de archivos a partir del primer mes.
		}
		else
		{
			inicio=6; //lectura de archivos a partir del septimo mes.
		}
		for(int i=inicio; i<inicio+6;i++)//ciclo para lectura de semestre seleccionado
		{		
			try
			{
				//Lector de archivos para la posicion i-esima 
				reader=new CSVReader(new FileReader(nombresArchivos[i]));
				String[] linea=reader.readNext();
				linea=reader.readNext();
				while(linea!=null)
				{
					int tres=linea[3].equals("")?0:Integer.parseInt(linea[3]);
					//separacion de coordenadas X y Y
					double cinco=linea[5].equals("")?0:Double.parseDouble(linea[5]);
					double seis=linea[6].equals("")?0:Double.parseDouble(linea[6]);
					
					//StreetSegID
					int cuatro=linea[4].equals("")?0:Integer.parseInt(linea[4]);

					double diez=linea[10].equals("")?0: Double.parseDouble(linea[10]);
					double once=linea[11].equals("")?0:Double.parseDouble(linea[11]);
					
					
					//creacion de infraccion en estructura de datos para campos definidos
					movingViolationsStack.push(new VOMovingViolations(Integer.parseInt(linea[0]), linea[2], linea[13], Double.parseDouble(linea[9]), linea[12], linea[15], linea[14], Double.parseDouble(linea[8]),tres,diez,once,cinco,seis,cuatro));
					linea=reader.readNext();
				}
				pormes[pos]=movingViolationsStack.size()-previo;
				previo=movingViolationsStack.size();
				pos++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if(reader!=null)
				{
					try
					{
						reader.close();
					}
					catch(IOException e)
					{
						e.printStackTrace();	
					}
				}
			}
		}

		return pormes; 
	}
	
	/**
	 * 1A Metodo para obtener las N franjas horarias con el mayor numero de infracciones
	 * @param nFranjas numero de franjas a retornar (nFranjas<24)
	 */
	public String[] nFranjasHorarias(int nFranjas)
	{
		//IDEA: registrar toda la informacion promedio de las infracciones en un VOranking por hora y anadirla a una cola de prioridad
		//el VOranking es inicializado en su id con el numero de infracciones para utilizar compareTo
		//location es inicializado con la franja horaria, streetsegid como 0

		IStack<VOMovingViolations> copiaViolationsStack =  movingViolationsStack; //copia de stack de infracciones
		MaxColaPrioridad<VOranking> estadisticasNInfracciones = null; //cola de prioridad con VOranking
		VOMovingViolations violacionActual=null; //violacion de recorrido
		VOranking rankingActual=null; //estadistica de recorrido
		
		int indice = 0; //indice de franja horaria
		boolean acc = false; //indicador de accidente
		
		//inicializacion de VOranking como bloques de informacion promedio por rango de hora
		VOranking[] franjas = new VOranking[24];
		for (int i = 0; i<10;i++)
		{
			franjas[i]= new VOranking("", 0, 100, 100, 0, "0"+i+":00:00 - 0"+i+":59:59",0);
		}
		for (int j = 10; j<24;j++)
		{
			franjas[j]= new VOranking("", 0, 100, 100, 0, j+":00:00 - "+j+":59:59",0);
		}
		while(!copiaViolationsStack.isEmpty()) //se vacia la pila copia para actualizar la informacion por violacion
		{
			violacionActual = copiaViolationsStack.pop();
			indice = Integer.parseInt(violacionActual.getTicketIssueDate().split("T")[1].split(":")[0]);
			indice = indice==24? 0:indice;//condicion de caso especial de franja horario
			acc = violacionActual.getAccidentIndicator().equals("Yes");
			franjas[indice].actualizarInfo(acc, (int)(violacionActual.getFINEAMT()+violacionActual.getPenalty1()+violacionActual.getPenalty2()-violacionActual.getTotalPaid()));
		}
		for(int k = 0; k<24; k++)
		{
			estadisticasNInfracciones.agregar(franjas[k]);
		}
		
		String[] mensaje =new String[nFranjas]; //arreglo de Strings a retornar
		for(int conteoFinal = 0; conteoFinal<nFranjas; conteoFinal++)
		{
			rankingActual = estadisticasNInfracciones.delMax();
			//mensaje requerido para infracciones
			mensaje[conteoFinal]="Franja horaria:"+ rankingActual.darLocation() +" Numero de infracciones:"+ rankingActual.darnumInfracciones()+"Porcentaje sin accidentes:"+rankingActual.darPorcentajeSinAccidentes()+"% Porcentaje con accidentes:"+rankingActual.porPorcentajeAccidentes()+"% Deuda Total:"+rankingActual.darTotalDeuda(); 
		}
		return mensaje;
	}
	
	/**
	 * 2A Metodo para ordenar infracciones geograficamente, Xcoord es la desigualdad principal y Ycoord la secundaria
	 */
	public HashTableChaining<Tupla,VOMovingViolations> ordenarGeograficamente()
	{
		//idea: Utilizar separate Chaining para entregar todas las infracciones en esa ubicacion geografica
		// el valor de la dupla con VOMovingViolations y la llave son la tupla "XCoord,YCoord"
		
		IStack<VOMovingViolations> copiaViolationsStack =  movingViolationsStack; //copia de stack de infracciones
		HashTableChaining<Tupla,VOMovingViolations> tablaGeografica = new HashTableChaining(); //tabla de ordenamiento hash separate Chaining
		VOMovingViolations violacionActual=null; //violacion de recorrido
		
		while(!copiaViolationsStack.isEmpty()) //se vacia la pila copia para actualizar la informacion por violacion
		{
			violacionActual=copiaViolationsStack.pop();
			tablaGeografica.put(new Tupla(violacionActual.getX(),violacionActual.getY()), violacionActual);
		}
		
		return tablaGeografica;
	}
	
	/**
	 * 2A Metodo para obtener informacion principal de infracciones en un par de coordenadas
	 */
	public String infoInfraccionesGeograficas(Dupla<Tupla,VOMovingViolations> pDupla)
	{
		int tamano = pDupla.chain.darTamano(); //tamano de arreglo dinamico de duplas que corresponden a las mismas coordenadas x,y
		System.out.println("Las " + tamano +"infracciones tienen la siguiente informacion:");
		VOMovingViolations violacionActual =null;//violacion de recorrido
		String mensaje= ""; //mensaje a retornar
		VOranking rankingActual=null; //estadistica de recorrido
		boolean acc = false;//indicador de accidente
		int pDeuda = 0; 
		
		for(int i = 0; i<tamano; i++) //actualizacion de toda la informacion
		{
			violacionActual = (VOMovingViolations) pDupla.chain.darElemento(i).getValue();
			acc = violacionActual.getAccidentIndicator().equals("Yes");
			pDeuda = (int)(violacionActual.getFINEAMT()+violacionActual.getPenalty1()+violacionActual.getPenalty2()-violacionActual.getTotalPaid());
			rankingActual.actualizarInfo(acc,pDeuda );
		}
		mensaje= " Numero de infracciones:"+ rankingActual.darnumInfracciones()+"Porcentaje sin accidentes:"+rankingActual.darPorcentajeSinAccidentes()+"% Porcentaje con accidentes:"+rankingActual.porPorcentajeAccidentes()+"% Deuda Total:"+rankingActual.darTotalDeuda(); 
		return mensaje;
	}
	
	
	/**
	 * 3A Metodo para obtener las infracciones dentro de un rango determinado en un arbol balanceado
	 * @param pRango formato AAAA-MM-DD/AAAA-MM-DD de fechas iniciales y finales
	 */
	public String[] infraccionesFecha(String pRango)
	{
		//idea: Inscribir las fechas en el arbol que esten en el rango ingresado con llave fecha y valor VOranking, para no modificar el codigo
		//del arbol rojo negro se verifica que la fecha a inscribir no existe todavia
		try
		{
			//Separacion de fechas parametro
			String fechaInicial = (pRango.split("/"))[0];
			String fechaFinal = (pRango.split("/"))[1];
			String fechaActual = ""; //fecha de infraccion

			IStack<VOMovingViolations> copiaViolationsStack =  movingViolationsStack; //copia de stack de infracciones
			IStack<String> copiaFechas = new Stack<String>(); //copia de fechas encontradas para retorno de mensajes
			RedBlackBST<String,VOranking> arbolBalanceado = new RedBlackBST(); //arbol balanceado de estadisticas por fecha
			VOMovingViolations violacionActual=null; //violacion de recorrido
			VOranking estadisticaActual=null; //estadistica de recorrido
			int n = 0;// numero de fechas distintivas
			
			boolean acc = false;//indicador de accidente
			int pDeuda = 0; 

			while(!copiaViolationsStack.isEmpty()) //se vacia la pila copia para actualizar la informacion por fechas
			{
				violacionActual=copiaViolationsStack.pop();
				fechaActual=(violacionActual.getTicketIssueDate().split("T"))[0];
				if(fechaActual.compareTo(fechaInicial)>0&&fechaActual.compareTo(fechaFinal)<0)
				{
					acc = violacionActual.getAccidentIndicator().equals("Yes");
					pDeuda = (int)(violacionActual.getFINEAMT()+violacionActual.getPenalty1()+violacionActual.getPenalty2()-violacionActual.getTotalPaid());
					if(arbolBalanceado.contains(fechaActual))
					{
						arbolBalanceado.get(fechaActual).actualizarInfo(acc, pDeuda);
					}
					else
					{
						//id como fecha, 1 infraccion, porcentaje inicial, porcentaje con inicial, deuda infraccion, no hay location comun, no hay streetsegid comun
						estadisticaActual=new VOranking(fechaActual,1,100,100,pDeuda,"",0); 
						arbolBalanceado.put(fechaActual,estadisticaActual );
						copiaFechas.push(fechaActual); //se anade la nueva fecha al arreglo
						n++;
					}
				}
			}
			//construccion del mensaje por fecha
			String[] mensaje = new String[n];
			for(int conteoFinal = 0; conteoFinal<n && !copiaFechas.isEmpty(); conteoFinal++)
			{
				estadisticaActual = arbolBalanceado.get(copiaFechas.pop());
				//mensaje requerido para infracciones
				mensaje[conteoFinal]= " Numero de infracciones:"+ estadisticaActual.darnumInfracciones()+"Porcentaje sin accidentes:"+estadisticaActual.darPorcentajeSinAccidentes()+"% Porcentaje con accidentes:"+estadisticaActual.porPorcentajeAccidentes()+"% Deuda Total:"+estadisticaActual.darTotalDeuda();
			}
			return mensaje;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public double[] calcularMiniMax(){
		double[] coordenadas= new double[4];
		double xmin,xmax,ymin,ymax; 
		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
		VOMovingViolations actual= iter.next();
		xmin=actual.getX(); 
		xmax=actual.getX();
		ymin=actual.getY(); 
		ymax=actual.getY();
		while(iter.hasNext()){
			actual=iter.next(); 
			if(actual.getX()<xmin){
				xmin=actual.getX(); 
			}
			if(actual.getX()>xmax){
				xmax=actual.getX(); 
			}
			if(actual.getY()<ymin){
				ymin=actual.getY(); 
			}
			if(actual.getY()>ymax){
				ymax=actual.getY(); 
			}
		}
		coordenadas[0]=xmin; 
		coordenadas[1]=xmax; 
		coordenadas[2]=ymin; 
		coordenadas[3]=ymax;
		return coordenadas; 
	}


	public MaxColaPrioridad<VOranking> darRankingInfracciones (int N){

		MaxColaPrioridad<VOranking> retornar= new MaxColaPrioridad<>(); 
		ArrayList<String> agregadas= new ArrayList<>();  
		while(agregadas.size()<N) {
			Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
			VOMovingViolations actual=iter.next(); 
			int masveces=0; 
			double accidente=0;
			double deuda=0;
			VOMovingViolations repetido=actual; 
			while (iter.hasNext()) { 
				if(!revisar(agregadas, actual.getViolationCode())) {
					int veces=0;
					Iterador<VOMovingViolations> iter2= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
					VOMovingViolations actual2=iter2.next();
					while(iter2.hasNext()) {
						if(actual2.getViolationCode().equals(actual.getViolationCode())) {
							veces++; 
							deuda+=actual2.getTotalPaid(); 
							if(actual2.getAccidentIndicator().equals("Yes")) {
								accidente++; 
							}
						}
						actual2=iter2.next(); 
					}
					if(veces>masveces) {
						masveces=veces; 
						repetido=actual; 
					}else {
						accidente=0;
						deuda=0; 
					}
					actual=iter.next(); 
				}else {
					actual=iter.next(); 
				}
			}
			agregadas.add(repetido.getViolationCode()); 
			double pPorcenacc=(accidente*100)/masveces; 
			double pPorcensinacc=100-pPorcenacc; 
			retornar.agregar(new VOranking(repetido.getViolationCode(),masveces, pPorcenacc, pPorcensinacc, deuda,null, 0)); 
		}
		return retornar; 
	}

	public boolean revisar(ArrayList<String> arreglo, String codigo) {
		boolean respuesta=false;
		for(int i=0; i<arreglo.size() && !respuesta; i++) {
			if(arreglo.get(i).equals(codigo)) {
				respuesta=true; 
			}
		}

		return respuesta;
	}
	
	public boolean revisarTupla(ArrayList<Tupla> arreglo, Tupla num) {
		boolean respuesta=false; 
		for (int i=0; i<arreglo.size() &&!respuesta; i++) {
			if(arreglo.get(i).compareTo(num)==0) {
				respuesta=true; 
			}
		}
		return respuesta; 
	}
	
	public VOranking ordenarPorlocalizacion(double x, double y) throws Exception 
	{
		Tupla buscada= new Tupla(x,y); 
		// Crear el arbol ordenado por las llaves(Tuplas)
		RedBlackBST<Tupla,IStack<VOMovingViolations>> arbol= new RedBlackBST<>();
		ArrayList<Tupla> agregadas= new ArrayList<>(); 
		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
		VOMovingViolations actual=iter.next(); 
		Tupla llave2=new Tupla(0,0); 
		while(iter.hasNext()) {
			Tupla llave=new Tupla(actual.getX(),actual.getY()); 
			if(!revisarTupla(agregadas, llave)) {
				IStack<VOMovingViolations> porAgregar= new Stack<>();
				Iterador<VOMovingViolations> iter2= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
				VOMovingViolations actual2=iter2.next(); 
				while (iter2.hasNext()) {
					llave2.setCoord(actual2.getX(),actual2.getY());
					if(llave.compareTo(llave2)==0) {
						porAgregar.push(actual2);
					}
				}
				arbol.put(llave, porAgregar);				
			}
			actual=iter.next(); 
		}
		IStack<VOMovingViolations> buscar=arbol.get(buscada);
		Iterador<VOMovingViolations> iter3=(Iterador<VOMovingViolations>) buscar.iterator();
		VOMovingViolations actual3=iter3.next(); 
		int acc=0; 
		double deuda=0; 
		while(iter3.hasNext()) {
			if(actual3.getAccidentIndicator().equals("Yes")) {
				acc++; 
				deuda+=actual3.getTotalPaid(); 
			}
		}
		double conacc=(acc*100)/buscar.size(); 
		double sinacc=100-conacc; 
				
		return new VOranking(null, buscar.size(),conacc, sinacc,deuda,buscar.darPrimero().darElemento().getLocation(),buscar.darPrimero().darElemento().getStreetId());
	}

	
	
	public void franjaFechaHora (double valorinicial, double valorfinal) {
		//PENDIENTE
	}

	public VOranking getInformacionloc( int pId){
		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
		VOMovingViolations actual=iter.next();
		int total=0; 
		int accidentes=0; 
		double deuda=0;
		int streetId=0; 
		while(iter.hasNext()) {
			if(actual.getAdressId()==pId) {
				total++;
				streetId=actual.getStreetId(); 
				if(actual.getAccidentIndicator().equals("Yes")) {
					accidentes++; 
				}
				deuda+=actual.getTotalPaid(); 
			}
			actual=iter.next(); 
		}
		double pPorcenacc=(accidentes*100)/total; 
		double pPorcensinacc=100-pPorcenacc; 
		VOranking retornar= new VOranking(null, total, pPorcenacc, pPorcensinacc, deuda, null,streetId ); 
		return retornar; 
	}
	
		public RedBlackBST<Double, String>  arbolRango (double inicial, double fin) throws Exception{
		RedBlackBST<Double, String> retornar= new RedBlackBST<>(); 
		String[] horas= new String[24]; 
		horas[0]="00";
		horas[1]="01";
		horas[2]="02"; 
		horas[3]="03"; 
		horas[4]="04"; 
		horas[5]="05"; 
		horas[6]="06"; 
		horas[7]="07"; 
		horas[8]="08"; 
		horas[9]="09" ; 
		horas[10]="10"; 
		horas[11]="11";
		horas[12]="12";
		horas[13]="13";
		horas[14]="14";
		horas[15]="15";
		horas[16]="16";
		horas[17]="17";
		horas[18]="18";
		horas[19]="19";
		horas[20]="20";
		horas[21]="21";
		horas[22]="22";
		horas[23]="23";

		for (int i=0; i<horas.length; i++) {
			Iterador<VOMovingViolations> iter=(Iterador<VOMovingViolations>) movingViolationsStack.iterator(); 
			VOMovingViolations actual=iter.next();
			double acumulado=0; 
			while(iter.hasNext()) {
			if(actual.getTicketIssueDate().split("T")[1].split(":")[0].equals(horas[i])) {
				acumulado+=actual.getTotalPaid(); 
			}
			actual=iter.next(); 
			}
			if(acumulado<fin&&acumulado>inicial) {
				retornar.put(acumulado,horas[i]);
			}
		}		
		return retornar;
	}
	
		public IStack<Dupla<String,Double>> tablaASCII(){
		IStack<Dupla<String,Double>> retornar= new Stack<>(); 
		Iterador<VOMovingViolations> iter=(Iterador<VOMovingViolations>) movingViolationsStack.iterator(); 
		VOMovingViolations actual=iter.next(); 
		ArrayList<String> revisadas= new ArrayList<>(); 
		while(iter.hasNext()) {
			double num=0; 
			if(!revisar(revisadas, actual.getViolationCode())) {
				Iterador<VOMovingViolations> iter2=(Iterador<VOMovingViolations>) movingViolationsStack.iterator(); 
				VOMovingViolations actual2=iter.next();
				if(actual2.getViolationCode().equals(actual.getViolationCode())) {
					num++; 
				}
			}
			Dupla<String, Double> agregar= new Dupla<>(actual.getViolationCode(),num); 
			actual=iter.next(); 
		}
		
		return retornar; 
	}
	/**
	 * 2C Retorna un arreglo String con la informacion de las infracciones que estan dentro del rango y las que estan fuera del rango de horas recibido
	 */
	public String[] infoInfraccionesHora(String pHoras)
	{
		//Idea: obtener las estadisticas de las infracciones dentro del rango y ordenar las estadisticas de las infracciones restantes en una Hash Table para su clasificacion
		try
		{
			//Separacion de fechas parametro
			String horaInicial = (pHoras.split("/"))[0];
			String horaFinal = (pHoras.split("/"))[1];
			String horaActual = ""; //fecha de infraccion

			IStack<VOMovingViolations> copiaViolationsStack =  movingViolationsStack; //copia de stack de infracciones
			VOMovingViolations violacionActual=null; //violacion de recorrido
			String codigoActual =""; //codigo de la infraccion
			HashTableChaining<String,VOranking> tablaViolationCode = new HashTableChaining(); //tabla de ordenamiento hash separate Chaining 
			IStack<String> copiaViolationCode = new Stack<String>(); //copia de violaciones encontradas para retorno de mensajes
			
			//id inicializada, ninfracciones inicializadas, porcentaje inicial, porcentaje con inicial, deuda infraccion, no hay location comun, no hay streetsegid comun
			VOranking estadisticaRango=new VOranking("", 0, 100, 100, 0, "",0); //estadistica de recorrido
			//VOranking estadisticaFuera=new VOranking("", 0, 100, 100, 0, "",0); //estadistica de recorrido para Duplas en hash table
			int cantidad=0;
			
			boolean acc = false;//indicador de accidente
			int pDeuda = 0; 

			while(!copiaViolationsStack.isEmpty()) //se vacia la pila copia para actualizar la informacion por fechas
			{
				violacionActual=copiaViolationsStack.pop();
				horaActual=(violacionActual.getTicketIssueDate().split("T"))[1].split(".")[0]; //HH:MM:SS
				if(horaActual.compareTo(horaInicial)>0&&horaActual.compareTo(horaFinal)<0)
				{
					acc = violacionActual.getAccidentIndicator().equals("Yes");
					pDeuda = (int)(violacionActual.getFINEAMT()+violacionActual.getPenalty1()+violacionActual.getPenalty2()-violacionActual.getTotalPaid());
					estadisticaRango.actualizarInfo(acc, pDeuda); //se actualiza la informacion de las estadisticas dentro del rango
				}
				else
				{
					if(tablaViolationCode.get(violacionActual.getViolationCode())!=null)
					{
						tablaViolationCode.get(violacionActual.getViolationCode()).getValue().actualizarInfo(acc, pDeuda);
					}
					else
					{
						tablaViolationCode.put(violacionActual.getViolationCode(), new VOranking(violacionActual.getViolationCode(),1,100,100,0,"",0));
						copiaViolationCode.push(violacionActual.getViolationCode());
						cantidad++;
					}
								
					
				}
			}
			//construccion del mensaje por fecha
			String[] mensaje = new String[cantidad+1];
			mensaje[0]= " Numero de infracciones dentro de rango:"+ estadisticaRango.darnumInfracciones()+", Porcentaje sin accidentes:"+estadisticaRango.darPorcentajeSinAccidentes()+"%, Porcentaje con accidentes:"+estadisticaRango.porPorcentajeAccidentes()+"%, Deuda Total:"+estadisticaRango.darTotalDeuda();
			int conteoXD=1;
			//informacion de infracciones por fuera de ese rango
			while(!copiaViolationCode.isEmpty()&&conteoXD<cantidad+1)
			{
				mensaje[conteoXD]= "Violation Code por fuera de rango:"+tablaViolationCode.get(copiaViolationCode.pop()).getKey()+", numero de infracciones:"+tablaViolationCode.get(copiaViolationCode.pop()).getValue().darnumInfracciones();
				conteoXD++;
			}
			return mensaje;

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 3C Retorna un arreglo String de tamano NLocalizaciones con los pares de coordenadas con mayor numero de infracciones
	 */
	public String[] infoNLocalizaciones(int NLocalizaciones)
	{
		//idea: recorrer el numero de elementos en el HashTableChaining utilizada en ordenarGeograficamente() con VOranking para actualizar una segunda MaxCola
		HashTableChaining<Tupla,VOMovingViolations> tablaOrdenadaGeo= ordenarGeograficamente();
		HashTableChaining<Tupla,VOranking> tablaEstadisticas= new HashTableChaining();
		Iterador<Tupla> recorrido = tablaOrdenadaGeo.keys(); //obtengo las llaves para realizar
		
		VOMovingViolations violacionActual=null; //violacion Actual
		VOranking rankingActual=null; //estadistica actual
		Tupla tuplaActual=null; //tupla de recorrido
		
		int contador = 0;
		int infraccion = 0;
		int pDeuda=0;
		boolean acc=false;
		while(recorrido.hasNext() && contador<NLocalizaciones) //paso de informacion a tabla de hash con VOranking
		{
			tuplaActual = recorrido.next();
			violacionActual= tablaOrdenadaGeo.get(tuplaActual).getValue();
			pDeuda = (int)(violacionActual.getFINEAMT()+violacionActual.getPenalty1()+violacionActual.getPenalty2()-violacionActual.getTotalPaid());
			acc = violacionActual.getAccidentIndicator().equals("Yes");
			infraccion= acc? 100:0;
			if(tablaEstadisticas.get(tuplaActual).getValue()==null)
			{
				rankingActual=new VOranking(""+violacionActual.getX()+","+violacionActual.getY(),1,100-infraccion,infraccion,pDeuda,violacionActual.getLocation(),violacionActual.getStreetId());
				tablaEstadisticas.put(tuplaActual, rankingActual);
			}
			else
			{
				tablaEstadisticas.get(tuplaActual).getValue().actualizarInfo(acc, pDeuda);
			}
			contador++; //registro de iteracion para break de while
		}
		
		String[] mensaje = new String[NLocalizaciones];
		for(int i=0;i<NLocalizaciones;i++)
		{
			mensaje[i]= "En el par de coordenadas:"+rankingActual.darCode()+", Numero de infracciones:"+ rankingActual.darnumInfracciones()+",Porcentaje sin accidentes:"+rankingActual.darPorcentajeSinAccidentes()+"%, Porcentaje con accidentes:"+rankingActual.porPorcentajeAccidentes()+"%, Deuda Total:"+rankingActual.darTotalDeuda()+", En location: "+rankingActual.darLocation()+" y StreetSegID:"+rankingActual.darSreetiId(); 
		}
		return null;
	}

	public int[] cargarDatos(int num) {
		int[] retornar=new int[2]; 
		String file=num==1?"Central-WashingtonDC-OpenStreetMap.xml":"exampleMap.xml";
		try {
			SAXParserFactory spf= SAXParserFactory.newInstance(); 
			spf.setNamespaceAware(true); 
			SAXParser saxParser= spf.newSAXParser(); 
			XMLReader xmlReader=saxParser.getXMLReader(); 
			xmlReader.setContentHandler(grafo);
			xmlReader.parse("."+File.separator+"data"+File.separator+file);

		}catch(Exception e) {
			System.out.println("Ocurrio un problema leyendo los datos"+e.getStackTrace());
			e.printStackTrace();
		}

		return retornar; 
	}
	
	public void cargarDatosJson(){
		Arco<Long,Double> agregar; 
		String file="."+File.separator+"data"+File.separator+"finalGraph.json";  
		int arcosagregados=0; 
		int verticesagregados=0; 
		try{
			JsonParser parser= new JsonParser(); 
			JsonArray arr= (JsonArray)parser.parse(new FileReader(file));
			for (int i=0; i<arr.size()&&arr!=null; i++){
				JsonObject obj=(JsonObject)arr.get(i); 
				Long id=Long.parseLong(obj.get("id").getAsString());
				Double lat=Double.parseDouble(obj.get("lat").getAsString()); 
				Double log=Double.parseDouble(obj.get("lon").getAsString()); 
				grafo.addVertex(id, new Vertice<verticeInfo, Long, Double>(id, new verticeInfo(lat,log)));	

				JsonArray arcos=obj.get("adj").getAsJsonArray(); 
				arcosagregados+=arcos.size(); 
				for (int j=0; j<arcos.size(); j++){
					Long llegada= arcos.get(j).getAsLong(); 
					grafo.getVertice(id).agregarLongAdyacente(llegada);
				}
				JsonArray infracciones=obj.get("infractions").getAsJsonArray(); 
				for(int k=0; k<infracciones.size(); k++){
					grafo.getVertice(id).setInfraccion(infracciones.get(k).getAsInt());
				}
			}
			Iterator<Vertice<verticeInfo, Long, Double>> iter = grafo.darTablaVertices().keys().iterator(); 
			Vertice<verticeInfo, Long, Double> actual=iter.next(); 
			double dis=0; 
			while(iter.hasNext()){
				Iterator<Long> iter2=actual.darLongAdyacente().iterator();
				Long pId=iter2.next(); 
				while (iter2.hasNext()){
					Vertice<verticeInfo, Long, Double> actual2=grafo.darTablaVertices().get(pId);
					dis=calcularHarvesine(actual,actual2); 
					grafo.addEdge(actual.darLlave(), pId, new Arco<Long, Double>(dis, actual.darLlave(),pId));
					pId=iter2.next(); 
				}
				actual=iter.next(); 
			}
			mapa=new Mapa(grafo); 
			
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
	
	public Vertice<verticeInfo, Long, Double> darAleatorio(){
		Vertice<verticeInfo, Long, Double> retornar=null; 
		int a; 
		while(retornar!=null){
			int contador=0; 
			a=(int)(Math.random()*grafo.darTablaVertices().size()); 
			Iterator<Vertice<verticeInfo, Long, Double>> iter = grafo.darTablaVertices().keys().iterator(); 
			retornar=iter.next(); 
			while(iter.hasNext()&&contador!=a){
				retornar=iter.next(); 
				contador++; 
			}
		}
		return retornar; 
	}
	public void requerimiento4(){ 
	Vertice<verticeInfo, Long, Double> inicio= darAleatorio(); 
	Vertice<verticeInfo, Long, Double> fin=darAleatorio(); 
	
	}
	public Stack<Arco<Long, Double>> BFS(Vertice<verticeInfo, Long, Double> inicio, Vertice<verticeInfo,Long, Double> fin){
		Stack<Arco<Long, Double>> retornar= new Stack<>(); 
		LinearProbingHashST<Long,Vertice<verticeInfo,Long, Double>> marcados= new LinearProbingHashST<>();
		Queue<Vertice<verticeInfo, Long, Double>> cola= new Queue<>(); 
		marcados.put(inicio.darLlave(), inicio);
		cola.enqueue(inicio);
		while(!cola.isEmpty()){
			Vertice<verticeInfo, Long, Double> actual=cola.dequeue(); 
			Stack<Long> adyacentes=actual.darAyacentes(); 
			Iterator<Long> iter = adyacentes.iterator();
			Long longactual=iter.next();
			while(iter.hasNext()){
				if(marcados.get(longactual)==null){
					marcados.put(longactual, grafo.getVertice(longactual));
					cola.enqueue(grafo.getVertice(longactual));
				}
			}
			
			
		}
		return retornar; 
	}
	
}















