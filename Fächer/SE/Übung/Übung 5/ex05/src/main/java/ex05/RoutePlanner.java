package ex05;

import java.util.List;

@Concept("Routenplanung")
public class RoutePlanner {
    
    @Concept("Start")
    private Node start;

    @Concept("Ziel")
    private Node goal;

    @Concept("Fahrzeit")
    private int travelTime;

    @Concept("Distanz")
    private int distance;

    @Concept("Route")
    private List<Path> route;
}
