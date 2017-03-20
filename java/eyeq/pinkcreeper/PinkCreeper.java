package eyeq.pinkcreeper;

import eyeq.pinkcreeper.entity.monster.EntityPinkCreeper;
import eyeq.pinkcreeper.client.renderer.entity.RenderPinkCreeper;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import eyeq.util.common.registry.UEntityRegistry;
import eyeq.util.world.biome.BiomeUtils;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.List;

import static eyeq.pinkcreeper.PinkCreeper.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
public class PinkCreeper {
    public static final String MOD_ID = "eyeq_pinkcreeper";

    @Mod.Instance(MOD_ID)
    public static PinkCreeper instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        registerEntities();
        if(event.getSide().isServer()) {
            return;
        }
        registerEntityRenderings();
        createFiles();
    }

    public static void registerEntities() {
        UEntityRegistry.registerModEntity(resource, EntityPinkCreeper.class, "PinkCreeper", 0, instance, 0xF377C1, 0xDF3A8D);
        List<Biome> biomes = BiomeUtils.getSpawnBiomes(EntityCreeper.class, EnumCreatureType.MONSTER);
        EntityRegistry.addSpawn(EntityPinkCreeper.class, 5, 1, 1, EnumCreatureType.MONSTER, biomes.toArray(new Biome[0]));
    }

    @SideOnly(Side.CLIENT)
    public static void registerEntityRenderings() {
        RenderingRegistry.registerEntityRenderingHandler(EntityPinkCreeper.class, RenderPinkCreeper::new);
    }

    public static void createFiles() {
        File project = new File("../1.11.2-PinkCreeper");

        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, EntityPinkCreeper.class, "Pink Creeper");
        language.register(LanguageResourceManager.JA_JP, EntityPinkCreeper.class, "クリ子ちゃん");

        ULanguageCreator.createLanguage(project, MOD_ID, language);
    }
}
