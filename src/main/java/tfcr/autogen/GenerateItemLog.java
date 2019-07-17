package tfcr.autogen;

import tfcr.data.WoodType;

import java.io.File;

public class GenerateItemLog {
    private static final String itemModelLocation = "models/item/itemlog/";

    private static final String itemJSON = "{\n" +
            "  \"parent\": \"item/generated\",\n" +
            "  \"textures\": {\n" +
            "    \"layer0\": \"tfcr:item/wood/itemlog/WOOD\"\n" +
            "  }\n" +
            "}";


    public static void generate() {
        System.out.print("Generating item log assets... ");
        String itemModelDir = GenerateBase.RESOURCE_BASE + File.separator + itemModelLocation;

        GenerateBase.appendSpacerToLangFile();

        for (WoodType woodType : WoodType.values()) {
            // Item JSON
            String filePath = itemModelDir + woodType.getName() + ".json";
            String toWrite = itemJSON.replace("WOOD", woodType.getName());

            GenerateBase.writeToFile(filePath, toWrite);

            // Lang file entries
            GenerateBase.appendToLangFile("item.tfcr.itemlog." + woodType.getName(), woodType.name + " Log");
        }
        System.out.println("Done");
    }
}
