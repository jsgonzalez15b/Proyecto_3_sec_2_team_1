package controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.opencsv.CSVReader;
import com.sun.corba.se.impl.orbutil.graph.Node;

import model.data_structures.IQueue;
import model.data_structures.IStack;
import model.data_structures.Iterador;
import model.data_structures.Nodo;
import model.data_structures.Queue;
import model.data_structures.Stack;
import model.vo.VODaylyStatistic;
import model.vo.VOMovingViolations;
import view.MovingViolationsManagerView;

public class Controller {

	private MovingViolationsManagerView view;

	/**
	 * Cola donde se van a cargar los datos de los archivos
	 */
	private IQueue<VOMovingViolations> movingViolationsQueue;

	/**
	 * Pila donde se van a cargar los datos de los archivos
	 */
	private IStack<VOMovingViolations> movingViolationsStack;


	public Controller() {
		view = new MovingViolationsManagerView();

		//TODO, inicializar la pila y la cola
		movingViolationsQueue = null;
		movingViolationsStack = null;
	}

	public void run() {
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
				IStack<Integer> lista= verificarObjectId(); 
				if(lista.isEmpty()){
					view.printMensage("No hay OBJECTID repetidos");
					break;
				}else{

					break;
				}
			case 3:
				view.printMensage("Ingrese el dia y la fecha en formato AAAA-MM-DD,HH:MM:SS.000Z");;
				String ingreso=sc.next();
				String fecha=ingreso.split(",")[0];
				String hora=ingreso.split(",")[1];
				String fecha2=ingreso.split(",")[2];
				String hora2=ingreso.split(",")[3];
				IQueue<VOMovingViolations> lista1=consultarPorFechaYHora(fecha,hora,fecha2,hora2);
				view.printfechaHora(lista1);
				break; 
			case 4:
				view.printMensage("Ingrese el código de violación");
				String codigo=sc.next();
				double[] arreglo=fineAmtPromedio(codigo);
				String mensaje="El promedio con accidentalidad es: "+arreglo[0]+" y sin accidentalidad es: "+ arreglo[1] ;
				view.printMensage(mensaje);
				break;
			case 5: 
				view.printMensage("Ingrese el ADDRESS_ID y la fecha en formato: ADDRESS_ID, AAAA-MM-DD, AAAA-MM-DD");
				String linea=sc.next();
				int addresid=Integer.parseInt(linea.split(",")[0]);
				String inicio=linea.split(",")[1];
				String fina=linea.split(",")[2];
				view.printAddresId(consultarPorDireccion(addresid, inicio, fina));
				break;
			case 6: 
				view.printMensage("Ingrese el promedio en formato: val1,val2");
				String entrada=sc.next();
				int in=Integer.parseInt(entrada.split(",")[0]);
				int fin2=Integer.parseInt(entrada.split(",")[1]);

				break; 
			case 7:
				view.printMensage("Ingrese el rango del valor total pagado en formato: val1,val2");
				String linea1=sc.next();
				int val1=Integer.parseInt(linea1.split(",")[0]);
				int val2=Integer.parseInt(linea1.split(",")[1]);
				view.printMensage("Ingrese como desea obserbar la lista: 1.Ascendentemente 2.Descendentemente");
				int orden=sc.nextInt();
				view.printTotalPagado(consultarPorTotalPagado(val1, val2, orden));
				break; 
			case 8: 
				view.printMensage("Ingrese la hora inicial y final en formato: HH:MM:SS.000Z,HH:MM:SS.000Z");
				String linea11=sc.next();
				String horainicio=linea11.split(",")[0];
				String horafinal=linea11.split(",")[1];
				view.printPorHora(consultarporHoraInicialyFinal(horainicio, horafinal));
				break; 
			case 9: 
				view.printMensage("Ingrese el codigo de violacion");
				String codigo1=sc.next();
				double[] a=consultarPromedioVariacion(codigo1);
				view.printMensage("El FINEAMT promedio es: "+a[0]+" y su desviación estandar es: "+a[1]);
				break; 
			case 10:
				int[] ar=darAccidentesporHoraDia();
				System.out.println("a");
				view.printASCII(ar);
				break; 
			case 11:
				view.printASCIIMeses(deudaMeses());
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
	public int[] loadMovingViolations(int num)
	{
		//estructuras de almacenamiento de infracciones
		movingViolationsQueue=new Queue<VOMovingViolations>();
		movingViolationsStack= new Stack<VOMovingViolations>();
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
					
					double diez=linea[10].equals("")?0: Double.parseDouble(linea[10]);
					double once=linea[11].equals("")?0:Double.parseDouble(linea[11]);
					//creacion de infraccion en estructura de datos para campos definidos
					movingViolationsStack.push(new VOMovingViolations(Integer.parseInt(linea[0]), linea[2], linea[13], Double.parseDouble(linea[9]), linea[12], linea[15], linea[14], Double.parseDouble(linea[8]),tres,diez,once,cinco,seis));
					movingViolationsQueue.enqueue(new VOMovingViolations(Integer.parseInt(linea[0]), linea[2], linea[13], Double.parseDouble(linea[9]), linea[12], linea[15], linea[14], Double.parseDouble(linea[8]),tres,diez, once,cinco,seis));
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

	public IQueue <VODaylyStatistic> getDailyStatistics () {
		IQueue<VODaylyStatistic> lista= new Queue<VODaylyStatistic>();
		Iterador<VOMovingViolations> iter=(Iterador<VOMovingViolations>) movingViolationsQueue.iterator();
		if(iter.hasNext()) {
			VOMovingViolations actual=(VOMovingViolations)iter.next();
			String fecha=actual.getTicketIssueDate().split("T")[0];
			int numInfracciones=0;
			int numAccidentes=0;
			double numafintotal=0;
			int vez=0; 
			while(vez<movingViolationsQueue.size()) {
				while(actual.getTicketIssueDate().split("T")[0].equals(fecha)) {
					numafintotal+=actual.getFINEAMT();
					numInfracciones++;
					if(actual.getAccidentIndicator().equals("Yes")) {
						numAccidentes++;
					}
					vez++; 
					actual=iter.next();
				}
				lista.enqueue(new VODaylyStatistic(fecha, numAccidentes, numInfracciones, numafintotal));
				numAccidentes=0;
				numafintotal=0;
				numInfracciones=0;
				fecha=actual.getTicketIssueDate().split("T")[0];
			}
			return lista; 
		}
		return lista;
	}

	public IStack <VOMovingViolations> nLastAccidents(int n) {
		IStack<VOMovingViolations> lista=new Stack<>();
		Iterador<VOMovingViolations> iter=(Iterador<VOMovingViolations>) movingViolationsStack.iterator();
		VOMovingViolations actual=iter.next();
		while(n>0&&iter.hasNext()){
			if(actual.getAccidentIndicator().equals("Yes")){
				lista.push(actual);
				n--;
			}
			actual=iter.next();
		}

		return lista;
	}

	public IStack<Integer> verificarObjectId (){
		IStack<Integer> retornar= new Stack<>();
		Iterador<VOMovingViolations> iter=(Iterador<VOMovingViolations>)movingViolationsStack.iterator();
		VOMovingViolations actual=iter.next();
		Nodo<VOMovingViolations> primero=movingViolationsStack.darPrimero();
		int objectid=actual.objectId();
		while(iter.hasNext()){
			boolean repetido=false; 
			while(primero.darSiguiente()!=null&&!repetido){
				if(primero.darSiguiente().darElemento().objectId()==objectid){
					retornar.push(objectid);
					repetido=true; 
				}
				primero=primero.darSiguiente();
			}
			objectid=iter.next().objectId();
		}
		return retornar; 
	}

	public IQueue<VOMovingViolations> consultarPorFechaYHora(String pFechaInicial, String pHoraInicial, String pFechaFinal, String pHoraFinal){
		IQueue<VOMovingViolations> retornar= new Queue<>();
		Iterador<VOMovingViolations> iter=(Iterador<VOMovingViolations>) movingViolationsQueue.iterator();
		VOMovingViolations actual=iter.next();
		int contador=0; 
		String fecha=actual.getTicketIssueDate().split("T")[0];
		String hora=actual.getTicketIssueDate().split("T")[1];
		while(contador<movingViolationsQueue.size()){
			if(fecha.compareTo(pFechaInicial)>=0 && hora.compareTo(pHoraInicial)>=0 && fecha.compareTo(pFechaFinal)<=0 && hora.compareTo(pHoraInicial)<0 )
			{
				retornar.enqueue(actual);
			}
			actual=iter.next();
			fecha=actual.getTicketIssueDate().split("T")[0];
			hora=actual.getTicketIssueDate().split("T")[1];
			contador++;
		}
		return retornar;
	}

	public double[] fineAmtPromedio(String pViolation){
		double[] retornar= new double[2];
		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
		VOMovingViolations actual=iter.next();
		int sumyes=0;
		int sumNo=0; 
		int cantYes=0; 
		int cantNo=0;
		while(iter.hasNext()){
			if(actual.getViolationCode().equals(pViolation)){
				if(actual.getAccidentIndicator().equals("Yes")){
					sumyes+=actual.getFINEAMT();
					cantYes++; 
				}else{
					sumNo+=actual.getFINEAMT();
					cantNo++; 
				}
			}
			actual=iter.next();
		}
		retornar[0]=(sumyes/cantYes);
		retornar[1]=(sumNo/cantNo);
		return retornar; 
	}

	public IStack <VOMovingViolations> consultarPorDireccion(int pDireccion,String FechaIn, String FechaFin){
		IStack <VOMovingViolations> retornar= new Stack<>();
		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
		VOMovingViolations actual=iter.next();
		while(iter.hasNext()){
			if(actual.getAdressId()==pDireccion){
				{
					if(actual.getTicketIssueDate().split("T")[0].compareTo(FechaIn)>0&&actual.getTicketIssueDate().split("T")[0].compareTo(FechaFin)<0){
						retornar.push(actual);
					}
				}
			}
		}
		return retornar; 
	}

	//El metodo necesita optimizacion, se necesita de un orden si se quiere un recorrido eficiente.
	public IQueue<String> consultarPorPromedioFINEAMT(int val1, int val2)
	{
		//Queue para retornar la informacion solicitada
		IQueue<String> retornar= new Queue<>();
		//Queue para  verificar no repeticion
		IQueue<String> verificar= new Queue<>();
		//Iterador interno para comparacion de valores
		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
		//Iterador externo para recorrido de todos los tipos de infracciones
		Iterador<VOMovingViolations> iter2= (Iterador<VOMovingViolations>) movingViolationsQueue.iterator();
		//Iterador para recorrido de tipos de infracciones realizados
		Iterador<String> iter3= (Iterador<String>) verificar.iterator();
		
		VOMovingViolations actual=iter.next();
		VOMovingViolations actual2=iter2.next();
		
		String violation=actual2.getViolationCode();
		String currentViolation="";
		verificar.enqueue(violation);
		int total=0;
		int cantidad=0; 
		boolean repetida=false;
		int promedioInfraccionActual=0;
		while(iter2.hasNext())//ciclo externo para recorrido de todos los tipos de infracciones
		{
			while(iter.hasNext())//ciclo interno para calculo de AMT promedio
			{
				if(actual.getViolationCode().equals(violation))
				{
					total+=actual.getFINEAMT();
					cantidad++; 
				}
				actual=iter.next();
			}
			promedioInfraccionActual=total/cantidad;
			if(promedioInfraccionActual>val1&&promedioInfraccionActual<val2)
			{
				retornar.enqueue(violation+promedioInfraccionActual);
			}
			//continuacion de recorrido externo para encontrar un nuevo violationCode
			while(iter2.hasNext()&&!repetida)
			{
				currentViolation=iter2.next().getViolationCode();
				while(iter3.hasNext()&&!repetida)
				{
					if(currentViolation.equals(iter.next().getViolationCode()))
					{
						repetida=true;
						violation=currentViolation;
					}
				}
				
			}
			repetida=false;
			
		}
		
		return retornar; 
	}

	public  IStack <VOMovingViolations> consultarPorTotalPagado(int val1, int val2, int pOrden){
		IStack <VOMovingViolations> retornar= new Stack <VOMovingViolations>();
		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
		VOMovingViolations actual= iter.next();
		while(iter.hasNext()){
			if(actual.getTotalPaid()>=val1 && actual.getTotalPaid()<=val2){
				retornar.push(actual);
			}
			actual=iter.next();
		}
		VOMovingViolations[] ordenar= new VOMovingViolations[retornar.size()];
		for(int i=0; i<retornar.size();i++) {
			ordenar[i]=retornar.pop();
		}
		ordenarMergeSort(ordenar, 2);
		for(int j=0;j<ordenar.length; j++) {
			retornar.push(ordenar[j]);
		}
		if(pOrden==2) {
			return retornar;
		}
		else {
			IStack <VOMovingViolations> retornarasc=new Stack<>();
			for(int k=0; k<retornar.size();k++) {
				retornarasc.push(retornar.pop());
			}
			return retornarasc; 
		}
	}
	public IQueue<VOMovingViolations> consultarporHoraInicialyFinal(String fech1, String fech2){
		IQueue<VOMovingViolations> retornar= new Queue<>();
		int contador=0; 
		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsQueue.iterator();
		VOMovingViolations actual=iter.next();
		while (contador<movingViolationsQueue.size()) {
			if(actual.getTicketIssueDate().split("T")[0].compareTo(fech1)>0&&actual.getTicketIssueDate().split("T")[1].compareTo(fech2)<0) {
				retornar.enqueue(actual);
			}
			contador++;
			actual=iter.next();
		}
		VOMovingViolations[] ordenar= new VOMovingViolations[retornar.size()];
		for(int i=0; i<retornar.size();i++) {
			ordenar[i]=retornar.dequeue();
		}
		ordenarMergeSort(ordenar, 1);
		for(int j=0;j<ordenar.length; j++) {
			retornar.enqueue(ordenar[j]);
		}
		return retornar; 
	}

	public double[] consultarPromedioVariacion(String pCodigo) {
		double[] retornar= new double[2];
		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
		VOMovingViolations actual=iter.next();
		int sum=0;
		int cantidad=0;
		ArrayList<VOMovingViolations> arreglo=new ArrayList<>();
		while(iter.hasNext()) {
			if(actual.getViolationCode().equals(pCodigo)) {
				cantidad++; 
				sum+=actual.getFINEAMT();
				arreglo.add(actual);
			}
			actual=iter.next();
		}
		double promedio=sum/cantidad;
		retornar[0]=promedio;
		int sum2=0; 
		for(int i=0; i<arreglo.size(); i++) {
			sum2+=Math.pow(arreglo.get(i).getFINEAMT()-promedio, 2);
		}
		retornar[1]=Math.sqrt(sum2/arreglo.size());
		return retornar; 
	}


	public int[] darAccidentesporHoraDia() {
		int[] retornar= new int[24]; 
		for(int i=0; i<retornar.length;i++) {
			Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
			VOMovingViolations actual= iter.next();
			int sum=0;
			while(iter.hasNext()) {
				int hora=Integer.parseInt(actual.getTicketIssueDate().split("T")[1].split(":")[0]);
				if(hora==i&& actual.getAccidentIndicator().equals("Yes")) {
					sum++; 
				}
				actual=iter.next();
			}
			retornar[i]=sum; 
			System.out.println(i+"");
		}
		return retornar; 
	}

	public double darDeudaTotal(String fecha1, String fecha2) {
		double sum=0;
		Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsStack.iterator();
		VOMovingViolations actual= iter.next();
		while(iter.hasNext()) {
			if(!(actual.getTicketIssueDate().split("T")[0].compareTo(fecha1)<0)&&!(actual.getTicketIssueDate().split("T")[0].compareTo(fecha2)>0)) {
				sum+=actual.getFINEAMT()+actual.getTotalPaid()+actual.getPenalty1()+actual.getPenalty2();
				actual=iter.next();
			}
		}
		return sum; 
	}

	public double[] deudaMeses() {
		double[] a=new double[4];
		int mes =Integer.parseInt(movingViolationsStack.darPrimero().darElemento().getTicketIssueDate().split("T")[0].split("-")[1]);
		int inicio=(mes==1||mes==2||mes==3||mes==4)?1:((mes==5||mes==6||mes==7||mes==8)?5:9);
		for(int i=inicio; i<4;i++) {
			a[i]=darDeudaTotal("2018-"+(i)+"-01", "2018-"+(i)+"-31");		
		}
		return a; 
	}

	public static void merge(VOMovingViolations[] arreglo, int l, int m, int r, int pModo) {
		int j; 
		int i; 
		int n1=m-l+1;
		int n2=r-m;
		VOMovingViolations[] izquierdo=new VOMovingViolations[n1];
		VOMovingViolations[] derecho=new VOMovingViolations[n2];
		for(i=0;i<n1;i++) {
			izquierdo[i]=arreglo[l+i];
		}
		for(j=0; j<n2;j++) {
			derecho[j]=arreglo[m+1+j];
		}
		i=0; 
		j=0;
		int k =l;
		while(i<n1&&j<n2) {
			if(izquierdo[i].compareTo(derecho[j],pModo)<0) {
				arreglo[k]=izquierdo[i];
				i++;
			}else {
				arreglo[k]=derecho[j];
				j++;
			}
			k++;
		}
		while(i<n1) {
			arreglo[k]=izquierdo[i];
			i++;
			k++;
		}
		while(i<n2) {
			arreglo[k]=derecho[j];
			j++;
			k++;
		}

	}

	public static void mergeSort(VOMovingViolations[]datos, int izq, int der, int pModo) {
		if(izq<der) {
			int m=izq+(der-izq)/2;
			mergeSort(datos,izq,m,pModo);
			mergeSort(datos, m+1, der,pModo);
			merge(datos, izq, m, der, pModo);
		}
	}

	public static void ordenarMergeSort( VOMovingViolations[ ] datos, int pModo ) {
		mergeSort(datos, 0, datos.length-1, pModo);
	}
	
	
	public double[] calcularMiniMax(){
	double[] coordenadas= new double[4];
	double xmin,xmax,ymin,ymax; 
	Iterador<VOMovingViolations> iter= (Iterador<VOMovingViolations>) movingViolationsQueue.iterator();
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
	

}















