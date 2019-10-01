package tfcr.autogen;

import tfcr.data.WoodType;

import java.io.File;

public class GenerateMiscItems {
    private static final String defaultItemModelLocation = "models/item/NAME";

    private static final String defaultItemJSON = "{\n" +
            "  \"parent\": \"item/generated\",\n" +
            "  \"textures\": {\n" +
            "    \"layer0\": \"tfcr:item/NAME\"\n" +
            "  }\n" +
            "}";

    public static void generate() {
        System.out.print("Generating random extra items... ");

        GenerateBase.appendSpacerToLangFile();

        String[] names = new String[]{
                "leaves",
                "mud_ball",
                "wicker",
                "small_rock",
                "sharp_rock",
                "raw_fiber",
                "plant_fiber",
                "cordage",
                "empty_ceramic_jug",
                "filled_ceramic_jug"
        };

        for (String name : names) {
            // Item JSON
            String itemModelDir = defaultItemModelLocation.replace("NAME", name);
            String filePath = GenerateBase.RESOURCE_BASE + File.separator + itemModelDir + ".json";
            String toWrite = defaultItemJSON.replace("NAME", name);

            GenerateBase.writeToFile(filePath, toWrite);

            // Lang file entries
            GenerateBase.appendToLangFile("item.tfcr." + name, GenerateBase.guessName(name));
        }
        System.out.println("Done");
    }
}
