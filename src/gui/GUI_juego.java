package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import java.util.ArrayList;

import logica.*;

@SuppressWarnings("serial")
public class GUI_juego extends JFrame {

	//DEBEN SER GLOBALES ???
	private static Juego juego;
	private JPanel contentPane;
	Color color_celda_pista = new Color(125, 206, 250);
	Color color_celda_normal = new Color(228, 227, 198);
	Color color_celda_repetida = new Color(250, 108, 80);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_juego frame = new GUI_juego();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI_juego() {
		setResizable(false);
		setTitle("Sudoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(80, 80, 550, 550); //Tamaño de la ventana: MODIFICAR SI HACE FALTA...
		
		contentPane = new JPanel(); //Panel principal (contiene todo).
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); //CAMBIAR TIPO DE BORDE.
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		Reloj panel_reloj = new Reloj();
		contentPane.add(panel_reloj, BorderLayout.NORTH);
		
		JPanel panel_tablero = new JPanel();
		contentPane.add(panel_tablero, BorderLayout.CENTER);
		panel_tablero.setLayout(new GridLayout(0, 9, 2, 2));
		panel_tablero.setBackground(Color.BLACK);
		panel_tablero.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));

		JPanel panelInferior = new JPanel();
		contentPane.add(panelInferior, BorderLayout.SOUTH);
		panelInferior.setLayout(new BorderLayout(0, 0));
		
		JPanel panelBotones = new JPanel();
		panelInferior.add(panelBotones, BorderLayout.NORTH);
		
		JButton btnReiniciar = new JButton("Reiniciar tablero"); //PENDIENTE
		btnReiniciar.setVisible(false);
		btnReiniciar.setEnabled(false);
		btnReiniciar.setMnemonic('R');
		panelBotones.add(btnReiniciar);
		
		JButton btnAbandonar = new JButton("Abandonar y salir"); //debería detener reloj acá?
		btnAbandonar.setMnemonic('A');
		btnAbandonar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panelBotones.add(btnAbandonar);
		
		JButton btnControlar = new JButton("Comprobar resoluci\u00F3n");
		btnControlar.setMnemonic('C');
		btnControlar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel_reloj.stop();
				
				String resultado = juego.comprobar_resolucion();
				if (resultado.equals("INCOMPLETO")) {
					JOptionPane.showMessageDialog(null, "Hay celdas sin completar!", "SUDOKU INCOMPLETO", JOptionPane.WARNING_MESSAGE);
					panel_reloj.start(); //Reanuda.
				}
				else {
					if (resultado.equals("DISTINTOS")) {
						JOptionPane.showMessageDialog(null, "La solución no es correcta.", "SUDOKU CON ERRORES", JOptionPane.ERROR_MESSAGE);
						panel_reloj.start(); //Reanuda.
					}
					else { //resultado.equals("IGUALES")
						deshabilitar_tablero(panel_tablero);
						JOptionPane.showMessageDialog(null, "La solución es correcta!", "SUDOKU RESUELTO", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		panelBotones.add(btnControlar);
		
		//Poner botón para mostrar solución en cualquier momento? (como otra forma de terminar el juego)
		//Poner un botón para obtener nuevas pistas? (por ej, que aparezca aleatoriamente una celda con su valor correcto)
		
		JPanel panelMensajes = new JPanel();
		panelInferior.add(panelMensajes, BorderLayout.SOUTH);
		
		JLabel lblMensajes = new JLabel("");
		lblMensajes.setHorizontalAlignment(SwingConstants.CENTER);
		panelMensajes.add(lblMensajes);

		//Creados y agregados a la GUI los componentes principales, creo una instancia del juego, inicia reloj:
		try {
			juego = new Juego(); //Puede generar excepción en relación al archivo de texto desde el cual se crea.
			generar_GUI_del_tablero(panel_tablero);
			panel_reloj.start(); //acá ?
		} catch (TextFileException e) {
			lblMensajes.setText( e.getMessage() );
			btnAbandonar.setText("SALIR");
			btnReiniciar.setEnabled(false);
			btnControlar.setEnabled(false);
		}
	} //GUI_juego

	private void generar_GUI_del_tablero(JPanel panel_tablero) {
		for (int i = 0; i < juego.getDimension(); i++) {
			for(int j = 0; j < juego.getDimension(); j++) {
				Celda c = juego.getCelda(i, j);
				ImageIcon imgCelda = c.getImagenCelda().getImagen();
				
				JLabel label_celda = new JLabel();
				
				if (i == 2 || i == 5) { //Borde inferior para paneles.
					label_celda.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
				}
				if (j == 2 || j == 5) {
					if (i == 2 || i == 5) { //Borde para esquinas inferiores derechas de paneles.
						label_celda.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 3, Color.BLACK));
					}
					else { //Borde derecho para paneles.
						label_celda.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, Color.BLACK));
					}
				}
				
				label_celda.setOpaque(true);
				//Comprender bien como funciona esto:
				label_celda.addComponentListener(new ComponentAdapter() {
					@Override
					public void componentResized(ComponentEvent e) {
						redimensionar(label_celda, imgCelda);
						label_celda.setIcon(imgCelda);
					}
				});
				
				if (c.es_una_pista()) {
					label_celda.setBackground(color_celda_pista);
				}
				else {
					label_celda.setBackground(color_celda_normal);
					label_celda.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							restaurarColores();
							c.actualizar();
							ArrayList<Celda> repetidos = juego.chequearRepetidos(c);
							if (repetidos.size() > 0) {
								c.getComponente().setBackground(color_celda_repetida);
							
								Celda celda_repetida;
								for (Celda celda : repetidos) {
									celda_repetida = juego.getCelda( celda.getFila(), celda.getColumna() );
									celda_repetida.getComponente().setBackground(color_celda_repetida);
								}
							}
							redimensionar(label_celda, imgCelda);
						}
					});
				}
				
				c.setComponente(label_celda);
				panel_tablero.add(label_celda);
			}
		}
	} //generar_GUI_del_tablero
	
	//Comprender bien como funciona esto.
	private void redimensionar(JLabel label, ImageIcon img) {
		Image image = img.getImage();
		if (image != null) {  
			Image newimg = image.getScaledInstance(label.getWidth(), label.getHeight(), java.awt.Image.SCALE_SMOOTH);
			img.setImage(newimg);
			label.repaint();
		}
	}

	//Setea el color de cada celda del tablero a su color original (una celda pierde su color original
	//si es marcada como repetida/en conflicto con la celda que está siendo actualizada).
	//Podría buscar la forma de "saber" que celdas cambiaron de color para restaurar solo esas celdas
	//y no tener que recorrer todo el tablero.
	private void restaurarColores() {
		Celda c;
		for (int i = 0; i < juego.getDimension(); i++) {
			for (int j = 0; j < juego.getDimension(); j++) {
				c = juego.getCelda(i, j);
				Color color = c.es_una_pista() ? color_celda_pista : color_celda_normal;
				c.getComponente().setBackground(color);
			}
		}
	}
	
	private void deshabilitar_tablero(JPanel panel) {
	    for (Component component : panel.getComponents()) {
	    	//Si se deshabilita el tablero pero no los oyentes de los label, se puede seguir cambiando el color de las celdas.
	    	//Esto es para evitar eso.
	    	if (component instanceof JLabel) {
	    		java.awt.event.MouseListener[] ml = component.getMouseListeners();
	    		if (ml.length > 0) {
	    			component.removeMouseListener(ml[0]);
	    		}
	        }
	    	
	        component.setEnabled(false);
	    }
	}
	
//	private void rehabilitar_tablero(JPanel panel) {
//	    for (Component component : panel.getComponents()) {
//	        component.setEnabled(true);
//	    }
//	}
	
}