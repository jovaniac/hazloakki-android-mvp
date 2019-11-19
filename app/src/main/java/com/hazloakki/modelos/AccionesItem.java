package com.hazloakki.modelos;

//Object of food item
public class AccionesItem extends RecyclerViewItem {
    private String idAccion;
    private String titulo;
    private String descripcion;
    private String urlImagen;

    public AccionesItem(String idAccion,String titulo, String descripcion, String urlImagen) {
        this.idAccion = idAccion;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
