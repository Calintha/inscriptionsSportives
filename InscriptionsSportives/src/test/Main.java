package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.sql.ResultSet;


public class Main {

	public static void main(String[] args) {
		
		Connection connection;
		Statement statement;
		ResultSet result;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost/java?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC","root","root");
			statement = connection.createStatement();
			
			result = statement.executeQuery("SELECT nom, prenom FROM personnes");
			
			while(result.next()) {
				System.out.println("Nom: " + result.getString("nom") + " Prenom: " + result.getString("prenom"));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
