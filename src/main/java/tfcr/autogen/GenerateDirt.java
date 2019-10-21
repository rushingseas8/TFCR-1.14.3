package tfcr.autogen;

import tfcr.data.Fertility;

import java.io.File;

// Generates dirt blocks
public class GenerateDirt {
    private static final String blockstateLocation = "blockstates/dirt/";
    private static final String modelLocation = "models/block/dirt/";
    private static final String itemModelLocation = "models/item/dirt/";

    private static final String blockStateJSON = "{\n" +
            "  \"variants\": {\n" +
            "    \"\": {\n" +
            "      \"model\": \"tfcr:block/dirt/TYPE\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private static final String modelJSON = "{\n" +
            "    \"parent\": \"minecraft:block/cube_all\",\n" +
            "    \"textures\": {\n" +
            "        \"all\": \"tfcr:blocks/dirt/TYPE\"\n" +
            "    }\n" +
            "}\n";

    private static final String itemJSON = "{\n" +
            "  \"parent\": \"tfcr:block/dirt/TYPE\"\n" +
            "}";


    public static void generate() {
        System.out.print("Generating dirt assets... ");
        String blockstateDir = GenerateBase.RESOURCE_BASE + File.separator + blockstateLocation;
        String modelDir = GenerateBase.RESOURCE_BASE + File.separator + modelLocation;
        String itemModelDir = GenerateBase.RESOURCE_BASE + File.separator + itemModelLocation;

        GenerateBase.appendSpacerToLangFile();

        for (Fertility fertility : Fertility.values()) {
            // Blockstate JSON
            String filePath = blockstateDir + fertility.getName() + ".json";
            String toWrite = blockStateJSON.replace("TYPE", fertility.getName());

            GenerateBase.writeToFile(filePath, toWrite);

            // Item JSON
            filePath = itemModelDir + fertility.getName() + ".json";
            toWrite = itemJSON.replace("TYPE", fertility.getName());

            GenerateBase.writeToFile(filePath, toWrite);

            // Model JSON
            filePath = modelDir + fertility.getName() + ".json";
            // Note that textures are currently saved as "TYPE_dirt".
            toWrite = modelJSON
                    .replace("TYPE", fertility.getName() + "_dirt");

            GenerateBase.writeToFile(filePath, toWrite);


            // Lang file entries
            GenerateBase.appendToLangFile("block.tfcr.dirt." + fertility.getName(), GenerateBase.guessName(fertility.getName() + "_dirt"));
        }
        System.out.println("Done");
    }

}
