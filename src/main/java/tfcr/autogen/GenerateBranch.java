package tfcr.autogen;

import tfcr.data.WoodType;

import java.io.File;

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

    private static final String blockstateLeafyJSON = "{\n" +
            "  \"variants\": {\n" +
            "    \"axis=y,extend_positive=false,extend_negative=false\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy\"\n" +
            "    },\n" +
            "    \"axis=y,extend_positive=true,extend_negative=false\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy_pos_extend\"\n" +
            "    },\n" +
            "    \"axis=y,extend_positive=false,extend_negative=true\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy_neg_extend\"\n" +
            "    },\n" +
            "    \"axis=y,extend_positive=true,extend_negative=true\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy_both_extend\"\n" +
            "    },\n" +
            "    \"axis=z,extend_positive=false,extend_negative=false\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy\",\n" +
            "      \"x\": 90\n" +
            "    },\n" +
            "    \"axis=z,extend_positive=true,extend_negative=false\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy_pos_extend\",\n" +
            "      \"x\": 90\n" +
            "    },\n" +
            "    \"axis=z,extend_positive=false,extend_negative=true\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy_neg_extend\",\n" +
            "      \"x\": 90\n" +
            "    },\n" +
            "    \"axis=z,extend_positive=true,extend_negative=true\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy_both_extend\",\n" +
            "      \"x\": 90\n" +
            "    },\n" +
            "    \"axis=x,extend_positive=false,extend_negative=false\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy\",\n" +
            "      \"x\": 90,\n" +
            "      \"y\": 90\n" +
            "    },\n" +
            "    \"axis=x,extend_positive=true,extend_negative=false\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy_pos_extend\",\n" +
            "      \"x\": 90,\n" +
            "      \"y\": 90\n" +
            "    },\n" +
            "    \"axis=x,extend_positive=false,extend_negative=true\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy_neg_extend\",\n" +
            "      \"x\": 90,\n" +
            "      \"y\": 90\n" +
            "    },\n" +
            "    \"axis=x,extend_positive=true,extend_negative=true\": {\n" +
            "      \"model\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy_both_extend\",\n" +
            "      \"x\": 90,\n" +
            "      \"y\": 90\n" +
            "    }\n" +
            "  }\n" +
            "}";

    // Model for normal, non-extended branches
    private static final String normalModelJSON = "{\n" +
            "    \"parent\": \"minecraft:block/cube\",\n" +
            "    \"textures\": {\n" +
            "        \"side\": \"tfcr:blocks/wood/bark/WOOD\",\n" +
            "        \"end\": \"tfcr:blocks/wood/log/WOOD\",\n" +
            "        \"leaf\": \"tfcr:blocks/wood/leaves/LEAVES\"\n" +
            "    },\n" +
            "    \"elements\": [\n" +
            "        {\n" +
            "            \"from\": [ LOWX, 0, LOWZ ],\n" +
            "            \"to\": [ HIGHX, 16, HIGHZ ],\n" +
            "            \"faces\": {\n" +
            "                \"down\":  { \"uv\": [ LOWX, LOWZ, HIGHX, HIGHZ ], \"texture\": \"#end\", \"cullface\": \"down\" },\n" +
            "                \"up\":    { \"uv\": [ LOWX, LOWZ, HIGHX, HIGHZ ], \"texture\": \"#end\", \"cullface\": \"up\"   },\n" +
            "                \"north\": { \"texture\": \"#side\" },\n" +
            "                \"south\": { \"texture\": \"#side\" },\n" +
            "                \"west\":  { \"texture\": \"#side\" },\n" +
            "                \"east\":  { \"texture\": \"#side\" }\n" +
            "            }\n" +
            "        }IFLEAFY\n" +
            "    ]\n" +
            "}\n";

    // Model of a branch with extension in the negative axis
    private static final String negExtendJSON = "{\n" +
            "    \"parent\": \"minecraft:block/cube\",\n" +
            "    \"textures\": {\n" +
            "        \"side\": \"tfcr:blocks/wood/bark/WOOD\",\n" +
            "        \"end\": \"tfcr:blocks/wood/log/WOOD\",\n" +
            "        \"leaf\": \"tfcr:blocks/wood/leaves/LEAVES\"\n" +
            "    },\n" +
            "    \"elements\": [\n" +
            "        {\n" +
            "            \"from\": [ LOWX, 0, LOWZ ],\n" +
            "            \"to\": [ HIGHX, 16, HIGHZ ],\n" +
            "            \"faces\": {\n" +
            "                \"up\":    { \"uv\": [ LOWX, LOWZ, HIGHX, HIGHZ ], \"texture\": \"#end\", \"cullface\": \"up\"   },\n" +
            "                \"north\": { \"texture\": \"#side\" },\n" +
            "                \"south\": { \"texture\": \"#side\" },\n" +
            "                \"west\":  { \"texture\": \"#side\" },\n" +
            "                \"east\":  { \"texture\": \"#side\" }\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"from\": [ LOWX_DELTA, -8, LOWX_DELTA ],\n" +
            "            \"to\": [ HIGHX_DELTA, 0, HIGHX_DELTA ],\n" +
            "            \"faces\": {\n" +
            "                \"north\": { \"uv\": [ LOWX_DELTA, 8, HIGHX_DELTA, 16 ], \"texture\": \"#side\" },\n" +
            "                \"south\": { \"uv\": [ LOWX_DELTA, 8, HIGHX_DELTA, 16 ], \"texture\": \"#side\" },\n" +
            "                \"west\":  { \"uv\": [ LOWX_DELTA, 8, HIGHX_DELTA, 16 ], \"texture\": \"#side\" },\n" +
            "                \"east\":  { \"uv\": [ LOWX_DELTA, 8, HIGHX_DELTA, 16 ], \"texture\": \"#side\" }\n" +
            "            }\n" +
            "        }IFLEAFY\n" +
            "    ]\n" +
            "}\n";

    private static final String posExtendJSON = "{\n" +
            "    \"parent\": \"minecraft:block/cube\",\n" +
            "    \"textures\": {\n" +
            "        \"side\": \"tfcr:blocks/wood/bark/WOOD\",\n" +
            "        \"end\": \"tfcr:blocks/wood/log/WOOD\",\n" +
            "        \"leaf\": \"tfcr:blocks/wood/leaves/LEAVES\"\n" +
            "    },\n" +
            "    \"elements\": [\n" +
            "        {\n" +
            "            \"from\": [ LOWX, 0, LOWZ ],\n" +
            "            \"to\": [ HIGHX, 16, HIGHZ ],\n" +
            "            \"faces\": {\n" +
            "                \"down\":  { \"uv\": [ LOWX, LOWZ, HIGHX, HIGHZ ], \"texture\": \"#end\", \"cullface\": \"down\" },\n" +
            "                \"north\": { \"texture\": \"#side\" },\n" +
            "                \"south\": { \"texture\": \"#side\" },\n" +
            "                \"west\":  { \"texture\": \"#side\" },\n" +
            "                \"east\":  { \"texture\": \"#side\" }\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"from\": [ LOWX_DELTA, 16, LOWX_DELTA ],\n" +
            "            \"to\": [ HIGHX_DELTA, 24, HIGHX_DELTA ],\n" +
            "            \"faces\": {\n" +
            "                \"north\": { \"uv\": [ LOWX_DELTA, 0, HIGHX_DELTA, 8 ], \"texture\": \"#side\" },\n" +
            "                \"south\": { \"uv\": [ LOWX_DELTA, 0, HIGHX_DELTA, 8 ], \"texture\": \"#side\" },\n" +
            "                \"west\":  { \"uv\": [ LOWX_DELTA, 0, HIGHX_DELTA, 8 ], \"texture\": \"#side\" },\n" +
            "                \"east\":  { \"uv\": [ LOWX_DELTA, 0, HIGHX_DELTA, 8 ], \"texture\": \"#side\" }\n" +
            "            }\n" +
            "        }IFLEAFY\n" +
            "    ]\n" +
            "}\n";

    private static final String bothExtendJSON = "{\n" +
            "    \"parent\": \"minecraft:block/cube\",\n" +
            "    \"textures\": {\n" +
            "        \"side\": \"tfcr:blocks/wood/bark/WOOD\",\n" +
            "        \"end\": \"tfcr:blocks/wood/log/WOOD\",\n" +
            "        \"leaf\": \"tfcr:blocks/wood/leaves/LEAVES\"\n" +
            "    },\n" +
            "    \"elements\": [\n" +
            "        {\n" +
            "            \"from\": [ LOWX, 0, LOWZ ],\n" +
            "            \"to\": [ HIGHX, 16, HIGHZ ],\n" +
            "            \"faces\": {\n" +
            "                \"north\": { \"texture\": \"#side\" },\n" +
            "                \"south\": { \"texture\": \"#side\" },\n" +
            "                \"west\":  { \"texture\": \"#side\" },\n" +
            "                \"east\":  { \"texture\": \"#side\" }\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"from\": [ LOWX_DELTA, 16, LOWX_DELTA ],\n" +
            "            \"to\": [ HIGHX_DELTA, 24, HIGHX_DELTA ],\n" +
            "            \"faces\": {\n" +
            "                \"north\": { \"uv\": [ LOWX_DELTA, 0, HIGHX_DELTA, 8 ], \"texture\": \"#side\" },\n" +
            "                \"south\": { \"uv\": [ LOWX_DELTA, 0, HIGHX_DELTA, 8 ], \"texture\": \"#side\" },\n" +
            "                \"west\":  { \"uv\": [ LOWX_DELTA, 0, HIGHX_DELTA, 8 ], \"texture\": \"#side\" },\n" +
            "                \"east\":  { \"uv\": [ LOWX_DELTA, 0, HIGHX_DELTA, 8 ], \"texture\": \"#side\" }\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"from\": [ LOWX_DELTA, -8, LOWX_DELTA ],\n" +
            "            \"to\": [ HIGHX_DELTA, 0, HIGHX_DELTA ],\n" +
            "            \"faces\": {\n" +
            "                \"north\": { \"uv\": [ LOWX_DELTA, 8, HIGHX_DELTA, 16 ], \"texture\": \"#side\" },\n" +
            "                \"south\": { \"uv\": [ LOWX_DELTA, 8, HIGHX_DELTA, 16 ], \"texture\": \"#side\" },\n" +
            "                \"west\":  { \"uv\": [ LOWX_DELTA, 8, HIGHX_DELTA, 16 ], \"texture\": \"#side\" },\n" +
            "                \"east\":  { \"uv\": [ LOWX_DELTA, 8, HIGHX_DELTA, 16 ], \"texture\": \"#side\" }\n" +
            "            }\n" +
            "        }IFLEAFY\n" +
            "    ]\n" +
            "}\n";

    // Used for the leafy variants of branches
    private static final String leafElementJSON = ",\n" +
            "        {\n" +
            "            \"from\": [ -0.01, -0.01, -0.01 ],\n" +
            "            \"to\": [ 16.01, 16.01, 16.01 ],\n" +
            "            \"faces\": {\n" +
            "                \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#leaf\", \"tintindex\": 0, \"cullface\": \"down\" },\n" +
            "                \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#leaf\", \"tintindex\": 0, \"cullface\": \"up\" },\n" +
            "                \"north\": { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#leaf\", \"tintindex\": 0, \"cullface\": \"north\" },\n" +
            "                \"south\": { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#leaf\", \"tintindex\": 0, \"cullface\": \"south\" },\n" +
            "                \"west\":  { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#leaf\", \"tintindex\": 0, \"cullface\": \"west\" },\n" +
            "                \"east\":  { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#leaf\", \"tintindex\": 0, \"cullface\": \"east\" }\n" +
            "            }\n" +
            "        }";

    private static final String itemJSON = "{\n" +
            "  \"parent\": \"tfcr:block/branch/WOOD/block_branch_NUM\"\n" +
            "}";

    private static final String itemLeafyJSON = "{\n" +
            "  \"parent\": \"tfcr:block/branch/WOOD/block_branch_NUM_leafy\"\n" +
            "}";

    // Minecraft "logs" tag
    private static final String logsTagsJSON = "{\n" +
            "    \"replace\": false,\n" +
            "    \"values\": [\nVALUES" +
            "    ]\n" +
            "}";

    private static final float DELTA = 0.01f;

    public static void generate() {
        System.out.print("Generating branch assets... ");
        String blockstateDir = GenerateBase.RESOURCE_BASE + File.separator + blockstateLocation;
        String modelDir = GenerateBase.RESOURCE_BASE + File.separator + modelLocation;
        String itemModelDir = GenerateBase.RESOURCE_BASE + File.separator + itemModelLocation;

        // "logs" tag
        String logsTagsDir = GenerateBase.DATA_BASE + GenerateBase.VANILLA_BLOCKS + "logs.json";
        StringBuilder logsTagsBuilder = new StringBuilder();

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

                // Leafy Blockstate JSON
                filePath = blockstateDir + woodType.getName() + "/block_branch_" + i + "_leafy.json";
                toWrite = blockstateLeafyJSON
                        .replace("WOOD", woodType.getName())
                        .replace("NUM", "" + i);

                GenerateBase.writeToFile(filePath, toWrite);

                // Item JSON
                filePath = itemModelDir + woodType.getName() + "/block_branch_" + i + ".json";
                toWrite = itemJSON
                        .replace("WOOD", woodType.getName())
                        .replace("NUM", "" + i);

                GenerateBase.writeToFile(filePath, toWrite);

                // Leafy item JSON
                filePath = itemModelDir + woodType.getName() + "/block_branch_" + i + "_leafy.json";
                toWrite = itemLeafyJSON
                        .replace("WOOD", woodType.getName())
                        .replace("NUM", "" + i);

                GenerateBase.writeToFile(filePath, toWrite);

                // Model JSON 1 (no extending)
                filePath = modelDir + woodType.getName() + "/block_branch_" + i + ".json";
                toWrite = normalModelJSON
                        .replace("LEAVES", "leaves_" + woodType.getName())
                        .replace("WOOD", "wood_" + woodType.getName())
                        .replace("LOWX", "" + (8 - (i / 2)))
                        .replace("LOWZ", "" + (8 - (i / 2)))
                        .replace("HIGHX", "" + (8 + (i / 2)))
                        .replace("HIGHZ", "" + (8 + (i / 2)))
                        .replace("IFLEAFY", "");

                GenerateBase.writeToFile(filePath, toWrite);

                // Model JSON 2 (extend negative only)
                filePath = modelDir + woodType.getName() + "/block_branch_" + i + "_neg_extend.json";
                toWrite = negExtendJSON
                        .replace("LEAVES", "leaves_" + woodType.getName())
                        .replace("WOOD", "wood_" + woodType.getName())
                        .replace("LOWX_DELTA", "" + (8 - (i / 2) - DELTA))
                        .replace("HIGHX_DELTA", "" + (8 + (i / 2) + DELTA))
                        .replace("LOWX", "" + (8 - (i / 2)))
                        .replace("LOWZ", "" + (8 - (i / 2)))
                        .replace("HIGHX", "" + (8 + (i / 2)))
                        .replace("HIGHZ", "" + (8 + (i / 2)))
                        .replace("IFLEAFY", "")
                        ;

                GenerateBase.writeToFile(filePath, toWrite);

                // Model JSON 3 (extend positive only)
                filePath = modelDir + woodType.getName() + "/block_branch_" + i + "_pos_extend.json";
                toWrite = posExtendJSON
                        .replace("LEAVES", "leaves_" + woodType.getName())
                        .replace("WOOD", "wood_" + woodType.getName())
                        .replace("LOWX_DELTA", "" + (8 - (i / 2) - DELTA))
                        .replace("HIGHX_DELTA", "" + (8 + (i / 2) + DELTA))
                        .replace("LOWX", "" + (8 - (i / 2)))
                        .replace("LOWZ", "" + (8 - (i / 2)))
                        .replace("HIGHX", "" + (8 + (i / 2)))
                        .replace("HIGHZ", "" + (8 + (i / 2)))
                        .replace("IFLEAFY", "")
                ;

                GenerateBase.writeToFile(filePath, toWrite);

                // Model JSON 4 (extend both)
                filePath = modelDir + woodType.getName() + "/block_branch_" + i + "_both_extend.json";
                toWrite = bothExtendJSON
                        .replace("LEAVES", "leaves_" + woodType.getName())
                        .replace("WOOD", "wood_" + woodType.getName())
                        .replace("LOWX_DELTA", "" + (8 - (i / 2) - DELTA))
                        .replace("HIGHX_DELTA", "" + (8 + (i / 2) + DELTA))
                        .replace("LOWX", "" + (8 - (i / 2)))
                        .replace("LOWZ", "" + (8 - (i / 2)))
                        .replace("HIGHX", "" + (8 + (i / 2)))
                        .replace("HIGHZ", "" + (8 + (i / 2)))
                        .replace("IFLEAFY", "")
                ;

                GenerateBase.writeToFile(filePath, toWrite);

                // Model JSON 5 (leafy, no extend)
                filePath = modelDir + woodType.getName() + "/block_branch_" + i + "_leafy.json";
                toWrite = normalModelJSON
                        .replace("LEAVES", "leaves_" + woodType.getName())
                        .replace("WOOD", "wood_" + woodType.getName())
                        .replace("LOWX_DELTA", "" + (8 - (i / 2) - DELTA))
                        .replace("HIGHX_DELTA", "" + (8 + (i / 2) + DELTA))
                        .replace("LOWX", "" + (8 - (i / 2)))
                        .replace("LOWZ", "" + (8 - (i / 2)))
                        .replace("HIGHX", "" + (8 + (i / 2)))
                        .replace("HIGHZ", "" + (8 + (i / 2)))
                        .replace("IFLEAFY", leafElementJSON);

                GenerateBase.writeToFile(filePath, toWrite);

                // Model JSON 6 (leafy, extend negative only)
                filePath = modelDir + woodType.getName() + "/block_branch_" + i + "_leafy_neg_extend.json";
                toWrite = negExtendJSON
                        .replace("LEAVES", "leaves_" + woodType.getName())
                        .replace("WOOD", "wood_" + woodType.getName())
                        .replace("LOWX_DELTA", "" + (8 - (i / 2) - DELTA))
                        .replace("HIGHX_DELTA", "" + (8 + (i / 2) + DELTA))
                        .replace("LOWX", "" + (8 - (i / 2)))
                        .replace("LOWZ", "" + (8 - (i / 2)))
                        .replace("HIGHX", "" + (8 + (i / 2)))
                        .replace("HIGHZ", "" + (8 + (i / 2)))
                        .replace("IFLEAFY", leafElementJSON);

                GenerateBase.writeToFile(filePath, toWrite);

                // Model JSON 7 (leafy, extend positive only)
                filePath = modelDir + woodType.getName() + "/block_branch_" + i + "_leafy_pos_extend.json";
                toWrite = posExtendJSON
                        .replace("LEAVES", "leaves_" + woodType.getName())
                        .replace("WOOD", "wood_" + woodType.getName())
                        .replace("LOWX_DELTA", "" + (8 - (i / 2) - DELTA))
                        .replace("HIGHX_DELTA", "" + (8 + (i / 2) + DELTA))
                        .replace("LOWX", "" + (8 - (i / 2)))
                        .replace("LOWZ", "" + (8 - (i / 2)))
                        .replace("HIGHX", "" + (8 + (i / 2)))
                        .replace("HIGHZ", "" + (8 + (i / 2)))
                        .replace("IFLEAFY", leafElementJSON);

                GenerateBase.writeToFile(filePath, toWrite);

                // Model JSON 8 (leafy, extend both)
                filePath = modelDir + woodType.getName() + "/block_branch_" + i + "_leafy_both_extend.json";
                toWrite = bothExtendJSON
                        .replace("LEAVES", "leaves_" + woodType.getName())
                        .replace("WOOD", "wood_" + woodType.getName())
                        .replace("LOWX_DELTA", "" + (8 - (i / 2) - DELTA))
                        .replace("HIGHX_DELTA", "" + (8 + (i / 2) + DELTA))
                        .replace("LOWX", "" + (8 - (i / 2)))
                        .replace("LOWZ", "" + (8 - (i / 2)))
                        .replace("HIGHX", "" + (8 + (i / 2)))
                        .replace("HIGHZ", "" + (8 + (i / 2)))
                        .replace("IFLEAFY", leafElementJSON);

                GenerateBase.writeToFile(filePath, toWrite);

                // Lang file entries
                GenerateBase.appendToLangFile("block.tfcr.branch." + woodType.getName() + ".block_branch_" + i, i + " wide " + woodType.name + " branch");
                GenerateBase.appendToLangFile("block.tfcr.branch." + woodType.getName() + ".block_branch_" + i + "_leafy", i + " wide " + woodType.name + " leafy branch");

                // Vanilla minecraft logs tag for branches
                // Need the actual resource name, not the internal name
                logsTagsBuilder.append("        \"").append("tfcr:branch/").append(woodType.getName()).append("/block_branch_").append(i).append("\"");
                logsTagsBuilder.append(",\n");
                logsTagsBuilder.append("        \"").append("tfcr:branch/").append(woodType.getName()).append("/block_branch_").append(i).append("_leafy").append("\"");
                logsTagsBuilder.append(",\n");
            }

            // Vanilla minecraft logs tag. Note this is 1:1 with WoodType, so is in the outer for loop.
            logsTagsBuilder.append("        \"").append("tfcr:log/").append(woodType.getName()).append("\"");
            if (woodType != WoodType.values()[WoodType.values().length - 1]) { // no commas for last entry
                logsTagsBuilder.append(",");
            }
            logsTagsBuilder.append("\n");
        }

        String toWrite = logsTagsJSON
                .replace("VALUES", logsTagsBuilder.toString());
        GenerateBase.writeToFile(logsTagsDir, toWrite); // Branch + log tags

        System.out.println("Done. Made " + (WoodType.values().length * 7 * 4 * 2) + " branch variant files.");
    }


}