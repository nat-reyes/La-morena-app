package com.example.lamorena.Activities;

import java.io.Serializable;

public class IngresoItem  implements Serializable {

    private int imageItem;
    private String placa;
    private String estado;
    private String empleado,fecha,hora,servicio,total,aseguradora,idcard;

    public IngresoItem(int imageItem, String placa, String estado) {
        this.imageItem = imageItem;
        this.placa = placa;
        this.estado = estado;
    }

    public IngresoItem(int img) {
        this.imageItem = img;
        this.placa = "";
        this.estado = "";
        this.empleado = "";
        this.fecha = "";
        this.hora = "";
        this.servicio = "";
        this.total = "";
        this.aseguradora = "";
        this.idcard = "";
    }

    public String getFecha() {
        return fecha;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAseguradora() {
        return aseguradora;
    }

    public void setAseguradora(String aseguradora) {
        this.aseguradora = aseguradora;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }



    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getImageItem() {
        return imageItem;
    }

    public void setImageItem(int imageItem) {
        this.imageItem = imageItem;
    }

    @Override
    public String toString() {
        return "IngresoItem{" +
                "imageItem=" + imageItem +
                ", placa='" + placa + '\'' +
                ", estado='" + estado + '\'' +
                ", empleado='" + empleado + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", servicio='" + servicio + '\'' +
                ", total='" + total + '\'' +
                ", aseguradora='" + aseguradora + '\'' +
                ", idcard='" + idcard + '\'' +
                '}';
    }
}
