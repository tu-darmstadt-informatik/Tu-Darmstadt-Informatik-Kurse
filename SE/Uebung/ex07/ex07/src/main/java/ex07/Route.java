package ex07;

import java.io.Serializable;
import java.util.ArrayList;

public interface Route extends Serializable {
    ArrayList<Way> getWays();
    double getLength() throws RouteException;
}