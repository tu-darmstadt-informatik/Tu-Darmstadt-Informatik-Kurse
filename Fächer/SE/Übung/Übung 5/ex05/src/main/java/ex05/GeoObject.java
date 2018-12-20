package ex05;

import java.util.Calendar;

@Concept("GeoObjekt")
public abstract class GeoObject {

    @Concept("Identifikationsnummer")
    private long id;

    @Concept("Eigenschaft")
    private Properties properties;

    @Concept("Benutzername")
    private String userName;

    @Concept("Aenderungsdatum")
    private Calendar changeDate;

    @Concept("IP")
    private String ip;
}
