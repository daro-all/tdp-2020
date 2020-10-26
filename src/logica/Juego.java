package logica;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

/*
 * Cómo variamos los path/juegos ???
 * - le damos al usuario la opción de ingresar un path ?
 * - permitimos que pueda editar un tablero ?
 * - le damos la opción de elegir entre varios tableros pre-establecidos ?
 * 
 * Faltaría considerar la opción de que se pueda variar dificultad del juego, mediante:
 * - dimensiones variables.
 * - cantidad de pistas mostradas.
 * - jugar con ambos factores a la vez.
 */

public class Juego {
	
	//ruta relativa. Estoy parado en proyecto-2.
	private String path = "src/txt/sudoku1.txt"; 
//	private String path = "src/txt/sudoku2.txt";
	
	private int dimension = 9; //TABLERO CLASICO POR DEFECTO.
	private int cant_minima_pistas = 20;
	private int cant_maxima_pistas = 35;
	
	private int[][] tablero; //Matriz en la que se vuelca el contenido del archivo de texto que contiene un juego. Servirá para posterior control del resultado.
	private Celda[][] tablero_para_GUI; //Matriz que contiene un juego válido, apta para interactuar con la GUI. El jugador modifica las celdas durante el juego.
	/*
	 * OPCIONAL:
	 * private cant_pistas_mostradas;
	 * Este dato sería seteado por generar_tablero_apto_para_GUI() y luego accesado desde la GUI para mostrarlo en la ventana del mismo. 
	 */

	public Juego() throws TextFileException {
		tablero = generarMatriz();
		//Muchas dudas con esto (a nivel general, no solo con este servicio):
		//Trabajo directamente con los atributos o los paso como parámetros? --> generarMatriz(dimension, path);
		if (!esTableroValido()) {
			throw new TextFileException("error : text file : \"El archivo no contiene un juego válido.\"");
			//Para generar un msj que sea mostrado en la GUI usando más de una línea, usar tags HTML.
		}
		else {
			tablero_para_GUI = generar_tablero_apto_para_GUI();
		}
	}
	
	//Si el archivo tiene más de DIMENSION filas o columnas no importa (ignora el contenido extra). <-- lo trato como un archivo incorrecto? aviso al usuario?
	//Lo que importa es que el archivo tenga al menos DIMENSION filas y DIMENSION columnas correctas.
	//Se retorna una matriz de DIMENSION x DIMENSION dígitos entre 1 y 9.
	private int[][] generarMatriz() throws TextFileException{
		File f = new File(path);
		Scanner sFile; //Para el archivo.
		Scanner sLine; //Para una línea de texto.
		String linea;
		int numero;
		int[][] matriz = new int[dimension][dimension]; //No hace falta inicializar.
		int fila = 0, col;
		
		try {
			sFile = new Scanner(f);
			while (sFile.hasNextLine() && fila < dimension) {
				linea = sFile.nextLine(); //Recupera una línea de texto del archivo.
				sLine = new Scanner(linea);
				sLine.useDelimiter(" "); //El delimitador para recuperar números será un espacio simple.
				col = 0;
				while (sLine.hasNext() && col < dimension) {
					numero = Integer.parseInt( sLine.next() ); //Recupera un número de la línea de texto, lo convierte a int.
					if (numero < 1 || numero > 9) {
						sLine.close();
						sFile.close();
						throw new TextFileException("error : text file : \"Números fuera de rango.\"");
					}
					else {
						matriz[fila][col] = numero;
						col++;
					}
				}
				sLine.close();
				
				//Al terminar de recorrer una línea controlo haber obtenido la cantidad de números (columnas) correspondientes.
				if (col < dimension) {
					sFile.close();
					throw new TextFileException("error : text file : \"Faltan números para el tablero.\"");
				}
				
				fila++;
			}
			sFile.close();
			
			//Al terminar de recorrer el archivo controlo haber obtenido la cantidad de filas correspondientes.
			if (fila < dimension) {
				throw new TextFileException("error : text file : \"Faltan números para el tablero.\"");
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

		return matriz;
	} //generarMatriz
	
	//Verifica que la matriz con el juego obtenido de un archivo de texto contenga un juego válido.
	//La matriz ya tiene DIMENSION x DIMENSION valores, todos en el rango 1..9.
	//Método: se chequea que cada fila tenga todos valores diferentes entre sí. Lo mismo para las columnas.
	//Controla simultáneamente una fila y una columna.
	//Si pasa estos chequeos... hay alguna otra forma en que pueda ser una solución no-válida ???
	//Se debe hacer este control en el servicio que lee del archivo de texto??? Es posible???
	private boolean esTableroValido() {
		ArrayList<Integer> valores_de_fila = new ArrayList<Integer>(dimension);
		ArrayList<Integer> valores_de_columna = new ArrayList<Integer>(dimension);
		boolean esValido = true;
		int nFil, nCol;
		int i = 0, j;
		while (i < dimension && esValido) {
			j = 0;
			while (j < dimension && esValido) {
				nFil = tablero[i][j]; //Avanza a través de una fila.
				nCol = tablero[j][i]; //Avanza a través de una columna.
				if (valores_de_fila.contains(nFil) || valores_de_columna.contains(nCol)){
					esValido = false;
				}
				else {
					valores_de_fila.add(nFil);
					valores_de_columna.add(nCol);
					j++;
				}
			}
			valores_de_fila.clear();
			valores_de_columna.clear();
			i++;
		}
		return esValido;
	} //esTableroValido
	
	//Genera un tablero con el juego, pero apto para trabajar con él desde la GUI.
	private Celda[][] generar_tablero_apto_para_GUI(){
		int cant_pistas;
		Random rand = new Random();
		int numero_random;
		boolean mostrar;
		int valor;
		Celda[][] tablero_para_GUI = new Celda[dimension][dimension];
		
		do {
			cant_pistas = 0;
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					//Con estas 2 líneas se varía la cantidad de pistas mostradas.
					//Así como está, hay un 40% de chance de que una celda sea pista. El tablero suele mostrar unas ~30.
					numero_random = rand.nextInt(5);
					mostrar = (numero_random == 0 || numero_random == 1) ? true : false;
					
					if (mostrar) {
						valor = tablero[i][j];
						cant_pistas++;
					}
					else {
						valor = 0;
					}
					tablero_para_GUI[i][j] = new Celda(valor, i, j);
				}
			}
		} while (cant_pistas < cant_minima_pistas || cant_pistas > cant_maxima_pistas);
		
		return tablero_para_GUI;
	} //generar_tablero_apto_para_GUI
	
	public int getDimension() {
		return dimension;
	}
	
	//get para tableros ?

//	public int getValor(int fila, int columna) {
//		return tablero_para_GUI[fila][columna].getValor();
//	}
	
	public Celda getCelda(int fila, int columna) {
		return tablero_para_GUI[fila][columna];
	}
	
//	public void actualizarCelda(Celda c) {
//		c.actualizar();
//	}

	//Recibe una celda y genera un ArrayList con celdas de la misma fila, con las de la misma columna, y con las del mismo panel, que tienen el mismo valor.
	public ArrayList<Celda> chequearRepetidos(Celda c) {
		ArrayList<Celda> repetidos = new ArrayList<Celda>();
		chequearRepetidosEnFila(c, repetidos);
		chequearRepetidosEnColumna(c, repetidos);
		chequearRepetidosEnPanel(c, repetidos);
		return repetidos;
	}
	
	//Chequeo la fila en la que se encuentra c.
	private void chequearRepetidosEnFila(Celda c, ArrayList<Celda> repetidos) {
		Celda celda;
		int fila_de_c = c.getFila();
		int columna_de_c = c.getColumna();
		
		for (int j = 0; j < dimension; j++) {
			if (j != columna_de_c) {
				celda = tablero_para_GUI[fila_de_c][j];
				if (celda.esta_activa() && celda.getValor() == c.getValor()) { //Sea una pista o no, lo que importa es que la celda esté activa (visible).
					repetidos.add(celda);
				}
			}
		}
	}
	
	//Chequeo la columna en la que se encuentra c.
	private void chequearRepetidosEnColumna(Celda c, ArrayList<Celda> repetidos) {
		Celda celda;
		int fila_de_c = c.getFila();
		int columna_de_c = c.getColumna();
		
		for (int i = 0; i < dimension; i++) {
			if (i != fila_de_c) {
				celda = tablero_para_GUI[i][columna_de_c];
				if (celda.esta_activa() && celda.getValor() == c.getValor()) {
					repetidos.add(celda);
				}
			}
		}
	}
	
	//Chequeo PANEL en que se encuentra c.
	private void chequearRepetidosEnPanel(Celda c, ArrayList<Celda> repetidos) {
		Celda celda;
		//Fila y columna inicial son las coordenadas de la celda ubicada en la esquina superior izquierda del panel en el que se encuentra c.
		int fila_inicial = c.getFila() - c.getFila() % 3;
		int col_inicial = c.getColumna() - c.getColumna() % 3;

		for (int i = fila_inicial; i < fila_inicial + 3; i++) {
			for (int j = col_inicial; j < col_inicial + 3; j++) {
				celda = tablero_para_GUI[i][j];
				if (celda != c) {
					if (celda.esta_activa() && celda.getValor() == c.getValor()) {
						//Controlo que no esté en los repetidos ya. <-- probablemente innecesario/irrelevante.
						//Esto puede darse cuando hay un conflicto con una celda de la misma fila/columna y que además está en el mismo panel.
						if (!repetidos.contains(celda)) {
							repetidos.add(celda);
						}
					}
				}
			}
		}
	}
	
	/*
	 * Compara el tablero con el que viene jugando el usuario contra el tablero con el juego original.
	 * Tres resultados posibles:
	 * - INCOMPLETO: si hay una celda que el usuario dejó sin elegir número.
	 * - DISTINTOS: si el usuario completó el tablero pero no con la solución correcta.
	 * - IGUALES: el usuario resolvió correctamente el juego. 
	 */
	public String comprobar_resolucion() {
		boolean iguales = true;
		String resultado = "";
		int i = 0, j;
		
		while (i < dimension && iguales) {
			j = 0;
			while (j < dimension && iguales) {
				if (tablero_para_GUI[i][j].getValor() == 0) {
					iguales = false;
					resultado = "INCOMPLETO";
				}
				else {
					if (tablero_para_GUI[i][j].getValor() != tablero[i][j]) {
						iguales = false;
						resultado = "DISTINTOS";
					}
				}
				j++;
			}
			i++;
		}
		if (iguales) {
			resultado = "IGUALES";
		}
		return resultado;
	} //comprobar_resolucion
	
}