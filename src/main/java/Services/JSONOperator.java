package Services;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class JSONOperator {

    public static boolean appendMapAsJSONToResponse(HttpServletResponse response, Map<String, String> values) {
        String jsonError = null;

        JSONObject json = new JSONObject();

        try {
           tryAppendMapToJSONObject(json,values);
        } catch (JSONException jse) {
            jsonError = "Cannot create json for file.";
        }

        try {
            if (jsonError == null) {
                response.setContentType("application/json");
                response.getWriter().write(json.toString());
            } else {
                response.sendError(500, jsonError);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private static void tryAppendMapToJSONObject(JSONObject object,Map<String,String> values) throws JSONException{
        Set<String> keys = values.keySet();

        for (String currentKey : keys) {
            String currentValue = values.get(currentKey);

            object.put(currentKey, currentValue);
        }
    }
}
