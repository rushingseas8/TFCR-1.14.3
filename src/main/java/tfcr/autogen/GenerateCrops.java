package tfcr.autogen;

import tfcr.data.CropType;
import tfcr.data.Fertility;

import java.io.File;

// Generates dirt blocks
public class GenerateCrops {
    private static final String blockstateLocation = "blockstates/plants/crops/";
    private static final String modelLocation = "models/block/plants/crops/";
    private static final String itemModelLocation = "models/item/plants/crops/";

    private static final String blockStateJSON = "{\n" +
            "    \"variants\": {\n" +
            "        \"age=0\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage0\" },\n" +
            "        \"age=1\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage0\" },\n" +
            "        \"age=2\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage1\" },\n" +
            "        \"age=3\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage1\" },\n" +
            "        \"age=4\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage2\" },\n" +
            "        \"age=5\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage2\" },\n" +
            "        \"age=6\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage3\" },\n" +
            "        \"age=7\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage3\" },\n" +
            "        \"age=8\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage4\" },\n" +
            "        \"age=9\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage4\" },\n" +
            "        \"age=10\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage5\" },\n" +
            "        \"age=11\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage5\" },\n" +
            "        \"age=12\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage6\" },\n" +
            "        \"age=13\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage6\" },\n" +
            "        \"age=14\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage7\" },\n" +
            "        \"age=15\": { \"model\": \"tfcr:block/plants/crops/PLANT/stage7\" }\n" +
            "    }\n" +
            "}\n";

    private static final String modelJSON = "{\n" +
            "    \"parent\": \"block/crop\",\n" +
            "    \"textures\": {\n" +
            "        \"crop\": \"tfcr:blocks/plants/crops/PLANT_STAGE\"\n" +
            "    }\n" +
            "}\n";

    private static final String itemJSON = "{\n" +
            "  \"parent\": \"tfcr:block/plants/crops/PLANT\"\n" +
            "}";


    public static void generate() {
        System.out.print("Generating crop assets... ");
        String blockstateDir = GenerateBase.RESOURCE_BASE + File.separator + blockstateLocation;
        String modelDir = GenerateBase.RESOURCE_BASE + File.separator + modelLocation;
        String itemModelDir = GenerateBase.RESOURCE_BASE + File.separator + itemModelLocation;

        GenerateBase.appendSpacerToLangFile();

        for (CropType type : CropType.values()) {
            // Blockstate JSON
            String filePath = blockstateDir + type.getName() + ".json";
            String toWrite = blockStateJSON.replace("PLANT", type.getName());

            GenerateBase.writeToFile(filePath, toWrite);

            // Item JSON
            filePath = itemModelDir + type.getName() + ".json";
            toWrite = itemJSON.replace("PLANT", type.getName());

            GenerateBase.writeToFile(filePath, toWrite);

            // Model JSON (one per stage)
            for (int stage = 0; stage < 8; stage++) {
                filePath = modelDir + type.getName() + File.separator + "stage" + stage + ".json";

                toWrite = modelJSON
                        .replace("PLANT", type.getName())
                        .replace("STAGE", "" + stage);

                GenerateBase.writeToFile(filePath, toWrite);
            }

            // Lang file entries
            GenerateBase.appendToLangFile("block.tfcr.plant.crop." + type.getName(), GenerateBase.guessName(type.getName()));
        }

        System.out.println("Done");
    }

}
