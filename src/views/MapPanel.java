package views;
import controllers.MapController;
import models.MapPoint;
import structures.graphs.Graph;
import structures.node.Node;

import javax.imageio.ImageIO;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class MapPanel extends JPanel {

    private BufferedImage backgroundImage;
    private final Graph<MapPoint> mapGraph;
    private MapController controller;

    private MapPoint inicioMarcado;
    private MapPoint destinoMarcado;
    private MapPoint seleccionPrincipal;
    private MapPoint seleccionSecundaria;

    private final List<MapPoint[]> aristasExploracion = new ArrayList<>();
    private final List<MapPoint[]> aristasRuta = new ArrayList<>();
    private Set<MapPoint> puntosRutaFinal = Set.of();

    private Integer ultimoClicX;
    private Integer ultimoClicY;
    private BiConsumer<Integer, Integer> onMouseMove;

    public MapPanel(String resourcePath, Graph<MapPoint> mapGraph) {
        this.mapGraph = mapGraph;

        System.out.println("[MapPanel] buscando recurso: " + resourcePath);

        try (InputStream in = MapPanel.class.getResourceAsStream(resourcePath)) {
            if (in != null) {
                backgroundImage = ImageIO.read(in);
                if (backgroundImage != null) {
                    System.out.println("[MapPanel] imagen cargada OK: "
                            + backgroundImage.getWidth() + "x" + backgroundImage.getHeight());
                } else {
                    System.err.println("[MapPanel] ImageIO.read devolvio null");
                }
            } else {
                System.err.println("[MapPanel] recurso no encontrado: " + resourcePath);
            }
        } catch (IOException e) {
            System.err.println("[MapPanel] Error leyendo la imagen: " + e.getMessage());
        }

        setPreferredSize(new java.awt.Dimension(800, 600));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ultimoClicX = e.getX();
                ultimoClicY = e.getY();
                if (controller != null) {
                    controller.onMapClick(e.getX(), e.getY());
                }
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (onMouseMove != null) {
                    onMouseMove.accept(e.getX(), e.getY());
                }
            }
        });
    }

    public void setController(MapController controller) {
        this.controller = controller;
    }

    // Métodos que llama MapController 
    
    public void setOnMouseMove(BiConsumer<Integer, Integer> onMouseMove) {
        this.onMouseMove = onMouseMove;
    }

    public void marcarSeleccionado(MapPoint principal, MapPoint secundaria) {
        this.seleccionPrincipal = principal;
        this.seleccionSecundaria = secundaria;
        repaint();
    }

    public void limpiarSeleccion() {
        this.seleccionPrincipal = null;
        this.seleccionSecundaria = null;
        repaint();
    }

    public void marcarInicio(MapPoint p) {
        this.inicioMarcado = p;
        repaint();
    }

    public void marcarDestino(MapPoint p) {
        this.destinoMarcado = p;
        repaint();
    }

    public void limpiarRecorrido() {
        aristasExploracion.clear();
        aristasRuta.clear();
        puntosRutaFinal = Set.of();
        repaint();
    }

    public void agregarAristaVisitada(MapPoint origen, MapPoint destino) {
        aristasExploracion.add(new MapPoint[]{origen, destino});
        repaint();
    }

    public void agregarAristaRuta(MapPoint origen, MapPoint destino) {
        aristasRuta.add(new MapPoint[]{origen, destino});
        repaint();
    }

    public void mostrarRutaFinal(Set<MapPoint> ruta) {
        this.puntosRutaFinal = ruta;
        aristasRuta.clear();
        List<MapPoint> ordenados = new ArrayList<>(ruta);
        for (int i = 0; i < ordenados.size() - 1; i++) {
            aristasRuta.add(new MapPoint[]{ordenados.get(i), ordenados.get(i + 1)});
        }
        repaint();
    }

    // --- Dibujo ---   
    // marca los puntos de la ruta ganadora SIN reconstruir las lineas ya dibujadas
    // (se usa al terminar la animacion progresiva, para no "resetear" el efecto)
    public void marcarPuntosRuta(Set<MapPoint> ruta) {
        this.puntosRutaFinal = ruta;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(new Color(230, 230, 230));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Color.RED);
            g2.drawString("No se cargo la imagen del mapa (ver consola)", 20, 30);
        }

        dibujarAristasGrafo(g2);
        dibujarAristasExploracion(g2);
        dibujarAristasRuta(g2);
        dibujarVistaPreviaSeleccion(g2);
        dibujarNodos(g2);
        dibujarUltimoClic(g2);
    }

    // calles del grafo tal como fueron definidas, en celeste brillante
  private void dibujarAristasGrafo(Graphics2D g2) {
    g2.setStroke(new BasicStroke(3));
    for (Node<MapPoint> nodo : mapGraph.getNodes()) {
        MapPoint origen = nodo.getData();
        for (Node<MapPoint> vecino : mapGraph.getGraph().get(nodo)) {
            MapPoint destino = vecino.getData();

            boolean esBidireccional = mapGraph.getGraph().get(vecino).contains(nodo);

            if (esBidireccional) {
                g2.setColor(new Color(0, 200, 255)); // celeste = bidireccional
            } else {
                g2.setColor(new Color(255, 100, 100)); // rojo claro = un solo sentido
            }

            g2.drawLine(origen.getX(), origen.getY(), destino.getX(), destino.getY());
        }
    }
}

    private void dibujarAristasExploracion(Graphics2D g2) {
        g2.setColor(new Color(255, 165, 0));
        g2.setStroke(new BasicStroke(4));
        for (MapPoint[] arista : aristasExploracion) {
            g2.drawLine(arista[0].getX(), arista[0].getY(), arista[1].getX(), arista[1].getY());
        }
    }

    private void dibujarAristasRuta(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(5));
        for (MapPoint[] arista : aristasRuta) {
            g2.drawLine(arista[0].getX(), arista[0].getY(), arista[1].getX(), arista[1].getY());
        }
        for (MapPoint p : puntosRutaFinal) {
            g2.fillOval(p.getX() - 7, p.getY() - 7, 14, 14);
        }
    }

    // linea punteada mostrando la union entre los 2 nodos seleccionados, ANTES de confirmar "Conectar"
    private void dibujarVistaPreviaSeleccion(Graphics2D g2) {
        if (seleccionPrincipal == null || seleccionSecundaria == null) return;
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{10, 8}, 0));
        g2.drawLine(seleccionPrincipal.getX(), seleccionPrincipal.getY(),
                seleccionSecundaria.getX(), seleccionSecundaria.getY());
    }

    private void dibujarNodos(Graphics2D g2) {
        for (Node<MapPoint> nodo : mapGraph.getNodes()) {
            MapPoint p = nodo.getData();

            if (p.equals(inicioMarcado)) {
                g2.setColor(Color.GREEN.darker());
            } else if (p.equals(destinoMarcado)) {
                g2.setColor(Color.PINK.darker());
            } else if (p.equals(seleccionPrincipal) || p.equals(seleccionSecundaria)) {
                g2.setColor(Color.BLUE);
            } else {
                g2.setColor(Color.ORANGE);
            }

            g2.fillOval(p.getX() - 9, p.getY() - 9, 18, 18);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(p.getX() - 9, p.getY() - 9, 18, 18);

            g2.setColor(Color.BLACK);
            g2.setFont(g2.getFont().deriveFont(java.awt.Font.BOLD, 12f));
            g2.drawString(p.getId(), p.getX() + 12, p.getY() - 8);
        }
    }

    private void dibujarUltimoClic(Graphics2D g2) {
        if (ultimoClicX == null || ultimoClicY == null) return;
        g2.setColor(Color.MAGENTA);
        g2.setStroke(new BasicStroke(2));
        int x = ultimoClicX, y = ultimoClicY;
        g2.drawLine(x - 10, y, x + 10, y);
        g2.drawLine(x, y - 10, x, y + 10);
        g2.drawString("(" + x + "," + y + ")", x + 12, y - 12);
    }
}