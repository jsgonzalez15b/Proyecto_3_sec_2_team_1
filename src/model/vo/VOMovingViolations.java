package model.vo;

/**
 * Representation of a Trip object
 */
public class VOMovingViolations 
{

	//Atributos de infraccion

	/**
	 * ID: elemento 1
	 */
	private int Id;

	//elemento 2 no utilizado: ROW_

	/**
	 * Location: elemento 3
	 */
	private String location; 
	/**
	 * addressID: elemento 4
	 */
	private int adressId;

	//elemento 5 no utilizado: STREETSEGID

	/**
	 * Xcoord: elemento 6
	 */
	private double coordx;
	/**
	 * Ycoord: elemento 7
	 */
	private double coordy;

	//elemento 8 no utilizado: TICKETTYPE

	/**
	 * FineAMT: elemento 9
	 */
	private double fineAMT;
	/**
	 * TotalPaid: elemento 10
	 */
	private double totalpaid; 
	/**
	 * Penalty1: elemento 11
	 */
	private double penalty1; 
	/**
	 * Penalty2: elemento 12
	 */
	private double penalty2; 
	/**
	 * AccidentIndicator: elemento 13
	 */
	private String accidenIndicator; 
	/**
	 * TicketIssueDate: elemento 14
	 */
	private String ticketIssueDate;
	/**
	 * ViolationCode: elemento 15
	 */
	private String violationcode; 
	/**
	 * ViolationDesc: elemento 16
	 */	
	private String description; 
	
	private int streetID;

	//elemento 17 no utilizado: ROW_ID

	//Constructor
	public VOMovingViolations(int pId, String pLocation, String pTicketIssueDate, double pTotoalpaid, String pAccidentIndicator, String pDescription, String pViolationCode, double pfineAMT, int pAdressId, double pPenalty1, double pPenalty2, double pCoordx, double pCoordy, int pStreetId)
	{
		//inicializacion de atributos
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
		coordx = pCoordx;
		coordy = pCoordy;
		streetID=pStreetId; 

	}

	public int getStreetId() {
		return streetID; 
	}

	/**
	 * @return id - Identificador único de la infracción
	 */
	public int objectId() 
	{
		// TODO Auto-generated method stub
		return Id;
	}	


	/**
	 * @return location - Dirección en formato de texto.
	 */
	public String getLocation()
	{
		// TODO Auto-generated method stub
		return location; 
	}

	/**
	 * @return date - Fecha cuando se puso la infracción .
	 */
	public String getTicketIssueDate()
	{
		// TODO Auto-generated method stub
		return ticketIssueDate;
	}

	/**
	 * @return totalPaid - Cuanto dinero efectivamente pagó el que recibió la infracción en USD.
	 */
	public double getTotalPaid() 
	{
		// TODO Auto-generated method stub
		return totalpaid;
	}

	/**
	 * @return accidentIndicator - Si hubo un accidente o no.
	 */
	public String  getAccidentIndicator() 
	{
		// TODO Auto-generated method stub
		return accidenIndicator; 
	}

	/**
	 * @return description - Descripción textual de la infracción.
	 */
	public String  getViolationDescription() 
	{
		// TODO Auto-generated method stub
		return description;
	}
	public String getViolationCode()
	{
		return violationcode;
	}

	public double getFINEAMT()
	{
		return fineAMT;
	}

	public int getAdressId()
	{
		return adressId;
	}

	public double getPenalty1()
	{
		return penalty1; 
	}

	public double getPenalty2() 
	{
		return penalty2; 
	}

	public double getX()
	{
		return coordx;
	}

	public double getY()
	{
		return coordy;
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

