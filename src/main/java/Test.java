import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.util.HashMap;
import java.util.Map;

public class Test extends Application {

    public static void main(String[] args){launch(args);}



        @Override
        public void start(Stage primaryStage) throws Exception{

            Map<String,Object> parameters = new HashMap<String,Object>();

            parameters.put("company_name","TEST COMPANY");


            //Loading the Login Page
            /*Parent root = FXMLLoader.load(getClass().getResource("C:/Users/ixhaferri/IdeaProjects/Login.fxml"));

            primaryStage.setTitle("Login Page");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();*/

            try {
                JasperReport jasperReport = JasperCompileManager.compileReport("C:/Users/ixhaferri/IdeaProjects/f2.jrxml");
                JasperPrint jp = JasperFillManager.fillReport(jasperReport, parameters,new JREmptyDataSource());

                new JasperViewerFX().viewReport("JRBeanCollectionDataSource example", jp);
            } catch (JRException e) {
                e.printStackTrace();
            }



        }





}
