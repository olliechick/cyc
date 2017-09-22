package seng202.team1;

public enum Filename {
    RESOURCES("src/main/resources/"),
    CSV_RESOURCES(RESOURCES + "csv/"),
        WIFI(CSV_RESOURCES + "NYC_Free_Public_WiFi_03292017.csv"),
        RETAILERS(CSV_RESOURCES + "Lower_Manhattan_Retailers.csv"),
        BIKE_TRIPS(CSV_RESOURCES + "201512-citibike-tripdata.csv"),
        WIFI_SAMPLE(CSV_RESOURCES + "wifipoint.csv"),
        RETAILERS_SAMPLE(CSV_RESOURCES + "retailerlocation.csv"),
        BIKE_TRIPS_SAMPLE(CSV_RESOURCES + "biketrip.csv"),
    HTML_RESOURCES(RESOURCES + "html/"),
        WIFI_IMAGE(HTML_RESOURCES + "WIFI.png"),
        RETAILER_IMAGE(HTML_RESOURCES + "departmentstore.png"),
        RETAILER_CLUSTERS(HTML_RESOURCES + "retailerCluster/"),
        WIFI_CLUSTERS(HTML_RESOURCES + "wifiCluster/"),
    //TODO individual images?
    USERS(RESOURCES + "users/");

    private String filename;

    Filename (String filename) {
        this.filename = filename;
    }

    public String filename() {
        return filename;
    }

}
