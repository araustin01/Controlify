package dev.isxander.controlify.screenkeyboard;

import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.bindings.ControlifyBindings;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_437;

public class ChatKeyboardWidget extends KeyboardWidget<KeyboardWidget.Key> {
   public ChatKeyboardWidget(class_437 screen, int x, int y, int width, int height, KeyPressConsumer keyPressConsumer) {
      super(screen, x, y, width, height, keyPressConsumer);
   }

   protected void arrangeKeys() {
      KeyboardWidget.KeyLayoutBuilder<KeyboardWidget.Key> builder = new KeyboardWidget.KeyLayoutBuilder(14.0F, 5, this);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofRegularKey(256, "Esc"), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(49, '1'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(50, '2'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(51, '3'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(52, '4'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(53, '5'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(54, '6'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(55, '7'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(56, '8'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(57, '9'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(48, '0'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofRegularKey(259, "Backspace"), ControlifyBindings.GUI_ABSTRACT_ACTION_1), 3.0F);
      builder.nextRow();
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofRegularKey(258, "Tab"), (InputBindingSupplier)null), 2.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(81, 'q'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(87, 'w'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(69, 'e'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(82, 'r'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(84, 't'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(89, 'y'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(85, 'u'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(73, 'i'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(79, 'o'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(80, 'p'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(92, '\\'), (InputBindingSupplier)null), 2.0F);
      builder.nextRow();
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofRegularKey(280, "Caps"), (InputBindingSupplier)null), 2.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(65, 'a'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(83, 's'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(68, 'd'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(70, 'f'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(71, 'g'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(72, 'h'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(74, 'j'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(75, 'k'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(76, 'l'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(39, '\'', 0, 39, '"', 1), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofRegularKey(257, "Enter"), ControlifyBindings.GUI_ABSTRACT_ACTION_2), 2.0F);
      builder.nextRow();
      builder.key(KeyboardWidget.Key.builder((new KeyboardWidget.KeyFunction((screen, key) -> {
         this.shiftMode = !this.shiftMode;
         key.setHighlighted(this.shiftMode);
      }, KeyboardWidget.Key.ForegroundRenderer.text(class_2561.method_43470("Shift")))).copyShifted(), ControlifyBindings.GUI_ABSTRACT_ACTION_3), 2.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(90, 'z'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(88, 'x'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(67, 'c'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(86, 'v'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(66, 'b'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(78, 'n'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(77, 'm'), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(44, ',', 0, 46, '.', 0), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(49, '!', 1, 47, '?', 1), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(47, '/', 0, 92, '\\', 0), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofRegularKey(265, "↑"), (InputBindingSupplier)null), 1.0F);
      builder.nextRow();
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofRegularKey(341, "Ctrl"), (InputBindingSupplier)null), 2.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofChar(32, ' '), (InputBindingSupplier)null), 9.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofRegularKey(263, "←"), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofRegularKey(264, "↓"), (InputBindingSupplier)null), 1.0F);
      builder.key(KeyboardWidget.Key.builder(KeyboardWidget.KeyFunction.ofRegularKey(262, "→"), (InputBindingSupplier)null), 1.0F);
      List var10001 = this.keys;
      Objects.requireNonNull(var10001);
      builder.build(var10001::add);
   }
}
