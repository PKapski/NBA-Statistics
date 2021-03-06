package nba_statistics.controllers.preseason;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nba_statistics.controllers.AccountController;
import nba_statistics.controllers.HelpController;
import nba_statistics.entities.Seasons;
import nba_statistics.services.SeasonsService;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.ResourceBundle;

import static nba_statistics.others.Alerts.*;
import static nba_statistics.others.Alerts.getAlertSeason;

public class AddSeason implements Initializable {
    @FXML private Button logOutBtn;
    @FXML private Text s10; @FXML private Text s11; @FXML private Text s12; @FXML private Text s13;
    @FXML private ComboBox<String> seasonsNameCombox;@FXML private TextField t30; @FXML private TextField t31; @FXML private TextField t32;
    @FXML private RadioButton newSeasonRadioBtn; @FXML private RadioButton existingSeasonRadioBtn; @FXML private Button addedSeasonBtn;
    @FXML private ImageView helpBtn;
    private Visibility v;
    private String currSeason;

    public void changeScreen(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AccountView.fxml"));
        Parent accountParent = loader.load();
        Scene preseasonScene = new Scene(accountParent);
        AccountController accountController = loader.getController();
        accountController.init(AccountController.getUser());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(preseasonScene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        v = new Visibility();
        newSeasonRadioBtn.setSelected(true);
        initSeasonName();
        helpBtn.setImage(new Image("/help.png"));

    }

    private Date getCurrDate(){
        java.util.Date date = new  java.util.Date(); //date from util
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return (Date.valueOf(formatter.format(date)));
    }

    private Date getSeasonStart(){
        SeasonsService seasonsService = new SeasonsService();
        return Date.valueOf(seasonsService.getSeason(currSeason).getStartDate());
    }

    private void initSeasonName()
    {
        SeasonsService seasonsService = new SeasonsService();
        ArrayList<String> seasonsName = seasonsService.getAllSeasonsName();
        seasonsNameCombox.setItems(FXCollections.observableArrayList(seasonsName));
    }

    public void selectNewSeason(){
        existingSeasonRadioBtn.setSelected(false);
        v.setVisibleNewSeason(s10, s11, s12, t30, t31, t32,addedSeasonBtn); seasonsNameCombox.setVisible(false);

    }

    public void selectExistingSeason(){
        newSeasonRadioBtn.setSelected(false);
        v.setVisibleExistingSeason(s10,s11,s12, seasonsNameCombox,t31,t32,addedSeasonBtn); t30.setVisible(false);
    }

    @SuppressWarnings("Duplicates")
    private void initScene(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Preseason/SelectionView.fxml"));
        Parent accountParent = loader.load();
        Selection controller = loader.getController();
        controller.setCurrSeasonTmp(currSeason);
        Scene preseasonScene = new Scene(accountParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(preseasonScene);
        window.show();
    }

    public void addedSeason(Event event) throws IOException {

        if (newSeasonRadioBtn.isSelected()){
            SeasonsService seasonsService = new SeasonsService();

            if (!seasonsService.checkSeason(t30.getText())) {

                switch (seasonsService.getData(t30.getText(), t31.getText(), t32.getText())) {
                    case 0:
                        currSeason = t30.getText();
                        information(0);
                        initScene(event);
                        break;
                    case 1:
                        getAlertDate(t31.getText());
                        break;
                    case 2:
                        getAlertDate(t32.getText());
                        break;
                    case 3:
                        getAlertDate();
                        break;
                }
            } else
                getAlertSeasonRepeat(t30.getText());
        } else if (existingSeasonRadioBtn.isSelected()){
            SeasonsService seasonsDao = new SeasonsService();
            if (seasonsDao.checkSeason(seasonsNameCombox.getValue())) {
                currSeason = seasonsNameCombox.getValue(); //get season but not save to database 'this season exist in database'
                //if(getSeasonStart().compareTo(getCurrDate()) > 0)
                    initScene(event);
                  //  else
                  //  getAlertSeasonIsStarted();
            } else {
                getAlertSeason(seasonsNameCombox.getValue());
            }
        }




    }

    @SuppressWarnings("Duplicates")
    @FXML
    void helpClicked(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Help.fxml"));
        Parent accountParent = (Parent)loader.load();
        HelpController helpController = loader.getController();
        helpController.setView("/Preseason/AddSeasonView.fxml");
        helpController.init();
        Stage parent = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage window = new Stage();
        window.initModality(Modality.WINDOW_MODAL);
        window.initOwner(parent);
        window.setHeight(350);
        window.setWidth(500);
        window.setTitle("Help window");
        Scene reviewerScene = new Scene(accountParent);
        window.setScene(reviewerScene);
        window.show();
    }
}
