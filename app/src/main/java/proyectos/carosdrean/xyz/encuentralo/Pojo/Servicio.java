package proyectos.carosdrean.xyz.encuentralo.Pojo;

/**
 * Created by josue on 3/05/2017.
 */

public class Servicio {
    private String id;
    private String categoria;
    private String nombreServicio;
    private String direccion;
    private String descripcion;
    private int logo;
    private String facebook;
    private String web;
    private String productos;
    private String portada;
    private String telefono;
    private int fotos;

    public Servicio(String id, String categoria, String nombreServicio, String direccion, String descripcion, int logo, String facebook, String web, String productos, String portada, String telefono, int fotos) {
        this.id = id;
        this.categoria = categoria;
        this.nombreServicio = nombreServicio;
        this.direccion = direccion;
        this.descripcion = descripcion;
        this.logo = logo;
        this.facebook = facebook;
        this.web = web;
        this.productos = productos;
        this.portada = portada;
        this.telefono = telefono;
        this.fotos = fotos;
    }

    public Servicio() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    public int getFotos() {
        return fotos;
    }

    public void setFotos(int fotos) {
        this.fotos = fotos;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
