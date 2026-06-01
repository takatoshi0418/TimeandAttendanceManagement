package com.github.takatoshi0418.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.takatoshi0418.exception.attendance.IllegalAttendanceException;
import com.github.takatoshi0418.view.FlashMessage;

import lombok.RequiredArgsConstructor;

/**
 * 横断的な例外ハンドラ
 */
@ControllerAdvice
@RequiredArgsConstructor
public class TransverseExceptionHandler {
    
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(TransverseExceptionHandler.class);

    /** メッセージソース */
    private final MessageSource messageSource;

    /**
     * IllegalAttendanceException例外のハンドラ
     * @param e 例外
     * @param redirectAttributes リダイレクト属性
     * @return リダイレクト先
     */
    @ExceptionHandler(IllegalAttendanceException.class)
    public String handlerAlreadyClockedIn(IllegalAttendanceException e, RedirectAttributes redirectAttributes) {
        logger.error(e.getMessage(), e);

        String message = messageSource.getMessage(e.getMessageKey(), null, Locale.JAPANESE);

        redirectAttributes.addFlashAttribute("messages", createFlashMessage(message));

        return "redirect:/dashboard";
    }

    /**
     * Flashメッセージを作成する
     * @param messages Flashメッセージの文字列
     * @return Flashメッセージのリスト
     */
    private List<FlashMessage> createFlashMessage(String ...messages) {
        List<FlashMessage> flashMessages = new ArrayList<>();

        if (messages == null || messages.length == 0) {
            return flashMessages;
        }

        for (String message : messages) {
            FlashMessage flashMessage = new FlashMessage(FlashMessage.Type.DANGER, message);
            flashMessages.add(flashMessage);
        }
        return flashMessages;
    }

}
