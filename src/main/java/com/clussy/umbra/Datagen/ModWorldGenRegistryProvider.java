package com.clussy.umbra.Datagen;

import com.clussy.umbra.UmbraMod; // Import your main mod class for MODID
import com.clussy.umbra.worldgen.dimension.ModDimensions; // Import your dimension class
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider; // Correct NeoForge import
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenRegistryProvider extends DatapackBuiltinEntriesProvider {
    // Builder to include the registries you are bootstrapping
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType) // Add dimension type bootstrapping
            .add(Registries.LEVEL_STEM, ModDimensions::bootstrapStem); // Add level stem (dimension) bootstrapping
    // IMPORTANT: If you have custom biomes, noise settings, features, etc.,
    // you MUST add their bootstrap methods to this builder as well!
    // Example: .add(Registries.BIOME, YourBiomeBootstrapClass::bootstrapBiomes)
    // Example: .add(Registries.NOISE_SETTINGS, YourNoiseSettingsBootstrapClass::bootstrapNoiseSettings)


    public ModWorldGenRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        // Constructor: pass modid, the builder, and your modid set
        super(output, registries, BUILDER, Set.of(UmbraMod.MODID));
    }
}