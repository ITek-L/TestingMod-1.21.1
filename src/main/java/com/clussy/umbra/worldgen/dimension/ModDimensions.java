package com.clussy.umbra.worldgen.dimension;

import com.clussy.umbra.UmbraMod; // Import your main mod class for MODID
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*; // Import biome related classes
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator; // Import NoiseBasedChunkGenerator
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings; // Import NoiseGeneratorSettings

import java.util.List;
import java.util.OptionalLong;

public class ModDimensions {
    // Resource Keys for your dimension
    public static final ResourceKey<LevelStem> UMBRA_DIM_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(UmbraMod.MODID, "umbra"));
    public static final ResourceKey<Level> UMBRA_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(UmbraMod.MODID, "umbra"));
    public static final ResourceKey<DimensionType> UMBRA_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(UmbraMod.MODID, "umbra_type"));


    // Bootstrap method for the Dimension Type
    public static void bootstrapType(BootstrapContext<DimensionType> context) {
        context.register(UMBRA_DIM_TYPE, new DimensionType(
                OptionalLong.of(18000), // fixedTime - Setting a time (e.g., dusk/dawn)
                false, // hasSkylight - Set to true if it has a sky
                false, // hasCeiling - Set to true for dimensions like the Nether
                false, // ultraWarm - Set to true if water evaporates (like the Nether)
                false, // natural - Affects portal behavior
                1.0, // coordinateScale - Scaling factor (1.0 for overworld scale)
                true, // bedWorks - Can you use beds?
                false, // respawnAnchorWorks - Can you use respawn anchors?
                -64, // minY - Minimum build height
                384, // height - Total build height (minY + height)
                384, // logicalHeight - Height for light/mechanics
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn - Blocks that burn forever
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation - Visual/sound effects (use overworld for now)
                0.1f, // ambientLight - Darker ambient light
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0))); // Monster spawning
    }

    // Bootstrap method for the Level Stem (the dimension instance)
    public static void bootstrapStem(BootstrapContext<LevelStem> context) {
        // Get lookups for necessary registries
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        // *** Chunk Generator Configuration ***
        // Create a Chunk Generator that uses a single biome (Badlands)
        NoiseBasedChunkGenerator chunkGenerator = new NoiseBasedChunkGenerator(
                // Use FixedBiomeSource with the Badlands biome
                new FixedBiomeSource(biomeRegistry.getOrThrow(Biomes.BADLANDS)),
                // Use Overworld noise settings as a base, you can use others or custom ones
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD)
        );

        // Create the Level Stem, linking the dimension type and chunk generator
        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.UMBRA_DIM_TYPE), chunkGenerator);

        // Register the Level Stem
        context.register(UMBRA_DIM_KEY, stem);
    }
}