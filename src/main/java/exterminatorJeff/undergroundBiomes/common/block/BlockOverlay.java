
package exterminatorJeff.undergroundBiomes.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/*
 * @author Zeno410
 */
public class BlockOverlay extends Block {

    private final String overlayFileName;
    private IIcon overlayTexture;
    private int renderID;
    private static boolean shown = false;

    public BlockOverlay(String overlayName) {
        super(Material.rock);
        if (overlayName.contains("null")) throw new RuntimeException();
        this.overlayFileName = overlayName;
        this.textureName = overlayName;
        renderID = super.getRenderType();

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        // Block icons registered by UB blocks.
        overlayTexture = iconRegister.registerIcon(overlayFileName);
        TextureMap textureMap = (TextureMap) iconRegister;
        // showTextureNames(textureMap);
        super.registerBlockIcons(iconRegister);
    }

    @Override
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return overlayTexture;
    }

    @Override
    public int getRenderType() {
        return renderID;
    }

    public static void showTextureNames(TextureMap textureMap) {
        // if (shown) return;
        shown = true;
    }
}
