package dev.isxander.controlify.gui.screen;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.GenericControllerConfig;
import dev.isxander.controlify.controller.id.ControllerType;
import dev.isxander.controlify.hid.HIDDevice;
import dev.isxander.controlify.platform.main.PlatformMainUtil;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.ClientUtils;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Objects;
import java.util.regex.Pattern;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_4286;
import net.minecraft.class_437;
import net.minecraft.class_5250;
import net.minecraft.class_7940;

public class SubmitUnknownControllerScreen extends class_437 implements DontInteruptScreen {
   public static final String SUBMISSION_URL = "https://api-controlify.isxander.dev/api/v1/submit";
   public static final Pattern NAME_PATTERN = Pattern.compile("^[\\w\\- ]{3,64}$");
   private final ControllerEntity controller;
   private class_4286 operationalCheckbox;
   private final class_437 lastScreen;
   private boolean invalidName;
   private class_4185 submitButton;
   private class_342 nameField;

   public SubmitUnknownControllerScreen(ControllerEntity controller, class_437 lastScreen) {
      super(class_2561.method_43471("controlify.controller_submission.title").method_27692(class_124.field_1067));
      if (!canSubmit(controller)) {
         throw new IllegalArgumentException("Controller ineligible for submission!");
      } else {
         this.controller = controller;
         this.lastScreen = lastScreen;
      }
   }

   protected void method_25426() {
      class_7940 content = (class_7940)this.method_37063(new class_7940(class_2561.method_43471("controlify.controller_submission.message"), this.field_22793));
      content.method_48984(this.field_22789 - 100);
      content.method_46421(this.field_22789 / 2 - content.method_25368() / 2);
      int titleBottomPadding = 11;
      int checkboxPadding = 6;
      int checkboxHeight = 20;
      int buttonHeight = 20;
      int nameFieldPaddingTop = 5;
      int nameFieldHeight = 20;
      int errorPadding = 4;
      Objects.requireNonNull(this.field_22793);
      int var10000 = 9 + titleBottomPadding + content.method_25364() + checkboxPadding + checkboxHeight + checkboxPadding + buttonHeight + nameFieldPaddingTop + nameFieldHeight + errorPadding;
      Objects.requireNonNull(this.field_22793);
      int allHeight = var10000 + 9;
      int y = this.field_22790 / 2 - allHeight / 2;
      this.method_37063(ClientUtils.createStringWidget(this.method_25440(), this.field_22793, 25, y));
      Objects.requireNonNull(this.field_22793);
      y += 9 + titleBottomPadding;
      content.method_46419(y);
      y += content.method_25364() + checkboxPadding;
      class_5250 operationalText = class_2561.method_43471("controlify.controller_submission.operational_checkbox").method_27692(class_124.field_1067);
      this.operationalCheckbox = class_4286.method_54787(operationalText, this.field_22793).method_54789(this.field_22789 / 2 - this.field_22793.method_27525(operationalText) / 2 - 8, y).method_54794(true).method_54788();
      this.method_37063(this.operationalCheckbox);
      y += checkboxHeight + checkboxPadding;
      this.submitButton = (class_4185)this.method_37063(class_4185.method_46430(class_2561.method_43471("controlify.controller_submission.submit"), this::onSubmitButton).method_46433(this.field_22789 / 2 - 155, y).method_46432(150).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43471("controlify.controller_submission.skip"), (btn) -> {
         this.method_25419();
      }).method_46433(this.field_22789 / 2 + 5, y).method_46432(150).method_46431());
      y += buttonHeight + nameFieldPaddingTop;
      this.nameField = (class_342)this.method_37063(new class_342(this.field_22793, this.field_22789 / 2 - 155, y, 310, 20, class_2561.method_43471("controlify.controller_submission.name_narration")));
      this.nameField.method_47404(class_2561.method_43471("controlify.controller_submission.name_hint"));
      this.nameField.method_1852(this.controller.name());
      this.nameField.method_1890((s) -> {
         this.invalidName = !this.checkValidName(s);
         this.submitButton.field_22763 = !this.invalidName;
         return true;
      });
   }

   public void method_25394(class_332 graphics, int mouseX, int mouseY, float delta) {
      this.method_25420(graphics, mouseX, mouseY, delta);
      super.method_25394(graphics, mouseX, mouseY, delta);
      if (this.invalidName) {
         graphics.method_27534(this.field_22793, class_2561.method_43471("controlify.controller_submission.invalid_name").method_27692(class_124.field_1061), this.field_22789 / 2, this.nameField.method_48202().method_49619() + 4, -1);
      }

   }

   protected void onSubmitButton(class_4185 button) {
      if (this.submit()) {
         this.dontShowAgain();
         this.method_25419();
      } else {
         this.dontShowAgain();
         this.method_25419();
      }

   }

   protected boolean submit() {
      try {
         HttpClient client = HttpClient.newHttpClient();
         HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://api-controlify.isxander.dev/api/v1/submit")).POST(BodyPublishers.ofString(this.generateRequestBody())).header("Content-Type", "application/json").build();
         HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
         if (response.statusCode() / 100 != 2) {
            CUtil.LOGGER.error("Received non-2xx status code from '{}', got {} with body '{}'", "https://api-controlify.isxander.dev/api/v1/submit", response.statusCode(), response.body());
            return false;
         } else {
            CUtil.LOGGER.log("Successfully sent controller information to '{}'", "https://api-controlify.isxander.dev/api/v1/submit");
            return true;
         }
      } catch (Exception var4) {
         CUtil.LOGGER.error("Failed to submit controller to '%s'".formatted(new Object[]{"https://api-controlify.isxander.dev/api/v1/submit"}), (Throwable)var4);
         return false;
      }
   }

   private String generateRequestBody() {
      HIDDevice hid = (HIDDevice)this.controller.info().hid().orElseThrow();
      JsonObject object = new JsonObject();
      object.addProperty("vendorID", hid.vendorId());
      object.addProperty("productID", hid.productId());
      object.addProperty("GUID", this.controller.guid());
      object.addProperty("reportedName", this.nameField.method_1882());
      object.addProperty("controlifyVersion", PlatformMainUtil.getControlifyVersion());
      object.addProperty("operational", this.operationalCheckbox.method_20372());
      Gson gson = new Gson();
      return gson.toJson(object);
   }

   private boolean checkValidName(String name) {
      return NAME_PATTERN.matcher(name.trim()).matches();
   }

   private void dontShowAgain() {
      ((GenericControllerConfig)this.controller.genericConfig().config()).dontShowControllerSubmission = true;
      Controlify.instance().config().setDirty();
   }

   public void method_25419() {
      this.dontShowAgain();
      Controlify.instance().config().saveIfDirty();
      this.field_22787.method_1507(this.lastScreen);
   }

   public boolean method_25422() {
      return false;
   }

   public static boolean canSubmit(ControllerEntity controller) {
      return controller.info().type() == ControllerType.DEFAULT && !((GenericControllerConfig)controller.genericConfig().config()).dontShowControllerSubmission;
   }
}
