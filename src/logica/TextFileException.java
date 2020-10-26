package logica;

/**
 * Modela la excepción ante un error en la carga del archivo de texto que contiene un juego.
 * @author Dario A. Leal
 */
@SuppressWarnings("serial")
public class TextFileException extends Exception {

	/**
	 * Inicializa la excepción indicando el origen del error.
	 * @param msg Especifica información adicional acerca de la excepción.
	 */
	public TextFileException(String msg){
		super(msg);
	}

}