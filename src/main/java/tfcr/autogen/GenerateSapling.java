package tfcr.autogen;

import tfcr.data.WoodType;

import java.io.File;

public class GenerateSapling {
    private static final String blockstateLocation = "blockstates/sapling/";
    private static final String modelLocation = "models/block/sapling/";
    private static final String itemModelLocation = "models/item/sapling/";

    private static final String blockStateJSON = "{\n" +
            "  \"variants\": {\n" +
            "    \"\": {\n" +
            "      \"model\": \"tfcr:block/sapling/WOOD\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private static final String modelJSON = "{\n" +
            "  \"parent\": \"tfcr:block/tinted_cross\",\n" +
            "  \"textures\": {\n" +
            "    \"cross\": \"tfcr:blocks/wood/sapling/WOOD\"\n" +
            "  }\n" +
            "}";

    private static final String itemJSON = "{\n" +
            "  \"parent\": \"tfcr:block/sapling/WOOD\"\n" +
            "}";


    public static void generate() {
        System.out.print("Generating sapling assets... ");
        String blockstateDir = GenerateBase.RESOURCE_BASE + File.separator + blockstateLocation;
        String modelDir = GenerateBase.RESOURCE_BASE + File.separator + modelLocation;
        String itemModelDir = GenerateBase.RESOURCE_BASE + File.separator + itemModelLocation;

        GenerateBase.appendSpacerToLangFile();

        for (WoodType woodType : WoodType.values()) {
            // Blockstate JSON
            String filePath = blockstateDir + woodType.getName() + ".json";
            String toWrite = blockStateJSON.replace("WOOD", woodType.getName());

            GenerateBase.writeToFile(filePath, toWrite);

            // Item JSON
            filePath = itemModelDir + woodType.getName() + ".json";
            toWrite = itemJSON.replace("WOOD", woodType.getName());

            GenerateBase.writeToFile(filePath, toWrite);

            // Model JSON
            filePath = modelDir + woodType.getName() + ".json";
            // Note that textures are currently saved as "wood_TYPE".
            toWrite = modelJSON
                    .replace("WOOD", woodType.getName());

            GenerateBase.writeToFile(filePath, toWrite);


            // Lang file entries
            GenerateBase.appendToLangFile("block.tfcr.sapling." + woodType.getName(), woodType.name + " Sapling");
        }
        System.out.println("Done");
    }
}
