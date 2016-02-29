package Services;


import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class JSONOperator {

    public static boolean appendMapAsJSONToResponse(HttpServletResponse response, Map<String, String> values) throws IOException {

        JSONObject json = new JSONObject();


        tryAppendMapToJSONObject(json, values);


        response.setContentType("application/json");
        response.getWriter().write(json.toString());

        return true;
    }

    private static void tryAppendMapToJSONObject(JSONObject object, Map<String, String> values) {
        Set<String> keys = values.keySet();

        for (String currentKey : keys) {
            String currentValue = values.get(currentKey);

            try {
                object.put(currentKey, currentValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
