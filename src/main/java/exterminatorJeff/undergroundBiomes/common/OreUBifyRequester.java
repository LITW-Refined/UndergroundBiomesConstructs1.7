
package exterminatorJeff.undergroundBiomes.common;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.teammetallurgy.metallurgy.metals.MetalBlock;

import Zeno410Utils.MinecraftName;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import exterminatorJeff.undergroundBiomes.api.UBAPIHook;
import exterminatorJeff.undergroundBiomes.api.UBOreTexturizer;

/**
 *
 * @author Zeno410
 */
public class OreUBifyRequester implements UBOreTexturizer {

    private HashSet<UBifyRequest> waitingRequests = new HashSet<UBifyRequest>();
    private boolean alreadyRun = false;

    OreUBifyRequester() {
        UBAPIHook.ubAPIHook.ubOreTexturizer = this;
    }

    @Deprecated
    public void setupUBOre(Block oreBlock, int metadata, String overlayName, FMLPreInitializationEvent event) {
        assert (oreBlock != null);
        assert (metadata >= 0);
        assert (metadata < 16);
        assert (overlayName != null);
        UndergroundBiomes.instance()
            .oreUBifier()
            .setupUBOre(oreBlock, overlayName, metadata, minecraftName(oreBlock, metadata), event);
    }

    @Deprecated
    public void requestUBOreSetup(Block oreBlock, int metadata, String overlayName) throws BlocksAreAlreadySet {
        assert (oreBlock != null);
        assert (metadata >= 0);
        assert (metadata < 16);
        assert (overlayName != null);
        waitingRequests.add(new UBifyRequestWithMetadata(oreBlock, metadata, overlayName));
    }

    public void setupUBOre(Block oreBlock, int metadata, String overlayName, String blockName,
        FMLPreInitializationEvent event) {
        setupUBOre(oreBlock, metadata, overlayName, new MinecraftName(blockName), event);
    }

    private void setupUBOre(Block oreBlock, int metadata, String overlayName, MinecraftName blockName,
        FMLPreInitializationEvent event) {
        UndergroundBiomes.instance()
            .oreUBifier()
            .setupUBOre(oreBlock, overlayName, metadata, blockName, event);
    }

    public void requestUBOreSetup(Block oreBlock, int metadata, String overlayName, String blockName)
        throws BlocksAreAlreadySet {
        assert (oreBlock != null);
        assert (metadata >= 0);
        assert (metadata < 16);
        assert (overlayName != null);
        MinecraftName properName = new MinecraftName(blockName);
        if (!properName.legit()) {
            properName = minecraftName(oreBlock, metadata);
            if (!properName.legit()) {
                new MinecraftName(blockName);
            }
        }
        waitingRequests.add(new UBifyRequestWithMetadata(oreBlock, metadata, overlayName, properName));
    }

    private class UBifyRequest {

        final Block ore;
        final String overlayName;

        UBifyRequest(Block ore, String overlayName) {
            this.ore = ore;
            this.overlayName = overlayName;
        }

        void fulfill(FMLPreInitializationEvent event) {
            setupUBOre(ore, overlayName, event);
        }
    }

    private class UBifyRequestWithMetadata extends UBifyRequest {

        final int metadata;
        final MinecraftName name;

        UBifyRequestWithMetadata(Block ore, int metadata, String overlayName, MinecraftName name) {
            super(ore, overlayName);
            this.metadata = metadata;
            this.name = name;
        }

        UBifyRequestWithMetadata(Block ore, int metadata, String overlayName) {
            this(ore, metadata, overlayName, minecraftName(ore, metadata));
        }

        @Override
        void fulfill(FMLPreInitializationEvent event) {
            setupUBOre(ore, metadata, overlayName, name, event);
        }
    }

    public void setupUBOre(Block oreBlock, String overlayName, FMLPreInitializationEvent event) {
        UndergroundBiomes.instance()
            .oreUBifier()
            .setupUBOre(oreBlock, overlayName, event);
    }

    public void requestUBOreSetup(Block oreBlock, String overlayName) throws BlocksAreAlreadySet {
        if (alreadyRun) {
            BlocksAreAlreadySet error = new BlocksAreAlreadySet(oreBlock, overlayName);
            if (UndergroundBiomes.crashOnProblems()) throw error;
        } else {
            waitingRequests.add(new UBifyRequest(oreBlock, overlayName));
        }
    }

    void fulfillRequests(FMLPreInitializationEvent event) {
        // this should not be run by anyting other than the Underground Biomes Constructs object
        alreadyRun = true;
        for (UBifyRequest request : waitingRequests) {
            request.fulfill(event);
        }
        waitingRequests.clear();
    }

    public void redoOres(int x, int z, World world) {
        UndergroundBiomes.instance()
            .redoOres(x, z, world);
    }

    private static MinecraftName minecraftName(Block block, int meta) {
        if (block instanceof MetalBlock) {
            return new MinecraftName(((MetalBlock) block).getUnlocalizedName(meta));
        }
        return new MinecraftName(block.getUnlocalizedName());
    }
}
