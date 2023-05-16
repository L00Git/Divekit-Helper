package thkoeln.divekithelper.mock.implementations;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import thkoeln.divekithelper.common.CommitFrequencyInterface;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;


public class MockCommitFrequency implements CommitFrequencyInterface {

    private String saveTestLevelPath = "src/test/resources/studentTestLevel.json";

    private HashMap<String, StudentTestLevel> students = new HashMap<>();

    private Gson gson;


    public MockCommitFrequency() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Instant.class, new InstantTypeConverter());
        this.gson = gsonBuilder.create();

        try {
            Reader reader = Files.newBufferedReader(Paths.get(saveTestLevelPath));
            students = gson.fromJson(reader, new TypeToken<HashMap<String, StudentTestLevel>>() {
            }.getType());

            reader.close();

        } catch (IOException e) {
            System.out.println("Coudn`t load studentTestLevel, didn`t find File ");
        }
    }

    public int getTestLevel(String testName) {

        String identifier = System.getenv("CI_PROJECT_PATH");

        if( identifier == null ){
            System.out.println("Cannot find  'CI_PROJECT_PATH' EnvironmentVariable returning TestLevel 1");
            return 2;
        }


        if (!students.containsKey(identifier))
            students.put(identifier, new StudentTestLevel());


        int result = students.get(identifier).triggerTest(testName);

        FileWriter writer;
        try {
            writer = new FileWriter(saveTestLevelPath);
            gson.toJson(students, writer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            System.out.println("Coudn`t save studentTestLevel");
        }

        return result;
    }


    //https://sites.google.com/site/gson/gson-type-adapters-for-common-classes-1
    private static class InstantTypeConverter
            implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
        @Override
        public JsonElement serialize(Instant src, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive( src.getEpochSecond() );
        }

        @Override
        public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            return Instant.ofEpochSecond( json.getAsLong() );
        }
    }

}
