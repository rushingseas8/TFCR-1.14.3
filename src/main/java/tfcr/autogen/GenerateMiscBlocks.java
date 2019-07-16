package tfcr.autogen;

import java.io.File;

public class GenerateMiscBlocks {
    private static final String defaultBlockstateLocation = "blockstates/NAME";
    private static final String defaultModelLocation = "models/block/NAME";
    private static final String defaultItemModelLocation = "models/item/NAME";

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

    public static void generate() {
        System.out.print("Generating random extra block assets... ");

        String[] cubeBlocks = new String[] {
                "mud",
                "leaf_roof"
        };

        for (String name : cubeBlocks) {
            String blockstateDir = GenerateBase.RESOURCE_BASE + File.separator + defaultBlockstateLocation.replace("NAME", name);
            String modelDir = GenerateBase.RESOURCE_BASE + File.separator + defaultModelLocation.replace("NAME", name);
            String itemModelDir = GenerateBase.RESOURCE_BASE + File.separator + defaultItemModelLocation.replace("NAME", name);

            // Blockstate JSON
            String toWrite = blockStateJSON.replace("NAME", name);
            GenerateBase.writeToFile(blockstateDir, toWrite);

            // Model JSON
            toWrite = modelJSON.replace("NAME", name);
            GenerateBase.writeToFile(modelDir, toWrite);

            // Item model JSON
            toWrite = itemModelDir.replace("NAME", name);
            GenerateBase.writeToFile(itemModelDir, toWrite);

            // Lang file entries
            GenerateBase.appendToLangFile("item.tfcr." + name, GenerateBase.guessName(name));
        }

        GenerateBase.appendSpacerToLangFile();
        System.out.println("Done");
    }
}
