/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package triaje;

/**
 * Cola de prioridad implementada como lista enlazada simple.
 *
 * • enqueue – O(n)  (recorrido lineal para colocar según prioridad)
 * • dequeue – O(1)  (elimina la cabeza)
 * • mostrarFila – O(n) (recorre y devuelve una cadena)
 *
 * No se emplea ninguna clase del Java Collections Framework.
 */
public class ColaPrioridad {
    private Nodo cabeza;   // primer paciente (más urgente)

    public ColaPrioridad() {
        this.cabeza = null;
    }

    /** Inserta manteniendo el orden de prioridad (1 = mayor urgencia). */
    public void enqueue(Paciente p) {
        Nodo nuevo = new Nodo(p);

        // Caso 1: lista vacía o prioridad más alta → inserción al frente
        if (cabeza == null || p.getPrioridad() < cabeza.datos.getPrioridad()) {
            nuevo.siguiente = cabeza;
            cabeza = nuevo;               // reasignación de puntero head
            return;
        }

        // Caso 2: buscar posición adecuada
        Nodo previo = cabeza;
        Nodo actual = cabeza.siguiente;

        // Avanzar mientras la prioridad del nodo actual sea <= la nueva prioridad
        while (actual != null && actual.datos.getPrioridad() <= p.getPrioridad()) {
            previo = actual;
            actual = actual.siguiente;
        }

        // Insertar entre previo y actual
        previo.siguiente = nuevo;
        nuevo.siguiente = actual;
    }

    /** Extrae y devuelve el paciente de mayor urgencia (cabeza). */
    public Paciente dequeue() {
        if (cabeza == null) {
            return null;
        }
        Paciente atendido = cabeza.datos;
        cabeza = cabeza.siguiente;        // GC liberará el nodo antiguo
        return atendido;
    }

    /** Devuelve una cadena con todos los pacientes en orden de atención. */
    public String mostrarFila() {
        if (cabeza == null) {
            return ">>> La fila está vacía.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(">>> Estado actual de la fila (de mayor a menor urgencia):\n");
        Nodo actual = cabeza;
        int pos = 1;
        while (actual != null) {
            sb.append(pos).append(". ").append(actual.datos).append('\n');
            actual = actual.siguiente;
            pos++;
        }
        return sb.toString();
    }

    /** Indica si la cola está vacía. */
    public boolean estaVacia() {
        return cabeza == null;
    }
}