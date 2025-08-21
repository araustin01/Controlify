package dev.isxander.controlify.platform.main.events;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.class_2168;
import net.minecraft.class_7157;
import net.minecraft.class_2170.class_5364;

@FunctionalInterface
public interface CommandRegistrationCallbackEvent {
   void onRegister(CommandDispatcher<class_2168> var1, class_7157 var2, class_5364 var3);
}
