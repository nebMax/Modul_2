package Modul_2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Calc extends Application {

    private enum Gender { MALE, FEMALE }

    private Gender gender;
    private TextField ageField = new TextField();
    private TextField heightField = new TextField();
    private TextField weightField = new TextField();
    private MenuButton activityField = new MenuButton();
    private MenuButton goalField = new MenuButton();
    private TableView<Person> tableView = new TableView<>();
    private ObservableList<Person> personList = FXCollections.observableArrayList();

    private static final double CALORIES_COLUMN_WIDTH = 150.0;

    private Label item3_1 = new Label();
    private Label item3_2 = new Label();

    private int bmr;
    private int totalCalories;

    private static final int BASE_METABOLISM_MALE = 88;
    private static final int BASE_METABOLISM_FEMALE = 447;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Person Table");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        ToggleGroup group = new ToggleGroup();
        Label sex = new Label("Стать:");
        RadioButton men = new RadioButton("Чоловік");
        RadioButton women = new RadioButton("Жінчина");
        men.setToggleGroup(group);
        women.setToggleGroup(group);

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (group.getSelectedToggle() != null) {
                RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
                gender = selectedRadioButton.getText().equals("Чоловік") ? Gender.MALE : Gender.FEMALE;
            }
        });

        grid.add(new Label("Gender:"), 0, 0);
        grid.add(men, 1, 0);
        grid.add(women, 1, 1);
        String menuButtonStyle = "-fx-background-color: #9b9dc6; -fx-text-fill: white;";
        grid.add(new Label("Age:"), 0, 2);
        grid.add(ageField, 1, 2);
        checkText(ageField);
        grid.add(new Label("Height:"), 0, 3);
        grid.add(heightField, 1, 3);
        checkText(heightField);
        grid.add(new Label("Weight:"), 0, 4);
        grid.add(weightField, 1, 4);
        checkText(weightField);
        activityField = new MenuButton("Низький");
        MenuItem item1 = myActivityItem("Низький");
        MenuItem item2 = myActivityItem("Середній");
        MenuItem item3 = myActivityItem("Високий");
        activityField.setStyle(menuButtonStyle);
        activityField.getItems().addAll(item1, item2, item3);
        grid.add(new Label("Activity:"), 0, 5);
        grid.add(activityField, 1, 5);
        goalField = new MenuButton("Схуднення");
        MenuItem item2_1 = myGoalItem("Схуднення");
        MenuItem item2_2 = myGoalItem("Набір ваги");
        MenuItem item2_3 = myGoalItem("Набір м'язів");
        goalField.setStyle(menuButtonStyle);
        goalField.getItems().addAll(item2_1, item2_2, item2_3);
        grid.add(new Label("Goal:"), 0, 6);
        grid.add(goalField, 1, 6);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> handleSubmitButton());
        grid.add(submitButton, 1, 8);
        String buttonStyle = "-fx-background-color: #9b9dc6; -fx-text-fill: white;";
        submitButton.setStyle(buttonStyle);
        double newWidth = 150.0;
        submitButton.setPrefWidth(newWidth);

        Button saveButton = new Button("Save to File");
        saveButton.setOnAction(e -> handleSaveButton());

        HBox buttonBox = new HBox(submitButton, saveButton);
        buttonBox.setSpacing(10);

        grid.add(buttonBox, 1, 9);  // Змінено рядок для додавання нової кнопки


        submitButton.setStyle(buttonStyle);
        saveButton.setStyle(buttonStyle);

        submitButton.setPrefWidth(newWidth);
        saveButton.setPrefWidth(newWidth);


        HBox rootBox = new HBox();
        VBox box = new VBox();
        HBox pane = new HBox();
        Label text = new Label("Результати:");
        text.setFont(new Font(25));
        FlowPane flowpane = new FlowPane();

        item3_1 = new Label("Ваш базовий метаболізм: %d ккал.");
        item3_2 = new Label("Для цілі вам необхідно споживати приблизно %d калорій в день.");
        flowpane.setStyle("-fx-padding: 10");
        flowpane.setVgap(10);
        flowpane.setHgap(50);
        flowpane.getChildren().addAll(item3_1, item3_2);
        pane.getChildren().add(flowpane);

        // Настройка таблицы
        TableColumn<Person, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn<Person, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Person, Integer> heightCol = new TableColumn<>("Height");
        heightCol.setCellValueFactory(new PropertyValueFactory<>("height"));

        TableColumn<Person, Integer> weightCol = new TableColumn<>("Weight");
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

        TableColumn<Person, String> activityCol = new TableColumn<>("Activity");
        activityCol.setCellValueFactory(new PropertyValueFactory<>("activity"));

        TableColumn<Person, String> goalCol = new TableColumn<>("Goal");
        goalCol.setCellValueFactory(new PropertyValueFactory<>("goal"));

        TableColumn<Person, Integer> caloriesCol = new TableColumn<>("Needs calories");
        caloriesCol.setCellValueFactory(new PropertyValueFactory<>("calories"));

        caloriesCol.setPrefWidth(CALORIES_COLUMN_WIDTH);

        tableView.getColumns().addAll(genderCol, ageCol, heightCol, weightCol, activityCol, goalCol, caloriesCol);
        tableView.setItems(personList);

        box.getChildren().addAll(text, pane, tableView);

        rootBox.getChildren().addAll(grid, box);
        Scene scene = new Scene(rootBox);
        primaryStage.setScene(scene);
        String backgroundColorStyle = "-fx-background-color: #caacd7;";
        scene.getRoot().setStyle(backgroundColorStyle);

        primaryStage.show();
    }

    private void handleSubmitButton() {
        if(validateFields()) {
            int age = Integer.parseInt(ageField.getText());
            int height = Integer.parseInt(heightField.getText());
            int weight = Integer.parseInt(weightField.getText());

            String activity = activityField.getText();
            String goal = goalField.getText();

            calculateCalories(age, height, weight, gender, activity, goal);

            // Створюємо об'єкт Person
            Person person = new Person(gender.name(), age, height, weight, activity, goal, totalCalories);

            // Додаємо його до списку
            personList.add(person);


            item3_1.setText(String.format("Ваш базовий метаболізм: %d ккал.", bmr));
            item3_2.setText(String.format("Для цілі вам необхідно споживати приблизно %d калорій в день.", totalCalories));
        } else   {
            showErrorAlert("Invalid number format");
        }
    }

    private boolean validateFields() {
        if (gender == null) {
            showErrorAlert("Gender");
            return false;
        }

        if (ageField.getText().isEmpty()) {
            showErrorAlert("Age");
            return false;
        }

        if (heightField.getText().isEmpty()) {
            showErrorAlert("Height");
            return false;
        }

        if (weightField.getText().isEmpty()) {
            showErrorAlert("Weight");
            return false;
        }

        if (activityField.getText().isEmpty()) {
            showErrorAlert("Activity");
            return false;
        }

        if (goalField.getText().isEmpty()) {
            showErrorAlert("Goal");
            return false;
        }

        return true;
    }

    private void handleSaveButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Table to File");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            saveTableToFile(file);
        }
    }

    private void saveTableToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Person person : personList) {
                writer.write(String.format("%s,%d,%d,%d,%s,%s,%d%n",
                        person.getGender(), person.getAge(), person.getHeight(),
                        person.getWeight(), person.getActivity(), person.getGoal(),
                        person.getCalories()));
            }
            writer.flush();
            showAlert("Table saved successfully to " + file.getAbsolutePath(), "Save Successful");
        } catch (IOException e) {
            showAlert("Error saving table to file", "Save Error");
        }
    }

    private void showAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private MenuItem myActivityItem(String s) {
        MenuItem item = new MenuItem(s);
        item.setOnAction(actionEvent -> {
            activityField.setText(s);
        });
        return item;
    }

    private MenuItem myGoalItem(String s) {
        MenuItem item = new MenuItem(s);
        item.setOnAction(actionEvent -> {
            goalField.setText(s);
        });
        return item;
    }

    private void checkText(TextField text){
        text.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && !text.getText().isEmpty()) {
                text.setStyle("-fx-border-color: null");
            }
        });
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValid(newValue) || (newValue.startsWith("0")||newValue.startsWith("-") && !newValue.isEmpty())) {
                text.setText(oldValue);
                text.setStyle("-fx-border-color:#d78b89;" +
                        "-fx-border-radius:3; ");
            } else {
                text.setStyle("-fx-border-color: null");
            }
        });
    }


    private void showErrorAlert(String fieldName) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText("Помилкове значення для " + fieldName);
        alert.showAndWait();
    }

    private boolean isValid(String text) {
        return text.matches("-?\\d*\\.?\\d*") && text.indexOf('.') == -1;
    }

    private void calculateCalories(int age, int height, int weight, Gender gender, String activity, String goal) {
        // Основний коефіцієнт метаболізму (BMR) за формулою Harris-Benedic
        if (gender == Gender.MALE) {
            bmr = (int) (BASE_METABOLISM_MALE + (13.397 * weight) + (4.799 * height) - (5.677 * age));
        } else {
            bmr = (int) (BASE_METABOLISM_FEMALE + (9.247 * weight) + (3.098 * height) - (4.330 * age));
        }

        // Множник активності
        double activityMultiplier;
        switch (activity) {
            case "Низький":
                activityMultiplier = 1.2;
                break;
            case "Середній":
                activityMultiplier = 1.55;
                break;
            case "Високий":
                activityMultiplier = 1.9;
                break;
            default:
                activityMultiplier = 1.0;
        }

        int extraCaloriesForGoal = 0;
        // Кількість калорій для різних мет
        switch (goal) {
            case "Схуднення":
                extraCaloriesForGoal = -500;  // Зменшуємо на 500 калорій для похудіння
                break;
            case "Набір ваги":
                extraCaloriesForGoal = 900;   // Додаємо 900 калорій для мети "Гігачад"
                break;
            case "Набір м'язів":
                extraCaloriesForGoal = 300;  // Додаємо 300 калорій для мети "Флеш"
                break;
        }

        // Загальна кількість калорій, враховуючи мету
        totalCalories = (int) (bmr * activityMultiplier + extraCaloriesForGoal);
    }

    public class Person {
        public String gender;
        public int age;
        public int height;
        public int weight;
        public String activity;
        public String goal;
        public int calories;

        public Person(String gender, int age, int height, int weight, String activity, String goal, int calories) {
            this.gender = gender;
            this.age = age;
            this.height = height;
            this.weight = weight;
            this.activity = activity;
            this.goal = goal;
            this.calories = calories;
        }

        public int getWeight() {
            return weight;
        }

        public int getHeight() {
            return height;
        }

        public int getAge() {
            return age;
        }

        public String getGender() {
            return gender;
        }

        public String getActivity() {
            return activity;
        }

        public String getGoal() {
            return goal;
        }

        public int getCalories() {
            return calories;
        }
    }
}
