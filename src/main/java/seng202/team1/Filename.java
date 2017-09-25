package seng202.team1;

public enum Filename {
    SOURCE("src/"),
    MAIN(SOURCE.filename() + "main/"),
    RESOURCES(MAIN.filename() + "resources/"),
    CSV_RESOURCES(RESOURCES.filename() + "csv/"),
        WIFI(CSV_RESOURCES.filename() + "NYC_Free_Public_WiFi_03292017.csv"),
        RETAILERS(CSV_RESOURCES.filename() + "Lower_Manhattan_Retailers.csv"),
        BIKE_TRIPS(CSV_RESOURCES.filename() + "201512-citibike-tripdata.csv"),
        WIFI_SAMPLE(CSV_RESOURCES.filename() + "wifipoint.csv"),
        RETAILERS_SAMPLE(CSV_RESOURCES.filename() + "retailerlocation.csv"),
        BIKE_TRIPS_SAMPLE(CSV_RESOURCES.filename() + "biketrip.csv"),
    HTML_RESOURCES(RESOURCES.filename() + "html/"),
        WIFI_IMAGE(HTML_RESOURCES.filename() + "WIFI.png"),
        RETAILER_IMAGE(HTML_RESOURCES.filename() + "departmentstore.png"),
        RETAILER_CLUSTERS(HTML_RESOURCES.filename() + "retailerCluster/"),
        WIFI_CLUSTERS(HTML_RESOURCES.filename() + "wifiCluster/"),
    //TODO individual images?
    USERS(RESOURCES.filename() + "users/");

    private String filename;

    Filename (String filename) {
        this.filename = filename;
    }

    public String filename() {
        return filename;
    }

}
