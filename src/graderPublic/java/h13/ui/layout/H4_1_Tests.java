package h13.ui.layout;

import h13.util.Links;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DisplayName("H4.1 | Algorithmusauswahl")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestForSubmission
public class H4_1_Tests extends H4_Tests {

    private MethodLink methodLink;

    @BeforeAll
    public void globalSetup() {
        methodLink = Links.getMethod(getTypeLink(), "initialize");
    }

    @Override
    public TypeLink getTypeLink() {
        return BasicTypeLink.of(ChooserView.class);
    }

    @DisplayName("Der Listener fügt bzw. entfernt ein Element aus dem GridPane korrekt.")
    @Order(16)
    @Test
    public void testListener() {
        testAdd();
        testRemove();
    }

    private void testAdd() {
        ChooserView chooserView = new ChooserView(3, view -> {
        });
        Parent root = chooserView.getView();
        List<Triple<String, Integer, Integer>> options = List.of(
            Triple.of("Option 1", 0, 1),
            Triple.of("Option 2", 0, 2),
            Triple.of("Option 3", 1, 0),
            Triple.of("Option 4", 1, 1),
            Triple.of("Option 5", 1, 2),
            Triple.of("Option 6", 2, 0),
            Triple.of("Option 7", 2, 1)
        );
        AtomicReference<CheckBox> lastNode = new AtomicReference<>(null);
        chooserView.addListener((MapChangeListener.Change<? extends String, ? extends CheckBox> change) -> {
            if (change.wasAdded()) {
                lastNode.set(change.getValueAdded());
            }
        });
        options.forEach(option -> {
            String text = option.getLeft();
            int expectedRow = option.getMiddle();
            int expectedColumn = option.getRight();
            lastNode.set(null);
            chooserView.add(text);
            int actualRow = chooserView.getNextRow();
            int actualColumn = chooserView.getNextColumn();

            Context context = contextBuilder(methodLink, null)
                .add("Label", text)
                .add("Expected row", expectedRow)
                .add("Expected column", expectedColumn)
                .add("Actual row", actualRow)
                .add("Actual column", actualColumn)
                .build();
            Assertions2.assertEquals(expectedRow, actualRow, context,
                result -> "Incorrect row for label %s".formatted(text));
            Assertions2.assertEquals(expectedColumn, actualColumn, context,
                result -> "Incorrect column for label %s".formatted(text));
            Assertions2.assertTrue(root.getChildrenUnmodifiable().contains(lastNode.get()), context,
                result -> "Label %s is not in the root pane.".formatted(text));
        });
    }

    private void testRemove() {
        ChooserView chooserView = new ChooserView(3, view -> {
        });
        GridPane root = chooserView.getView();
        List<Triple<CheckBox, Integer, Integer>> options = Stream.of(
                Triple.of("Option 1", 2, 0),
                Triple.of("Option 2", 1, 2),
                Triple.of("Option 3", 1, 1),
                Triple.of("Option 4", 1, 0),
                Triple.of("Option 5", 0, 2),
                Triple.of("Option 6", 0, 1),
                Triple.of("Option 7", 0, 0)
            )
            .map(triple -> Triple.of(new CheckBox(triple.getLeft()), triple.getMiddle(), triple.getRight()))
            .toList();
        List<CheckBox> boxes = options.stream().map(Triple::getLeft).toList();
        ObservableMap<String, CheckBox> boxesWrapper = FXCollections.observableMap(boxes.stream().collect(Collectors.toMap(CheckBox::getText, Function.identity())));
        FieldLink optionsLink = Links.getField(getTypeLink(), "options");
        optionsLink.set(chooserView, boxesWrapper);
        chooserView.initialize();

        root.getChildren().addAll(boxes);
        FieldLink rowLink = Links.getField(getTypeLink(), "nextRow");
        rowLink.set(chooserView, 2);
        FieldLink columnLink = Links.getField(getTypeLink(), "nextColumn");
        columnLink.set(chooserView, 1);

        AtomicReference<CheckBox> lastNode = new AtomicReference<>(null);
        chooserView.addListener((MapChangeListener.Change<? extends String, ? extends CheckBox> change) -> {
            if (change.wasRemoved()) {
                lastNode.set(change.getValueRemoved());
            }
        });
        options.forEach(option -> {
            String text = option.getLeft().getText();
            int expectedRow = option.getMiddle();
            int expectedColumn = option.getRight();
            lastNode.set(null);
            chooserView.remove(text);
            int actualRow = chooserView.getNextRow();
            int actualColumn = chooserView.getNextColumn();

            Context context = contextBuilder(methodLink, null)
                .add("Label", text)
                .add("Expected row", expectedRow)
                .add("Expected column", expectedColumn)
                .add("Actual row", actualRow)
                .add("Actual column", actualColumn)
                .build();
            Assertions2.assertEquals(expectedRow, actualRow, context,
                result -> "Incorrect row for label %s".formatted(text));
            Assertions2.assertEquals(expectedColumn, actualColumn, context,
                result -> "Incorrect column for label %s".formatted(text));
            Assertions2.assertFalse(root.getChildrenUnmodifiable().contains(lastNode.get()), context,
                result -> "Label %s is still in the root pane.".formatted(text));
        });
    }
}
