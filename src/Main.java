import com.library.ui.LibraryApp;

import javax.swing.*;

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        LibraryApp app = new LibraryApp();
        app.setVisible(true);
    });
}
