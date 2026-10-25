/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package triaje;

/**
 * TAD Paciente – mantiene la información del paciente.
 * La prioridad sigue la convención:
 *   1 = mayor urgencia, 5 = menor urgencia.
 */
public class Paciente {
    private String nombre;
    private int edad;
    private String motivo;
    private int prioridad;   // 1 … 5

    public Paciente(String nombre, int edad, String motivo, int prioridad) {
        this.nombre = nombre;
        this.edad = edad;
        this.motivo = motivo;
        setPrioridad(prioridad);
    }

    // ---------- Getters ----------
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public String getMotivo() { return motivo; }
    public int getPrioridad() { return prioridad; }

    // ---------- Setter ----------
    public void setPrioridad(int prioridad) {
        if (prioridad < 1) this.prioridad = 1;
        else if (prioridad > 5) this.prioridad = 5;
        else this.prioridad = prioridad;
    }

    @Override
    public String toString() {
        return String.format(
                "[%s | %d años | %s | Prioridad %d]",
                nombre, edad, motivo, prioridad);
    }
}