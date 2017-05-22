package proyectos.carosdrean.xyz.encuentralo.Interfaces;

import java.util.List;

import proyectos.carosdrean.xyz.encuentralo.Modulos.Route;

/**
 * Created by josue on 9/05/2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
