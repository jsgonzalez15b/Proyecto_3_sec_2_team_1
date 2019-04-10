package model.vo;

public class VOranking implements Comparable<VOranking> {

	private String violationcode; 

	private int numinfracciones; 

	private double porcenacc; 

	private double porcensinacc;

	private double deuda;

	private int streetId; 
	private String location; 

	public VOranking(String pCode, int pNum, double pPorcenacc, double pPorcensinacc, double pDeuda, String pLocation,int p) {
		violationcode=pCode; 
		numinfracciones=pNum; 
		porcenacc=pPorcenacc; 
		porcensinacc=pPorcensinacc; 
		deuda=pDeuda;
		streetId=p; 
		location=pLocation; 
	}

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
