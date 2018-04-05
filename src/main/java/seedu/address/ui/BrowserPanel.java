package seedu.address.ui;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.StudentInfoChangedEvent;
import seedu.address.commons.events.model.StudentInfoDisplayEvent;
import seedu.address.commons.events.ui.BrowserDisplayEvent;
import seedu.address.model.student.Address;
import seedu.address.model.student.Student;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String STUDENT_MISC_INFO_PAGE = "StudentMiscInfo.html";
    public static final String STUDENT_INFO_PAGE_STYLESHEET = "StudentInfoTheme.css";
    public static final String SEARCH_PAGE_URL =
            "https://www.google.com.sg/maps/place/";


    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }

    private void loadStudentPage(Student student) {
        Address location = student.getAddress();
        String append = location.urlstyle();
        loadPage(SEARCH_PAGE_URL + append);
    }

    /**
     * Loads the student's full information page on the browser including his/her profile picture if it exists
     */
    private void loadStudentInfoPage() {
        String jarFolder = new File(MainApp.class.getProtectionDomain().getCodeSource().getLocation()
                .getPath()).getParentFile().getPath().replace('\\', '/');
        String studentPageFilePath = "file:/" + jarFolder + "/data/view/" + STUDENT_MISC_INFO_PAGE;
        loadPage(studentPageFilePath);

    }


    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    public void reloadPage() {
        Platform.runLater(() -> browser.getEngine().reload());
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());

    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

    @Subscribe
    private void handleBrowserDisplayEvent(BrowserDisplayEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadStudentPage(event.getStudentSelection().student);
    }



    @Subscribe
    private void handleStudentInfoDisplayEvent(StudentInfoDisplayEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadStudentInfoPage();
    }

    @Subscribe
    private void handleStudentInfoChangedEvent(StudentInfoChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        reloadPage();
    }
}
