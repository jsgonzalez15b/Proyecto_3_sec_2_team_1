package model.vo;

public class VODaylyStatistic {
	
	
	private String fecha; 
	private int accidentes; 
	private int infracciones; 
	private double fineamt;
	
	public VODaylyStatistic(String pFecha, int pAccidente, int pInfracciones, double pFineAMT){
		fecha=pFecha;
		accidentes=pAccidente;
		infracciones=pInfracciones;
		fineamt=pFineAMT;		
	}
	
	public String darFecha() {
		return fecha;
	}
	
	public int darAccidente() {
		return accidentes; 
	}
	
	public int darInfracciones() {
		return infracciones;
	}
	
	public double darTotalFineAMT() {
		return fineamt;
	}
}
