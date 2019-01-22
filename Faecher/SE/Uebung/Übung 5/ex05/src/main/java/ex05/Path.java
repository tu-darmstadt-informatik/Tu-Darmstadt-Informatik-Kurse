package ex05;

import java.util.List;

@Concept("Weg")
public class Path extends GeoObject{

    @Concept("Punte")
    private List<Node> nodeList;
}
