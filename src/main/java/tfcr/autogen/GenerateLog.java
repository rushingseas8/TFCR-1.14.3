package tfcr.autogen;

import tfcr.blocks.BranchBlock;
import tfcr.data.WoodType;

import java.io.File;
import java.util.List;

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

    private static final String logsTagsJSON = "{\n" +
            "    \"replace\": false,\n" +
            "    \"values\": [\n" +
            "        VALUES" +
            "    ]\n" +
            "}";

    public static void generate() {
        System.out.print("Generating log assets... ");
        String blockstateDir = GenerateBase.RESOURCE_BASE + File.separator + blockstateLocation;
        String modelDir = GenerateBase.RESOURCE_BASE + File.separator + modelLocation;
        String itemModelDir = GenerateBase.RESOURCE_BASE + File.separator + itemModelLocation;
        String logsTagsDir = GenerateBase.DATA_BASE + GenerateBase.VANILLA_BLOCKS + "logs.json";
        StringBuilder logsTagsBuilder = new StringBuilder();

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

        List<BranchBlock> allBlocks = BranchBlock.getAllBlocks();
        for (int i = 0; i < allBlocks.size(); i++) {
            BranchBlock branchBlock = allBlocks.get(i);

            // Add the name to the values list
            logsTagsBuilder.append(branchBlock.getRegistryName());
            if (i != allBlocks.size() - 1) { // no commas for last entry
                logsTagsBuilder.append(",");
            }
            logsTagsBuilder.append("\n");
        }

        System.out.println("Done");
    }

}
