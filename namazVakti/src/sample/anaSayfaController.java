package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.Date;
import java.util.Locale;

import java.sql.*;


public class anaSayfaController  {
    @FXML
    ImageView btnKucult;

    @FXML
    BorderPane bpAnaLayout;

    @FXML
    Label lblKalanSaat;

    @FXML
    Label lblGun1;
    @FXML
    Label lblGun1Imsak;
    @FXML
    Label lblGun1Ogle;
    @FXML
    Label lblGun1Ikindi;
    @FXML
    Label lblGun1Aksam;
    @FXML
    Label lblGun1Yatsi;

    @FXML
    Label lblGun2;
    @FXML
    Label lblGun2Imsak;
    @FXML
    Label lblGun2Ogle;
    @FXML
    Label lblGun2Ikindi;
    @FXML
    Label lblGun2Aksam;
    @FXML
    Label lblGun2Yatsi;

    @FXML
    Label lblGun3;
    @FXML
    Label lblGun3Imsak;
    @FXML
    Label lblGun3Ogle;
    @FXML
    Label lblGun3Ikindi;
    @FXML
    Label lblGun3Aksam;
    @FXML
    Label lblGun3Yatsi;

    @FXML
    Label lblGun4;
    @FXML
    Label lblGun4Imsak;
    @FXML
    Label lblGun4Ogle;
    @FXML
    Label lblGun4Ikindi;
    @FXML
    Label lblGun4Aksam;
    @FXML
    Label lblGun4Yatsi;

    @FXML
    Label lblGun5;
    @FXML
    Label lblGun5Imsak;
    @FXML
    Label lblGun5Ogle;
    @FXML
    Label lblGun5Ikindi;
    @FXML
    Label lblGun5Aksam;
    @FXML
    Label lblGun5Yatsi;

    @FXML
    Label lblYer;

    @FXML
    Label lblZaman;

    double koordinatX=0;
    double koordinatY=0;
    String sehir;
    String ilce;
    JSONObject objIlceJSON;
    JSONArray dzIlceJSON;
    String strIlceJSON;
    JSONParser JSONDonusturucu=new JSONParser();
    tasiyici tasi=new tasiyici();
    String[] dzSimdi;

    int sayac;
    LocalDateTime simdi;
    LocalDateTime imsak;
    LocalDateTime ogle;
    LocalDateTime ikindi;
    LocalDateTime aksam;
    LocalDateTime yatsi;

    Locale lclTurkiye=new Locale("tr", "TR");

    int imsakKalan;
    int ogleKalan;
    int ikindiKalan;
    int aksamKalan;
    int yatsiKalan;

    int[] dzImsakKalan;
    int[] dzOgleKalan;
    int[] dzIkindiKalan;
    int[] dzAksamKalan;
    int[] dzYatsiKalan;
    ArrayList<String> liste;
    int saat;
    int dakika;
    int saniye;

    public anaSayfaController(){
        //tasi.strIlceJSON="0";
    }


    @FXML
    private ImageView butonCikis;



    @FXML
    private void kucult(){
        ((Stage)btnKucult.getScene().getWindow()).setIconified(true);
    }

    @FXML
    private void mauseBas(MouseEvent event){
        koordinatX=event.getSceneX();
        koordinatY=event.getSceneY();
    }

    @FXML
    private void mauseSurukle(MouseEvent event){
        Stage st=(Stage)butonCikis.getScene().getWindow();
        st.setX(event.getScreenX()-koordinatX);
        st.setY(event.getScreenY()-koordinatY);
    }


    @FXML
    private void initialize()throws Exception{
        ilce=tasiyici.ilce;
        sehir=tasiyici.sehir;
        ilce=ilce.substring(0,1).toUpperCase(lclTurkiye)+ilce.substring(1).toLowerCase(lclTurkiye);
        sehir=sehir.substring(0,1).toUpperCase(lclTurkiye)+sehir.substring(1).toLowerCase(lclTurkiye);
        lblYer.setText(sehir+" / "+ilce);
        lblKalanAyarla(kalanVakit(bugunTarih(1)),1);
        lblKalanAyarla(kalanVakit(bugunTarih(2)),2);
        lblKalanAyarla(kalanVakit(bugunTarih(3)),3);
        lblKalanAyarla(kalanVakit(bugunTarih(4)),4);
        lblKalanAyarla(kalanVakit(bugunTarih(5)),5);
        sayac=0;

        System.out.println(tasiyici.sehir+" "+tasiyici.ilce);

        //apGunAyarla taskGun=new apGunAyarla();
        //new Thread(taskGun).start();
        //lblZaman.textProperty().bind(taskGun.messageProperty());
        //System.out.println(taskGun.messageProperty().toString());

        apLblKalanAyarla task=new apLblKalanAyarla();
        lblKalanSaat.textProperty().bind(task.messageProperty());

        new Thread(task).start();

        lblKalanSaat.textProperty().addListener(e->{

            liste=kalanVakit(bugunTarih(1));
            simdi=LocalDateTime.now();
            imsak=LocalDateTime.of(simdi.getYear(),simdi.getMonth(),simdi.getDayOfMonth(),Integer.parseInt(liste.get(0).split(":")[0].trim()),Integer.parseInt(liste.get(0).split(":")[1].trim()));
            ogle=LocalDateTime.of(simdi.getYear(),simdi.getMonth(),simdi.getDayOfMonth(),Integer.parseInt(liste.get(1).split(":")[0].trim()),Integer.parseInt(liste.get(1).split(":")[1].trim()));
            ikindi=LocalDateTime.of(simdi.getYear(),simdi.getMonth(),simdi.getDayOfMonth(),Integer.parseInt(liste.get(2).split(":")[0].trim()),Integer.parseInt(liste.get(2).split(":")[1].trim()));
            aksam=LocalDateTime.of(simdi.getYear(),simdi.getMonth(),simdi.getDayOfMonth(),Integer.parseInt(liste.get(3).split(":")[0].trim()),Integer.parseInt(liste.get(3).split(":")[1].trim()));
            yatsi=LocalDateTime.of(simdi.getYear(),simdi.getMonth(),simdi.getDayOfMonth(),Integer.parseInt(liste.get(4).split(":")[0].trim()),Integer.parseInt(liste.get(4).split(":")[1].trim()));
            //saat=Integer.parseInt(lblKalanSaat.getText().replaceAll("[a-zA-ZğüşöçİĞÜŞÖÇ]+:","").trim().split("[.]")[0]);
            //dakika=Integer.parseInt(lblKalanSaat.getText().replaceAll("[a-zA-ZğüşöçİĞÜŞÖÇ]+:","").trim().split("[.]")[1]);
            //saniye=Integer.parseInt(lblKalanSaat.getText().replaceAll("[a-zA-ZğüşöçİĞÜŞÖÇ]+:","").trim().split("[.]")[2]);
            if(lblKalanSaat.getText().contains("İmsak:")) {
                bpAnaLayout.setStyle("-fx-background-color: #CBF0FF;");

            }
            else if(lblKalanSaat.getText().contains("Öğle:")){
                bpAnaLayout.setStyle("-fx-background-color: #FFF0BA;");

            }
            else if(lblKalanSaat.getText().contains("İkindi:")){
                bpAnaLayout.setStyle("-fx-background-color: #FFDC8A;");
            }
            else if(lblKalanSaat.getText().contains("Akşam:")){
                bpAnaLayout.setStyle("-fx-background-color: #6FA5E0;");
            }
            else if(lblKalanSaat.getText().contains("Yatsı:")){
                bpAnaLayout.setStyle("-fx-background-color: #3A75FF;");
            }





        });
        //lblSayacAyarla(kalanVakit(bugunTarih(1)));
        //hangiVakit(kalanVakit(bugunTarih(1)));
    }


    private Connection sqlBaglanti(){

        try{
            Class.forName("org.sqlite.JDBC");
            Connection bag=DriverManager.getConnection("jdbc:sqlite:girisController.db");
            return bag;
        }
        catch (Exception h){
            System.err.println(h);
        }
        return  null;

    }



    private void dosyaYaz(String strYol,String strMetin){
        File dosya=new File(strYol);

    }


    @FXML
    private void cikis(){
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private ArrayList<String> kalanVakit(String gun){
        ArrayList<String> liste=new ArrayList<String>();
        strIlceJSON=tasi.strIlceJSON;
        strIlceJSON="{\"ilceler\":"+strIlceJSON+"}";
        try{
            objIlceJSON=(JSONObject) JSONDonusturucu.parse(strIlceJSON);
            dzIlceJSON=(JSONArray) objIlceJSON.get("ilceler");

            for(Object o:dzIlceJSON){
                JSONObject obj=(JSONObject)o;
                String a=(String)obj.get("MiladiTarihKisa");
                if(((String)obj.get("MiladiTarihKisa")).equals((Object)gun)){
                    liste.add((String)obj.get("Imsak"));
                    liste.add((String)obj.get("Ogle"));
                    liste.add((String)obj.get("Ikindi"));
                    liste.add((String)obj.get("Aksam"));
                    liste.add((String)obj.get("Yatsi"));
                    return liste;
                }
            }
        }
        catch (Exception h){
            System.err.println(h);
        }

        return liste;
    }

    private String bugunTarih(int gun){
        DateTimeFormatter tf=DateTimeFormatter.ofPattern("dd.MM.yyy");
        LocalDateTime simdi = LocalDateTime.now();
        String tarih=tf.format(simdi);
        if(gun!=1) {
             tarih = tf.format(simdi.plusDays(gun-1));
        }

        return tarih;
    }


    private void lblKalanAyarla(ArrayList<String>liste,int gun){
        switch (gun){
            case 1:
                lblGun1.setText(bugunTarih(1));
                lblGun1Imsak.setText(liste.get(0));
                lblGun1Ogle.setText(liste.get(1));
                lblGun1Ikindi.setText(liste.get(2));
                lblGun1Aksam.setText(liste.get(3));
                lblGun1Yatsi.setText(liste.get(4));
                break;
            case 2:
                lblGun2.setText(bugunTarih(2));
                lblGun2Imsak.setText(liste.get(0));
                lblGun2Ogle.setText(liste.get(1));
                lblGun2Ikindi.setText(liste.get(2));
                lblGun2Aksam.setText(liste.get(3));
                lblGun2Yatsi.setText(liste.get(4));
                break;
            case 3:
                lblGun3.setText(bugunTarih(3));
                lblGun3Imsak.setText(liste.get(0));
                lblGun3Ogle.setText(liste.get(1));
                lblGun3Ikindi.setText(liste.get(2));
                lblGun3Aksam.setText(liste.get(3));
                lblGun3Yatsi.setText(liste.get(4));
                break;
            case 4:
                lblGun4.setText(bugunTarih(4));
                lblGun4Imsak.setText(liste.get(0));
                lblGun4Ogle.setText(liste.get(1));
                lblGun4Ikindi.setText(liste.get(2));
                lblGun4Aksam.setText(liste.get(3));
                lblGun4Yatsi.setText(liste.get(4));
                break;
            case 5:
                lblGun5.setText(bugunTarih(5));
                lblGun5Imsak.setText(liste.get(0));
                lblGun5Ogle.setText(liste.get(1));
                lblGun5Ikindi.setText(liste.get(2));
                lblGun5Aksam.setText(liste.get(3));
                lblGun5Yatsi.setText(liste.get(4));
                break;
        }
    }


    private int[] saniyedenSaate(long sn){
        int saniye=(int)sn;
        int dakika=saniye/60;
        saniye=saniye%60;

        int saat=dakika/60;
        dakika=dakika%60;

        int[]dizi={saat,dakika,saniye};
        return dizi;
    }



    private String suankiSaat(){
        DateTimeFormatter tf=DateTimeFormatter.ofPattern("HH:mm:ssss");
        return tf.format(LocalDateTime.now());
    }

    @FXML
    private String arkaPlanAyarla(){
        String renk="";


        return null;
    }


}






class apLblKalanAyarla extends Task<Void> {

    tasiyici tasi = new tasiyici();

    JSONObject objIlceJSON;
    JSONArray dzIlceJSON;
    String strIlceJSON;
    JSONParser JSONDonusturucu = new JSONParser();

    LocalDateTime simdi = LocalDateTime.now();
    LocalDateTime imsak = tasi.imsak;
    LocalDateTime ogle = tasi.ogle;
    LocalDateTime ikindi = tasi.ikindi;
    LocalDateTime aksam = tasi.aksam;
    LocalDateTime yatsi = tasi.yatsi;
    LocalDateTime imsakYarin;
    int i = 0;
    ArrayList<String> liste;
    ArrayList<String> listeYarin;

    int imsakKalan;
    int ogleKalan;
    int ikindiKalan;
    int aksamKalan;
    int yatsiKalan;
    int imsakYarinKalan;

    int[] dzImsakKalan;
    int[] dzOgleKalan;
    int[] dzIkindiKalan;
    int[] dzAksamKalan;
    int[] dzYatsiKalan;
    int[] dzImsakYarinKalan;

    String[] strDzImsakKalan;
    String[] strDzOgleKalan;
    String[] strDzIkindiKalan;
    String[] strDzAksamKalan;
    String[] strDzYatsiKalan;
    String[] strDzImsakYarinKalan;

    String strSaat;
    String strDakika;
    String strSanyiye;

    @Override
    protected Void call() {

        while (true) {
            liste = liste = kalanVakit(bugunTarih(1));
            listeYarin = kalanVakit(bugunTarih(2));
            simdi = LocalDateTime.now();
            imsakYarin = LocalDateTime.of(simdi.getYear(), simdi.getMonth(), simdi.plusDays(1).getDayOfMonth(), Integer.parseInt(listeYarin.get(0).split(":")[0].trim()), Integer.parseInt(listeYarin.get(0).split(":")[1].trim()));
            imsak = LocalDateTime.of(simdi.getYear(), simdi.getMonth(), simdi.getDayOfMonth(), Integer.parseInt(liste.get(0).split(":")[0].trim()), Integer.parseInt(liste.get(0).split(":")[1].trim()));
            ogle = LocalDateTime.of(simdi.getYear(), simdi.getMonth(), simdi.getDayOfMonth(), Integer.parseInt(liste.get(1).split(":")[0].trim()), Integer.parseInt(liste.get(1).split(":")[1].trim()));
            ikindi = LocalDateTime.of(simdi.getYear(), simdi.getMonth(), simdi.getDayOfMonth(), Integer.parseInt(liste.get(2).split(":")[0].trim()), Integer.parseInt(liste.get(2).split(":")[1].trim()));
            aksam = LocalDateTime.of(simdi.getYear(), simdi.getMonth(), simdi.getDayOfMonth(), Integer.parseInt(liste.get(3).split(":")[0].trim()), Integer.parseInt(liste.get(3).split(":")[1].trim()));
            yatsi = LocalDateTime.of(simdi.getYear(), simdi.getMonth(), simdi.getDayOfMonth(), Integer.parseInt(liste.get(4).split(":")[0].trim()), Integer.parseInt(liste.get(4).split(":")[1].trim()));

            imsakKalan = (int) Duration.between(simdi, imsak).getSeconds();
            ogleKalan = (int) Duration.between(simdi, ogle).getSeconds();
            ikindiKalan = (int) Duration.between(simdi, ikindi).getSeconds();
            aksamKalan = (int) Duration.between(simdi, aksam).getSeconds();
            yatsiKalan = (int) Duration.between(simdi, yatsi).getSeconds();
            imsakYarinKalan = (int) Duration.between(simdi, imsakYarin).getSeconds();

            dzImsakYarinKalan = saniyedenSaate(imsakKalan);
            dzImsakKalan = saniyedenSaate(imsakKalan);
            dzOgleKalan = saniyedenSaate(ogleKalan);
            dzIkindiKalan = saniyedenSaate(ikindiKalan);
            dzAksamKalan = saniyedenSaate(aksamKalan);
            dzYatsiKalan = saniyedenSaate(yatsiKalan);


            if (dzYatsiKalan[0] < 0 || dzYatsiKalan[1] < 0 || dzYatsiKalan[2] < 0) {
                //yatsı vakti geçmiş imsak bekleniyor
                strSaat = ((24 - simdi.getHour()) + imsakYarin.getHour()) + "";
                strDakika = (60 - simdi.getMinute() + imsakYarin.getHour()) + "";
                strSanyiye = (60 - simdi.getSecond()) + "";
                updateMessage("İmsak: " + strSaat + "." + strDakika + "." + strSanyiye);
                //updateMessage("İmsaK: "+dzImsakYarinKalan[0]+"."+dzImsakYarinKalan[1]+"."+dzImsakYarinKalan[2]);
            } else if (dzAksamKalan[0] < 0 || dzAksamKalan[1] < 0 || dzAksamKalan[2] < 0) {
                //aksam vakti geçmiş yatsı bekleniyor
                updateMessage("Yatsı: " + dzYatsiKalan[0] + "." + dzYatsiKalan[1] + "." + dzYatsiKalan[2]);
            } else if (dzIkindiKalan[2] < 0 || dzIkindiKalan[1] < 0 || dzIkindiKalan[2] < 0) {
                //ikindi vakti geçmiş akşam bekleniyor
                updateMessage("Akşam: " + dzAksamKalan[0] + "." + dzAksamKalan[1] + "." + dzAksamKalan[2]);
            } else if (dzOgleKalan[0] < 0 || dzOgleKalan[1] < 0 || dzOgleKalan[2] < 0) {
                //ogle vakti geçmiş ikindi bekleniyor
                updateMessage("İkindi: " + dzIkindiKalan[0] + "." + dzIkindiKalan[1] + "." + dzIkindiKalan[2]);
            } else if (dzImsakKalan[0] < 0 || dzImsakKalan[1] < 0 || dzImsakKalan[2] < 0) {
                //imsak vaktigeçmiş öğle bekleniyor
                updateMessage("Öğle: " + dzOgleKalan[0] + "." + dzOgleKalan[1] + "." + dzOgleKalan[2]);
            } else {
                //imsak vakti bekleniyor

                updateMessage("İmsak: " +dzImsakKalan[0] + "." + dzImsakKalan[1] + "." + dzImsakKalan[2]);
            }



            if (i < 0)
                break;
        }

        return null;
    }

    private String[] zamanFormat(int[] dzZaman) {


        String[] dzFormatlanmisZaman = {strSaat, strDakika, strSanyiye};

        return dzFormatlanmisZaman;
    }

    @Override
    protected void updateMessage(String message) {

        super.updateMessage(message);
    }

    @Override
    protected void updateProgress(long workDone, long max) {
        updateMessage(workDone + "");
        super.updateProgress(workDone, max);
    }

    private ArrayList<String> kalanVakit(String gun) {
        ArrayList<String> liste = new ArrayList<String>();
        strIlceJSON = tasi.strIlceJSON;
        strIlceJSON = "{\"ilceler\":" + strIlceJSON + "}";
        try {
            objIlceJSON = (JSONObject) JSONDonusturucu.parse(strIlceJSON);
            dzIlceJSON = (JSONArray) objIlceJSON.get("ilceler");

            for (Object o : dzIlceJSON) {
                JSONObject obj = (JSONObject) o;
                String a = (String) obj.get("MiladiTarihKisa");
                if (((String) obj.get("MiladiTarihKisa")).equals((Object) gun)) {
                    liste.add((String) obj.get("Imsak"));
                    liste.add((String) obj.get("Ogle"));
                    liste.add((String) obj.get("Ikindi"));
                    liste.add((String) obj.get("Aksam"));
                    liste.add((String) obj.get("Yatsi"));
                    liste.add("");
                    return liste;
                }
            }
        } catch (Exception h) {
            System.err.println(h);
        }

        return liste;
    }

    private String bugunTarih(int gun) {
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("dd.MM.yyy");
        LocalDateTime simdi = LocalDateTime.now();
        String tarih = tf.format(simdi);
        if (gun != 1) {
            tarih = tf.format(simdi.plusDays(gun - 1));
        }

        return tarih;
    }


    private int[] saniyedenSaate(long sn) {
        int saniye = (int) sn;
        int dakika = saniye / 60;

        saniye = saniye % 60;

        int saat = dakika / 60;
        dakika = dakika % 60;


        int[] dizi = {saat, dakika, saniye};
        return dizi;
    }
}











class apGunAyarla extends Task<Void>{
    FileInputStream fs=(FileInputStream) getClass().getResourceAsStream("/sehir.txt");
    int i=0;

    BufferedReader bfOkuyucu;
    String strDosyadanVeri;
    String strJSON;
    JSONObject objJSON;
    JSONArray dzJSON;
    JSONParser JSONDonusturucu=new JSONParser();
    JSONObject gun;
    String strHicriGun;
    LocalDateTime simdi=LocalDateTime.now();
    Locale turkiye=new Locale("tr", "TR");
    String strMiladiGun;
    private  String bugunTarih(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy",turkiye );
        Date simdi = new Date();
        String tarih=sdf.format((Date)simdi);


        return tarih;
    }

    @Override
    protected Void call() {
        strMiladiGun=bugunTarih();
        strJSON = istekAt("https://ezanvakti.herokuapp.com/vakitler?ilce=" + dosyadanIlceID());
        strJSON = "{\"ilceler\":" + strJSON + "}";
        try {
            objJSON = (JSONObject) JSONDonusturucu.parse(strJSON);
            dzJSON = (JSONArray) objJSON.get("ilceler");
            gun = (JSONObject) dzJSON.get(0);
            strHicriGun = gun.get("HicriTarihUzun").toString();
        }
        catch (Exception h) {
            System.err.println("Hata: " + h);
        }
        try {
            Thread.sleep(1000);
        } catch (Exception h) {
            System.err.println(h);
        }
        while (true){
            if(simdi.getHour()==23&&simdi.getMinute()==59&&simdi.getSecond()==59) {
                strMiladiGun=bugunTarih();
                strJSON = istekAt("https://ezanvakti.herokuapp.com/vakitler?ilce=" + dosyadanIlceID());
                strJSON = "{\"ilceler\":" + strJSON + "}";
                try {
                    objJSON = (JSONObject) JSONDonusturucu.parse(strJSON);
                    dzJSON = (JSONArray) objJSON.get("ilceler");
                    gun = (JSONObject) dzJSON.get(0);
                    strHicriGun = gun.get("HicriTarihUzun").toString();
                }
                catch (Exception h) {
                    System.err.println("Hata: " + h);
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception h) {
                    System.err.println(h);
                }
            }
            updateMessage(strHicriGun+" / "+strMiladiGun);
            if (i<0)
                break;
        }
        return null;
    }


    private String dosyadanIlceID(){
        String ilceID;
        String[] dzDosyadanVeri;
        try {
            DataInputStream ds = new DataInputStream(fs);
            bfOkuyucu = new BufferedReader(new InputStreamReader(ds));
            strDosyadanVeri = bfOkuyucu.readLine();
            System.out.println(strDosyadanVeri);
        }
        catch (Exception h) {
            System.err.println(h);
        }

        dzDosyadanVeri=strDosyadanVeri.split(":");
        return dzDosyadanVeri[0].trim();
    }

    @Override
    protected void updateMessage(String message) {
        super.updateMessage(message);
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


}
