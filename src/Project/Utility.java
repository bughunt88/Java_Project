package Project;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Utility {
	private String year ;
	private String month ; 
	private String firstDay ;
	private String lastDay ;
	
	
	public String getFirstDay() {
		return firstDay;
	}

	public String getLastDay() {
		return lastDay;
	}

	public Utility() {		
	}

	public Utility(int year, int month) {
		this.year = String.valueOf(year)  ;
		this.month = String.valueOf(month) ;
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH) ;
		//System.out.println( lastDay );
		
		this.firstDay 
			= new DecimalFormat("0000").format(Integer.valueOf( this.year )) + "/"
			 + new DecimalFormat("00").format(Integer.valueOf( this.month )) + "/" + "01" ;
		
		this.lastDay 
			= new DecimalFormat("0000").format(Integer.valueOf( this.year )) + "/"
				 + new DecimalFormat("00").format(Integer.valueOf( this.month )) + "/" 
				+ new DecimalFormat("00").format( lastDay ) ;
					
	}
}
