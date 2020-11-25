package demo.bd;

import java.sql.DriverManager;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;

public class Miconexion {
	
static {
		
		try {
			// invocar a las clases el driver 
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	// parte del driver, pinchar en el error 
	
	//IP 127.0.0.1
	//port 3306
	// name root
	// clave mysql
	
	public Connection getConexion() {
		Connection cnx = null;
		
		try {
			// necesitaba un casteo dando click al error 
			cnx = (Connection) DriverManager.getConnection(
					//"jdbc:mysql://10.10.9.90:3306/tienda?useSSL=false", prooduccion
					//"jdbc:mysql://10.10.9.70:3306/tienda?useSSL=false", calidad
					"jdbc:mysql://localhost:3306/tienda?useSSL=false",
					//solamente cambias bd_java
					"root",
					"mysql"); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cnx;
		
	}

}
