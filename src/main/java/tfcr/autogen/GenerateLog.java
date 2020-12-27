package tfcr.autogen;

import tfcr.data.WoodType;

import java.io.File;

public class GenerateLog {
    private static final String blockstateLocation = "blockstates/log/";
    private static final String modelLocation = "models/block/log/";
    private static final String itemModelLocation = "models/item/log/";

    private static final String blockStateJSON = "{\n" +
            "  \"variants\": {\n" +
            "    \"axis=y\": {\n" +
            "      \"model\": \"tfcr:block/log/WOOD\"\n" +
            "    },\n" +
            "    \"axis=z\": {\n" +
            "      \"model\": \"tfcr:block/log/WOOD\",\n" +
            "      \"x\": 90\n" +
            "    },\n" +
            "    \"axis=x\": {\n" +
            "      \"model\": \"tfcr:block/log/WOOD\",\n" +
            "      \"x\": 90,\n" +
            "      \"y\": 90\n" +
            "    }\n" +
            "  }\n" +
            "}\n";

    private static final String modelJSON = "{\n" +
            "  \"parent\": \"minecraft:block/cube_column\",\n" +
            "  \"textures\": {\n" +
            "    \"end\": \"tfcr:blocks/wood/log/WOOD\",\n" +
            "    \"side\": \"tfcr:blocks/wood/bark/WOOD\"\n" +
            "  }\n" +
            "}\n";

    private static final String itemJSON = "{\n" +
            "  \"parent\": \"tfcr:block/log/WOOD\"\n" +
            "}";

    public static void generate() {
        System.out.print("Generating log assets... ");
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
                    .replace("WOOD", "wood_" + woodType.getName());

            GenerateBase.writeToFile(filePath, toWrite);

            // Lang file entries
            GenerateBase.appendToLangFile("block.tfcr.log." + woodType.getName(), woodType.name + " Log");
        }

        System.out.println("Done");
    }

}
