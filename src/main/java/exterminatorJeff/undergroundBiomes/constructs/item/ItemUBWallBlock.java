
package exterminatorJeff.undergroundBiomes.constructs.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import exterminatorJeff.undergroundBiomes.api.NamedItem;
import exterminatorJeff.undergroundBiomes.api.UBIDs;
import exterminatorJeff.undergroundBiomes.common.block.BlockMetadataBase;
import exterminatorJeff.undergroundBiomes.constructs.block.UBWall;

/**
 *
 * @author Zeno410
 */
public class ItemUBWallBlock extends ItemBlock {

    private Block ubWall;
    private NamedItem name;
    protected String[] names;

    private static String[] names(Block appearance, NamedItem name) {
        String[] result = new String[BlockMetadataBase.metadatas];
        for (int i = 0; i < BlockMetadataBase.metadatas; i++) {
            BlockMetadataBase sourceBlock = ((UBWall) appearance).baseBlock();
            result[i] = sourceBlock.getBlockTypeName(i) + "." + name.internal();
        }
        return result;
    }

    public ItemUBWallBlock(Block ubBlock) {
        this(ubBlock, UBIDs.UBWallsItemName);
    }

    public ItemUBWallBlock(Block ubBlock, NamedItem name) {
        // super(_structure, appearance,names(appearance,_name));
        super(ubBlock);
        // this(Blocks.cobblestone_wall, ubBlock,names(ubBlock,name));

        this.names = names(ubBlock, name);
        this.ubWall = ubBlock;
        this.name = name;
        this.setUnlocalizedName(name.internal());
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        // this.setUnlocalizedName(_name.external());
        // bFull3D = true;
    }

    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return ((UBWall) ubWall).baseBlock()
            .getUnlocalizedName() + "." + (String) this.names[stack.getItemDamage()];
    }
}
