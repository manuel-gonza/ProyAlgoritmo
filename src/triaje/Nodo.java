/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package triaje;

/**
 * Nodo de la lista enlazada simple. Cada nodo contiene un Paciente
 * y una referencia al siguiente nodo.
 */
public class Nodo {
    Paciente datos;
    Nodo siguiente;

    public Nodo(Paciente datos) {
        this.datos = datos;
        this.siguiente = null;
    }
}