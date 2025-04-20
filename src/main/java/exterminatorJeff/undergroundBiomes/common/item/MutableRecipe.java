
package exterminatorJeff.undergroundBiomes.common.item;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

/**
 *
 * @author Zeno410
 */
public class MutableRecipe {

    private IRecipe current;

    public void set(IRecipe newRecipe) {
        if (current != null) {
            // remove current recipe
            CraftingManager.getInstance()
                .getRecipeList()
                .remove(current);
        }
        current = newRecipe;
        if (current != null) {
            CraftingManager.getInstance()
                .getRecipeList()
                .add(current);
        }
    }

}
