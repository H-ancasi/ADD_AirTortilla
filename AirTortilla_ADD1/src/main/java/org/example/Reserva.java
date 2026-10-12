package org.example;

import java.time.LocalDate;

public class Reserva {

    private String id;
    private String localizador;
    private String letraOrigen;
    private String letraDestino;
    private String nombre;
    private LocalDate fechaReserva;


    public Reserva(String idBase, String localizadorBase, String origenBase, String destinoBase, String nombreBase, LocalDate fechaBase) {

        id=idBase;
        localizador=localizadorBase;
        letraOrigen=origenBase;
        letraDestino=destinoBase;
        nombre=nombreBase;
        fechaReserva=fechaBase;

    }


    public String getId() {
        return id;
    }
    public String getLocalizador() {
        return localizador;
    }
    public String getLetraOrigen() {
        return letraOrigen;
    }
    public String getLetraDestino() {
        return letraDestino;
    }
    public String getNombre() {
        return nombre;
    }
    public LocalDate getFechaReserva() {
        return fechaReserva;
    }



    public void setId(String newId) {
        id = newId;
    }
    public void setLocalizador(String newLocalizador) {
        localizador = newLocalizador;
    }
    public void setLetraOrigen(String newLetraOrigen) {
        letraOrigen = newLetraOrigen;
    }
    public void setLetraDestino(String newLetraDestino) {
        letraDestino = newLetraDestino;
    }
    public void setNombre(String newNombre) {
        nombre = newNombre;
    }
    public void setFechaReserva(LocalDate newFechaReserva) {
        fechaReserva = newFechaReserva;
    }


    @Override
    public String toString() {
        return "RESERVA: \nID:"+id+"\nLocalizador:"+localizador+"\nOrigen:"+letraOrigen+"\nDestino:"+letraDestino+"\nNombre del pasajero:"+nombre+"\nFecha Reserva:"+fechaReserva;
    }


}
