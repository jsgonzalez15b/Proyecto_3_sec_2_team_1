package view;

import java.util.Iterator;

import model.data_structures.Arco;
import model.data_structures.Dupla;
import model.data_structures.Grafo;
import model.data_structures.IQueue;
import model.data_structures.IStack;
import model.data_structures.Iterador;
import model.data_structures.MaxColaPrioridad;
import model.data_structures.RedBlackBST;
import model.data_structures.Stack;
import model.data_structures.Tupla;
import model.data_structures.Vertice;
import model.vo.VODaylyStatistic;
import model.vo.VOMovingViolations;
import model.vo.VOranking;
import model.vo.verticeInfo;

public class MovingViolationsManagerView 
{
	public MovingViolationsManagerView()
	{
		
	}
	
	public void printMenu()
	{
		System.out.println("---------ISIS 1206 - Estructuras de datos----------");
		System.out.println("---------------------Proyecto 3--------------------");
		System.out.println("1. Cargar desde JSON las infracciones y mapa correspondiente");
		System.out.println("2. Encontrar el camino con menos infracciones entre dos vertices aleatorios");
		System.out.println("3. Identificar los n vertices con mayor numero de infracciones y subgrafos asociados");
		System.out.println("4. Retornar camino mas corto escogido entre dos vertices aleatorios");
		System.out.println("5. Retonar aproximación matriz");
		System.out.println("6. Kruskal: Calcular arbol de extension minima al subgrafo mas grande de 3.");
		System.out.println("7. Prim: Calcular arbol de extension minima al subgrafo mas grande de 3.");
	}
	
//	public void printDailyStatistics(IQueue<VODaylyStatistic> dailyStatistics) {
//		System.out.println("Se encontraron "+ dailyStatistics.size() + " elementos");
//		int vez=0; 
//		for (VODaylyStatistic dayStatistic : dailyStatistics) 
//		{
//			System.out.println(dayStatistic.darFecha()+"- accidentes:"+dayStatistic.darAccidente()+",	infracciones:"	+dayStatistic.darInfracciones()+",	multas totales:"+dayStatistic.darTotalFineAMT() );;
//			vez++; 
//			if(vez==dailyStatistics.size()){
//				break; 
//			}
//		}
//	}


	public void printASCII(int[] lista) {
		// TODO Auto-generated method stub
		System.out.println("Porcentaje de infracciones que tuvieron accidentes por hora. 2018");
		System.out.println("Hora|  % de accidentes");
		int total=0; 
		for(int i=0; i< lista.length; i++) {
			total+=lista[i]; 
		}
		for(int j=00; j<lista.length; j++) {
			int porcentaje=lista[j]*100/total; 
			int equises=(int)porcentaje/1;
			String x="";
			for(int k=0; k<equises; k++) {
				x=x+"X";
			}
			System.out.println(j+"  |  "+x);			
		}
		System.out.println("Cada X corresponde al 1%");
	}

	public void printASCIIMeses (double[] lista) {
		System.out.println("Deuda  acumulada por mes de infracciones. 2018");
		System.out.println("Hora|  Dinero");
		for(int i=0;i<lista.length; i++) {
			int equises=(int)lista[i]/1;
			String x="";
			for(int j=0; j<equises; j++) {
				x=x+"X";
			}
			System.out.println((i+1)+"  |  "+x);
		}
		System.out.println("Cada X corresponde a 100 USD");
	}

	public void printMensage(String mensaje) 
	{
		System.out.println(mensaje);
	}
	
	public void printCargar(int[] pormes, int num, double[] Minmax)
	{
		String[] meses= new String[6]; 
		int sum=0; 
		if(num==1){
			meses[0]="Enero:"; 
			meses[1]="Febrero:"; 
			meses[2]="Marzo:"; 
			meses[3]="Abril:"; 
			meses[4]="Mayo:";
			meses[5]="Junio"; 
		}else{
			meses[0]="Julio:"; 
			meses[1]="Agosto:"; 
			meses[2]="Septiembre:"; 
			meses[3]="Octubre:"; 
			meses[4]="Noviembre:";
			meses[5]="Diciembre"; 
		}
		for(int i=0; i<pormes.length; i++){
			System.out.println("En el mes "+meses[i]+" hay "+ pormes[i]+" infracciones registradas");
			sum+=pormes[i];
		}
		System.out.println("En total hay " + sum +" infracciones cargadas");
		System.out.println("La zona geogrÃ¡fica con las infracciones esta comprendida entre las coordenadas "+ "(" +Minmax[0]+","+Minmax[1]+") para X y " + "(" +Minmax[2]+","+Minmax[3]+") para Y");
	}
	
	/**
	 * Metodo que imprime N franjas horarias con el mayor numero de infracciones
	 * @param info Arreglo de Strings que contiene: franja, numero de infracciones, porcentaje sin y con, y valor total
	 */
	public void printNFranjas(String[] info)
	{
		int elementos = info.length;
		System.out.println("Las " + elementos +"franjas horarias con mayor nÃºmero de infracciones son:");
		for(int hello=0;hello<elementos;elementos++)
		{
			System.out.println(info[hello]);
		}
		
	}
	
	/**
	 * Metodo que imprime N Infracciones en un rango de tiempo determinado
	 * @param pInfracciones Arreglo de Strings que contiene: PREGUNTAR
	 */
	public void printInfraccionesRango(String[] pInfracciones)
	{
		int elementos = pInfracciones.length;
		for(int hello=0;hello<elementos;elementos++)
		{
			System.out.println(pInfracciones[hello]);
		}
		
	}

	public void printRanking(MaxColaPrioridad<VOranking> cola) {
		// TODO Auto-generated method stub
		Iterador<VOranking> iter=(Iterador<VOranking>) cola.iterator(); 
		VOranking actual=iter.next(); 
		int numero=0; 
		while(iter.hasNext()) {
			numero++; 
			System.out.println(numero+". "+ actual.darCode()+","+actual.darnumInfracciones()+","+actual.darPorcentajeSinAccidentes()+" sin accidentes,"+actual.porPorcentajeAccidentes()+"con accidentes,"+ actual.darTotalDeuda()+"por pagar");
			actual=iter.next(); 
		}
	}

	public void printVORanking(VOranking obtenido) {
		System.out.println("Total de infracciones:"+ obtenido.darnumInfracciones()+",Infreacciones sin accidentes: "+obtenido.darPorcentajeSinAccidentes()+", Porcentaje con infracciones: "+obtenido.porPorcentajeAccidentes()+", Valor total a pagar: "+obtenido.darTotalDeuda()+", location: "+obtenido.darLocation()+",StreetSegId: "+ obtenido.darSreetiId());
	}
	
		public void printArbolRango(RedBlackBST<Double, String> arbolRango) {
		if(arbolRango.isEmpty()) {
			System.out.println("No se ha encontrado ninguna zona horaria que tenga el intervalo ingresado por parametro");
		}else {
			arbolRango.darRaiz().imprimir(); 
		}
		
	}
	
	/**
	 * Metodo que imprime N Infracciones en un rango al dia determinado
	 * @param pMensaje Arreglo de Strings que contiene informacion dentro y fuera del rango.
	 */
	public void printInfraccionesHora (String[] pMensaje)
	{
		int elementos = pMensaje.length;
		for(int hello=0;hello<elementos;elementos++)
		{
			System.out.println(pMensaje[hello]);
		}
	}
	
	/**
	 * Metodo que imprime N coordenadas con el mayor numero de infracciones
	 * @param coordenadasNesimas Arreglo de Strings que contiene informacion de coordenadas.
	 */
	public void printCoordenadasNesimas(String[] coordenadasNesimas)
	{
		int elementos = coordenadasNesimas.length;
		System.out.println("Las estadisticas para el numero de coordenadas ingresado son:");
		for(int hello=0;hello<elementos;elementos++)
		{
			System.out.println(coordenadasNesimas[hello]);
		}
	}
	
		public void printACIIViolationcode(IStack<Dupla<String, Double>> cola ){
		System.out.println("Porcentaje de infracciones por violation code");
		System.out.println("Codigo|  % de accidentes");
		Iterador<Dupla<String,Double>> iterador=(Iterador<Dupla<String, Double>>) cola.iterator(); 
		Dupla<String,Double> actual=iterador.next(); 
		double total=0; 
		while(iterador.hasNext()) {
			total+=actual.getValue(); 
			actual=iterador.next(); 
		}
		Iterador<Dupla<String,Double>> iterador2=(Iterador<Dupla<String, Double>>) cola.iterator(); 
		Dupla<String,Double> actual2=iterador.next();
		while (iterador2.hasNext()) {
			int porcentaje=(int) (actual2.getValue()*100/total); 
			int equises=(int) porcentaje/3; 
			String x=""; 
			for(int i=0;i<equises;i++) {
				x=x+"*";
			}
			System.out.println(actual2.getKey()+"  |  "+x);
			
			System.out.println("Cada * corresponde al 3%");	
		}
	}
		
		public void printreq4(Stack<Arco<Long,Double>>arcos, Grafo<verticeInfo, Long, Double> grafo) {
			System.out.println("El camino más corto entre los vertices tiene "+(arcos.size()+1)+" vertices");
			double distancia=0; 
			Arco<Long, Double> actual=arcos.darPrimero().darElemento(); 
			verticeInfo info=grafo.getInfoVertex(actual.darInicio()); 
			System.out.println("vertice0:"+actual.darInicio()+", Ubicación: "+info.darLatitud()+" lat, "+info.darlongitud()+" long");
			int count=1; 
			while(!arcos.isEmpty()) {
				actual=arcos.pop(); 
				distancia+=actual.darPeso(); 
				info=grafo.getInfoVertex(actual.darAdyacente()); 
				System.out.println("vertice"+count+":"+actual.darAdyacente()+", Ubicación: "+info.darLatitud()+" lat, "+info.darlongitud()+" long");
				count++; 
			}
			System.out.println("La distancia en km del camino es de:" + distancia);	
			
		}

		public void printreq5(Stack<Vertice<verticeInfo, Long, Double>> retornar) {
			System.out.println("Se encontraron "+ retornar.size()+" vertices de la aproximación");
			Iterator<Vertice<verticeInfo, Long, Double>> iter = retornar.iterator(); 
			Vertice<verticeInfo, Long, Double> actual; 
			while(iter.hasNext()) {
				actual=iter.next(); 
				System.out.println("Id:"+actual.darLlave()+", Ubicación:"+actual.darValor().darLatitud()+"lat, "+actual.darValor().darlongitud()+"long");
			}
			
		}
	
}
