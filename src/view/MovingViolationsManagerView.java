package view;

import model.data_structures.Dupla;
import model.data_structures.IQueue;
import model.data_structures.IStack;
import model.data_structures.Tupla;
import model.vo.VODaylyStatistic;
import model.vo.VOMovingViolations;
import model.vo.VOranking;

public class MovingViolationsManagerView 
{
	public MovingViolationsManagerView() {
		
	}
	
	public void printMenu() {
		System.out.println("---------ISIS 1206 - Estructuras de datos----------");
		System.out.println("---------------------Taller 3----------------------");
		System.out.println("1. Cree una nueva coleccion de infracciones en movimiento");
		System.out.println("2. Obtenga las franjas horarias con mayor numero de infracciones");
		System.out.println("3. Ordenar infracciones por localizacion geografica y buscar infracciones en coordenadas x,y");
		System.out.println("4. Obtenga infracciones en un rango de fechas");
		System.out.println("5. ");
		System.out.println("6. ");
		System.out.println("7. ");
		System.out.println("8. ");
		System.out.println("9. ");
		System.out.println("10. ");
		System.out.println("11. ");
		System.out.println("12. ");
		System.out.println("Digite el numero de opcion para ejecutar la tarea, luego presione enter: (Ej., 1):");
		
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
		System.out.println("La zona geográfica con las infracciones esta comprendida entre las coordenadas "+ "(" +Minmax[0]+","+Minmax[1]+") para X y " + "(" +Minmax[2]+","+Minmax[3]+") para Y");
	}
	
	/**
	 * Metodo que imprime N franjas horarias con el mayor numero de infracciones
	 * @param info Arreglo de Strings que contiene: franja, numero de infracciones, porcentaje sin y con, y valor total
	 */
	public void printNFranjas(String[] info)
	{
		int elementos = info.length;
		System.out.println("Las " + elementos +"franjas horarias con mayor número de infracciones son:");
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
		
		
	}
}