package logica;

import java.awt.Component;

/*
 * Almacena todo el contenido relacionado a una celda:
 * - un n�mero de un d�gito y su imagen asociada.
 * - sus coordenadas (fila y columna).
 * - si se trata de una celda que contiene una pista.
 * - si se trata de una celda que est� activa (es decir, mostrando el valor almacenado).
 * - el componente que la representa en la GUI.
 * 
 * En ter�a, con guardar el valor y la imagen deber�a alcanzar, yo agregue informaci�n extra tal vez de forma innecesaria.
 * Eso se nota especialmente con el atributo Component. Creo que estoy mezclando con cosas de la GUI.
 * De todas formas... Originalmente guardaba un atributo de tipo JLabel, lo reemplac� por Component por si la forma de
 * representar una celda en la GUI cambia (a un JButton, por ejemplo). Igual, este cambio no se si evitar�a
 * conflictos si se diese esa situaci�n.
 * 
 * Una celda se crea una �nica vez, al inicio del juego. Luego se trabaja modificando su estado interno.
 */
public class Celda {
	private int valor; //Integer
	private EntidadGrafica imagenCelda;
	private int fila, columna;
	private boolean es_una_pista;
	private boolean activa; //false si valor == 0 (celda inactiva), true si valor != 0 (celda activa). 
	private Component componente;
	
	public Celda(int valor, int fila, int columna) {
		this.valor = valor;
		imagenCelda = new EntidadGrafica();
		imagenCelda.setImagen(this.valor);
		this.fila = fila;
		this.columna = columna;
		
		//Si al crear una celda el valor es != 0, la celda almacena una pista, lo que significa que el contenido de la celda
		//no puede ser alterado.
		es_una_pista = this.valor != 0;
		
		//Al momento de la creaci�n, estos estados son equivalentes. Diferencia:
		//Celda que es pista --> est� activa y no puede ser modificada.
		//Celda que no es pista --> su status de activa puede variar, al igual que el contenido.
		activa = es_una_pista;
		
		componente = null;
	}
	
	//Actualiza valor, imagen y status de activa para la celda.
	//La actualizaci�n se da avanzando de forma autom�tica sobre valor.
	//Solo se pueden actualizar celdas que no son pista (quien usa este servicio debe controlar eso).
	public void actualizar() {
		if (valor == 9) {
			valor = 0;
			activa = false;
		}
		else {
			valor++;
			activa = true;
		}
		imagenCelda.setImagen(valor);
	}
	
	public int getValor() {
		return valor;
	}
	
	public EntidadGrafica getImagenCelda() {
		return imagenCelda;
	}

	public int getFila() {
		return fila;
	}
	
	public int getColumna() {
		return columna;
	}
	
	public boolean es_una_pista() {
		return es_una_pista;
	}

	public boolean esta_activa() {
		return activa;
	}
	
	public void setComponente(Component componente) {
		this.componente = componente;
	}
	
	public Component getComponente() {
		return componente;
	}
	
}