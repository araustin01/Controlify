package dev.isxander.controlify.controller.info;

import dev.isxander.controlify.controller.id.ControllerType;
import dev.isxander.controlify.controllermanager.UniqueControllerID;
import dev.isxander.controlify.hid.HIDDevice;
import java.util.Optional;

public record ControllerInfo(UniqueControllerID ucid, ControllerType type, Optional<HIDDevice> hid) {
   public ControllerInfo(UniqueControllerID ucid, ControllerType type, Optional<HIDDevice> hid) {
      this.ucid = ucid;
      this.type = type;
      this.hid = hid;
   }

   public UniqueControllerID ucid() {
      return this.ucid;
   }

   public ControllerType type() {
      return this.type;
   }

   public Optional<HIDDevice> hid() {
      return this.hid;
   }
}
