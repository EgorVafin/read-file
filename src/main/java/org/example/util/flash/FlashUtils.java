package org.example.util.flash;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public class FlashUtils {
    public static void addMessage(RedirectAttributes redirectAttributes,
                             String message,
                             FlashMessage.Type type) {
        redirectAttributes.addFlashAttribute("messages", List.of(
                new FlashMessage(message, type)
        ));
    }
}
