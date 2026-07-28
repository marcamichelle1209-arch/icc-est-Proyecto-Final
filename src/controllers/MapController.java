package controllers;

import javax.swing.JOptionPane;

import models.MapPoint;

public class MapController {
    private boolean agregraNodo = false;
    private MapPoint primerSeleccionado = null;
    private MapPoint segundoSeleccionado = null;

    public void activarAgregarNodo(){
        agregarNodo = true;
        cancelarSeleccion();
        JOptionPane.showMessageDialog(null, );
    }

}
