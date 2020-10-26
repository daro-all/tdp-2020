package gui;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Reloj extends JPanel { //Esto es así para que Reloj sea tratado como un panel desde la GUI.
	
	private ImageIcon[] imagenes;
	private int hs, min, seg;
	private JLabel hs_decena, hs_unidad, dp1, min_decena, min_unidad, dp2, seg_decena, seg_unidad;
	private Timer timer;
	
	public Reloj() {
		imagenes = setImagenesReloj();
		hs = min = seg = 0;
		
		generar_panel_reloj();
	
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Actualiza valores.
				if (seg < 59) {
					seg++;
				}	
				else {
					if (min < 59) {
						min++;
						seg = 0;
					}
					else {
						hs++;
						min = 0;
						seg = 0;
					}
				}
				
				hs_decena.setIcon(imagenes[hs / 10]);
				redimensionar(hs_decena, imagenes[hs / 10]);
				
				hs_unidad.setIcon(imagenes[hs % 10]);
				redimensionar(hs_unidad, imagenes[hs % 10]);
				
				min_decena.setIcon(imagenes[min / 10]);
				redimensionar(min_decena, imagenes[min / 10]);
				
				min_unidad.setIcon(imagenes[min % 10]);
				redimensionar(min_unidad, imagenes[min % 10]);
				
				seg_decena.setIcon(imagenes[seg / 10]);
				redimensionar(seg_decena, imagenes[seg / 10]);
				
				seg_unidad.setIcon(imagenes[seg % 10]);
				redimensionar(seg_unidad, imagenes[seg % 10]);
			}
		});
	} //Reloj

	//Crea un arreglo donde empareja cada dígito de 0 a 9 con una imagen que lo representa.
	//También hay una imagen para el símbolo ":"
	private ImageIcon[] setImagenesReloj() {		
		ImageIcon[] img_array = new ImageIcon[11];
		for (int i = 0; i < 10; i++) {
			img_array[i] = new ImageIcon(getClass().getResource("/img/r" + i + ".png"));
		}
		img_array[10] = new ImageIcon(getClass().getResource("/img/rdp.png"));
		return img_array;
	}
	
	private void generar_panel_reloj() {
		this.setLayout(new GridLayout(0, 8, 0, 0));
		
		hs_decena = new JLabel();
		hs_decena.setOpaque(true);
		this.add(hs_decena);
		
		hs_unidad = new JLabel();
		hs_unidad.setOpaque(true);
		this.add(hs_unidad);
		
		dp1 = new JLabel();
		dp1.setOpaque(true);
		dp1.setIcon(imagenes[10]);
		this.add(dp1);
		
		min_decena = new JLabel();
		min_decena.setOpaque(true);
		this.add(min_decena);
		
		min_unidad = new JLabel();
		min_unidad.setOpaque(true);
		this.add(min_unidad);
		
		dp2 = new JLabel();
		dp2.setOpaque(true);
		dp2.setIcon(imagenes[10]);
		this.add(dp2);
		
		seg_decena = new JLabel();
		seg_decena.setOpaque(true);
		this.add(seg_decena);
		
		seg_unidad = new JLabel();
		seg_unidad.setOpaque(true);
		this.add(seg_unidad);
	} //generar_panel_reloj
	
	//Comprender bien como funciona esto.
	private void redimensionar(JLabel label, ImageIcon img) {
		Image image = img.getImage();
		if (image != null) {  
			Image newimg = image.getScaledInstance(label.getWidth(), label.getHeight(), java.awt.Image.SCALE_SMOOTH);
			img.setImage(newimg);
			label.repaint();
		}
	}

	public void start() {
		timer.start();
	}
	
	public void stop() {
		timer.stop();
	}
	
//	public void restart() {
//		hs = min = seg = 0;
//		timer.restart();
//	}
//	
//	public int getHoras() {
//		return hs;
//	}
//	
//	public int getMinutos() {
//		return min;
//	}
//
//	public int getSegundos() {
//		return seg;
//	}
	
}