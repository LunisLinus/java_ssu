package org.knit.Task20;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knit.solutions.Task20.clipboard.SystemClipboardService;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

import static org.assertj.core.api.Assertions.assertThat;

class SystemClipboardServiceTest {

    private SystemClipboardService systemClipboardService;

    @BeforeEach
    void setUp() {
        systemClipboardService = new SystemClipboardService();
    }

    @Test
    void copyToClipboard_shouldSetClipboardContent() throws Exception {
        String text = "TestClipboardText";
        systemClipboardService.copyToClipboard(text);

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String clipboardContent = (String) clipboard.getData(DataFlavor.stringFlavor);

        assertThat(clipboardContent).isEqualTo(text);
    }
}
