package Util.Email.Logging;

public class LoggerTest {
	/**
     * Test
     * seven type:
     * 	SEVERE->WARNING->INFO->CONFIG->FINE->FINER->FINEST
     * @param args
     */
    public static void main(String[] args) {  
        //
    	EmailLogger.getDefaultLoggerHanlder().info(
    			"JDK Logger is logging information at INFO Level " + 
    			"use logger.info");
    	
    	EmailLogger.info(
    			"JDK Logger is logging information at INFO Level " + 
    			"use EmailLogger.info");
   }  
}
