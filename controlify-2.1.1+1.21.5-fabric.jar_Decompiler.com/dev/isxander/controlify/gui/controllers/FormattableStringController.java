package dev.isxander.controlify.gui.controllers;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import dev.isxander.yacl3.gui.controllers.string.StringController;
import net.minecraft.class_2561;

public class FormattableStringController extends StringController {
   private final ValueFormatter<String> formatter;

   public FormattableStringController(Option<String> option, ValueFormatter<String> formatter) {
      super(option);
      this.formatter = formatter;
   }

   public class_2561 formatValue() {
      return this.formatter.format(this.getString());
   }
}
