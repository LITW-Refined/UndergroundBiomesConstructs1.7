package exterminatorJeff.undergroundBiomes.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import Zeno410Utils.Acceptor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exterminatorJeff.undergroundBiomes.api.NamedBlock;
import exterminatorJeff.undergroundBiomes.common.UndergroundBiomes;
import exterminatorJeff.undergroundBiomes.worldGen.BiomeUndergroundDecorator;

public abstract class BlockMetadataBase extends BlockStone {

    public static boolean test963 = false;
    protected IIcon[] textures = { null, null, null, null, null, null, null, null };
    protected boolean replaceableByOre = true;
    public final NamedBlock namer;
    public static final int metadatas = 8;
    private int renderID;
    protected float ubExplosionResistance;
    private final Acceptor<Double> hardnessUpdater = new Acceptor<Double>() {

        public void accept(Double newHardness) {
            // we use the standard rather than the passed since we're having to cut back resistance
            setHardness(1.5F * UndergroundBiomes.hardnessModifier());
        }
    };

    private final Acceptor<Double> resistanceUpdater = new Acceptor<Double>() {

        public void accept(Double newResistance) {
            // we use the standard rather than the passed since we're having to cut back resistance
            setResistance(1.66F * UndergroundBiomes.resistanceModifier());
            ubExplosionResistance = blockResistance;
        }
    };

    public BlockMetadataBase(NamedBlock block) {
        super();
        this.setBlockName(block.internal());
        this.setCreativeTab(UndergroundBiomes.tabModBlocks);
        namer = block;
        try {
            renderID = super.getRenderType();
        } catch (java.lang.NoSuchFieldError e) {
            renderID = super.getRenderType();
        } catch (Error e) {
            renderID = super.getRenderType();
        }
        UndergroundBiomes.instance()
            .settings().hardnessModifier.informOnChange(this.hardnessUpdater);
        hardnessUpdater.accept(
            UndergroundBiomes.instance()
                .settings().hardnessModifier.value());
        UndergroundBiomes.instance()
            .settings().resistanceModifier.informOnChange(this.resistanceUpdater);
        resistanceUpdater.accept(
            UndergroundBiomes.instance()
                .settings().resistanceModifier.value());

    }

    @Override
    public int getRenderType() {
        return renderID;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        for (int i = 0; i < 8; i++) {
            textures[i] = iconRegister.registerIcon("undergroundbiomes:" + getBlockName(i));
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        IIcon result = textures[metadata & 7];
        if (result == null) throw new RuntimeException("" + metadata);
        return textures[metadata & 7];
    }

    @SideOnly(Side.CLIENT)
    protected String getTextureName() {
        return this.textureName == null ? "MISSING_ICON_BLOCK_" + getIdFromBlock(this) + "_" + this.getUnlocalizedName()
            : this.textureName;
    }

    public void getSubBlocks(Item id, CreativeTabs tabs, List<ItemStack> list) {
        for (int i = 0; i < 8; i++) {
            list.add(new ItemStack(id, 1, i));
        }
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata & 7;
    }

    @Override
    public boolean isReplaceableOreGen(World world, int x, int y, int z, Block target) {

        if (target == null) return this.replaceableByOre;
        // this obnoxious call is needed because something is redoing ore placement without calling my routines
        if (!UndergroundBiomes.instance()
            .settings().newGeneration.value()) {
            BiomeUndergroundDecorator.needsRedo(x, z, world);
        }
        return this.replaceableByOre && target.getUnlocalizedName()
            .equals(Blocks.stone.getUnlocalizedName());
    }

    public float getBlockHardness(int meta) {
        float result = blockHardness;
        return result;
    }

    public float getBlockExplosionResistance(int meta) {
        float result = this.ubExplosionResistance;
        return result;
    }

    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        return getBlockExplosionResistance(getDamageValue(world, x, y, z) & 7);
    }

    public float getBlockHardness(World world, int x, int y, int z) {
        return getBlockHardness(getDamageValue(world, x, y, z) & 7);
    }

    protected ItemStack createStackedBlock(int metadata) {
        return new ItemStack(this, 1, metadata & 7);
    }

    public ItemStack itemDropped(int metadata, Random random, int fortune, int y) {
        return new ItemStack(this, 1, metadata & 7);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = 1;
        ItemStack stack = itemDropped(metadata, world.rand, fortune, y);

        if ((fortune != 0) && (UndergroundBiomes.fortuneAffected.contains(
            stack.getItem()
                .getUnlocalizedName()))) {
            // Fortune III gives up to 4 items
            int j = world.rand.nextInt(fortune + 2);
            count = (j < 1) ? 1 : j;
        }
        for (int i = 0; i < count; i++) {
            ret.add(stack);
        }
        return ret;
    }

    public abstract String getBlockTypeName(int index);

    public abstract boolean hasRareDrops();

    public String getBlockName(int index) {
        return getBlockTypeName(index);
    }
}
