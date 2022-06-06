import com.google.gson.*;

import java.lang.reflect.Type;

class Custom implements JsonDeserializer<Tags> {
    @Override
    public Tags deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Tags tags = new Tags();
        JsonArray jsonArray = json.getAsJsonArray();
        if (jsonArray.size() != 1) {
            throw new IllegalStateException("unexpected json");
        }
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject(); // get only element
        JsonElement jsonElement = jsonObject.get("tags");
        if (!jsonElement.isJsonNull()) {
            tags.setTags(jsonElement.getAsString());
        }
        return tags;
    }
}