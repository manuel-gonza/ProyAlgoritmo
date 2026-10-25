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
 *   • Campos de texto para Nombre, Edad, Motivo, Prioridad.
 *   • Botón "Agregar Paciente" → enqueue().
 *   • Botón "Atender Paciente" → dequeue().
 *   • Área de texto (JTextArea) para mostrar la fila.
 *
 * La lógica de la cola sigue siendo la misma (sin JCF).
 */
public class TriajeGUI extends JFrame {

    // ---------- UI ----------
    private JTextField tfNombre;
    private JTextField tfEdad;
    private JTextField tfMotivo;
    private JTextField tfPrioridad;
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
        setSize(500, 400);
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

        // --- fila 0: Nombre ---
        gbc.gridx = 0; gbc.gridy = 0;
        panelDatos.add(new JLabel("Nombre:"), gbc);
        tfNombre = new JTextField(15);
        gbc.gridx = 1;
        panelDatos.add(tfNombre, gbc);

        // --- fila 1: Edad ---
        gbc.gridx = 0; gbc.gridy = 1;
        panelDatos.add(new JLabel("Edad:"), gbc);
        tfEdad = new JTextField(5);
        gbc.gridx = 1;
        panelDatos.add(tfEdad, gbc);

        // --- fila 2: Motivo ---
        gbc.gridx = 0; gbc.gridy = 2;
        panelDatos.add(new JLabel("Motivo:"), gbc);
        tfMotivo = new JTextField(20);
        gbc.gridx = 1;
        panelDatos.add(tfMotivo, gbc);

        // --- fila 3: Prioridad ---
        gbc.gridx = 0; gbc.gridy = 3;
        panelDatos.add(new JLabel("Prioridad (1‑5):"), gbc);
        tfPrioridad = new JTextField(3);
        gbc.gridx = 1;
        panelDatos.add(tfPrioridad, gbc);

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
        String nombre = tfNombre.getText().trim();
        String edadStr = tfEdad.getText().trim();
        String motivo = tfMotivo.getText().trim();
        String prioridadStr = tfPrioridad.getText().trim();

        // Validaciones básicas
        if (nombre.isEmpty() || edadStr.isEmpty() || motivo.isEmpty() || prioridadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int edad, prioridad;
        try {
            edad = Integer.parseInt(edadStr);
            prioridad = Integer.parseInt(prioridadStr);
            if (prioridad < 1 || prioridad > 5) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Edad debe ser un entero y Prioridad entre 1 y 5.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Paciente p = new Paciente(nombre, edad, motivo, prioridad);
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
        tfNombre.setText("");
        tfEdad.setText("");
        tfMotivo.setText("");
        tfPrioridad.setText("");
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
