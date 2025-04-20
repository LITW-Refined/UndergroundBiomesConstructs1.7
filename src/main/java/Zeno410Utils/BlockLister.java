
package Zeno410Utils;

import java.util.Set;
import java.util.TreeSet;

import net.minecraft.block.Block;

/**
 *
 * @author Zeno410
 */
public class BlockLister {

    public void listRegistrations() {
        Set<Object> keys = Block.blockRegistry.getKeys();
        TreeSet<Object> sortedKeys = new TreeSet<Object>();
        for (Object name : Block.blockRegistry.getKeys()) {
            sortedKeys.add(name);
        }
        for (Object name : sortedKeys) {
            Object block = Block.blockRegistry.getObject(name);
            int ID = Block.blockRegistry.getIDForObject(block);
        }

    }

}
