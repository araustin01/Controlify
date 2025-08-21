package dev.isxander.controlify.driver;

import com.google.common.collect.Lists;
import dev.isxander.controlify.controller.ControllerEntity;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompoundDriver implements Driver {
   private final List<Driver> drivers;

   public CompoundDriver(List<Driver> drivers) {
      this.drivers = drivers;
   }

   public void addComponents(ControllerEntity controller) {
      Iterator var2 = Lists.reverse(this.drivers).iterator();

      while(var2.hasNext()) {
         Driver driver = (Driver)var2.next();
         driver.addComponents(controller);
      }

   }

   public void update(ControllerEntity controller, boolean outOfFocus) {
      Iterator var3 = this.drivers.iterator();

      while(var3.hasNext()) {
         Driver driver = (Driver)var3.next();
         driver.update(controller, outOfFocus);
      }

   }

   public void close() {
      Iterator var1 = this.drivers.iterator();

      while(var1.hasNext()) {
         Driver driver = (Driver)var1.next();
         driver.close();
      }

   }

   public String toString() {
      Stream var10000 = this.drivers.stream().map((d) -> {
         return d.getClass().getSimpleName();
      });
      return "CompoundDriver{" + (String)var10000.collect(Collectors.joining(",")) + "}";
   }
}
