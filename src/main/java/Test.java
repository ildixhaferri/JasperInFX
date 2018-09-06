import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Test extends Application {

    private Button print, save, backPage, firstPage, nextPage, lastPage, zoomIn, zoomOut;
    private ImageView report;
    private Label lblReportPages;
    private Stage dialog;
    private TextField txtPage;

    private SimpleIntegerProperty currentPage;
    private int imageHeight = 0, imageWidth = 0, reportPages = 0;

    private JasperPrint jasperPrint;


    public static void main(String[] args){launch(args);}



        @Override
        public void start(Stage primaryStage) throws Exception{


            Map<String,Object> parameters = new HashMap<String,Object>();
            currentPage = new SimpleIntegerProperty(this, "currentPage", 1);

            parameters.put("company_name","TEST COMPANY");


            //Loading the Login Page
            /*Parent root = FXMLLoader.load(getClass().getResource("C:/Users/ixhaferri/IdeaProjects/Login.fxml"));

            primaryStage.setTitle("Login Page");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();*/

            Parent root = createContentPane();

            primaryStage = new Stage();
            primaryStage.setScene(new Scene(root));

            primaryStage.show();


            try {
                JasperReport jasperReport = JasperCompileManager.compileReport("C:/Users/Ildi Xhaferri/Desktop/JAVA PROJECTS/2RMLAB/JasperInFX/f2.jrxml");
                JasperPrint jp = JasperFillManager.fillReport(jasperReport, parameters,new JREmptyDataSource());

                viewReport("JRBeanCollectionDataSource example", jp);
            } catch (JRException e) {
                e.printStackTrace();
            }



        }


    private BorderPane createContentPane() {
        print = new Button(null, new ImageView(getClass().getResource("printer.png").toExternalForm()));
        save = new Button(null, new ImageView(getClass().getResource("save.png").toExternalForm()));
        backPage = new Button(null, new ImageView(getClass().getResource("backimg.png").toExternalForm()));
        firstPage = new Button(null, new ImageView(getClass().getResource("firstimg.png").toExternalForm()));
        nextPage = new Button(null, new ImageView(getClass().getResource("nextimg.png").toExternalForm()));
        lastPage = new Button(null, new ImageView(getClass().getResource("lastimg.png").toExternalForm()));
        zoomIn = new Button(null, new ImageView(getClass().getResource("zoomin.png").toExternalForm()));
        zoomOut = new Button(null, new ImageView(getClass().getResource("zoomout.png").toExternalForm()));

        // Pref sizes
        print.setPrefSize(30, 30);
        save.setPrefSize(30, 30);
        backPage.setPrefSize(30, 30);
        firstPage.setPrefSize(30, 30);
        nextPage.setPrefSize(30, 30);
        lastPage.setPrefSize(30, 30);
        zoomIn.setPrefSize(30, 30);
        zoomOut.setPrefSize(30, 30);


        txtPage = new TextField("1");
        txtPage.setPrefSize(40, 30);
        txtPage.setOnAction((ActionEvent event) -> {
            try {
                int p = Integer.parseInt(txtPage.getText());
                //setCurrentPage((p > 0 && p <= reportPages) ? p : 1);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Invalid number", ButtonType.OK).show();
            }
        });

        lblReportPages = new Label("/ 1");

        HBox menu = new HBox(5);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(5));
        menu.setPrefHeight(50.0);
        menu.getChildren().addAll(print, save, firstPage, backPage, txtPage, lblReportPages, nextPage, lastPage, zoomIn, zoomOut);

        // This imageview will preview the pdf inside scrollpane
        report = new ImageView();
        report.setFitHeight(imageHeight);
        report.setFitWidth(imageWidth);

        // Centralizing the ImageView on Scrollpane
        Group contentGroup = new Group();
        contentGroup.getChildren().add(report);

        StackPane stack = new StackPane(contentGroup);
        stack.setAlignment(Pos.CENTER);
        stack.setStyle("-fx-background-color: gray");

        ScrollPane scroll = new ScrollPane(stack);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        BorderPane root = new BorderPane();
        root.setTop(menu);
        root.setCenter(scroll);
        root.setPrefSize(1024, 768);

        return root;
    }


    public void setCurrentPage(int page) {
        try {
            if(page > 0 && page <= reportPages) {
                currentPage.set(page);
                txtPage.setText(Integer.toString(page));

                if (page == 1) {
                    backPage.setDisable(true);
                    firstPage.setDisable(true);
                }

                if (page == reportPages) {
                    nextPage.setDisable(true);
                    lastPage.setDisable(true);
                }

                // Rendering the current page
                float zoom = (float) 1.33;
                BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, page - 1, zoom);
                WritableImage fxImage = new WritableImage(imageHeight, imageWidth);
                report.setImage(SwingFXUtils.toFXImage(image, fxImage));
            }
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    public void viewReport(String title, JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;

        // Report rendered image properties
        imageHeight = jasperPrint.getPageHeight() + 284;
        imageWidth = jasperPrint.getPageWidth() + 201;
        reportPages = jasperPrint.getPages().size();
        lblReportPages.setText("/ " + reportPages);

        setCurrentPage(1);

        // With only one page those buttons are unnecessary
        if (reportPages == 1) {
            nextPage.setDisable(true);
            lastPage.setDisable(true);
        }

        //setTitle(title);
        //show();
    }

}
