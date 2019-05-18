package model.vo; 

public class verticeInfo implements Comparable<verticeInfo>
{

	double latitud; 
	double longitud; 
	
	public verticeInfo(double pLatitud, double pLongitud) {
		latitud=pLatitud; 
		longitud=pLongitud; 
	}
	
	public double darLatitud() {
		return latitud;
	}
	
	public double darlongitud() {
		return longitud; 
	}
	
	public void serLatitud(double p) {
		latitud=p; 
	}
	public void setLongitud(double p) {
		longitud=p; 
	}
	
	public int compareTo(verticeInfo otro) {
		return this.latitud>otro.darLatitud()?1:(this.latitud<otro.darLatitud()?-1:0); 
	}
}
