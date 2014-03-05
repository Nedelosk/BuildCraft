/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.api.blueprints;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.minecraft.block.Block;

public class BlueprintManager {

	private static class SchematicConstructor {
		Class clas;
		Object [] params;
	}

	private static final HashMap <Block, SchematicConstructor> schematicClasses =
			new HashMap<Block, SchematicConstructor>();

	public static void registerSchematicClass (Block block, Class clas, Object ... params) {
		SchematicConstructor c = new SchematicConstructor ();
		c.clas = clas;
		c.params = params;

		schematicClasses.put(block, c);
	}

	public static Schematic newSchematic (Block block) {
		if (!schematicClasses.containsKey(block)) {
			registerSchematicClass(block, Schematic.class);
		}

		try {
			SchematicConstructor c = schematicClasses.get(block);
			return (Schematic) c.clas.getConstructors() [0].newInstance(c.params);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
