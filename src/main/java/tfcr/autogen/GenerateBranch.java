package tfcr.autogen;

import tfcr.TFCR;
import tfcr.data.WoodType;

import java.io.*;

public class GenerateBranch {
    private static final String blockstateLocation = "blockstates/branch/";
    private static final String modelLocation = "models/block/branch/";
    private static final String itemModelLocation = "models/item/branch/";

    private static final String blockStateJSON = "{\n" +
            "  \"variants\": {\n" +
            "    \"axis=y,extend_positive=false,extend_negative=false\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM\"\n" +
            "    },\n" +
            "    \"axis=y,extend_positive=true,extend_negative=false\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_pos_extend\"\n" +
            "    },\n" +
            "    \"axis=y,extend_positive=false,extend_negative=true\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_neg_extend\"\n" +
            "    },\n" +
            "    \"axis=y,extend_positive=true,extend_negative=true\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_both_extend\"\n" +
            "    },\n" +
            "    \"axis=z,extend_positive=false,extend_negative=false\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM\",\n" +
            "      \"x\": 90\n" +
            "    },\n" +
            "    \"axis=z,extend_positive=true,extend_negative=false\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_pos_extend\",\n" +
            "      \"x\": 90\n" +
            "    },\n" +
            "    \"axis=z,extend_positive=false,extend_negative=true\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_neg_extend\",\n" +
            "      \"x\": 90\n" +
            "    },\n" +
            "    \"axis=z,extend_positive=true,extend_negative=true\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_both_extend\",\n" +
            "      \"x\": 90\n" +
            "    },\n" +
            "    \"axis=x,extend_positive=false,extend_negative=false\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM\",\n" +
            "      \"x\": 90,\n" +
            "      \"y\": 90\n" +
            "    },\n" +
            "    \"axis=x,extend_positive=true,extend_negative=false\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_pos_extend\",\n" +
            "      \"x\": 90,\n" +
            "      \"y\": 90\n" +
            "    },\n" +
            "    \"axis=x,extend_positive=false,extend_negative=true\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_neg_extend\",\n" +
            "      \"x\": 90,\n" +
            "      \"y\": 90\n" +
            "    },\n" +
            "    \"axis=x,extend_positive=true,extend_negative=true\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_both_extend\",\n" +
            "      \"x\": 90,\n" +
            "      \"y\": 90\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private static final String modelJSON = "{\n" +
            "    \"parent\": \"minecraft:block/cube\",\n" +
            "    \"textures\": {\n" +
            "        \"side\": \"tfcr:blocks/wood/bark/WOOD\",\n" +
            "        \"end\": \"tfcr:blocks/wood/log/WOOD\"\n" +
            "    },\n" +
            "    \"elements\": [{\n" +
            "        \"from\": [ LOWX, LOWY, LOWZ ],\n" +
            "        \"to\": [ HIGHX, HIGHY, HIGHZ ],\n" +
            "        \"faces\": {\n" +
            "            \"down\":  { \"uv\": [ LOWX, LOWZ, HIGHX, HIGHZ ], \"texture\": \"#end\", \"cullface\": \"down\" },\n" +
            "            \"up\":    { \"uv\": [ LOWX, LOWZ, HIGHX, HIGHZ ], \"texture\": \"#end\", \"cullface\": \"up\"   },\n" +
            "            \"north\": { \"uv\": [ LOWXUV, LOWYUV, HIGHXUV, HIGHYUV ], \"texture\": \"#side\" },\n" +
            "            \"south\": { \"uv\": [ LOWXUV, LOWYUV, HIGHXUV, HIGHYUV ], \"texture\": \"#side\" },\n" +
            "            \"west\":  { \"uv\": [ LOWXUV, LOWYUV, HIGHXUV, HIGHYUV ], \"texture\": \"#side\" },\n" +
            "            \"east\":  { \"uv\": [ LOWXUV, LOWYUV, HIGHXUV, HIGHYUV ], \"texture\": \"#side\" }\n" +
            "        }\n" +
            "    }]\n" +
            "}\n";

    private static final String itemJSON = "{\n" +
            "  \"parent\": \"tfcr:block/branch/WOOD/block_branch_NUM\"\n" +
            "}";

    public static void generate() {
        System.out.print("Generating branch assets... ");
        String blockstateDir = GenerateBase.RESOURCE_BASE + File.separator + blockstateLocation;
        String modelDir = GenerateBase.RESOURCE_BASE + File.separator + modelLocation;
        String itemModelDir = GenerateBase.RESOURCE_BASE + File.separator + itemModelLocation;

        GenerateBase.appendSpacerToLangFile();

        for (int woodIndex = 0; woodIndex < WoodType.values().length; woodIndex++) {
            WoodType woodType = WoodType.values()[woodIndex];
            for (int i = 2; i <= 14; i += 2) {
                // Blockstate JSON
                String filePath = blockstateDir + woodType.getName() + "/block_branch_" + i + ".json";
                String toWrite = blockStateJSON
                        .replace("WOOD", woodType.getName())
                        .replace("NUM", "" + i);

                GenerateBase.writeToFile(filePath, toWrite);

                // Item JSON
                filePath = itemModelDir + woodType.getName() + "/block_branch_" + i + ".json";
                toWrite = itemJSON
                        .replace("WOOD", woodType.getName())
                        .replace("NUM", "" + i);

                GenerateBase.writeToFile(filePath, toWrite);

                // Model JSON 1 (no extending)
                filePath = modelDir + woodType.getName() + "/block_branch_" + i + ".json";
                toWrite = modelJSON
                        .replace("WOOD", "wood_" + woodType.getName())
                        .replace("LOWXUV", "" + (4 - (i / 4f)))
                        .replace("HIGHXUV", "" + (4 + (i / 4f)))
                        .replace("LOWYUV", "" + 0)
                        .replace("HIGHYUV", "" + 8)
                        .replace("LOWY", "" + 0)
                        .replace("HIGHY", "" + 16)
                        .replace("LOWX", "" + (8 - (i / 2)))
                        .replace("LOWZ", "" + (8 - (i / 2)))
                        .replace("HIGHX", "" + (8 + (i / 2)))
                        .replace("HIGHZ", "" + (8 + (i / 2)));

                GenerateBase.writeToFile(filePath, toWrite);

                // Model JSON 2 (extend negative only)
                filePath = modelDir + woodType.getName() + "/block_branch_" + i + "_neg_extend.json";
                toWrite = modelJSON
                        .replace("WOOD", "wood_" + woodType.getName())
                        .replace("LOWXUV", "" + (4 - (i / 4f)))
                        .replace("HIGHXUV", "" + (4 + (i / 4f)))
                        .replace("LOWYUV", "" + 0)
                        .replace("HIGHYUV", "" + 12)
                        .replace("LOWY", "" + -8)
                        .replace("HIGHY", "" + 16)
                        .replace("LOWX", "" + (8 - (i / 2)))
                        .replace("LOWZ", "" + (8 - (i / 2)))
                        .replace("HIGHX", "" + (8 + (i / 2)))
                        .replace("HIGHZ", "" + (8 + (i / 2)));

                GenerateBase.writeToFile(filePath, toWrite);

                // Model JSON 3 (extend positive only)
                filePath = modelDir + woodType.getName() + "/block_branch_" + i + "_pos_extend.json";
                toWrite = modelJSON
                        .replace("WOOD", "wood_" + woodType.getName())
                        .replace("LOWXUV", "" + (4 - (i / 4f)))
                        .replace("HIGHXUV", "" + (4 + (i / 4f)))
                        .replace("LOWYUV", "" + 4)
                        .replace("HIGHYUV", "" + 16)
                        .replace("LOWY", "" + 0)
                        .replace("HIGHY", "" + 24)
                        .replace("LOWX", "" + (8 - (i / 2)))
                        .replace("LOWZ", "" + (8 - (i / 2)))
                        .replace("HIGHX", "" + (8 + (i / 2)))
                        .replace("HIGHZ", "" + (8 + (i / 2)));

                GenerateBase.writeToFile(filePath, toWrite);

                // Model JSON 4 (extend both)
                filePath = modelDir + woodType.getName() + "/block_branch_" + i + "_both_extend.json";
                toWrite = modelJSON
                        .replace("WOOD", "wood_" + woodType.getName())
                        .replace("LOWXUV", "" + (4 - (i / 4f)))
                        .replace("HIGHXUV", "" + (4 + (i / 4f)))
                        .replace("LOWYUV", "" + 0)
                        .replace("HIGHYUV", "" + 16)
                        .replace("LOWY", "" + -8)
                        .replace("HIGHY", "" + 24)
                        .replace("LOWX", "" + (8 - (i / 2)))
                        .replace("LOWZ", "" + (8 - (i / 2)))
                        .replace("HIGHX", "" + (8 + (i / 2)))
                        .replace("HIGHZ", "" + (8 + (i / 2)));

                GenerateBase.writeToFile(filePath, toWrite);

                // Lang file entries
                GenerateBase.appendToLangFile("block.tfcr.branch." + woodType.getName() + ".block_branch_" + i, i + " wide " + woodType.name + " branch");
            }
        }
        System.out.println("Done");
    }


}