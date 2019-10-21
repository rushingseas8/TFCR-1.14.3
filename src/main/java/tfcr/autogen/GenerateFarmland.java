package tfcr.autogen;

import tfcr.data.Fertility;
import tfcr.data.WoodType;

import java.io.File;

// Generates farmland/dirt/grass
public class GenerateFarmland {
    private static final String blockstateLocation = "blockstates/farmland/";
    private static final String modelLocation = "models/block/farmland/";
    private static final String itemModelLocation = "models/item/farmland/";

    private static final String blockStateJSON = "{\n" +
            "  \"variants\": {\n" +
            "    \"moisture=0\": { \"model\": \"tfcr:block/farmland/TYPEDRY\" },\n" +
            "    \"moisture=1\": { \"model\": \"tfcr:block/farmland/TYPEWET\" },\n" +
            "    \"moisture=2\": { \"model\": \"tfcr:block/farmland/TYPEWET\" },\n" +
            "    \"moisture=3\": { \"model\": \"tfcr:block/farmland/TYPEWET\" },\n" +
            "    \"moisture=4\": { \"model\": \"tfcr:block/farmland/TYPEWET\" },\n" +
            "    \"moisture=5\": { \"model\": \"tfcr:block/farmland/TYPEWET\" },\n" +
            "    \"moisture=6\": { \"model\": \"tfcr:block/farmland/TYPEWET\" },\n" +
            "    \"moisture=7\": { \"model\": \"tfcr:block/farmland/TYPEWET\" }\n" +
            "  }\n" +
            "}\n";

    private static final String modelJSON = "{\n" +
            "    \"parent\": \"minecraft:block/template_farmland\",\n" +
            "    \"textures\": {\n" +
            "        \"particle\": \"tfcr:blocks/dirt/DIRT\",\n" +
            "        \"dirt\": \"tfcr:blocks/dirt/DIRT\",\n" +
            "        \"top\": \"tfcr:blocks/farmland/TYPE\"\n" +
            "    }\n" +
            "}\n";

    private static final String itemJSON = "{\n" +
            "  \"parent\": \"tfcr:block/farmland/TYPE\"\n" +
            "}";


    public static void generate() {
        System.out.print("Generating farmland assets... ");
        String blockstateDir = GenerateBase.RESOURCE_BASE + File.separator + blockstateLocation;
        String modelDir = GenerateBase.RESOURCE_BASE + File.separator + modelLocation;
        String itemModelDir = GenerateBase.RESOURCE_BASE + File.separator + itemModelLocation;

        GenerateBase.appendSpacerToLangFile();

        for (Fertility fertility : Fertility.values()) {
            // Blockstate JSON
            String filePath = blockstateDir + fertility.getName() + ".json";
            String toWrite = blockStateJSON
                    .replace("TYPEDRY", fertility.getName() + "_farmland_dry")
                    .replace("TYPEWET", fertility.getName() + "_farmland_wet");

            GenerateBase.writeToFile(filePath, toWrite);

            // Item JSON
            filePath = itemModelDir + fertility.getName() + ".json";
            toWrite = itemJSON.replace("TYPE", fertility.getName() + "_farmland_dry");

            GenerateBase.writeToFile(filePath, toWrite);

            // Model JSON (dry)
            filePath = modelDir + fertility.getName() + "_farmland_dry.json";
            toWrite = modelJSON
                    .replace("DIRT", fertility.getName() + "_dirt")
                    .replace("TYPE", fertility.getName() + "_farmland_dry");

            GenerateBase.writeToFile(filePath, toWrite);

            // Model JSON (wet)
            filePath = modelDir + fertility.getName() + "_farmland_wet.json";
            toWrite = modelJSON
                    .replace("DIRT", fertility.getName() + "_dirt")
                    .replace("TYPE", fertility.getName() + "_farmland_wet");

            GenerateBase.writeToFile(filePath, toWrite);


            // Lang file entries
            GenerateBase.appendToLangFile("block.tfcr.farmland." + fertility.getName(), GenerateBase.guessName(fertility.getName() + "_farmland"));
        }
        System.out.println("Done");
    }

}
