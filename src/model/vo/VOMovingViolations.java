package model.vo;

/**
 * Representation of a Trip object
 */
public class VOMovingViolations {

	private int Id;
	private String location; 
	private String ticketIssueDate;
	private double totalpaid; 
	private String accidenIndicator; 
	private String description; 
	private String violationcode; 
	private double fineAMT;
	private int adressId; 
	private double penalty1; 
	private double penalty2; 


	public VOMovingViolations(int pId, String pLocation, String pTicketIssueDate, double pTotoalpaid, String pAccidentIndicator, String pDescription, String pViolationCode, double pfineAMT, int pAdressId, double pPenalty1, double pPenalty2){
		Id=pId;
		location=pLocation;
		ticketIssueDate=pTicketIssueDate;
		totalpaid=pTotoalpaid;
		accidenIndicator=pAccidentIndicator;
		description=pDescription; 		
		violationcode=pViolationCode; 
		fineAMT=pfineAMT;
		adressId=pAdressId;
		penalty1=pPenalty1;
		penalty2=pPenalty2;
	}
	
	
	/**
	 * @return id - Identificador único de la infracción
	 */
	public int objectId() {
		// TODO Auto-generated method stub
		return Id;
	}	
	
	
	/**
	 * @return location - Dirección en formato de texto.
	 */
	public String getLocation() {
		// TODO Auto-generated method stub
		return location; 
	}

	/**
	 * @return date - Fecha cuando se puso la infracción .
	 */
	public String getTicketIssueDate() {
		// TODO Auto-generated method stub
		return ticketIssueDate;
	}
	
	/**
	 * @return totalPaid - Cuanto dinero efectivamente pagó el que recibió la infracción en USD.
	 */
	public double getTotalPaid() {
		// TODO Auto-generated method stub
		return totalpaid;
	}
	
	/**
	 * @return accidentIndicator - Si hubo un accidente o no.
	 */
	public String  getAccidentIndicator() {
		// TODO Auto-generated method stub
		return accidenIndicator; 
	}
		
	/**
	 * @return description - Descripción textual de la infracción.
	 */
	public String  getViolationDescription() {
		// TODO Auto-generated method stub
		return description;
	}
	public String getViolationCode(){
		return violationcode;
	}
	
	public double getFINEAMT() {
		return fineAMT;
	}
	
	public int getAdressId(){
		return adressId;
	}
	
	public double getPenalty1() {
		return penalty1; 
	}
	
	public double getPenalty2() {
		return penalty2; 
	}

	public int compareTo(VOMovingViolations otro, int pModo) { 
		if(pModo==1) {
			//Comparo por violationdesc
		return getViolationDescription().compareTo(otro.getViolationDescription())==0?0:(getViolationDescription().compareTo(otro.getViolationDescription())<0?-1:1);
				
		}else if(pModo==2) {
			//Comparo fecha de infracción
			return getTicketIssueDate().split("T")[0].compareTo(otro.getTicketIssueDate().split("T")[0])==0?0:(this.ticketIssueDate.split("T")[0].compareTo(otro.getTicketIssueDate().split("T")[0])<0?-1:1);
		}else {
			return 0; 
		}
	}
}

