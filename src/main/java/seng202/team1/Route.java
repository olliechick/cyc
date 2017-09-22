package seng202.team1;

public class Route {
    public double startLat;
    public double startLng;
    public double endLat;
    public double endLng;
    public String overviewPolyline;

    public Route(double startLat, double startLng, double endLat, double endLng, String overviewPolyline) {
        this.startLat = startLat;
        this.startLng = startLng;
        this.endLat = endLat;
        this.endLng = endLng;
        this.overviewPolyline = overviewPolyline;
    }

    @Override
    public Route deserialize(JsonElement json, Type type,
                            JsonDeserializationContext context) throws JsonParseException {

        JsonObject jobject = json.getAsJsonObject();

        return new Route(
                jobject.get("id").getAsInt(),
                jobject.get("name").getAsString(),
                new Timestamp(jobject.get("update_date").getAsLong()));
    }
}
