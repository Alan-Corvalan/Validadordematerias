/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.argentinaprograma.validador_de_correlatividades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ALAN
 */
public class Conexion {
    
    Connection conectar = null;
    String usuario = "root" ;
    String contraseña = "root" ;
    String bd = "argprograma";
    String ip = "localhost" ;
    String puerto = "3306" ;
    
     //jdbc:mysql://localhost:3306/dbname
     String ruta =  "jdbc:mysql://"+ ip +":"+ puerto + "/" + bd;
     
  public Connection EstablecerConeccion() {
      try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          
          conectar = DriverManager.getConnection(ruta, usuario, contraseña);
          System.out.println("Se conecto correctamente");
                  
                  
      } catch (Exception e) {
          System.out.println("No se conecto");
      }
  return conectar;
  
  }     
   public void cerrarConeccion() throws SQLException {
        try {
            conectar.close();
        } catch (Exception e) {
    
}
   }
}
