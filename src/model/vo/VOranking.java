package model.vo;

public class VOranking {

	private String violationcode; 
	
	private int numinfracciones; 
	
	private double porcenacc; 
	
	private double porcensinacc;
	
	private double deuda;
	
	private int streetId; 
	
	public VOranking(String pCode, int pNum, double pPorcenacc, double pPorcensinacc, double pDeuda) {
		violationcode=pCode; 
		numinfracciones=pNum; 
		porcenacc=pPorcenacc; 
		porcensinacc=pPorcensinacc; 
		deuda=pDeuda;
		streetId=0; 
	}
	public void setStreetId(int nuevo) {
		streetId=nuevo; 
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
		
}
