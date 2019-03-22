package view;

import model.data_structures.IQueue;
import model.data_structures.IStack;
import model.vo.VODaylyStatistic;
import model.vo.VOMovingViolations;

public class MovingViolationsManagerView 
{
	public MovingViolationsManagerView() {
		
	}
	
	public void printMenu() {
		System.out.println("---------ISIS 1206 - Estructuras de datos----------");
		System.out.println("---------------------Taller 3----------------------");
		System.out.println("1. Cree una nueva coleccion de infracciones en movimiento");
		System.out.println("2. Verificar que no hay ObjectId repetidos entre las facturas");
		System.out.println("3. Dar infracciones por fecha y hora de inicio");
		System.out.println("4. Dar promedio de FINEAMT por Codigo de violación");
		System.out.println("5. Dar infracciones por address_ID y fecha");
		System.out.println("6. Consultar por rango de FINEAMT");
		System.out.println("7. Consultar infracciones por cantidad pagada");
		System.out.println("8. Consultar infracciones por hora inicial y final (Ordenada ascendentemente por VIOLATIONDESC.)");
		System.out.println("9. Consultar promedio de FINEATME y desviación estandar por codigo de violación");
		System.out.println("10.Generar gráfica ASCII con el porcentaje de infracciones que tuvieron accidentes por hora del dia ");
		System.out.println("11. Generar gráfica ASCII con el porcentaje de valor de la deuda por mes");
		System.out.println("12. Salir");
		System.out.println("Digite el nï¿½mero de opciï¿½n para ejecutar la tarea, luego presione enter: (Ej., 1):");
		
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
	public void printOBJECTID(IQueue<Integer> lista) {
		System.out.println("Se encontraron "+ lista.size() + " elementos");
		int vez=0; 
		for (Integer Id : lista) 
		{
			System.out.println(Id+"");;
			vez++; 
			if(vez==lista.size()){
				break; 
			}
		}
	}
	
	public void printMovingViolations(IStack<VOMovingViolations> violations) {
		System.out.println("Se encontraron "+ violations.size() + " elementos");
		for (VOMovingViolations violation : violations) 
		{
			System.out.println(violation.objectId() + " " 
								+ violation.getTicketIssueDate() + " " 
								+ violation.getLocation()+ " " 
								+ violation.getViolationDescription());
		}
	}
	
	public void printMensage(String mensaje) {
		System.out.println(mensaje);
	}

	public void printfechaHora(IQueue<VOMovingViolations> lista1) {
		System.out.println("Se encontraron "+ lista1.size() + " elementos");
		int vez=0; 
		for(VOMovingViolations vo: lista1){
			System.out.println(vo.objectId()+","+vo.getTicketIssueDate());
			vez++;
			if(vez==lista1.size()){
				break; 
			}
		}
		
	}

	public void printAddresId(IStack<VOMovingViolations> lista1) {
		System.out.println("Se encontraron "+ lista1.size() + " elementos");
		int vez=0;
		for(VOMovingViolations vo: lista1){
			System.out.println(vo.objectId()+","+vo.getTicketIssueDate()+","+vo.getAdressId());
			vez++;
			if(vez==lista1.size()){
				break;
			}
		}
	}

	public void printTotalPagado(IStack<VOMovingViolations> lista1) {
		System.out.println("Se encontraron "+ lista1.size() + " elementos");
		int vez=0;
		for(VOMovingViolations vo: lista1){
			System.out.println(vo.objectId()+","+vo.getTicketIssueDate()+","+vo.getTotalPaid());
			vez++;
			if(vez==lista1.size()){
				break;
			}
		}		
	}

	public void printPorHora(IQueue<VOMovingViolations> lista) {
		// TODO Auto-generated method stub
		System.out.println("Se encontraron "+ lista.size()+" elementos");
		int vez=0; 
		for(VOMovingViolations vo: lista) {
			vez++;
			System.out.println(vo.objectId()+","+vo.getTicketIssueDate()+","+vo.getViolationDescription());
			if(vez==lista.size()) {
				break; 
			}
		}
	}

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

	public void printCargar(int[] pormes, int num, double[] Minmax) {
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


}
