package dev.isxander.controlify.controllermanager;

import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.controlify.hid.ControllerHIDService;
import java.util.List;
import java.util.Optional;

public interface ControllerManager {
   void discoverControllers();

   void tick(boolean var1);

   boolean probeConnectedControllers();

   List<ControllerEntity> getConnectedControllers();

   Optional<ControllerEntity> getController(ControllerUID var1);

   boolean isControllerConnected(ControllerUID var1);

   boolean isControllerGamepad(UniqueControllerID var1);

   Optional<ControllerEntity> reinitController(ControllerEntity var1, ControllerHIDService.ControllerHIDInfo var2);

   void closeController(ControllerUID var1);

   void close();
}
