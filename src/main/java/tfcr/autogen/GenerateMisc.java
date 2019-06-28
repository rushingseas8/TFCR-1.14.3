package tfcr.autogen;

import tfcr.data.WoodType;

import java.io.File;

public class GenerateMisc {
    private static final String defaultItemModelLocation = "models/item/NAME";

    private static final String defaultItemJSON = "{\n" +
            "  \"parent\": \"item/generated\",\n" +
            "  \"textures\": {\n" +
            "    \"layer0\": \"tfcr:item/NAME\"\n" +
            "  }\n" +
            "}";

    private static String guessName(String registryName) {
        String[] words = registryName.split("_");
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String word : words) {
            // Ignore empty words by skipping them entirely (to avoid double spaces)
            if (word == null || word.isEmpty()) {
                continue;
            }

            // Handle spacing between words, but not before the first word
            if (first) {
                first = false;
            } else {
                builder.append(" ");
            }

            // Add the title-case'd word
            builder.append(word.substring(0, 1).toUpperCase());
            builder.append(word.substring(1).toLowerCase());
        }
        return builder.toString();
    }

    public static void generate() {
        System.out.print("Generating random extra items... ");

        GenerateBase.appendSpacerToLangFile();

        String[] names = new String[]{
                "leaves",
                "mud_ball"
        };

        for (String name : names) {
            // Item JSON
            String itemModelDir = defaultItemModelLocation.replace("NAME", name);
            String filePath = GenerateBase.RESOURCE_BASE + File.separator + itemModelDir + ".json";
            String toWrite = defaultItemJSON.replace("NAME", name);

            GenerateBase.writeToFile(filePath, toWrite);

            // Lang file entries
            GenerateBase.appendToLangFile("item.tfcr." + name, guessName(name));
        }
        System.out.println("Done");
    }
}
