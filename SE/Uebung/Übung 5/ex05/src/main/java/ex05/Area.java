package ex05;

import java.util.List;

@Concept("Flaeche")
public class Area extends GeoObject {

    @Concept("Wege")
    private List<Path> paths;
}
