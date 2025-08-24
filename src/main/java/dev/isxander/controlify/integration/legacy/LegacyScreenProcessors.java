package dev.isxander.controlify.integration.legacy;

import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorFactory;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.virtualmouse.VirtualMouseBehaviour;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.input.ControllerStateView;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.Controlify;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;

/**
 * Registers Controlify screen processors for Legacy4J custom screens so we can drive them via native navigation.
 * Uses reflection to avoid a hard dependency.
 */
public class LegacyScreenProcessors {
    private static boolean registered = false;

    public static void register() {
        if (registered) return;
        registered = true;
        tryRegisterPlayGameScreen();
    }

    private static void tryRegisterPlayGameScreen() {
        try {
            Class<?> playGameCls = Class.forName("wily.legacy.client.screen.PlayGameScreen", false, Controlify.class.getClassLoader());
            ScreenProcessorFactory.register(playGameCls, LegacyPlayGameScreenProcessor::new);
        } catch (Throwable ignored) {}
    }

    /** Processor for PlayGameScreen: handles tab switching and list navigation without virtual mouse. */
    public static class LegacyPlayGameScreenProcessor extends ScreenProcessor<Screen> {
        private Field tabListField;
        private Field selectedTabField;
        private Field renderableListsField; // List of lists
        private Field currentListField; // current list object containing renderables
        private Field renderablesField; // List<AbstractButton>

        @SuppressWarnings("unchecked")
        public LegacyPlayGameScreenProcessor(Screen screen) {
            super(screen);
            try {
                tabListField = screen.getClass().getDeclaredField("tabList");
                tabListField.setAccessible(true);
                Object tabList = tabListField.get(screen);
                selectedTabField = tabList.getClass().getDeclaredField("selectedTab");
                selectedTabField.setAccessible(true);
            } catch (Throwable ignored) {}
            // Attempt to locate a field named saveRenderableList / creationList / serverRenderableList
            for (Field f : screen.getClass().getDeclaredFields()) {
                if (f.getName().endsWith("RenderableList")) {
                    f.setAccessible(true);
                }
            }
        }

        @Override
        public VirtualMouseBehaviour virtualMouseBehaviour() {
            return VirtualMouseBehaviour.DISABLED; // Force dpad navigation
        }

        private int getSelectedTab() {
            try {
                if (selectedTabField != null) return selectedTabField.getInt(tabListField.get(screen));
            } catch (Throwable ignored) {}
            return 0;
        }

        private void setSelectedTab(int idx) {
            try {
                if (selectedTabField != null) selectedTabField.setInt(tabListField.get(screen), idx);
            } catch (Throwable ignored) {}
        }

        @Override
        public void onControllerUpdate(ControllerEntity controller) {
            // Let base handle focus-based navigation for now; we only intercept tab switching
            handleTabs(controller);
            super.onControllerUpdate(controller);
        }

        private void handleTabs(ControllerEntity controller) {
            InputBinding left = ControlifyBindings.GUI_NAVI_LEFT.on(controller);
            InputBinding right = ControlifyBindings.GUI_NAVI_RIGHT.on(controller);
            if (left.justPressed()) {
                int tab = (getSelectedTab() + 2) % 3; // wrap backwards among 3 tabs
                setSelectedTab(tab);
            } else if (right.justPressed()) {
                int tab = (getSelectedTab() + 1) % 3;
                setSelectedTab(tab);
            }
        }

        @Override
        public void setInitialFocus() {
            // Fallback to first focusable widget
            for (GuiEventListener l : screen.children()) {
                if (l instanceof AbstractWidget w) {
                    screen.setFocused(w);
                    return;
                }
            }
        }

        @Override
        public void handleButtons(ControllerEntity controller) {
            InputComponent input = controller.input().orElseThrow();
            ControllerStateView state = input.stateNow();
            // Use existing base behaviour for A/B etc.
            super.handleButtons(controller);
        }
    }
}
