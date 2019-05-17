package tfcr.autogen;

import tfcr.data.WoodType;

import java.io.File;

public class GenerateLeaves {
    private static final String blockstateLocation = "blockstates/leaves/";
    private static final String modelLocation = "models/block/leaves/";
    private static final String itemModelLocation = "models/item/leaves/";

    private static final String blockStateJSON = "{\n" +
            "  \"variants\": {\n" +
            "    \"\": {\n" +
            "      \"model\": \"tfcr:block/leaves/WOOD\"\n" +
            "    }\n" +
            "  }\n" +
            "}\n";

    private static final String modelJSON = "{\n" +
            "  \"parent\": \"minecraft:block/leaves\",\n" +
            "  \"textures\": {\n" +
            "    \"all\": \"tfcr:blocks/wood/leaves/WOOD\"\n" +
            "  }\n" +
            "}\n";

    private static final String itemJSON = "{\n" +
            "  \"parent\": \"tfcr:block/leaves/WOOD\"\n" +
            "}";


    public static void generate() {
        System.out.print("Generating leaves assets... ");
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
                    .replace("WOOD", "leaves_" + woodType.getName());

            GenerateBase.writeToFile(filePath, toWrite);


            // Lang file entries
            GenerateBase.appendToLangFile("block.tfcr.leaves." + woodType.getName(), woodType.name + " Leaves");
        }
        System.out.println("Done");
    }

}
