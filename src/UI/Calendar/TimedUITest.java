package UI.Calendar;

import UI.Calendar.TimedUI;
import Util.Email.Logging.EmailLogger;

public class TimedUITest {
	/**
	 * Variables
	 */
	private boolean isSureOrQuit;
	private long duration;
	private TimedUI TimedUI;
	
	// ******************************************************
	
    public TimedUITest() {  
       
    }  
    
    public void setDuration(long duration) {
    	this.duration = duration;
    }
    
    public long getDuration() {
    	return this.duration;
    }
    
    public void setIsSureOrQuit(boolean isSureOrQuit) {
    	this.isSureOrQuit = isSureOrQuit;
    }
    
    public boolean getIsSureOrQuit() {
    	return this.isSureOrQuit;
    }
    
    public void run() {  
    	//
    	boolean flag;
	    new Thread() {  
	    	@Override
	    	public void run() {
	    		// create a calendar ui
	    		TimedUI = new TimedUI(null);
	    		// wait
	    		int idx = 0;
	    		while (TimedUI.getIsClose() == false) {  
	    			// do nothing
	    			// EmailLogger.info("TimedUI waits for close - " + idx);
	    			idx++;
	    			try {
	    				Thread.sleep(1000);
	    			} catch (InterruptedException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    		} 
        	  
	    		setIsSureOrQuit(TimedUI.getIsSureOrQuit());
	    		setDuration(TimedUI.getDuration());
	    		// flag = getIsSureOrQuit();
	    		TimedUI.dispose();
              
	    		EmailLogger.info("**********************************************");
	    		EmailLogger.info("TimedUITest - end run() - inner: ");
	    		// EmailLogger.info("TimedUITest - duration: " + ct.getDuration());
	    		EmailLogger.info("isSureOrQuit: " + getIsSureOrQuit());
	    		// System.exit(1);
	    	}  
	    }.start(); 
       
       EmailLogger.info("**********************************************");
       EmailLogger.info("TimedUITest - end run() - outer: ");
       // EmailLogger.info("TimedUITest - duration: " + ct.getDuration());
       EmailLogger.info("isSureOrQuit: " + getIsSureOrQuit());
    }   
   
    public static void main(String[] args) {  
       TimedUITest ct = new TimedUITest(); 
       ct.run();
    }  
}