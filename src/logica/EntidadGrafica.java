package logica;

import javax.swing.ImageIcon; //entender bien su utilidad.

public class EntidadGrafica {
	private ImageIcon imagen;
	private String[] imagenes;
	
	//Hacer constructor que reciba valor para no tener que setear la imagen aparte ?
	public EntidadGrafica() {
		imagen = new ImageIcon();
		//imagenes = new String[] {"/img/1.png", "/img/2.png", "/img/3.png", "/img/4.png", "/img/5.png", "/img/6.png", "/img/7.png", "/img/8.png", "/img/9.png"};
		
		//ASI NO ANDA, ES ALGO CON EL PATH, NO ENTIENDO... (emulé lo que hice para recuperar el txt del juego y acá falló)
		//imagenes = new String[] {"src/img/1.png", "src/img/2.png", "src/img/3.png", "src/img/4.png", "src/img/5.png", "src/img/6.png", "src/img/7.png", "src/img/8.png", "src/img/9.png"};

		//imagen.setImage(null); ???
		
		//Esta es una manera más eficiente de crear el arreglo:
		imagenes = setArregloDeImagenes(); //estoy usando servicios de la clase, no se supone que esto es desaconsejado?
	}
	
	//Crea un arreglo donde empareja cada dígito de 0 a 9 con una imagen que lo representa.
	//Para el caso del 0, se trata de una imagen vacía.
	private String[] setArregloDeImagenes() {
		String[] toReturn = new String[10];
		for (int i = 0; i <= 9; i++)
			toReturn[i] = "/img/" + i + ".png";
		return toReturn;
	}

	//valor es un dígito entre 0 y 9.
	//Validar rango ???
	//Asociamos a esta entidad gráfica el número en formato imagen correspondiente al dígito recibido.
	//Si se recibe, por ej., valor  = 8, se setea esta entidad con la imagen de un número 8.
	//Si se recibe valor = 0, se setea la imagen con una imagen vacía.
	public void setImagen(int valor) {
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagenes[valor]));
		imagen.setImage(imageIcon.getImage());
		//Se hace así porque al crear el ImageIcon, se le pasa una URL (que es la que obtiene el getResource),
		//pero para setear la ImageIcon el parámetro debe ser algo de tipo Image.
		//Por eso es que esto no es válido:
		//imagen.setImage(getClass().getResource(imagenes[indice]));
	
		/*
		ESTO SERÍA UN CONTROL EXTRA POR SI EL RECURSO NO ES ENCONTRADO:
		
		java.net.URL imgURL = getClass().getResource(imagenes[indice]);
	    if (imgURL != null) {
	    	System.out.println("encontrada!"); //O setear la img de una.
	    } else {
	        System.out.println("Couldn't find file"); //O lanzar exc. o algo similar.
	    }
	    */
	    
		//Comprender bien el uso/aparición de getClass acá.
	}
	
	public ImageIcon getImagen() {
		return imagen;
	}
	
	//NECESARIOS ?
//	public void setImagenes(String[] imagenes) {
//		this.imagenes = imagenes;
//	}
//	
//	public String[] getImagenes() {
//		return imagenes;
//	}
	
}