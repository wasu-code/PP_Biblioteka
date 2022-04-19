
package biblioteka;
//import java.util.Scanner;
//import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;


public class Biblioteka {


    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        File f = new File("C:/pliki_biblioteki");
        f.mkdir();
        
        File f2 = new File("C:/pliki_biblioteki/pliki_uzytkownikow");
        f2.mkdir();
        
        File f3 = new File("C:/pliki_biblioteki/uzytkownicy.bin");
        f3.createNewFile();
        System.out.println("Dane logowania dla konta administrartor - login: admin , hasło: admin");
        Uzytkownik u = new Uzytkownik();
        u.Menu1();
        
    }     

}
