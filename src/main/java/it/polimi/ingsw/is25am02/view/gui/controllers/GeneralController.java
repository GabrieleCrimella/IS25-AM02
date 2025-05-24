package it.polimi.ingsw.is25am02.view.gui.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GeneralController {
    private VBox notificationContainer;

    public enum NotificationType {
        INFO, SUCCESS, ERROR
    }

    public GeneralController() {

    }

    public VBox getNotificationContainer() {
        return notificationContainer;
    }

    public VBox newNotificazionContainer() {
        notificationContainer = new VBox();
        notificationContainer.setId("notificationContainer");
        notificationContainer.setAlignment(Pos.TOP_RIGHT);
        notificationContainer.setMouseTransparent(true);
        notificationContainer.setPickOnBounds(false);
        notificationContainer.setSpacing(10);
        notificationContainer.setPadding(new Insets(20, 20, 20, 20));
        notificationContainer.getStyleClass().add("notification-container");
        return notificationContainer;
    }

    public void showNotification(String message, BuildController.NotificationType type, int durationMillis) {
        Label notification = new Label(message);
        notification.getStyleClass().add("toast-notification");

        switch (type) {
            case SUCCESS -> notification.getStyleClass().add("toast-success");
            case ERROR -> notification.getStyleClass().add("toast-error");
            case INFO -> notification.getStyleClass().add("toast-info");
        }

        notificationContainer.getChildren().add(notification);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), notification);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        PauseTransition delay = new PauseTransition(Duration.millis(durationMillis));
        delay.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), notification);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> notificationContainer.getChildren().remove(notification));
            fadeOut.play();
        });
        delay.play();
    }

}
