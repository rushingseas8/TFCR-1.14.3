package tfcr.autogen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateBase {

    public static final File RESOURCE_BASE = new File("./src/main/resources/assets/tfcr");

    private static StringBuilder langFile = new StringBuilder("{");

    public static void writeToFile(String path, String contents) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            System.out.print("Trying to create directory: \"" + file.getParentFile() + "\"... ");
            boolean success = file.getParentFile().mkdir();
            System.out.println(success ? "Success." : "Failed!");
        }

        try (
            BufferedWriter bos = new BufferedWriter(new FileWriter(file))
        ) {
            bos.write(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendToLangFile(String internalName, String humanReadableName) {
        langFile.append("\t\"").append(internalName).append("\": \"").append(humanReadableName).append("\",\n");
    }

    public static void appendSpacerToLangFile() {
        langFile.append("\n");
    }

    public static void main(String[] args) {
        System.out.println("---");
        System.out.println("Generating block assets.");
        System.out.println("---");

        GenerateBranch.generate();
        GenerateLog.generate();
        GenerateLeaves.generate();
        GenerateSapling.generate();
        GenerateMiscBlocks.generate();

        System.out.println("---");
        System.out.println("Generating item assets.");
        System.out.println("---");

        GenerateItemLog.generate();
        GenerateMiscItems.generate();

        /*

  "block.tfcr.block_branch": "Branch",
  "block.tfcr.marsh_marigold": "Marsh Marigold",

  "item.tfcr.ore.bismuthinite": "Bismuthinite"
         */


        System.out.println("---");
        System.out.println("Finalizing lang file.");
        System.out.println("---");

        appendSpacerToLangFile();
        appendToLangFile("block.tfcr.block_branch", "Branch");
        appendToLangFile("block.tfcr.marsh_marigold", "Marsh Marigold");

        appendSpacerToLangFile();
        appendToLangFile("item.tfcr.ore.bismuthinite", "Bismuthinite");

        appendSpacerToLangFile();
        appendToLangFile("itemGroup.tfcrWood", "TFCR Wood");

        langFile.delete(langFile.length() - 2, langFile.length());
        langFile.append("\n}\n");
        writeToFile(RESOURCE_BASE + File.separator + "lang/en_us.json", langFile.toString());

        System.out.println("---");
        System.out.println("Done generating.");
        System.out.println("---");
    }
}