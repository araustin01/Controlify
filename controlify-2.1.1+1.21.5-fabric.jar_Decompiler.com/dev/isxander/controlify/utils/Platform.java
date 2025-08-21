package dev.isxander.controlify.utils;

public enum Platform {
   UNKNOWN,
   MAC,
   LINUX,
   WINDOWS,
   ANDROID,
   IOS;

   private static final Platform currentPlatform;

   public static Platform current() {
      return currentPlatform;
   }

   // $FF: synthetic method
   private static Platform[] $values() {
      return new Platform[]{UNKNOWN, MAC, LINUX, WINDOWS, ANDROID, IOS};
   }

   static {
      String osName = System.getProperty("os.name");
      if (osName.startsWith("Linux")) {
         if ("dalvik".equalsIgnoreCase(System.getProperty("java.vm.name"))) {
            currentPlatform = ANDROID;
         } else {
            currentPlatform = LINUX;
         }
      } else if (!osName.startsWith("Mac") && !osName.startsWith("Darwin")) {
         if (osName.startsWith("Windows")) {
            currentPlatform = WINDOWS;
         } else if (osName.startsWith("iOS")) {
            currentPlatform = IOS;
         } else {
            CUtil.LOGGER.log("Unable to determine platform: " + osName);
            currentPlatform = UNKNOWN;
         }
      } else {
         currentPlatform = MAC;
      }

   }
}
