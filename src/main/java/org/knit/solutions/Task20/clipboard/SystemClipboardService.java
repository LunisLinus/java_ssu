package org.knit.solutions.Task20.clipboard;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

@Service
public class SystemClipboardService implements ClipboardService {

    @Override
    public void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(text), null);
    }
}
