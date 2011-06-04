
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.PriorityQueue;

import javax.swing.JApplet;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class RecorridoAncho extends JApplet {

	JLabel label = new JLabel();

	JDialog dialog = new JDialog();

	JTextField field = new JTextField();

	public String getAppletInfo() {
		return "Programa desarrollado para la materia de"
				+ "Ciencias II por:\n"
				+ "- Camilo Hernando Nova, Julian Felipe Pinzon"
				+ " y Jairo Alexander Pineda";
	}

	public void init() {
		field.setPreferredSize(new Dimension(50, 20));
		field.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					dialog.setVisible(false);
			}
		});

		label.setText("Numero de Vertices");
		dialog.add(label, BorderLayout.WEST);
		dialog.add(field, BorderLayout.EAST);
		dialog.setSize(180, 90);

		dialog.setLocation(300, 300);
		dialog.setModal(true);
		dialog.setVisible(true);

		int a;
		try {
			a = Integer.parseInt(field.getText());
		} catch (NumberFormatException e) {
			a = 0;
		}
		Grafo grafo[] = new Grafo[a + 1];
		grafo[0] = new Grafo();
		grafo[0].vertice = 0;
		grafo[0].conexos = new Nodo();

		Nodo nodoTemp;
		for (int i = 1; i < grafo.length; i++) {
			grafo[i] = new Grafo();
			grafo[i].vertice = i;
			grafo[i].conexos = new Nodo();
			nodoTemp = grafo[i].conexos;

			label.setText("Adyacente a " + i);
			field.setText("");
			dialog.setVisible(true);

			String[] cad = field.getText().split(",");
			for (int j = 0; j < cad.length; j++) {
				try {
					nodoTemp.valor = Integer.parseInt(cad[j]);
					if (j != cad.length - 1) {
						nodoTemp.sig = new Nodo();
						nodoTemp = nodoTemp.sig;
					}
				} catch (NumberFormatException e) {
					a = 0;
				}
			}
		}

		//----------------------FIN INSERTAR
		// DATOS--------------------------------//

		label.setText("Vertice Inicial");
		field.setText("");
		dialog.setVisible(true);

		int vertice;
		try {
			vertice = Integer.parseInt(field.getText());
		} catch (NumberFormatException e) {
			vertice = 0;
		}
		int secuencia = 1;
		int aux = 0;

		grafo[vertice].padre = 0;
		grafo[vertice].secuencia = secuencia;
		secuencia++;
		PriorityQueue cola = new PriorityQueue();
		cola.add(Integer.valueOf(vertice));

		do {
			vertice = ((Integer) cola.poll()).intValue();
			nodoTemp = grafo[vertice].conexos;

			while (nodoTemp != null) {
				aux = nodoTemp.valor;

				if (grafo[aux].secuencia == 0) {
					grafo[aux].secuencia = secuencia;
					secuencia++;
					grafo[aux].padre = vertice;
					cola.add(Integer.valueOf(aux));
				}
				nodoTemp = nodoTemp.sig;
			}

		} while (!cola.isEmpty());

		Graph graph = new Graph(grafo);
		graph.setBackground(Color.WHITE);

		add(graph);
		setSize(400, 600);
	}

	class Grafo {
		int vertice;

		int padre;

		int secuencia;

		Nodo conexos;
	}

	class Nodo {
		int valor;

		Nodo sig;
	}

	class Graficados {
		String numero;

		int x;

		int y;

		Graficados sig;
	}

	class Graph extends Canvas {

		private final Grafo grafo[];

		public Graph(Grafo grafo[]) {
			this.grafo = grafo;
		}

		public void paint(Graphics g) {
			g.setColor(Color.BLACK);
			g.drawString("Desarrollado por Camilo Nova", 100, 30);

			int posicionX = 200;
			int posicionY = 80;
			int nodo = 0;

			//para guardar los hijos
			Nodo hijos[] = new Nodo[grafo.length];
			for (int i = 0; i < hijos.length; i++)
				hijos[i] = new Nodo();

			for (int i = 1; i < grafo.length; i++) {
				Nodo aux = hijos[i];
				for (int j = 0; j < grafo.length; j++)
					if (grafo[i].vertice == grafo[j].padre) {
						aux.valor = j;
						aux.sig = new Nodo();
						aux = aux.sig;
					}
			}

			Graficados graficados = new Graficados();
			Graficados cabeza = graficados;
			for (int i = 1; i < grafo.length; i++)
				if (grafo[i].padre == 0) {
					String num = String.valueOf(grafo[i].vertice);
					g.drawOval(posicionX - 8, posicionY - 15, 20, 20);
					g.setColor(Color.RED);
					g.drawString(num, posicionX, posicionY);
					g.setColor(Color.BLACK);
					graficados.numero = num;
					graficados.x = posicionX + 3;
					graficados.y = posicionY - 3;
					graficados.sig = new Graficados();
					graficados = graficados.sig;
				}

			posicionX = 50;
			for (int i = 1; i < hijos.length; i++) {
				if (hijos[i].sig != null)
					posicionY += 50;
				while (hijos[i].sig != null) {
					posicionX += 50;
					String num = String.valueOf(hijos[i].valor);

					int padre = 0;
					for (int k = 1; k < grafo.length; k++)
						if (String.valueOf(grafo[k].vertice).equals(num))
							padre = grafo[k].padre;

					// Pintamos las lineas
					Graficados aux = cabeza;
					while (aux.sig != null) {
						g.drawOval(posicionX - 8, posicionY - 15, 20, 20);
						if (aux.numero.equals(String.valueOf(padre)))
							g.drawLine(aux.x, aux.y + 7, posicionX - 8 + 10,
									posicionY - 15);
						aux = aux.sig;
					}

					g.setColor(Color.RED);
					g.drawString(num, posicionX, posicionY);
					g.setColor(Color.BLACK);
					graficados.numero = num;
					graficados.x = posicionX;
					graficados.y = posicionY;
					graficados.sig = new Graficados();
					graficados = graficados.sig;

					hijos[i] = hijos[i].sig;
				}
				posicionX = 50;
			}
		}
	}
}