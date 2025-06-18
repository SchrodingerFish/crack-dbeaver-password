package cn.java666.tools.crackdbeaverpassword;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JsonCombUtil {

    public static String combineConnections(String firstJsonString, String secondJsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode firstJson = mapper.readTree(firstJsonString);
        JsonNode secondJson = mapper.readTree(secondJsonString);
        ArrayNode connectionsArray = mapper.createArrayNode();
        JsonNode connectionsNode = secondJson.get("connections");
        if (connectionsNode != null && connectionsNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = connectionsNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String connectionId = entry.getKey();
                JsonNode connDetails = entry.getValue();
                ObjectNode connectionObject = mapper.createObjectNode();
                connectionObject.put("id", connectionId);
                connectionObject.put("provider", connDetails.get("provider").asText());
                connectionObject.put("driver", connDetails.get("driver").asText());
                connectionObject.put("name", connDetails.get("name").asText());
                connectionObject.put("folder", connDetails.get("folder").asText());
                JsonNode configuration = connDetails.get("configuration");
                String host = configuration.get("host").asText();
                int port = configuration.get("port").asInt();
                String url = configuration.get("url").asText();
                String database = null;
                if (configuration.has("database")) {
                    database = configuration.get("database").asText();
                }
                connectionObject.put("host", host);
                connectionObject.put("port", port);
                if (database != null){
                    connectionObject.put("database", database);
                }else{
                    connectionObject.putNull("database");
                }
                connectionObject.put("url", url);
                JsonNode firstJsonConnection = firstJson.get(connectionId);
                if (firstJsonConnection != null && firstJsonConnection.isObject()) {
                    JsonNode connectionDetails = firstJsonConnection.get("#connection");
                    if (connectionDetails != null && connectionDetails.isObject()) {
                        String user = connectionDetails.get("user").asText();
                        String password = connectionDetails.get("password").asText();
                        connectionObject.put("user", user);
                        connectionObject.put("password", password);
                    } else {
                        connectionObject.putNull("user");
                        connectionObject.putNull("password");
                    }
                } else {
                    connectionObject.putNull("user");
                    connectionObject.putNull("password");
                }
                connectionsArray.add(connectionObject);
            }
        }
        ObjectNode result = mapper.createObjectNode();
        result.set("connections", connectionsArray);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
    }

}
