/**
 *
 * @author Zeno410
 */

package exterminatorJeff.undergroundBiomes.constructs.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import exterminatorJeff.undergroundBiomes.api.NamedBlock;
import exterminatorJeff.undergroundBiomes.common.UndergroundBiomes;
import exterminatorJeff.undergroundBiomes.common.block.BlockMetadataBase;
import exterminatorJeff.undergroundBiomes.constructs.entity.UndergroundBiomesTileEntity;
import exterminatorJeff.undergroundBiomes.constructs.util.UndergroundBiomesBlock;
import exterminatorJeff.undergroundBiomes.constructs.util.UndergroundBiomesBlockList;

public class UBWallBase extends BlockWall implements ITileEntityProvider {

    private int storedID;
    private NamedBlock name;

    public UBWallBase(BlockMetadataBase _baseBlock, NamedBlock namer) {
        super(_baseBlock);
        name = namer;
        this.isBlockContainer = false;
        // this.setCreativeTab(UndergroundBiomes.tabModBlocks);
        this.setCreativeTab(null);
        this.setBlockName("wall");
    }

    public void register() {
        name.register(storedID, this);
    }

    @Override
    public void registerBlockIcons(IIconRegister arg0) {
        // super.registerBlockIcons(arg0);
    }

    public void reRegister() {
        name.reRegister(storedID, this);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    public TileEntity createNewTileEntity(World world, int i) {
        throw new RuntimeException();
    }

    @Override
    public TileEntity createTileEntity(World world, int i) {
        return new UndergroundBiomesTileEntity();
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        if (!(UndergroundBiomes.wallsOn())) return;
        for (int i = 0; i < UndergroundBiomesBlockList.detailedBlockCount; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {}

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     *
     * no action; normally removes the TileEntity. However, the tile entity is needed for the item drop
     * procedure. The actual drop is at the end of getBlockDropped. Kludgey.
     * 
     * 
     * /**
     * Called when the block receives a BlockEvent - see World.addBlockEvent. By default, passes it on to the tile
     * entity at this location. Args: world, x, y, z, blockID, EventID, event parameter
     */

    public final UndergroundBiomesTileEntity ubTileEntity(IBlockAccess world, int x, int y, int z) {
        UndergroundBiomesTileEntity result;
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof UndergroundBiomesTileEntity) {
            result = (UndergroundBiomesTileEntity) tileEntity;
            return result;
        }
        return null;
    }

    public final UndergroundBiomesBlock ubBlock(IBlockAccess world, int x, int y, int z) {
        return UndergroundBiomesBlockList.indexed(ubTileEntity(world, x, y, z).masterIndex());
    }

    public final UndergroundBiomesBlock safeUBBlock(IBlockAccess world, int x, int y, int z) {
        UndergroundBiomesTileEntity entity = ubTileEntity(world, x, y, z);
        if (entity == null) return ubBlock(0);
        return UndergroundBiomesBlockList.indexed(ubTileEntity(world, x, y, z).masterIndex());
    }

    public final UndergroundBiomesBlock ubBlock(int reference) {
        return UndergroundBiomesBlockList.indexed(reference);
    }

    @Override
    public int getDamageValue(World world, int x, int y, int z) {
        UndergroundBiomesTileEntity entity = ubTileEntity(world, x, y, z);
        if (entity == null) return damageDropped(0);
        return damageDropped(ubTileEntity(world, x, y, z).masterIndex());
    }

    @Override
    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
        super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
        TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
        return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return safeUBBlock(world, x, y, z).hardness();
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return ubBlock(metadata).icon();
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int metadataFromEntity = ((UndergroundBiomesTileEntity) (world.getTileEntity(x, y, z))).masterIndex();
        return ubBlock(metadataFromEntity).icon();
    }

    @Override
    public boolean isReplaceableOreGen(World world, int x, int y, int z, Block target) {
        return false;
    }

    public float getBlockExplosionResistance(int meta) {
        return ubBlock(meta).explosionResistance();
    }

    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        return safeUBBlock(world, x, y, z).explosionResistance();
    }

    @Override
    public int getRenderType() {
        return 32;
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    public ItemStack itemDropped(int metadata, Random random, int fortune, int y) {
        return new ItemStack(this, 1, metadata);
    }

    public String getBlockName(int meta) {
        return ubBlock(meta).name();
    }

    @Override
    public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int p_149725_5_) {
        super.onBlockPreDestroy(world, x, y, z, p_149725_5_);
        // the tile entity we need for the drops is destroyed during the drop procedure
        // so we have to grab the index here
        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity != null && (entity instanceof UndergroundBiomesTileEntity)) {
            cacheCode(x, y, z, ubBlock(world, x, y, z), world);
        }
    }

    private void cacheCode(int x, int y, int z, UndergroundBiomesBlock code, World world) {
        UndergroundBiomes.instance()
            .ubCodeLocations(world)
            .add(x, y, z, code);
    }

    private UndergroundBiomesBlock unCacheCode(int x, int y, int z, World world) {
        UndergroundBiomesBlock result;
        result = UndergroundBiomes.instance()
            .ubCodeLocations(world)
            .get(x, y, z);
        UndergroundBiomes.instance()
            .ubCodeLocations(world)
            .remove(x, y, z);
        return result;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {

        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for (int i = 0; i < count; i++) {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null) {
                int index = this.unCacheCode(x, y, z, world).index;
                ret.add(new ItemStack(item, 1, index));
            }
        }

        world.removeTileEntity(x, y, z);
        return ret;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int p_149664_5_) {
        super.onBlockDestroyedByPlayer(world, x, y, z, p_149664_5_);
        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity != null && (entity instanceof UndergroundBiomesTileEntity)) {
            world.removeTileEntity(x, y, z);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, entity, itemStack);
        // if (world.isRemote) {
        int index = itemStack.getItemDamage();
        UndergroundBiomesTileEntity target = (UndergroundBiomesTileEntity) (world.getTileEntity(x, y, z));
        if (target == null) {
            // or maybe not
            target = new UndergroundBiomesTileEntity();
            target.setWorldObj(world);
            world.addTileEntity(target);
            world.setTileEntity(x, y, z, target);
        }
        target.setMasterIndex(index);
        target = (UndergroundBiomesTileEntity) (world.getTileEntity(x, y, z));

        index = target.masterIndex();

        // }
    }
}
