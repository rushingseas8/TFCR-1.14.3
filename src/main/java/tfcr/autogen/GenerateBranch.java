package tfcr.autogen;

import tfcr.TFCR;

import java.io.*;

public class GenerateBranch {
    private static final String blockstateLocation = "blockstates/branch/";
    private static final String itemModelLocation = "models/item/branch/";

    private static final String blockStateJSON = "{\n" +
            "  \"variants\": {\n" +
            "    \"axis=y\": {\n" +
            "      \"model\": \"tfcr:block/branch/block_branch_NUM\"\n" +
            "    },\n" +
            "    \"axis=z\": {\n" +
            "      \"model\": \"tfcr:block/branch/block_branch_NUM\",\n" +
            "      \"x\": 90\n" +
            "    },\n" +
            "    \"axis=x\": {\n" +
            "      \"model\": \"tfcr:block/branch/block_branch_NUM\",\n" +
            "      \"x\": 90,\n" +
            "      \"y\": 90\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private static final String itemJSON = "{\n" +
            "  \"parent\": \"tfcr:block/branch/block_branch_NUM\"\n" +
            "}";

    public static void generate() {
        System.out.print("Generating branch assets... ");
        String blockstateDir = GenerateBase.RESOURCE_BASE + File.separator + blockstateLocation;
        String itemModelDir = GenerateBase.RESOURCE_BASE + File.separator + itemModelLocation;

        GenerateBase.appendSpacerToLangFile();

        for (int i = 2; i <= 14; i+=2) {
            // Blockstate JSON
            String filePath = blockstateDir + "block_branch_" + i + ".json";
            String toWrite = blockStateJSON.replace("NUM", "" + i);

            GenerateBase.writeToFile(filePath, toWrite);

            // Item JSON
            filePath = itemModelDir + "block_branch_" + i + ".json";
            toWrite = itemJSON.replace("NUM", "" + i);

            GenerateBase.writeToFile(filePath, toWrite);

            // Lang file entries
            GenerateBase.appendToLangFile("block.tfcr.branch.block_branch_" + i, i + " wide branch");
        }
        System.out.println("Done");
    }


}