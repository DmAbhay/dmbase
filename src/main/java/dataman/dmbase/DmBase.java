//package dataman.dmbase;
//
//import java.util.Base64;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import dataman.dmbase.config.FilePathService;
//import dataman.dmbase.config.IniConfig;
//import dataman.dmbase.dbutil.DatabaseUtil;
//import dataman.dmbase.encryptiondecryptionutil.EncryptionDecryptionUtil;
//import dataman.dmbase.redissessionutil.RedisSessionUtil;
//
//public class DmBase {
//	
//	@Autowired
//	private FilePathService filePathService;
//	
//	@Autowired
//	private static RedisSessionUtil redisSessionUtil;
//	
//	public static void main(String[] args) {
//		
//	
//		IniConfig configReader = new IniConfig("C:\\Users\\Dataman\\myconfigfile\\myconfig.properties");
//		//System.out.println(configReader.getProperty("companyDb"));
//		
//		DatabaseUtil databaseUtil = new DatabaseUtil(
//				configReader.getProperty("sqlHostName"),
//				configReader.getProperty("sqlPort"),
////				configReader.getProperty("companyDb"),
////				"dlm_transaction",
//				"donation_Transaction",
//				configReader.getProperty("sqlUser"),
//				configReader.getProperty("sqlPassword")
//		);
//		
//		
//		
////		String sqlQuery = "SELECT centralDataPath FROM projectMaster WHERE projectId = 10002;";
////        String columnName = "centralDataPath";
////        String databaseName = databaseUtil.getDatabaseName(sqlQuery, columnName);
//        
//        
////        System.out.println(databaseUtil.getRowAsMap("SELECT * FROM projectMaster WHERE projectId = 10001;"));
//		System.out.println(databaseUtil.getRowsAsList("SELECT * FROM registration;"));
//		List<Map<String, Object>> rows = databaseUtil.getRowsAsList("SELECT * FROM registration");
//
//        // Print results
//        for (Map<String, Object> row : rows) {
//            System.out.println(row);
//        }
//        
//        
//        EncryptionDecryptionUtil encryptionDecryption = new EncryptionDecryptionUtil();
//        System.out.println(encryptionDecryption.getSecretKey().toString());
//		try {
//			System.out.println(EncryptionDecryptionUtil.generateAESKey());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        System.out.println("SecretKey: " + Base64.getEncoder().encodeToString(encryptionDecryption.getSecretKey().getEncoded()));
//	
//		
//		
//		DatabaseUtil databaseUtill = new DatabaseUtil(
//                "javaserver",
//                "1434",
//                "donation_Transaction",
//                configReader.getProperty("sqlUser"),
//                configReader.getProperty("sqlPassword")
//        );
//		
//
//        System.out.println(databaseUtill.getRowAsMap("SELECT * FROM registration WHERE code = 1000100000000000063"));
//		
//        
//        
//       
//        
//        
//        
//        
//	}
//
//}
