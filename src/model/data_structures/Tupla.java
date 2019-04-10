package model.data_structures;

public class Tupla implements Comparable<Tupla>{

	private double xCoord, yCoord; 
	
	public Tupla (double pxCoord, double pyCoord) {
		xCoord=pxCoord; 
		yCoord=pyCoord; 
	}
	public double darXCoord() {
		return xCoord; 
	}
	public double darYCoord() {
		return yCoord; 
	}
	
	public void setCoord(double x,double y) {
		xCoord=x; 
		yCoord=y; 
	}
	
	public int compareTo(Tupla o) {
		int result;  
		if(this.xCoord<o.darXCoord()) {
			result=-1; 
		}else if(this.xCoord>o.darXCoord()) {
			result=1; 
		}else {
			result=this.yCoord>o.darYCoord()?1:(this.yCoord<o.darYCoord()?-1:0); 
		}
		return result; 
	}



}
