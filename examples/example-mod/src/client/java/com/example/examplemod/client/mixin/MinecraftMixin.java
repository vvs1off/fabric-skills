package com.example.examplemod.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.Minecraft;

/**
 * Appends the mod name to the window title.
 *
 * <p>No Fabric API event covers the window title, so a mixin is justified. {@code @ModifyReturnValue}
 * (MixinExtras, bundled with Fabric Loader) chains with other mods that change the title, unlike
 * {@code @Overwrite} or a cancelling {@code @Inject}.
 */
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@ModifyReturnValue(method = "createTitle()Ljava/lang/String;", at = @At("RETURN"))
	private String examplemod$appendModName(String original) {
		return original + " | Example Mod";
	}
}
