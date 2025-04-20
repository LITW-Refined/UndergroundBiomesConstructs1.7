package exterminatorJeff.undergroundBiomes.intermod;

import cpw.mods.fml.common.Loader;

public enum ModIds {

    BOP("BiomesOPlenty"),
    HIGHLANDS("Highlands"),;

    public final String modId;

    ModIds(String modId) {
        this.modId = modId;
    }

    public boolean isLoaded() {
        return Loader.isModLoaded(modId);
    }
}
