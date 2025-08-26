package dev.isxander.controlify.integration.legacy;

import dev.isxander.controlify.utils.CUtil;
import wily.legacy.client.screen.TabList;

/**
 * Type-safe facade for Legacy4J integration using direct API calls.
 * Requires Legacy4J present at compile and runtime when enabled via Gradle property.
 */
public class LegacyApiFacade {
    // With a traditional mod dependency, we assume availability when enabled.
    private static final boolean LEGACY_COMPILE_TIME_AVAILABLE = true;
    private static final boolean LEGACY_RUNTIME_AVAILABLE = true;
    
    static {
        CUtil.LOGGER.log("[Legacy4J] Direct API facade enabled (traditional mod dependency)");
    }
    
    public static boolean isAvailable() {
        return LEGACY_RUNTIME_AVAILABLE;
    }
    
    public static boolean hasCompileTimeAccess() {
        return LEGACY_COMPILE_TIME_AVAILABLE;
    }
    
    /**
     * Wrapper for tab navigation functionality using direct API calls
     */
    public static class TabListAccess {
        public static boolean controlTab(Object screen, boolean left, boolean right) {
            if (!(screen instanceof TabList.Access access))
                return false;

            TabList tabList = access.getTabList();
            if (tabList == null)
                return false;

            boolean result = tabList.controlTab(left, right);
            if (result)
                CUtil.LOGGER.log("[Legacy4J] TabList.controlTab: left=%s right=%s".formatted(left, right));

            return result;
        }
    }
}
