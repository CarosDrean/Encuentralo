package proyectos.carosdrean.xyz.encuentralo.Adaptador;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import proyectos.carosdrean.xyz.encuentralo.R;

/**
 * Created by josue on 3/05/2017.
 */

public class DetalleCategoriaViewHolder extends RecyclerView.ViewHolder {
    private View mView;

    private TextView productos;
    private TextView nombreServicio;
    private TextView descripcion;
    private CircleImageView perfil;

    public LinearLayout contenedor = (LinearLayout) itemView.findViewById(R.id.contenedor);
    public CircleImageView portada = (CircleImageView)itemView.findViewById(R.id.iv_avatar);

    public DetalleCategoriaViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setProducto(String producto) {
        productos = (TextView)mView.findViewById(R.id.tv_productos);
        productos.setText(producto);
    }

    public void setNombreServicio(String nombreServicios) {
        nombreServicio = (TextView)mView.findViewById(R.id.tv_name);
        nombreServicio.setText(nombreServicios);
    }

    public void setDescripcion(String descripcions) {
        descripcion = (TextView)itemView.findViewById(R.id.tv_descripcion);
        descripcion.setText(descripcions);
    }

}