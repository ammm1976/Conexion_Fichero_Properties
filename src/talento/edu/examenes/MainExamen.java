package talento.edu.examenes;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Scanner;

/**
 * 
 * TODO: haced una conexion a la base de datos para ver si existe un usuario y su contraseña
 * 
 * el usuario y la contraseña la pedimos por teclado (clase Scanner)
 * 
 * La tabla de la base de datos es usuarios del esquema bddni
 * En dicha tabla, tenemos dos columnas: usuario y password, que son las que tenemos que consultar
 * 
 * Una vez solicitadas las credenciales del usuario (usuario y password) hacemos una consulta 
 * SELECT en la base de datos. Si esta, le mostramos un mensaje de bienvenida.
 * 
 * Si no coincide el nombre o la contraseña, le mostramos un mensaje de login incorrecto
 * 
 * Importante: Tratar las excepciones con try catch y si se produce algun fallo, imprimidlo con printStackTrace
 * 
 * El driver de la base de datos es MYSQL (jar)
 * El usuario y la contraseña para conectarse a la base de datos viene en un fichero de propiedades (parametros.properties)
 *
 */
public class MainExamen {

	public static boolean esLoginCorrecto(String nombre, String password)
	{
		boolean loginCorrecto = false;
		Driver driver = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String usuarioConexion = null;
		String passwordConexion = null;
		
		try {
			//1 CARGO EL DRIVER
			driver = new com.mysql.cj.jdbc.Driver();
			DriverManager.registerDriver(driver);
			
			//2 OBTENGO CONEXION
			Properties properties = new Properties();
			properties.load(new FileReader("parametros.properties"));
			usuarioConexion = properties.getProperty("usuario");
			passwordConexion = properties.getProperty("password");

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bddni", usuarioConexion, passwordConexion);

			//3 CREO LA INSTRUCCION - STATEMENT
			String consulta = "SELECT * FROM bddni.usuarios WHERE usuario = ? AND password = ?";
			statement = connection.prepareStatement(consulta);
			statement.setString(1, nombre);
			statement.setString(2, password);
			
			//4 RECUPERRAR RESULTADOS
			statement.execute();
			loginCorrecto = statement.getResultSet().next();
			
			//5 CERRAR LA CONEXION
			connection.close();
			
		} catch (Exception e) {
			e.getStackTrace();
			// TODO: handle exception
		}
		
		return loginCorrecto;
	}
		
	public static void main(String[] args) {
		System.out.println("Intro el nombre");
		Scanner scanner = new Scanner(System.in);
		String nombre = scanner.nextLine();
		
		System.out.println("Intro la contraseña");
		String password = scanner.nextLine();
		
		if (esLoginCorrecto(nombre, password))
		{
			System.out.println("LOGIN CORRECTO!");
		} else
			System.out.println("LOGIN INCORRECTO :S ");
	}
}
