package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Scanner;

public class girisController {

    @FXML
    private ImageView btnCikis;

    @FXML
    private ComboBox<String> cmbSehir;

    @FXML
    private ComboBox<String> cmbIlce;

    @FXML
    private Button btnGiris;

    JSONParser JSONDonusturucu;
    boolean sehirIDYaziliMi=false;
    JSONObject objSehirJSON;
    String strSehirJSON;
    JSONArray dzSehirJSON;

    JSONObject objIlceJSON;
    JSONArray dzIlceJSON;

    Scanner trSehirJSON;
    String strIlceJSON;
    tasiyici tasi=new tasiyici();



    @FXML
    private void giris()throws IOException{


        tasi.strIlceJSON = istekAt("https://ezanvakti.herokuapp.com/vakitler?ilce=" + ilceID(cmbIlce.getSelectionModel().getSelectedItem()));
        tasi.sehir = cmbSehir.getSelectionModel().getSelectedItem();
        tasi.ilce = cmbIlce.getSelectionModel().getSelectedItem();





        yeniSayafa();

    }

    private void yeniSayafa(){
        try {
        FXMLLoader fxmlYukleyici = new FXMLLoader(getClass().getResource("anaSayfa.fxml"));
        Parent kok = fxmlYukleyici.load();
        Stage pencere=new Stage();
        pencere.initModality(Modality.APPLICATION_MODAL);
        pencere.initStyle(StageStyle.UNDECORATED);
        pencere.setTitle("Namaz Vakti");
        pencere.setScene(new Scene(kok));
        gizle();
        pencere.show();
    }
    catch (Exception h){
        System.err.println(h);
    }}

    @FXML
    private void initialize(){
        btnCikis.setOpacity(.8);

        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "ImageRotator");

        cmbSehirDuzenle();


    }

    @FXML
    private void cikis() {
        Stage anaPencere = (Stage) btnCikis.getScene().getWindow();
        anaPencere.close();
        Platform.exit();

    }

    @FXML
    private void sehirDegis(ActionEvent event){
        String id=sehirID(cmbSehir.getSelectionModel().getSelectedItem());
        cmbIlceDuzenle(id);
    }

    private void sqliteGuncelle(String sehir,String ilce,String ilceID){
        String sqlSorgu="UPDATE tablo SET (sehir,ilce,ilceID) = (?,?,?) WHERE id = 1";

        try{
            Connection bag=sqlBaglanti();
            PreparedStatement sorgu=bag.prepareStatement(sqlSorgu);

            sorgu.setString(1,sehir);
            sorgu.setString(2,ilce);
            sorgu.setString(3,ilceID);
            sorgu.executeUpdate();
            System.out.println("Güncelleme Başarılı");
        }
        catch (Exception h){
            System.err.println(h+"YENİ SORGU");
        }
    }


    @FXML
    private void cmbSehirDuzenle(){
        try{

            strSehirJSON="{\"sehirler\":"+istekAt("https://ezanvakti.herokuapp.com/sehirler?ulke=2")+"}";
            System.out.println(strSehirJSON);
            JSONDonusturucu=new JSONParser();
            objSehirJSON=(JSONObject) JSONDonusturucu.parse(strSehirJSON);
            dzSehirJSON=(JSONArray) objSehirJSON.get("sehirler");
            for(Object o:dzSehirJSON){
                JSONObject obje=(JSONObject) o;
                String sehir=(String) obje.get("SehirAdi");

                cmbSehir.getItems().add(sehir);
            }

        }
        catch (org.json.simple.parser.ParseException h){
            System.err.println("JSON Dönüştürme Hatası");
        }


    }
    private Connection sqlBaglanti(){

        try{
            Class.forName("org.sqlite.JDBC");
            Connection bag=DriverManager.getConnection("jdbc:sqlite:src/sample/girisController.db");
            return bag;
        }
        catch (Exception h){
            System.err.println(h);
        }
        return  null;

    }

    @FXML
    private void cmbIlceDuzenle(String sehirID){

        String site="https://ezanvakti.herokuapp.com/ilceler?sehir="+sehirID;
        String cevapJSON=istekAt(site);
        if(cevapJSON.isEmpty()){
            System.err.println("İnternet Yok");
            return;
        }
        String strIlceJSON="{\"ilceler\":"+cevapJSON+"}";

        try {
            objIlceJSON =(JSONObject) JSONDonusturucu.parse(strIlceJSON);
            dzIlceJSON=(JSONArray) objIlceJSON.get("ilceler");
            cmbIlce.getItems().clear();
            for(Object o:dzIlceJSON){
                JSONObject obj=(JSONObject) o;
                String strIlce=(String)obj.get("IlceAdi");
                cmbIlce.getItems().add(strIlce);

            }
        }
        catch (ParseException h){
            System.err.println("Dönüştürme Hatası");
        }

    }

    private String ilceID(String ilce){
        String ilceID="";

        for(Object o:dzIlceJSON){
            JSONObject obj=(JSONObject)o;
            if(ilce==obj.get("IlceAdi")){
                ilceID=(String)obj.get("IlceID");
            }
        }

        return ilceID;
    }


    private String sehirID(String sehir){
        String  sehirID="Şehir Bulunamadı";
        for(Object o:dzSehirJSON){
            JSONObject obj=(JSONObject) o;
            if(sehir==obj.get("SehirAdi")){
                sehirID=(String)obj.get("SehirID");
            }
        }


        return sehirID;

    }
   public static   String istekAt(String siteURL) {

        String veri="";
        try {
            URL site = new URL(siteURL);
            URLConnection yc = site.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                veri+=inputLine;

            in.close();
        }
        catch (Exception e) {

        }

        return veri;
    }

    private  void gizle(){
        Stage pencere=(Stage)this.btnCikis.getScene().getWindow();
        pencere.close();

    }

    /*@FXML
    private void eskiGiris(ActionEvent event)throws IOException{
        try {
            FileInpsutStream fs=new FileInputStream(sehirDosya);
            DataInputStream ds=new DataInputStream(fs);

            bfOkuyucu = new BufferedReader(new InputStreamReader(ds));
            dosyadanIlceID=bfOkuyucu.readLine();

            System.out.println(dosyadanIlceID);
        }
        catch (Exception h){
            System.err.println(h);
        }


        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("anaSayfa.fxml"));
        Parent root1=(Parent) fxmlLoader.load();
        Stage st=new Stage();
        st.setScene(new Scene(root1));
    }


*/
}
