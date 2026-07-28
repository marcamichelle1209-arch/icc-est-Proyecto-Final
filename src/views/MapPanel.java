package views;

import controllers.MapController;
import models.MapPoint;
import structures.graphs.Graph;
import structures.node.Node;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MapPanel extends JPanel {

    private BufferedImage backgroundImage;
    private final Graph<MapPoint> mapGraph;
    private MapController controller;

    // Estado visual
    private MapPoint inicioMarcado;
    private MapPoint destinoMarcado;
    private MapPoint seleccionPrincipal;
    private MapPoint seleccionSecundaria;
    private final List<MapPoint> visitados = new ArrayList<>();
    private Set<MapPoint> rutaFinal = Set.of();

    public MapPanel(String imagePath, Graph<MapPoint> mapGraph) {
        this.mapGraph = mapGraph;

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/resources/maps/barcelonaMap.jpeg"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el mapa: " + imagePath);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null) {
                    controller.onMapClick(e.getX(), e.getY());
                }
            }
        });
    }

    public void setController(MapController controller) {
        this.controller = controller;
    }

    // Métodos que llama MapController 
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
        visitados.clear();
        rutaFinal = Set.of();
        repaint();
    }

    public void agregarNodoVisitado(MapPoint punto) {
        visitados.add(punto);
        repaint();
    }

    public void mostrarRutaFinal(Set<MapPoint> ruta) {
        this.rutaFinal = ruta;
        repaint();
    }

    // --- Dibujo ---   

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        dibujarAristas(g2);
        dibujarNodos(g2);
        dibujarVisitados(g2);
        dibujarRuta(g2);
    }

    private void dibujarAristas(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(2));
        for (Node<MapPoint> nodo : mapGraph.getNodes()) {
            MapPoint origen = nodo.getData();
            for (Node<MapPoint> vecino : mapGraph.getGraph().get(nodo)) {
                MapPoint destino = vecino.getData();
                g2.drawLine(origen.getX(), origen.getY(), destino.getX(), destino.getY());
            }
        }
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

            g2.fillOval(p.getX() - 8, p.getY() - 8, 16, 16);
            g2.setColor(Color.BLACK);
            g2.drawString(p.getId(), p.getX() + 10, p.getY() - 8);
        }
    }

    private void dibujarVisitados(Graphics2D g2) {
        g2.setColor(new Color(255, 165, 0, 150));
        for (MapPoint p : visitados) {
            g2.fillOval(p.getX() - 10, p.getY() - 10, 20, 20);
        }
    }

    private void dibujarRuta(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(3));
        for (MapPoint p : rutaFinal) {
            g2.fillOval(p.getX() - 6, p.getY() - 6, 12, 12);
        }
    }
}