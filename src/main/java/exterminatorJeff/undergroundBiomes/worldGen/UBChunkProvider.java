package exterminatorJeff.undergroundBiomes.worldGen;

import java.util.WeakHashMap;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import Zeno410Utils.PlaneLocation;
import exterminatorJeff.undergroundBiomes.common.UndergroundBiomes;

/**
 *
 * @author Zeno410
 */
public class UBChunkProvider extends ChunkProviderWrapper {

    BiomeUndergroundDecorator decorator;
    WeakHashMap<PlaneLocation, Chunk> generatingChunks = new WeakHashMap<PlaneLocation, Chunk>();
    int level = 0;
    public final int dimension;

    public UBChunkProvider(IChunkProvider toWrap, BiomeUndergroundDecorator _decorator, int dimension) {
        super(toWrap);

        decorator = _decorator;
        this.dimension = dimension;
    }

    @Override
    public Chunk provideChunk(int i, int j) {
        PlaneLocation place = new PlaneLocation(i, j);
        if (UndergroundBiomes.instance()
            .settings().newGeneration.value()) {
            Chunk result = generatingChunks.get(place);
            if (result != null) return result;
        }
        Chunk result = super.provideChunk(i, j);
        if (UndergroundBiomes.instance()
            .settings().newGeneration.value()) {
            // we're storing to improve speed and reduce insanity
            generatingChunks.put(place, result);
            // decorator.replaceChunkBlocks(result, i, j, dimension);
        } else {
            // we replace early for speed
            decorator.replaceChunkBlocks(result, i, j, dimension);
        }

        return result;
    }

    @Override
    public void populate(IChunkProvider ichunkprovider, int i, int j) {
        super.populate(ichunkprovider, i, j);
        if (UndergroundBiomes.instance()
            .settings().newGeneration.value()) {
            Chunk target = ichunkprovider.provideChunk(i, j);
            if (target.isTerrainPopulated) {
                decorator.replaceChunkBlocks(target, i, j, dimension);
                decorator.replaceChunkOres(ichunkprovider, i, j);
            }
        } else {
            // replace ores anyway
            decorator.replaceChunkOres(ichunkprovider, i, j);
        }
    }

}
