package Util.Email;

import Util.CreateFileUtil;
import Util.Email.Logging.EmailLogger;

/**
 * path manager
 * @author ddk
 * @time 2014-12-07
 */
public final class PathManager {
	// Vars
	private static boolean IsSetclsPath = false;
	private static String defaultResPath = null;
	private static String repPath = null;
	private static String clsPath = null;
	private static String downloadPath = null;
	private static String dbPath = null;
	private static String iconPath = null;
	private static String loggingPath = null;
	private static String propsPath = null;
	
	private static String resPath = null;
	private static String pathToDownload = null;
	private static String pathToDB = null;
	private static String pathToIcon = null;
	private static String pathToLogs = null;
	private static String pathToProps = null;
	
	private static String imgDir = null;
	private static String fileDir = null;
	private static String pdfDir = null;
	private static String htmlDir = null;
	private static String otherDir = null;
	
	// static variables initialization
	static {
		String osName = System.getProperty("os.name");
		// windows
		if(osName.contains("Windows")) {
			resPath = "res\\";
			pathToDownload = "downloads\\";
			pathToDB = "sqlite3\\";
			pathToIcon = "icons\\";
			pathToLogs = "logs\\";
			pathToProps = "props\\";
			
			imgDir = "images\\";
			fileDir = "files\\";
			pdfDir = "pdfs\\";
			htmlDir = "htmls\\";
			otherDir = "others\\";
		} else if(osName.contains("unix")) {
			resPath = "res/";
			pathToDownload = "downloads/";
			pathToDB = "sqlite3/";
			pathToIcon = "icons/";
			pathToLogs = "logs/";
			pathToProps = "props/";
			
			imgDir = "images/";
			fileDir = "files/";
			pdfDir = "pdfs/";
			htmlDir = "htmls/";
			otherDir = "others/";
		}
	}
	
	public static String dbName = "email.db";
	public static boolean isRelative = false;
	
	// ******************************************************************
	
	public static String getDirSplitStr() {
		String osName = System.getProperty("os.name");
		//
		if(osName.contains("Windows")) {
			return "\\";
		} else if(osName.contains("unix")) {
			return "/";
		}
		
		// other return windows
		return "\\";
	}
	
	//
	public static void setIsRelative(final boolean flag) {
		isRelative = flag;
	}
	
	//
	public static boolean getIsRelative() {
		return isRelative;
	}
	
	public static void SetIsSetclsPath(boolean flag, String defaultPath) {
		System.out.println("**************** 000 ******************");
		IsSetclsPath = flag;
		if(IsSetclsPath == true && defaultPath != null) {
			defaultResPath = defaultPath;
			System.out.println("000 - init - clsPath: " + clsPath);
		}
	}
	//
	public static String getResourcePath() {
		if(clsPath == null) {
			// get source path
			if(IsSetclsPath == false) {
				clsPath = PathManager.class.getResource("").getPath();
			
				String osName = System.getProperty("os.name");
				// windows
				if(osName.contains("Windows")) {
					if(clsPath.indexOf("/") == 0 || clsPath.indexOf("\\") == 0) {
						clsPath = clsPath.substring(1);
					} else if(clsPath.indexOf("\\") == 0) {
						clsPath = clsPath.substring(2);
					}
					
					System.out.println("init clsPath: " + clsPath);
					int idx = clsPath.indexOf("bin");
					if(idx != -1) {
						clsPath = clsPath.substring(0, idx);
					} else {
						int idx2 = clsPath.lastIndexOf(".jar");
						clsPath = clsPath.substring(0, idx2);
						idx2 = clsPath.lastIndexOf("/");
						clsPath = clsPath.substring(0, idx2);
					}
					
					clsPath = clsPath.replace("/", "\\");
					clsPath = clsPath + resPath;
					System.out.println("final absolutely clsPath: " + clsPath);
					
					if(isRelative == true) {
						clsPath = clsPath.substring(clsPath.indexOf("Emails"));
						clsPath = clsPath.substring(clsPath.indexOf("Emails"));
						System.out.println("final relatively clsPath: " + clsPath);
					}
				// unix
			    } else if(osName.contains("unix")) {
			    	System.out.println("init clsPath:" + clsPath);
			    	int idx = clsPath.indexOf("bin");
					if(idx != -1) {
						clsPath = clsPath.substring(0, idx);
					} else {
						int idx2 = clsPath.lastIndexOf(".jar");
						clsPath = clsPath.substring(0, idx2);
						idx2 = clsPath.lastIndexOf("/");
						clsPath = clsPath.substring(0, idx2);
					}
					clsPath = clsPath + resPath;
					System.out.println("final absolutely clsPath: " + clsPath);
					
					if(isRelative == true) {
						clsPath = clsPath.substring(clsPath.indexOf("Emails"));
						clsPath = clsPath.substring(clsPath.indexOf("Emails"));
						System.out.println("final relatively clsPath: " + clsPath);
					}
			    }
			} else {
				String osName = System.getProperty("os.name");
				clsPath = defaultResPath;
				if(osName.contains("Windows")) {
					clsPath = clsPath.replace("/", "\\");
				}
				clsPath = clsPath + resPath;
				System.out.println("final absolutely clsPath: " + clsPath);
			}
		}
		
		return clsPath;
	}
	
	/**
	 * 得到icon文件夹的路径
	 * 返回的路径前后均包含斜杠
	 */
	public static String getIconsResourcePath() {
		if(iconPath == null) {
			// get clsPath
			getResourcePath();
			// set dbPath
			iconPath = clsPath + pathToIcon;
			EmailLogger.info("final iconPath: " + iconPath);
			// create path if does not exist
			CreateFileUtil.createDir(iconPath);
		}
		
		return iconPath;
	}
	
	/**
	 * 得到logs文件夹的路径
	 * 返回的路径前后均包含斜杠
	 */
	public static String getLogsResourcePath() {
		if(loggingPath == null) {
			// get clsPath
			getResourcePath();
			// set dbPath
			loggingPath = clsPath + pathToLogs;
			
			// create path if does not exist
			// CreateFileUtil.createDir(loggingPath);
			
			// EmailLogger.info("final loggingPath: " + loggingPath);
		}
		
		return loggingPath;
	}
	
	/**
	 * 得到props文件夹的路径
	 * 返回的路径前后均包含斜杠
	 */
	public static String getPropsResourcePath() {
		if(propsPath == null) {
			// get clsPath
			getResourcePath();
			// set dbPath
			propsPath = clsPath + pathToProps;
			EmailLogger.info("final propsPath: " + propsPath);
			// create path if does not exist
			CreateFileUtil.createDir(propsPath);
		}
		
		return propsPath;
	}
	
	/**
	 * 得到downloadPath文件夹的路径
	 * 返回的路径前后均包含斜杠
	 */
	public static String getDownloadResourcePath() {
		if(downloadPath == null) {
			// get clsPath
			getResourcePath();
			// set dbPath
			downloadPath = clsPath + pathToDownload;
			EmailLogger.info("final downloadPath: " + downloadPath);
			// create path if does not exist
			CreateFileUtil.createDir(downloadPath);
		}
		
		// return path
		return downloadPath;
	}
	/**
	 * 得到sqlite3数据库文件的路径（
	 * 返回的路径前后均包含斜杠，不包含数据库文件名）
	 */
	public static String getDBResourcePath() {
		if(dbPath == null) {
			// get clsPath
			getResourcePath();
			// set dbPath
			dbPath = clsPath + pathToDB;
			EmailLogger.info("final dbPath: " + dbPath);
			// create path if does not exist
			CreateFileUtil.createDir(dbPath);
		}
		
		// return path
		return dbPath;
	}
	
	/**
	 * @return 得到database路径
	 */
	public static String getDBPath() {
		// get path
		String dirName = getDBResourcePath() + dbName;
		// create path if does not exist
		CreateFileUtil.createDir(dirName);
		// return path
		return dirName;
	}
	
	/**
	 * @return 得到database的images的路径
	 */
	public static String getDBImagesPath() {
		// get path
		String dirName = getDBResourcePath() + imgDir;
		// create path if does not exist
		CreateFileUtil.createDir(dirName);
		// return path
		return dirName;
	}
	
	/**
	 * @return 得到database的files的路径
	 */
	public static String getDBFilesPath() {
		// get path
		String dirName = getDBResourcePath() + fileDir;
		// create path if does not exist
		CreateFileUtil.createDir(dirName);
		// return path
		return dirName;
	}
	
	/**
	 * @return 得到database的pdfs的路径
	 */
	public static String getDBPdfsPath() {
		// get path
		String dirName = getDBResourcePath() + pdfDir;
		// create path if does not exist
		CreateFileUtil.createDir(dirName);
		// return path
		return dirName;
	}
	
	/**
	 * @return 得到database的htmls的路径
	 */
	public static String getDBHtmlsPath() {
		// get path
		String dirName = getDBResourcePath() + htmlDir;
		// create path if does not exist
		CreateFileUtil.createDir(dirName);
		// return path
		return dirName;
	}
	
	/**
	 * @return 得到database的others的路径
	 */
	public static String getDBOthersPath() {
		// get path
		String dirName = getDBResourcePath() + otherDir;
		// create path if does not exist
		CreateFileUtil.createDir(dirName);
		// return path
		return dirName;
	}
	
	/**
	 * 
	 */
	public static void createAllDirs() {
		//
		EmailLogger.info("**********************************************");
		EmailLogger.info("Stat to created all dirs for email client!");
		
		EmailLogger.info("DBfileDir: " + PathManager.getDBPath());
		EmailLogger.info("ImagesDir: " + PathManager.getDBImagesPath());
		EmailLogger.info("FilesDir: " + PathManager.getDBFilesPath());
		EmailLogger.info("PdfsDir: " + PathManager.getDBPdfsPath());
		EmailLogger.info("HtmlsDir: " + PathManager.getDBHtmlsPath());
		EmailLogger.info("OthersDir: " + PathManager.getDBOthersPath());
		
		EmailLogger.info("**********************************************");
		
		EmailLogger.info("iconsDir: " + PathManager.getIconsResourcePath());
		EmailLogger.info("logsDir: " + PathManager.getLogsResourcePath());
		EmailLogger.info("propsDir " + PathManager.getPropsResourcePath());
		EmailLogger.info("dowloadsDir " + PathManager.getDownloadResourcePath());
		
		return;
	}
	
	/**
	 * Test
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * before use logger, first call PathManager.createdAllDirs() 
		 * before using email client, but has a problem as above
		 * problem: 
		 * 		Couldn't get lock for logFilePath
		 * so, before run this code, you should first manually create the logging file
		 * in pathToProjcet/res/logs
		 */
		createAllDirs();
	}
}