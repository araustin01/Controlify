package dev.isxander.splitscreen.client.integrations;

import com.mojang.logging.LogUtils;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.ControlifyBindApi;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.api.event.ControlifyEvents;
import dev.isxander.controlify.api.event.ControlifyEvents.ControllerConnected;
import dev.isxander.controlify.api.event.ControlifyEvents.ControllerDisconnected;
import dev.isxander.controlify.api.event.ControlifyEvents.ControllerStateUpdate;
import dev.isxander.controlify.api.event.ControlifyEvents.FinishedInit;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchArguments;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchException;
import dev.isxander.splitscreen.util.CSUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.class_128;
import net.minecraft.class_148;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class ControlifyExtension implements ControlifyEntrypoint {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final class_2561 BINDING_CATEGORY = class_2561.method_43471("controlify.splitscreen.bind.category");
   public static InputBindingSupplier ADD_PLAYER_BIND;
   @Nullable
   private ControllerEntity controller;

   public void onControllersDiscovered(ControlifyApi controlify) {
   }

   public void onControlifyInit(ControlifyApi controlifyApi) {
      ADD_PLAYER_BIND = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id(CSUtil.rl("add_player")).category(BINDING_CATEGORY);
      });
      ControlifyEvents.CONTROLLER_STATE_UPDATE.register(this::onControllerStateUpdate);
      ControlifyEvents.FINISHED_INIT.register(this::onFinishedInit);
      ControlifyEvents.CONTROLLER_CONNECTED.register(this::onControllerConnected);
      ControlifyEvents.CONTROLLER_DISCONNECTED.register(this::onControllerDisconnected);
      ClientTickEvents.START_CLIENT_TICK.register(this::onClientTick);
   }

   private void onControllerStateUpdate(ControllerStateUpdate event) {
      ControllerEntity controller = event.controller();
      Controlify controlify = Controlify.instance();
      SplitscreenBootstrapper.getController().ifPresent((splitController) -> {
         controller.input().ifPresent((input) -> {
            if (ADD_PLAYER_BIND.on(controller).justPressed()) {
               int controllersConnected = ((ControllerManager)Controlify.instance().getControllerManager().orElseThrow()).getConnectedControllers().size();
               boolean isCurrentController = controller == controlify.getCurrentController().orElse((Object)null);
               if (controllersConnected >= 2 && !isCurrentController) {
                  splitController.summonNewPawnClient(controller.uid());
               }
            }

         });
      });
   }

   private void onFinishedInit(FinishedInit event) {
      if (RelaunchArguments.CONTROLLER.get().isPresent() && this.controller == null) {
         RelaunchException exception = new RelaunchException("Relaunched client could not find controller it is associated to!");
         class_128 report = class_128.method_560(exception, "Relaunch failed");
         throw new class_148(report);
      }
   }

   private void onControllerConnected(ControllerConnected event) {
      ControllerUID relaunchController = (ControllerUID)RelaunchArguments.CONTROLLER.get().orElse((Object)null);
      if (event.controller().uid().equals(relaunchController)) {
         LOGGER.info("Our bound controller {} has been connected!", relaunchController);
         this.controller = event.controller();
      }

   }

   private void onControllerDisconnected(ControllerDisconnected event) {
      if (event.controller() == this.controller) {
         LOGGER.warn("Controller {} assciated with this client has been disconnected! This client is now closing.", event.controller().uid());
         this.controller = null;
         class_310.method_1551().method_1592();
      }

   }

   private void onClientTick(class_310 minecraft) {
      if (this.controller != null) {
         Controlify.instance().setCurrentController(this.controller, true);
      }

   }
}
