package buildcraft.lib.gui.json;

import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;

import buildcraft.lib.client.sprite.ISprite;
import buildcraft.lib.client.sprite.SpriteChanging;
import buildcraft.lib.client.sprite.SpriteRaw;
import buildcraft.lib.expression.FunctionContext;
import buildcraft.lib.gui.ContainerBC_Neptune;
import buildcraft.lib.gui.GuiIcon;
import buildcraft.lib.gui.ISimpleDrawable;
import buildcraft.lib.gui.elem.GuiElementDrawable;
import buildcraft.lib.gui.pos.GuiRectangle;

public class ElementTypeSprite extends ElementType<GuiJson<ContainerBC_Neptune>, ContainerBC_Neptune> {
    public static final String NAME = "buildcraftlib:drawable";
    public static final ElementTypeSprite INSTANCE = new ElementTypeSprite();

    // Args:
    // - pos[0], pos[1]: the position of the sprite (where it will be drawn, relative to the root of the gui). Defaults
    // to 0,0
    // - size[0], size[1]: the size of the sprite to be drawn.
    // - area[0-3]: mapping for pos[0], pos[1], size[0], size[1]
    // - source.sprite source.texture: the sprite where this texture will be drawn from. Defaults to the gui's texture
    // - source.area[0-3]: the area where the sprite will be taken from. 0-16 for sprites, 0-256 for textures

    private ElementTypeSprite() {
        super(NAME);
    }

    @Override
    public void addToGui(GuiJson<ContainerBC_Neptune> gui, JsonGuiInfo info, JsonGuiElement json, Map<String, Supplier<String>> guiProps) {
        FunctionContext ctx = createContext(json);
        inheritProperty(json, "area[0]", "pos[0]");
        inheritProperty(json, "area[1]", "pos[1]");
        inheritProperty(json, "area[2]", "size[0]");
        inheritProperty(json, "area[3]", "size[1]");

        inheritProperty(json, "source.area[0]", "source.pos[0]");
        inheritProperty(json, "source.area[1]", "source.pos[1]");
        inheritProperty(json, "source.area[2]", "source.size[0]");
        inheritProperty(json, "source.area[3]", "source.size[1]");

        int posX = resolveEquationInt(json, "pos[0]", ctx);
        int posY = resolveEquationInt(json, "pos[1]", ctx);
        int sizeX = resolveEquationInt(json, "size[0]", ctx);
        int sizeY = resolveEquationInt(json, "size[1]", ctx);

        double u = resolveEquationDouble(json, "source.pos[0]", ctx);
        double v = resolveEquationDouble(json, "source.pos[1]", ctx);
        double us = resolveEquationDouble(json, "source.size[0]", ctx);
        double vs = resolveEquationDouble(json, "source.size[1]", ctx);

        boolean foreground = resolveEquationBool(json, "foreground", ctx, false);

        // TODO: Allow this to be changing as well!
        SrcTexture tex = resolveTexture(info, json, "source");
        String origin = tex.origin;
        int texSize = tex.texSize;

        ISprite sprite;
        if (guiProps.containsKey(origin)) {
            sprite = new SpriteChanging(guiProps.get(origin), () -> u, () -> v, () -> us, () -> vs);
        } else {
            sprite = new SpriteRaw(new ResourceLocation(origin), u, v, us, vs, texSize);
        }

        GuiRectangle rect = new GuiRectangle(posX, posY, sizeX, sizeY);
        ISimpleDrawable icon = new GuiIcon(sprite, texSize);
        gui.guiElements.add(new GuiElementDrawable(gui, gui.rootElement, rect, icon, foreground));
    }
}
