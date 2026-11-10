/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package triaje;

/**
 * TAD Paciente – mantiene la información del paciente.
 * La prioridad sigue la convención:
 *   1 = mayor urgencia, 5 = menor urgencia.
 *
 * El paciente se identifica con un documento:
 *   - DNI (peruanos): 8 dígitos.
 *   - Carné de extranjería: 9 dígitos.
 *   - Pasaporte (extranjeros): de 6 a 12 letras o números.
 */
public class Paciente {

    // Tipos de documento permitidos
    public static final String DNI = "DNI";
    public static final String CARNE_EXTRANJERIA = "Carné de extranjería";
    public static final String PASAPORTE = "Pasaporte";

    private String tipoDocumento;
    private String numeroDocumento;
    private String apellidos;
    private String nombres;
    private int edad;
    private String motivo;
    private int prioridad;   // 1 … 5

    // Si algún dato no es válido, se avisa con una excepción
    // para que el paciente nunca se cree con datos malos.
    public Paciente(String tipoDocumento, String numeroDocumento,
                    String apellidos, String nombres, int edad, String motivo, int prioridad) {
        validarDocumento(tipoDocumento, numeroDocumento);
        if (apellidos == null || apellidos.trim().isEmpty()) {
            throw new IllegalArgumentException("Los apellidos no pueden estar vacíos.");
        }
        if (nombres == null || nombres.trim().isEmpty()) {
            throw new IllegalArgumentException("Los nombres no pueden estar vacíos.");
        }
        if (edad < 0 || edad > 120) {
            throw new IllegalArgumentException("La edad debe estar entre 0 y 120.");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo no puede estar vacío.");
        }
        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5.");
        }
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento.trim().toUpperCase();
        this.apellidos = apellidos.trim();
        this.nombres = nombres.trim();
        this.edad = edad;
        this.motivo = motivo.trim();
        this.prioridad = prioridad;
    }

    /**
     * Revisa que el número de documento cumpla con el formato
     * del tipo de documento elegido. Se puede usar sin crear un paciente.
     */
    public static void validarDocumento(String tipoDocumento, String numeroDocumento) {
        if (tipoDocumento == null || numeroDocumento == null) {
            throw new IllegalArgumentException("Debe indicar el tipo y el número de documento.");
        }
        String numero = numeroDocumento.trim();

        if (tipoDocumento.equals(DNI)) {
            if (numero.length() != 8 || !soloNumeros(numero)) {
                throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos.");
            }
        } else if (tipoDocumento.equals(CARNE_EXTRANJERIA)) {
            if (numero.length() != 9 || !soloNumeros(numero)) {
                throw new IllegalArgumentException("El carné de extranjería debe tener 9 dígitos.");
            }
        } else if (tipoDocumento.equals(PASAPORTE)) {
            if (numero.length() < 6 || numero.length() > 12 || !soloLetrasYNumeros(numero)) {
                throw new IllegalArgumentException("El pasaporte debe tener de 6 a 12 letras o números.");
            }
        } else {
            throw new IllegalArgumentException("Tipo de documento no válido.");
        }
    }

    // Devuelve true si todos los caracteres son dígitos
    private static boolean soloNumeros(String texto) {
        for (int i = 0; i < texto.length(); i++) {
            if (!Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Devuelve true si todos los caracteres son letras (sin tildes ni ñ) o dígitos
    private static boolean soloLetrasYNumeros(String texto) {
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            boolean esLetra = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
            boolean esDigito = c >= '0' && c <= '9';
            if (!esLetra && !esDigito) {
                return false;
            }
        }
        return true;
    }

    // ---------- Getters ----------
    public String getTipoDocumento() { return tipoDocumento; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public String getApellidos() { return apellidos; }
    public String getNombres() { return nombres; }
    public int getEdad() { return edad; }
    public String getMotivo() { return motivo; }
    public int getPrioridad() { return prioridad; }

    // No hay setters a propósito: si se cambiara la prioridad con el
    // paciente ya en la cola, la lista quedaría desordenada.

    @Override
    public String toString() {
        return String.format(
                "[%s %s | %s | %d años | %s | Prioridad %d]",
                tipoDocumento, numeroDocumento, apellidos + ", " + nombres, edad, motivo, prioridad);
    }
}
