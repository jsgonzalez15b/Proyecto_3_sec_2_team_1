package model.vo;

public class VOranking implements Comparable<VOranking> 
{
	//Atributos estadisticos de infracciones (para uso general)
	private String violationcode; 

	private int numinfracciones; 

	private double porcenacc; 

	private double porcensinacc;

	private double deuda;

	private int streetId; 
	
	private String location; 

	//Constructor
	public VOranking(String pCode, int pNum, double pPorcenacc, double pPorcensinacc, double pDeuda, String pLocation,int p) {
		violationcode=pCode; 
		numinfracciones=pNum; 
		porcenacc=pPorcenacc; 
		porcensinacc=pPorcensinacc; 
		deuda=pDeuda;
		location=pLocation; 
		streetId=p; 
		
	}
	
	//Métodos
	public String darLocation() {
		return location; 
	}
	public int darSreetiId() {
		return streetId; 
	}

	public String darCode() {
		return violationcode;
	}
	public int darnumInfracciones() {
		return numinfracciones; 
	}
	public double darPorcentajeSinAccidentes() {
		return porcensinacc; 
	}
	public double porPorcentajeAccidentes() {
		return porcenacc; 
	}
	public double darTotalDeuda() {
		return deuda; 
	}
	
	/**
	 * Metodo para incrementar actualizar la información de las infracciones por criterio (numinfracciones, porcenacc, porcensinacc, deuda)
	 * @param pAccidente booleano que indica si hubo accidente o no
	 * @param pDeuda	 entero que indica deuda a agregar
	 */
	public void actualizarInfo(boolean pAccidente, int pDeuda)
	{
		
		int numeroPorcentaje=0;
		if(pAccidente)
		{
			numeroPorcentaje=(int) (numinfracciones*porcenacc); //obtiene el numero de infracciones con accidente para actualizarlo
			numeroPorcentaje++; //actualiza el numero de infracciones
			numinfracciones++; //aumenta el numero de infracciones
			porcenacc= numinfracciones/numeroPorcentaje;
			porcensinacc= 1-porcenacc;
			violationcode= numinfracciones+"";
		}
		else
		{
			numeroPorcentaje=(int) (numinfracciones*porcensinacc); //obtiene el numero de infracciones sin accidente para actualizarlo
			numeroPorcentaje++; //actualiza el numero de infracciones
			numinfracciones++; //aumenta el numero de infracciones
			porcensinacc= numinfracciones/numeroPorcentaje;
			porcenacc= 1-porcensinacc;
			violationcode= numinfracciones+"";
		}
		
		
	}
	public int compareTo(VOranking otro) {
		int result=0; 
		if(this.violationcode.compareTo(otro.darCode())<0) {
			result =-1; 
		}else if (this.violationcode.compareTo(otro.darCode())>0) {
			result=1; 
		}
		return result; 
	}


}
