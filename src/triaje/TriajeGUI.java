/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package triaje;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Interfaz gráfica simple con Swing.
 *
 * Componentes principales:
 *   • Lista para el tipo de documento (DNI, carné de extranjería o pasaporte).
 *   • Campos de texto para Documento, Apellidos, Nombres, Edad, Motivo.
 *   • Lista para la Prioridad (del 1 al 5).
 *   • Botón "Agregar Paciente" → enqueue().
 *   • Botón "Atender Paciente" → dequeue().
 *   • Área de texto (JTextArea) para mostrar la fila.
 *
 * La lógica de la cola sigue siendo la misma (sin JCF).
 */
public class TriajeGUI extends JFrame {

    // ---------- UI ----------
    private JComboBox<String> cbTipoDocumento;
    private JTextField tfDocumento;
    private JTextField tfApellidos;
    private JTextField tfNombres;
    private JTextField tfEdad;
    private JTextField tfMotivo;
    private JComboBox<Integer> cbPrioridad;
    private JTextArea taSalida;
    private JButton btnAgregar;
    private JButton btnAtender;
    private JButton btnMostrar;

    // ---------- Modelo ----------
    private final ColaPrioridad cola = new ColaPrioridad();

    public TriajeGUI() {
        super("Sistema de Triaje Médico – GUI");
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 600);
        setLocationRelativeTo(null);   // centrado en pantalla
        setVisible(true);
    }

    /** Construye y organiza los componentes Swing. */
    private void initComponents() {
        // Panel de entrada de datos
        JPanel panelDatos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.anchor = GridBagConstraints.WEST;

        // --- fila 0: Tipo de documento ---
        gbc.gridx = 0; gbc.gridy = 0;
        panelDatos.add(new JLabel("Tipo de documento:"), gbc);
        cbTipoDocumento = new JComboBox<>(new String[] {
            Paciente.DNI, Paciente.CARNE_EXTRANJERIA, Paciente.PASAPORTE
        });
        gbc.gridx = 1;
        panelDatos.add(cbTipoDocumento, gbc);

        // --- fila 1: Número de documento ---
        gbc.gridx = 0; gbc.gridy = 1;
        panelDatos.add(new JLabel("N° de documento:"), gbc);
        tfDocumento = new JTextField(15);
        gbc.gridx = 1;
        panelDatos.add(tfDocumento, gbc);

        // --- fila 2: Apellidos ---
        gbc.gridx = 0; gbc.gridy = 2;
        panelDatos.add(new JLabel("Apellidos:"), gbc);
        tfApellidos = new JTextField(20);
        gbc.gridx = 1;
        panelDatos.add(tfApellidos, gbc);

        // --- fila 3: Nombres ---
        gbc.gridx = 0; gbc.gridy = 3;
        panelDatos.add(new JLabel("Nombres:"), gbc);
        tfNombres = new JTextField(20);
        gbc.gridx = 1;
        panelDatos.add(tfNombres, gbc);

        // --- fila 4: Edad ---
        gbc.gridx = 0; gbc.gridy = 4;
        panelDatos.add(new JLabel("Edad:"), gbc);
        tfEdad = new JTextField(5);
        gbc.gridx = 1;
        panelDatos.add(tfEdad, gbc);

        // --- fila 5: Motivo ---
        gbc.gridx = 0; gbc.gridy = 5;
        panelDatos.add(new JLabel("Motivo:"), gbc);
        tfMotivo = new JTextField(20);
        gbc.gridx = 1;
        panelDatos.add(tfMotivo, gbc);

        // --- fila 6: Prioridad ---
        gbc.gridx = 0; gbc.gridy = 6;
        panelDatos.add(new JLabel("Prioridad (1-5):"), gbc);
        cbPrioridad = new JComboBox<>(new Integer[] {1, 2, 3, 4, 5});
        gbc.gridx = 1;
        panelDatos.add(cbPrioridad, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAgregar = new JButton("Agregar Paciente");
        btnAtender = new JButton("Atender Paciente");
        btnMostrar = new JButton("Mostrar Fila");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnAtender);
        panelBotones.add(btnMostrar);

        // Área de salida
        taSalida = new JTextArea(10, 40);
        taSalida.setEditable(false);
        JScrollPane scroll = new JScrollPane(taSalida);

        // Añadir listeners
        btnAgregar.addActionListener(e -> agregarPaciente());
        btnAtender.addActionListener(e -> atenderPaciente());
        btnMostrar.addActionListener(e -> actualizarSalida());

        // Layout general
        setLayout(new BorderLayout(10,10));
        add(panelDatos, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);
    }

    /** Acción del botón “Agregar Paciente”. */
    private void agregarPaciente() {
        String tipoDocumento = (String) cbTipoDocumento.getSelectedItem();
        String documento = tfDocumento.getText().trim();
        String apellidos = tfApellidos.getText().trim();
        String nombres = tfNombres.getText().trim();
        String edadStr = tfEdad.getText().trim();
        String motivo = tfMotivo.getText().trim();
        int prioridad = (Integer) cbPrioridad.getSelectedItem();

        // Validaciones básicas
        if (documento.isEmpty() || apellidos.isEmpty() || nombres.isEmpty() || edadStr.isEmpty()
                || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar el documento según su tipo
        try {
            Paciente.validarDocumento(tipoDocumento, documento);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            tfDocumento.requestFocus();
            return;
        }

        // Un mismo documento no puede estar dos veces en la fila
        if (cola.existeDocumento(tipoDocumento, documento)) {
            JOptionPane.showMessageDialog(this,
                    "Ya hay un paciente en la fila con ese documento.", "Error", JOptionPane.ERROR_MESSAGE);
            tfDocumento.requestFocus();
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "La edad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            tfEdad.requestFocus();
            return;
        }
        if (edad < 0 || edad > 120) {
            JOptionPane.showMessageDialog(this,
                    "La edad debe estar entre 0 y 120.", "Error", JOptionPane.ERROR_MESSAGE);
            tfEdad.requestFocus();
            return;
        }

        Paciente p = new Paciente(tipoDocumento, documento, apellidos, nombres, edad, motivo, prioridad);
        cola.enqueue(p);
        JOptionPane.showMessageDialog(this, "Paciente agregado exitosamente.", "Info", JOptionPane.INFORMATION_MESSAGE);
        limpiarCampos();
        actualizarSalida();
    }

    /** Acción del botón “Atender Paciente”. */
    private void atenderPaciente() {
        if (cola.estaVacia()) {
            JOptionPane.showMessageDialog(this, "No hay pacientes en la fila.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Paciente atendido = cola.dequeue();
        JOptionPane.showMessageDialog(this,
                "Se atendió al paciente:\n" + atendido,
                "Paciente atendido", JOptionPane.INFORMATION_MESSAGE);
        actualizarSalida();
    }

    /** Refresca el JTextArea con la representación textual de la cola. */
    private void actualizarSalida() {
        taSalida.setText(cola.mostrarFila());
    }

    /** Vacía los campos de texto después de agregar. */
    private void limpiarCampos() {
        cbTipoDocumento.setSelectedIndex(0);
        tfDocumento.setText("");
        tfApellidos.setText("");
        tfNombres.setText("");
        tfEdad.setText("");
        tfMotivo.setText("");
        cbPrioridad.setSelectedIndex(0);
    }

    /** Punto de entrada del programa. */
    public static void main(String[] args) {
        // Forzar el look‑and‑feel nativo (opcional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(TriajeGUI::new);
    }
}
