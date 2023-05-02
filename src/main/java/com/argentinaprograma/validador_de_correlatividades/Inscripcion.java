/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.argentinaprograma.validador_de_correlatividades;

import java.util.ArrayList;



/**
 *
 * @author ALAN
 */
public class Inscripcion {
    Materia materia;

    Alumno  alumno;

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

 
   

    public Inscripcion(Materia materia, Alumno alumno) {
        this.materia = materia;
        this.alumno = alumno;
    }
    
   public boolean ValidarInscripcion(ArrayList<String> materiasAprobadas) {
    boolean aprobada = true;
    if (alumno.getMateriasAprobadas().containsAll(materia.getCorrelativas())) {
        System.out.println("Puede inscribirse");
        aprobada = true;
    } else {
        System.out.println("No puede inscribirse");
        aprobada = false;
    }
    return aprobada;
}

}