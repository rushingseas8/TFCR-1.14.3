package tfcr.utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;
import tfcr.TFCR;
import tfcr.data.WoodType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateHelper {

    public static String getTreeTemplateLocation(WoodType woodType, int age) {
        return woodType.getName() + "/age_" + age;
    }

    public static Template getTemplate(World world, String name) {
        if (world.isRemote) {
            System.out.println("getTemplate failed, since world is remote.");
            return null;
        }
        ResourceLocation location = new ResourceLocation(TFCR.MODID, name);
        Template template = ((ServerWorld) world).getStructureTemplateManager().getTemplate(location);
        if (template == null) {
            System.out.println("Failed to get template at location: \"" + location + "\".");
        }
        return template;
    }


    /**
     * Exposes the internal list of BlockInfo objects in a Template via reflection.
     * @param template The Template to get the blocks from.
     * @return A List of Template.BlockInfo objects in the provided Template.
     */
    // This warning arises during the cast from Object -> (List<List<Template.BlockInfo>>).
    // It should succeed, but cannot be checked at compile time.
    @SuppressWarnings("unchecked")
    public static List<Template.BlockInfo> getBlocks(Template template) {
        if (template == null) {
            return null;
        }

        try {
            Field templateBlocks = Template.class.getDeclaredField("blocks");
            templateBlocks.setAccessible(true);

            List<List<Template.BlockInfo>> blocks = (List<List<Template.BlockInfo>>) templateBlocks.get(template);

            ArrayList<Template.BlockInfo> blockInfos = new ArrayList<>();
            for (List<Template.BlockInfo> blockList : blocks) {
                blockInfos.addAll(blockList);
            }

            return blockInfos;
        } catch (NoSuchFieldException n) {
            n.printStackTrace();
            System.out.println("Failed to get field \"blocks\" from Template. Did the API change?");
        } catch (IllegalAccessException c) {
            c.printStackTrace();
            System.out.println("Failed to access field \"blocks\" from Template. Security issue?");
        }

        return null;
    }

    /**
     * Gets a map of BlockPos to IBlockState from a Template using reflection.
     * @param template The Template to get the blocks from.
     * @return A map of all the non-empty IBlockStates and their positions
     */
    public static Map<BlockPos, BlockState> getBlockMap(Template template) {
        List<Template.BlockInfo> templateBlockList = getBlocks(template);
        return templateBlockList == null ?
                null :
                templateBlockList.stream().collect(Collectors.toMap(a -> a.pos, a -> a.state));
    }
}
