package dev.isxander.controlify.api.ingameguide;

import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.guide.ActionPriority;
import dev.isxander.controlify.api.guide.GuideActionNameSupplier;

public interface IngameGuideRegistry {
   void registerGuideAction(InputBinding var1, ActionLocation var2, ActionPriority var3, GuideActionNameSupplier<IngameGuideContext> var4);

   void registerGuideAction(InputBinding var1, ActionLocation var2, GuideActionNameSupplier<IngameGuideContext> var3);
}
