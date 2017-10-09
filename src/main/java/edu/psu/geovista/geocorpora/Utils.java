package edu.psu.geovista.geocorpora;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Utils {

    public static ArrayList<String> parseFeatureProperties2(JSONObject obj) {

        ArrayList<String> out = new ArrayList<>();

        String geoNameId = String.valueOf((Long) obj.get("geoNameId"));
        out.add(geoNameId);

        String featureCode = (String) obj.get("featureCode");
        // out.add(featureCode);

        String featureClass = (String) obj.get("featureClass");
        // out.add(featureClass);

        String name = (String) obj.get("toponym");
        out.add(name);

        String text = (String) obj.get("name");
        // out.add(text);

        String countryCode = (String) obj.get("countryCode");
        out.add(countryCode);

        return out;
    }

    private static JSONObject createNewFeatureObject(JSONObject obj1) {

        JSONObject newObj = new JSONObject();

        JSONObject obj2 = (JSONObject) obj1.get("properties");
        JSONArray arr = (JSONArray) obj2.get("positions");
        JSONObject obj3 = (JSONObject) obj1.get("geometry");
        JSONObject newObj2 = new JSONObject();
        newObj2.put("positions", arr);
        newObj.put("properties", newObj2);
        newObj2.put("geoNameId", obj2.get("geoNameId"));
        newObj2.put("toponym", obj2.get("toponym"));
        newObj2.put("countryCode", obj2.get("countryCode"));
        JSONObject newObj3 = new JSONObject();
        newObj3.put("coordinates", obj3.get("coordinates"));
        newObj.put("geometry", newObj3);

        return newObj;
    }

    private static JSONObject simplifyGeoJSON(JSONObject json) {
        JSONObject newJson = new JSONObject();
        JSONArray newArr = new JSONArray();
        newJson.put("features", newArr);

        JSONArray arr = (JSONArray) json.get("features");
        for (int i = 0; i < arr.size(); i++) {
            JSONObject obj = (JSONObject) arr.get(i);
            JSONObject newObj = Utils.createNewFeatureObject(obj);
            newArr.add(newObj);
        }

        return newJson;
    }
}
