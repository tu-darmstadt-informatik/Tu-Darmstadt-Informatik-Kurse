package ex07;

import java.io.*;
import java.nio.file.Path;

public class Navigation implements Serializable {

    private static final long serialVersionUID = -8343119537569214659L;
    private Route route;
    private Double length;
    private String unit;
    private transient Path workingDir = null;

    public Navigation(Route route, String unit) {
        this.route = route;
        setLength();
        this.unit = unit;
    }

    private double getLength() throws RuntimeException {

        switch (getUnit()) {
        case "km":
            return getLengthKM();
        case "miles":
            return getLengthMiles();
        default:
            return 0;
        }
    }

    private void setLength(){
    this.length = this.route.getLength();

    }
    private double getLengthKM(){
      return length;
    }

    private double getLengthMiles(){
        return length / 1609.344;
    }

    public String getUnit() {
    	return unit;
    }

    public Path getworkingDir(){
      return workingDir;
    }

    @Override
    public String toString() {
        String result = "";
        java.util.List<Way> ways = route.getWays();
        Way lastWay = ways.get(0);
        boolean isFirst = true;
        for (Way w : ways) {
            if (isFirst) {
                isFirst = false;
            } else {
                result += NavigationService.forTurn(lastWay, w);
            }
            result += NavigationService.forWay(w);
            lastWay = w;
        }
        return result;
    }

    public void printRoute(StringBuilder sb) {
        sb.append(toString());
        sb.append('\n');
        sb.append("Route length: " + getLength());
    }

    public Route importRoute(String routeFileName) {
        Route route = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getworkingDir().resolve(routeFileName).toFile()))) {
            route = (Route) (ois.readObject());
        } catch (Exception e) {
            System.err.println("Could not import route");
        }
        setLength();
        return route;
    }

    public void setWorkingDir(String newWorkingDir) {
        this.workingDir = java.nio.file.Paths.get(newWorkingDir);
    }
}
