package proyectos.carosdrean.xyz.encuentralo.Pojo;

/**
 * Created by josue on 3/05/2017.
 */

public class Categoria {
    private String categoria;
    private int fondo;

    public Categoria(String categoria, int fondo) {
        this.categoria = categoria;
        this.fondo = fondo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getFondo() {
        return fondo;
    }

    public void setFondo(int fondo) {
        this.fondo = fondo;
    }
}
