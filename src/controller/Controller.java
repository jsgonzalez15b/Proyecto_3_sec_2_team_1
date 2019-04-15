package controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.opencsv.CSVReader;
import com.sun.corba.se.impl.orbutil.graph.Node;
import com.sun.org.apache.xerces.internal.util.IntStack;

import model.data_structures.Dupla;
import model.data_structures.HashTableChaining;
import model.data_structures.IMaxColaPrioridad;
import model.data_structures.IQueue;
import model.data_structures.IStack;
import model.data_structures.Iterador;
import model.data_structures.MaxColaPrioridad;
import model.data_structures.Nodo;
import model.data_structures.Queue;
import model.data_structures.RedBlackBST;
import model.data_structures.Stack;
import model.data_structures.Tupla;
import model.vo.VODaylyStatistic;
import model.vo.VOMovingViolations;
import model.vo.VOranking;
import sun.awt.image.VolatileSurfaceManager;
import sun.nio.cs.ext.ISCII91;
import view.MovingViolationsManagerView;

public class Controller
{
	/**
	 * View para interaccion con usuario
	 */
	private MovingViolationsManagerView view;

	/**
	 * Pila donde se van a cargar los datos de los archivos
	 */
	private IStack<VOMovingViolations> movingViolationsStack;

	/**
	 * Cola de prioridad
	 */
	private IMaxColaPrioridad<VOMovingViolations> colaprioridad; 


	public Controller()
	{
		view = new MovingViolationsManagerView();

		//TODO, inicializar la pila y las colas
		movingViolationsStack = null;
		colaprioridad=null; 
	}

	public void run()
	{
		Scanner sc = new Scanner(System.in);
		boolean fin = false;

		while(!fin)
		{
			view.printMenu();

			int option = sc.nextInt();

			switch(option)
			{
			case 1:
				view.printMensage("Ingrese el numero del semestre que desea cargar");
				int num=sc.nextInt();
				int[] pormes=this.loadMovingViolations(num);
				view.printCargar(pormes, num,calcularMiniMax());				

				break;

			case 2:
				view.printMensage("Ingrese el numero de franjas a obtener");
				int num2=sc.nextInt();
				String[] lasFranjas=this.nFranjasHorarias(num2);
				view.printNFranjas(lasFranjas);
				
				break;
			case 3:
				this.ordenarGeograficamente();
				view.printMensage("Se ordenaron las infracciones geograficamente");
				view.printMensage("Ingrese el par de coordenadas x,y: XXXXXX.X,YYYYYY.Y");
				String num3=sc.next();
				HashTableChaining<Tupla,VOMovingViolations> tablaGeografica= ordenarGeograficamente();
				view.printMensage(infoInfraccionesGeograficas(tablaGeografica.get(new Tupla(Integer.parseInt(num3.split(",")[0]),Integer.parseInt(num3.split(",")[1])))));
				
				break;
			case 4:
				view.printMensage("Ingrese el rango de fechas con el siguiente formato (inicial-final): AAAA/MM/DD-AAAA/MM/DD");
				String fechaRango = sc.next();
				String[] infraccionesRango=this.infraccionesFecha(fechaRango);
				view.printInfraccionesRango(infraccionesRango);
				
				break;
			case 5: 
				
			case 6: 
				view.printMensage("Ingrese el promedio en formato: val1,val2");
				String entrada=sc.next();
				int in=Integer.parseInt(entrada.split(",")[0]);
				int fin2=Integer.parseInt(entrada.split(",")[1]);

				break; 
			case 7:
				
			case 8: 
				
			case 9: 
				
			case 10:
				
			case 11:
				
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
	public int[] loadMovingViolations(int num)
	{
		//estructuras de almacenamiento de infracciones
		movingViolationsStack= new Stack<VOMovingViolations>();
		colaprioridad=new MaxColaPrioridad<VOMovingViolations>(); 
		//creacion e inicializacion de arreglo con nombre de los archivos de infracciones por mes 
		String[] nombresArchivos=new String[12];
		nombresArchivos[0]="."+File.separator+"data"+File.separator+"Moving_Violations_Issued_in_January_2018_ordered.csv";
		nombresArchivos[1]="."+File.separator+"data"+File.separator+"Moving_Violations_Issued_in_February_2018_ordered.csv";
		nombresArchivos[2]="."+File.separator+"data"+File.separator+"Moving_Violations_Issued_in_March_2018.csv";
		nombresArchivos[3]="."+File.separator+"data"+File.separator+"Moving_Violations_Issued_in_April_2018.csv";
		nombresArchivos[4]="."+File.separator+"data"+File.separator+"Moving_Violations_Issued_in_May_2018.csv";
		nombresArchivos[5]="."+File.separator+"data"+File.separator+"Moving_Violations_Issued_in_June_2018.csv";
		nombresArchivos[6]="."+File.separator+"data"+File.separator+"Moving_Violations_Issued_in_July_2018.csv";
		nombresArchivos[7]="."+File.separator+"data"+File.separator+"Moving_Violations_Issued_in_August_2018.csv";
		nombresArchivos[8]="."+File.separator+"data"+File.separator+"Moving_Violations_Issued_in_September_2018.csv";
		nombresArchivos[9]="."+File.separator+"data"+File.separator+"Moving_Violations_Issued_in_October_2018.csv";
		nombresArchivos[10]="."+File.separator+"data"+File.separator+"Moving_Violations_Issued_in_November_2018.csv";
		nombresArchivos[11]="."+File.separator+"data"+File.separator+"Moving_Violations_Issued_in_December_2018.csv";
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
					colaprioridad.agregar(new VOMovingViolations(Integer.parseInt(linea[0]), linea[2], linea[13], Double.parseDouble(linea[9]), linea[12], linea[15], linea[14], Double.parseDouble(linea[8]),tres,diez, once,cinco,seis,cuatro));
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
	 * Metodo para obtener las N franjas horarias con el mayor numero de infracciones
	 * @param nFranjas numero de franjas a retornar (nFranjas<24)
	 */
	public String[] nFranjasHorarias(int nFranjas)
	{
		//IDEA: registrar toda la información promedio de las infracciones en un VOranking por hora y añadirla a una cola de prioridad
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
			franjas[i]= new VOranking("", 0, 1, 1, 0, "0"+i+":00:00 - 0"+i+":59:59",0);
		}
		for (int j = 10; j<24;j++)
		{
			franjas[j]= new VOranking("", 0, 1, 1, 0, j+":00:00 - "+j+":59:59",0);
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
			mensaje[conteoFinal]="Franja horaria:"+ rankingActual.darLocation() +" Número de infracciones:"+ rankingActual.darnumInfracciones()+"Porcentaje sin accidentes:"+rankingActual.darPorcentajeSinAccidentes()+"% Porcentaje con accidentes:"+rankingActual.porPorcentajeAccidentes()+"% Deuda Total:"+rankingActual.darTotalDeuda(); 
		}
		return mensaje;
	}
	
	/**
	 * Metodo para ordenar infracciones geograficamente, Xcoord es la desigualdad principal y Ycoord la secundaria
	 */
	public HashTableChaining<Tupla,VOMovingViolations> ordenarGeograficamente()
	{
		//idea: Utilizar separate Chaining para entregar todas las infracciones en esa ubicación geográfica
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
	 * Metodo para obtener informacion principal de infracciones en un par de coordenadas
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
		mensaje= " Número de infracciones:"+ rankingActual.darnumInfracciones()+"Porcentaje sin accidentes:"+rankingActual.darPorcentajeSinAccidentes()+"% Porcentaje con accidentes:"+rankingActual.porPorcentajeAccidentes()+"% Deuda Total:"+rankingActual.darTotalDeuda(); 
		return mensaje;
	}
	
	
	/**
	 * Metodo para obtener las infracciones dentro de un rango determinado
	 */
	public String[] infraccionesFecha(String pRango)
	{
		//A PENDIENTE
		return null;
	}
	
//	public IQueue <VODaylyStatistic> getDailyStatistics () {
//		IQueue<VODaylyStatistic> lista= new Queue<VODaylyStatistic>();
//		Iterador<VOMovingViolations> iter=(Iterador<VOMovingViolations>) movingViolationsQueue.iterator();
//		if(iter.hasNext()) {
//			VOMovingViolations actual=(VOMovingViolations)iter.next();
//			String fecha=actual.getTicketIssueDate().split("T")[0];
//			int numInfracciones=0;
//			int numAccidentes=0;
//			double numafintotal=0;
//			int vez=0; 
//			while(vez<movingViolationsQueue.size()) {
//				while(actual.getTicketIssueDate().split("T")[0].equals(fecha)) {
//					numafintotal+=actual.getFINEAMT();
//					numInfracciones++;
//					if(actual.getAccidentIndicator().equals("Yes")) {
//						numAccidentes++;
//					}
//					vez++; 
//					actual=iter.next();
//				}
//				lista.enqueue(new VODaylyStatistic(fecha, numAccidentes, numInfracciones, numafintotal));
//				numAccidentes=0;
//				numafintotal=0;
//				numInfracciones=0;
//				fecha=actual.getTicketIssueDate().split("T")[0];
//			}
//			return lista; 
//		}
//		return lista;
//	}



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
			Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) colaprioridad.iterator();
			VOMovingViolations actual=iter.next(); 
			int masveces=0; 
			double accidente=0;
			double deuda=0;
			VOMovingViolations repetido=actual; 
			while (iter.hasNext()) { 
				if(!revisar(agregadas, actual.getViolationCode())) {
					int veces=0;
					Iterador<VOMovingViolations> iter2= (Iterador<VOMovingViolations>) colaprioridad.iterator();
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
	
	public VOranking ordenarPorlocalización(double[] entrada) throws Exception {
		Tupla buscada= new Tupla(entrada[0], entrada[1]); 
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
		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) colaprioridad.iterator();
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


}















