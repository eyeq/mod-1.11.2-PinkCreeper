package eyeq.pinkcreeper.client.renderer.entity;

import eyeq.util.client.renderer.EntityRenderResourceLocation;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;

import static eyeq.pinkcreeper.PinkCreeper.MOD_ID;

public class RenderPinkCreeper extends RenderCreeper {
    protected static final ResourceLocation textures = new EntityRenderResourceLocation(MOD_ID, "creeper_pink");

    public RenderPinkCreeper(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCreeper entity) {
        return textures;
    }
}
