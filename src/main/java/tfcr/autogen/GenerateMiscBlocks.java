package tfcr.autogen;

import java.io.File;

public class GenerateMiscBlocks {
    private static final String blockstateLocation = "blockstates/NAME";
    private static final String modelLocation = "models/block/NAME";
    private static final String itemModelLocation = "models/item/NAME";

    private static final String blockStateJSON = "{\n" +
            "  \"variants\": {\n" +
            "    \"\": {\n" +
            "      \"model\": \"tfcr:block/NAME\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private static final String modelJSON = "{\n" +
            "  \"parent\": \"tfcr:block/cube_all\",\n" +
            "  \"textures\": {\n" +
            "    \"all\": \"tfcr:blocks/NAME\"\n" +
            "  }\n" +
            "}";

    private static final String itemJSON = "{\n" +
            "  \"parent\": \"tfcr:block/NAME\"\n" +
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
        System.out.print("Generating random extra blocks... ");

        GenerateBase.appendSpacerToLangFile();

        String[] names = new String[]{
                "mud"
        };

        for (String name : names) {
            // Blockstate JSON
            String filePath = GenerateBase.RESOURCE_BASE + File.separator + blockstateLocation.replace("NAME", name) + ".json";
            String toWrite = blockStateJSON.replace("NAME", name);

            GenerateBase.writeToFile(filePath, toWrite);

            // Item JSON
            filePath = GenerateBase.RESOURCE_BASE + File.separator + itemModelLocation.replace("NAME", name) + ".json";
            toWrite = itemJSON.replace("NAME", name);

            GenerateBase.writeToFile(filePath, toWrite);

            // Model JSON
            filePath = GenerateBase.RESOURCE_BASE + File.separator + modelLocation.replace("NAME", name) + ".json";
            toWrite = modelJSON.replace("NAME", name);

            GenerateBase.writeToFile(filePath, toWrite);

            // Lang file entries
            GenerateBase.appendToLangFile("block.tfcr." + name, guessName(name));
        }
        System.out.println("Done");
    }
}
