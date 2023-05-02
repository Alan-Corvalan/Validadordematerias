package com.argentinaprograma.validador_de_correlatividades;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mysql.cj.util.StringUtils;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Scanner;
public class App 
{
    private static Scanner sc = new Scanner(System.in).useDelimiter("\n");

    private static Conexion conexion = new Conexion();
    
     public static void main(String[] args) throws SQLException, JsonProcessingException, IOException {
        
        menu();
    
    }
     
// MENU CONTEXTUAL OK    
    public static void menu() throws SQLException, IOException {
        
        boolean salir = false;
        int opcion; 
        
 
        while (!salir) {
 
            System.out.println("1. Consultar Estudiante");
            System.out.println("2. Crear Materia");
            System.out.println("3. Consultar Correlativas");
            System.out.println("4. Agregar Estudiante");
            System.out.println("5. Eliminar Estudiante");
            System.out.println("6. Salir");
 
            try {
 
                System.out.println("Escribe una de las opciones");
                opcion = sc.nextInt();
 
                switch (opcion) {
                    case 1 : {
                           consultaestudiante();
                    }
                    case 2 : {
                        crearMateria();
                        System.out.println("La materia y sus correlativas se han creado");
                    }
                    case 3 : {
                        consultarCorrelativas();
                    }
                    case 4 : {
                        agregarestudiante();
                    }
                    case 5 : { 
                        eliminarestudiante();
                       
                    }
                    case 6 : {
                        salir = true;
                        System.out.println("Usted salio");
                    }
                    default : System.out.println("Solo números entre 1 y 6");
                }
            } catch (InputMismatchException e) {
                System.out.println("Debes insertar un número entre 1 y 6");
                sc.next();
        
        
    }
        }}
    
    //CONSULTAR ESTUDIANTE OK
    public static void consultaestudiante() throws SQLException {
    
        System.out.println("Ingrese nombre o numero de legajo del estudiante");
        String nombrelegajo = sc.next();
        nombrelegajo = nombrelegajo.substring(0, 1).toUpperCase() + nombrelegajo.substring(1).toLowerCase();
        if (StringUtils.isStrictlyNumeric(nombrelegajo)) {
            if (nombrelegajo.length() != 5) {
                System.out.println("El número de legajo debe contener 5 caracteres numéricos. Intente nuevamente.");
            return;
        }
    }
        
        conexion.EstablecerConeccion();
        PreparedStatement pstmt = conexion.conectar.prepareStatement("SELECT nombre FROM alumnos WHERE nombre=? OR legajo=?;");
        pstmt.setString(1, nombrelegajo);
        pstmt.setString(2, nombrelegajo);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
        String nombreEncontrado = rs.getString("nombre");
        System.out.println("La persona ingresada se llama " + nombreEncontrado);
        boolean salir1 = false;
        int opcion1 = 0; 
        while(!salir1) {
        
            System.out.println("\n\n1. Consultar lista de materias aprobadas");
            System.out.println("2. Inscribir a una materia");
            System.out.println("3. Salir");
            System.out.println("Ingrese una opcion");
              
                try {
                        opcion1 = sc.nextInt();
              }
                catch (InputMismatchException e) {
                        System.out.println("Debes insertar un número entre 1 y 3");
                        sc.next();
            }
            
            switch (opcion1) {
                case 1 : { Listarmateria(nombreEncontrado);
                                    }

                case 2 : { Validarmateria(nombreEncontrado);
                
                }

                case 3 : { salir1 = true;
                           System.out.println("Usted salio");
        }
                default : System.out.println("Solo números entre 1 y 3");
    }
}     
     
        
        }   
        else {
        System.out.println("No se encontró ninguna estudiante con ese nombre o legajo.\nVerifique los caracteres ingresados o agregue o un nuevo estudiante \n\n");
        }   
        
        conexion.cerrarConeccion();
  }
  
  //LISTAR MATERIA
    public static void Listarmateria(String nombreEncontrado) throws SQLException {
    conexion.EstablecerConeccion();
    PreparedStatement pstmt = conexion.conectar.prepareStatement("SELECT materiasaprobadas FROM alumnos WHERE nombre = ?;");
    pstmt.setString(1, nombreEncontrado);
    ResultSet rs = pstmt.executeQuery();

    ArrayList<String> materiasAprobadas = new ArrayList<>();
    if (rs.next()) {
        String[] materias = rs.getString("materiasaprobadas").split(","); // Separa las materias aprobadas por coma
        materiasAprobadas.addAll(Arrays.asList(materias)); // Agrega las materias aprobadas al ArrayList
    }

    System.out.println("Materias aprobadas de " + nombreEncontrado + ":");
    if (materiasAprobadas.isEmpty()) {
        System.out.println("No tiene materias aprobadas.");
    } else {
        for (String materia : materiasAprobadas) {
            System.out.println("- " + materia);
        }
    }

    conexion.cerrarConeccion();
}
    
//CREAR MATERIA OK
    
    public static void crearMateria() throws SQLException{
    
        Materia materia = new Materia();
        ////---CREACION DE MATERIAS---

        System.out.println("Que nombre quiere que tenga la materia?");
        String nombre = sc.next();
        nombre = nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase();
        materia.setNombre(nombre);

        System.out.println("Cuantas correlativas tiene?");

        int numero = sc.nextInt();

        System.out.println("Que materias desea agregar a las correlativas?");
        ArrayList<String> correlativas = new ArrayList<>();

        String input;
        

        for (int i = 0; i < numero; i++) {
            input = sc.next();
            input = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
            correlativas.add(input);
        }
        
        String correlativasJson = new Gson().toJson(correlativas);

        conexion.EstablecerConeccion();
        Statement stmt = conexion.conectar.createStatement();
        stmt.executeUpdate("INSERT INTO materia VALUES(\"" + nombre + "\",'" + correlativasJson + "');");
        conexion.cerrarConeccion();
        
    }
    
 //CONSULTAR CORRELATIVAS OK

    public static void consultarCorrelativas() throws SQLException, JsonProcessingException, IOException {

    Materia materia = new Materia();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        HashMap<String, ArrayList<String>> hmMaterias = new HashMap<>();

    conexion.EstablecerConeccion();
     Statement stmt = conexion.conectar.createStatement();

    ResultSet rs = stmt.executeQuery("SELECT * FROM materia");
        while (rs.next()) {

        materia = new Materia(rs.getString("nombre"));

        String jsonText = objectMapper.writeValueAsString(rs.getString("correlativas"));

        ArrayList<String> nombreCorrelativas = objectMapper.readValue(jsonText, ArrayList.class);

        materia.setCorrelativas(nombreCorrelativas);

        hmMaterias.put(materia.getNombre(), materia.getCorrelativas());

    }
    conexion.cerrarConeccion();

    // Solicita al usuario el nombre de la materia a buscar
    
    System.out.println("Ingrese el nombre de la materia que desea buscar:");
    String nombreMateria = sc.next();
    nombreMateria = nombreMateria.substring(0, 1).toUpperCase() + nombreMateria.substring(1).toLowerCase();


    // Verifica si la materia existe en el HashMap
    if (!hmMaterias.containsKey(nombreMateria)) {
        System.out.println("La materia no existe.");
    } else {
        // Obtiene la correlativa de la materia y la muestra en pantalla
        ArrayList<String> correlativas = hmMaterias.get(nombreMateria);
        System.out.println("La correlativa de la materia " + nombreMateria + " es:");
        for (String correlativa : correlativas) {
            System.out.println(correlativa);
        }
    }
    
    }

 //AGREGAR ESTUDIANTE OK
    public static void agregarestudiante() throws SQLException{
        
        //NOMBRE
    Alumno alumno = new Alumno();
    System.out.println("Ingrese el nombre y apellido del estudiante");
    String nombre = sc.next();
        nombre = nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase();
        alumno.setNombre(nombre);
        
   // Validar si el alumno ya está registrado en la base de datos
    conexion.EstablecerConeccion();
    Statement stmt = conexion.conectar.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM alumnos WHERE nombre='" + nombre + "'");
    rs.next();
    int count = rs.getInt("count");
    
    if (count > 0) {
        System.out.println("El alumno ya está registrado en la base de datos.");
        conexion.cerrarConeccion();
         // salimos de la función
    }
    else{
    //AGREGAR LEGAJO
    int numerodelegajo = 0;
    boolean legajovalido = false;
    do{
         System.out.println("Ingrese el numero de legajo (debe contener 5 caracteres numericos)");
         String numerodelegajostr = sc.next();
         
         if(numerodelegajostr.length()!= 5) {
             System.out.println("El numero de legajo debe contener 5 caracteres numericos");
             continue;
         }
         boolean soloDigitos = true;
    for (int i = 0; i < numerodelegajostr.length(); i++) {
        if (!Character.isDigit(numerodelegajostr.charAt(i))) {
            soloDigitos = false;
            break;
        }
    }
    if (!soloDigitos) {
        System.out.println("El número de legajo debe contener solamente números.");
        continue;
    }
  numerodelegajo = Integer.parseInt(numerodelegajostr);
        alumno.setLegajo(numerodelegajo);
        legajovalido = true;
    } while (!legajovalido);

    //CONVERTIR EL LEGAJO A FORMATO DE 5 CARACTERES
    String numerodelegajoCompleto = String.format("%05d", numerodelegajo);
  
    
    
    //MATERIAS APROBADAS
    System.out.println("Cuantas materias aprobadas tiene?");

        int numero = sc.nextInt();

        System.out.println("Ingrese las materias aprobadas");
        ArrayList<String> materiasaprobadas = new ArrayList<>();

        String input;
        

        for (int i = 0; i < numero; i++) {
            input = sc.next();
            input = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
            materiasaprobadas.add(input);
        }
        String materiasaprobadasJson = new Gson().toJson(materiasaprobadas);
        
     
        
        stmt.executeUpdate("INSERT INTO alumnos VALUES('" + nombre + "','" + numerodelegajoCompleto + "','" + materiasaprobadasJson + "');");
        conexion.cerrarConeccion();
        
        System.out.println("El alumno ha sido registrado con el nombre " + nombre + " con el numero de legajo " + numerodelegajoCompleto + " y con las siguientes materias aprobadas" + materiasaprobadas);

    }
}

    //ELIMINAR ESTUDIANTE OK

    public static void eliminarestudiante() throws SQLException {
   
       int numerodelegajoeliminado = 0;
    boolean legajovalido1 = false;
    do{
         System.out.println("Ingrese el numero de legajo del estudiante que desea eliminar(debe contener 5 caracteres numericos)");
         String numerodelegajostr1 = sc.next();
         
         if(numerodelegajostr1.length()!= 5) {
             System.out.println("El numero de legajo debe contener 5 caracteres numericos");
             continue;
         }
         boolean soloDigitos1 = true;
    for (int i = 0; i < numerodelegajostr1.length(); i++) {
        if (!Character.isDigit(numerodelegajostr1.charAt(i))) {
            soloDigitos1 = false;
            break;
        }
    }
    if (!soloDigitos1) {
        System.out.println("El número de legajo debe contener solamente números.");
        continue;
    }
    numerodelegajoeliminado = Integer.parseInt(numerodelegajostr1);
    legajovalido1 = true;
} while (!legajovalido1);
    String numerodelegajoeliminadocompleto = String.format("%05d", numerodelegajoeliminado);
       
    conexion.EstablecerConeccion();
    Statement stmt = conexion.conectar.createStatement();
    stmt.executeUpdate("DELETE FROM alumnos WHERE legajo = '" + numerodelegajoeliminadocompleto + "'");
    conexion.cerrarConeccion();
    
    System.out.println("El estudiante ah sido eliminado del registro con exito!");

}
    

// VALIDADOR DE INSCRIPCIONES

    
   public static void Validarmateria(String nombreEstudiante) throws SQLException {
    
    conexion.EstablecerConeccion();

    // Crear un ArrayList con las materias en la tabla "materia"
    ArrayList<String> materiasEnTabla = new ArrayList<String>();
    PreparedStatement pstmt = conexion.conectar.prepareStatement("SELECT nombre FROM materia");
    ResultSet rsMateriasEnTabla = pstmt.executeQuery();
    while (rsMateriasEnTabla.next()) {
        materiasEnTabla.add(rsMateriasEnTabla.getString("nombre"));
    }

    // Pedir al usuario que ingrese el nombre de la materia
    System.out.println("Ingrese el nombre de la materia:");
    String nombreMateria = sc.next();
    nombreMateria = nombreMateria.substring(0, 1).toUpperCase() + nombreMateria.substring(1).toLowerCase();

    // Verificar que la materia exista en la tabla "materia"
    if (!materiasEnTabla.contains(nombreMateria)) {
        System.out.println("La materia no existe en la tabla \"materia\"");
        conexion.cerrarConeccion();
        return;
    }

    // Crear un ArrayList con las materias aprobadas por el estudiante en la tabla "alumnos"
    ArrayList<String> materiasAprobadasEnTabla = new ArrayList<String>();
    pstmt = conexion.conectar.prepareStatement("SELECT m.nombre FROM materia m JOIN alumnos a ON m.nombre = a.materiasaprobadas WHERE a.nombre = ?");
    pstmt.setString(1, nombreEstudiante);
    ResultSet rsMateriasAprobadasEnTabla = pstmt.executeQuery();
    while (rsMateriasAprobadasEnTabla.next()) {
        materiasAprobadasEnTabla.add(rsMateriasAprobadasEnTabla.getString("nombre"));
    }

    // Crear un objeto Inscripcion y comprobar si el alumno puede inscribirse en la materia
    Inscripcion inscripcion = new Inscripcion(new Materia(nombreMateria), new Alumno(nombreEstudiante));
    if (inscripcion.ValidarInscripcion(materiasAprobadasEnTabla)) {
        System.out.println("El alumno puede inscribirse en la materia");
    } else {
        System.out.println("El alumno no puede inscribirse en la materia");
    }

    // Cerrar la conexión a la base de datos
    conexion.cerrarConeccion();
}

        
    }

