package ex05;

@Concept("Punkt")
public class Node extends GeoObject {

    @Concept("Längengrad")
    private double longitude;

    @Concept("Breitengrad")
    private double latitude;
}
