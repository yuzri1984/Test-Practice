package test.practice.Test.Practice.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Currency {

    private String main;
    private String description;
    private String icon;

    public String getMain() { return main; }
    public void setMain(String main) { this.main = main; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public static Currency create(JsonObject jo) {
        Currency c = new Currency();
        c.setMain(jo.getString("main"));
        c.setDescription(jo.getString("description"));
        c.setIcon(jo.getString("icon"));
        return c;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("main", main)
            .add("description", description)
            .add("icon", icon)
            .build();
    }
    
}
