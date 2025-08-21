package dev.isxander.splitscreen.client;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.splitscreen.client.features.configsync.ConfigSyncRegistry;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_412;
import net.minecraft.class_437;
import net.minecraft.class_500;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_9112;
import net.minecraft.class_642.class_8678;
import org.jetbrains.annotations.Nullable;

public class LocalSplitscreenPawn implements SplitscreenPawn {
   private final class_310 minecraft;
   private SplitscreenPosition position = null;
   private final int index;
   @Nullable
   private final ControllerUID associatedController;
   protected byte[] nonce;

   public LocalSplitscreenPawn(class_310 minecraft, int index, @Nullable ControllerUID associatedController) {
      this.minecraft = minecraft;
      this.index = index;
      this.associatedController = associatedController;
   }

   public int pawnIndex() {
      return this.index;
   }

   public void joinServer(String host, int port, @Nullable byte[] nonce) {
      String ip = host + ":" + port;
      class_639 address = new class_639(host, port);
      class_642 data = new class_642("Splitscreen Master", ip, class_8678.field_45609);
      this.nonce = nonce;
      class_412.method_36877(this.minecraft.field_1755, this.minecraft, address, data, false, (class_9112)null);
   }

   public void disconnectFromServer() {
      if (this.minecraft.field_1687 != null) {
         this.minecraft.field_1687.method_8525();
      }

      this.minecraft.method_18099();
      this.minecraft.method_1507(new class_500((class_437)null));
   }

   public void closeGame() {
      this.minecraft.method_1592();
   }

   public void useController(ControllerUID controllerUid) {
      Controlify.instance().setCurrentController((ControllerEntity)((ControllerManager)Controlify.instance().getControllerManager().orElseThrow()).getConnectedControllers().stream().filter((controller) -> {
         return controller.uid().equals(controllerUid);
      }).findAny().orElseThrow(), true);
   }

   public void onConfigSave(class_2960 config) {
      ConfigSyncRegistry.onSave(config);
   }

   public SplitscreenPosition getWindowSplitscreenMode() {
      return this.position;
   }

   @Nullable
   public ControllerUID getAssociatedController() {
      return this.associatedController;
   }

   public boolean isRemote() {
      return false;
   }

   public byte[] getLastLoginNonce() {
      return this.nonce;
   }

   public void setLastLoginNonce(byte[] nonce) {
      this.nonce = nonce;
   }
}
