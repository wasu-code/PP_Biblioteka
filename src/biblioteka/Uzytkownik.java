
package biblioteka;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.io.EOFException;
import java.util.InputMismatchException;
import java.io.File;


public class Uzytkownik {
    
    public String login = null;
    private String haslo = null;
    public String uprawnienia = "guest";
    
    public int maxWypozyczonych=3;
    public Ksiazka ks = new Ksiazka();
    
    public Uzytkownik() {
        
    }
    
    public int Wybor(int max) {
        
        int wybor = 0;
        
        while (wybor<1 || wybor>max) {
            try {
                Scanner daneW = new Scanner(System.in);
                wybor = daneW.nextInt();
                
            } catch (java.util.InputMismatchException e) {
                System.out.println("Mysisz wpisać wartość numeryczną");
            }
            if (wybor<1 || wybor>max) {
                System.out.println("Podaj numer z zakresu od 1 do "+ max);
            }
        }
        
        return wybor;
    }
    
    public void Menu1() throws FileNotFoundException, IOException {
        System.out.println("Menu:\n1) Zaloguj\n2) Zarejestruj\n3) Zakończ");
        Scanner daneW = new Scanner(System.in);
        //boolean loop = true;
        int wybor = Wybor(3);
        
        switch (wybor){
            case 1:
                Logowanie();
                break;
            case 2:
                Rejestracja();
                break;
            case 3:
                this.login=null;
                this.haslo=null;
                this.uprawnienia="guest";
                System.out.println("Do zobaczenia ponownie :)");
                System.exit(0);
                break;
            default:
                System.out.println("Nie podano właściwej wartości");
        } 
    }
    
    private void Rejestracja() throws FileNotFoundException, IOException {
        //inne niż gość, admin i już użyte
        System.out.println("Podaj login");
        Scanner daneW = new Scanner(System.in);
        String loginPodany = daneW.nextLine();
        
        if (SprawdzLogin(loginPodany)==true || loginPodany.equals("admin")==true || loginPodany.equals("guest")==true) {
            System.out.println("Konto o podanym loginie już istnieje:\nCo chcesz zrobić? \n1) Podaj inny login \n2) Przejdź do logowania \n3) Wróć");
            //switch
            int wybor = Wybor(3);
            switch (wybor) {
                case 1:
                    Rejestracja();
                    break;
                case 2:
                    Logowanie();
                    break;
                case 3:
                    Menu1();
                    break;
                default:
                    
            }
        } else {
            System.out.println("Ustaw hasło");
            String hasloPodane = daneW.nextLine();
            //zapisz hasło i login i użytkownik do pliku
            try {
                RandomAccessFile plik = new RandomAccessFile("C:/pliki_biblioteki/uzytkownicy.bin","rw");
                plik.seek(plik.length());
                plik.writeUTF(loginPodany);
                plik.writeUTF(hasloPodane);
                plik.writeUTF("user");
                plik.close();
            } catch (FileNotFoundException e) {
                System.out.println("Nie znaleziono pliku");
            } catch (Exception e) {
                System.out.println("Napotkano następujący błąd: "+ e.getMessage());
            }
            
            
            //zapisano i przejdź do menu zalogowanych
            this.login=loginPodany;
            this.haslo=hasloPodane;
            this.uprawnienia="user";
            
            File nowyUser = new File("C:/pliki_biblioteki/pliki_uzytkownikow/"+this.login+".bin");
            if (nowyUser.createNewFile()) {
            System.out.println("Utworzono profil");
            } 
            
            Menu_zalogowanych();
        }
    }
    
    private void Logowanie() throws FileNotFoundException, IOException{
        //znaleźć login w plik; tak->odczytaj hasło i sprawdź cgodność->zapis this -> menu logowanych; nie-> menu jeszcze raz, rejestracja, wróć
        
        Scanner daneW = new Scanner(System.in);
        
        //SprawdzLogin();
        System.out.println("Podaj login");
        String loginPodany = daneW.nextLine();
        
        String loginWPliku;
        String hasloPodane;
        if (loginPodany.equals("admin")) {
                    System.out.println("Podaj hasło:");
                        //Scanner danehaslo = new Scanner(System.in);
                        hasloPodane=daneW.nextLine();
                        if (hasloPodane.equals("admin")) {
                            this.login="admin";
                            this.haslo="admin";
                            this.uprawnienia="admin";
                            Menu_administratora();
                            return;
                        } else {
                            System.out.println("Niewłaściwe hasło: \n1) Spróbuj ponownie \n2) Przejdź do rejestracji \n3) wróć");
                            int wybor = Wybor(3);
                            switch (wybor) {
                                case 1:
                                    Logowanie();
                                    return;
                                case 2:
                                    Rejestracja();
                                    return;
                                case 3:
                                    Menu1();
                                    return;
                                default:
                            }
                        }
        } else {
            RandomAccessFile plik = new RandomAccessFile("C:/pliki_biblioteki/uzytkownicy.bin","rw");
            plik.seek(0);
            while (plik.getFilePointer() != plik.length()) {
                loginWPliku = plik.readUTF();
                if (loginWPliku.equals(loginPodany)) {
                    System.out.println("Podaj hasło:");
                    hasloPodane = daneW.nextLine();
                    if (hasloPodane.equals(plik.readUTF())) {
                        this.login=loginPodany;
                        this.haslo=hasloPodane;
                        this.uprawnienia =plik.readUTF();
                        plik.close();
                        Menu_zalogowanych();
                        return;
                    } else {
                        System.out.println("Niezgodne dane. Co chcesz zrobić: \n1) Spróbuj ponownie \n2) Przejdź do rejestracji \3) wróć");
                        int wybor = Wybor(3);
                        switch (wybor) {
                            case 1:
                                plik.close();
                                Logowanie();
                                return;
                            case 2:
                                plik.close();
                                Rejestracja();
                                return;
                            case 3:
                                plik.close();
                                Menu1();
                                return;
                            default:
                        }
                    }
                }
            }
            System.out.println("Nie znaleziono konta o podanym loginie. \n1) Spróbuj ponownie \n2) Zarejestruj się \n3) Wróć");
            int wybor =Wybor(3);
            switch (wybor) {
                case 1:
                    plik.close();
                    Logowanie();
                    return;
                case 2:
                    plik.close();
                    Rejestracja();
                    return;
                case 3:
                    plik.close();
                    Menu1();
                    return;
            }
            plik.close();
        }
        System.out.println("Operacja logowania nie powiodła się. Jeśli nie posiadasz konta, spróbuj się zarejestrować.");
        Menu1();
        //Napotkaliśmy następujący błąd getMessage Spróbuj ponownie Logowanie()
    }
    
    public boolean SprawdzLogin(String loginPodany) throws FileNotFoundException, IOException {
        
        RandomAccessFile plik = new RandomAccessFile("C:/pliki_biblioteki/uzytkownicy.bin","rw");
        plik.seek(0);
        String loginWPliku;
        
        while (plik.getFilePointer() != plik.length()) {
            try {
                loginWPliku = plik.readUTF();
            } catch (EOFException e) {
                
                break;
            }
            if (loginWPliku.equals(loginPodany)) {
                this.login=loginPodany;
                
                break;
            } else {
                try {
                    plik.readUTF();
                    plik.readUTF();
                } catch (EOFException e) {
                    
                    break;
                }
                
            }
        }
        plik.close();
        if (this.login==null) {
            return false;
        } else {
            return true;
        }
    }
    
    
    public void Wypozycz() throws FileNotFoundException, IOException {
        
        String sciezka = "C:/pliki_biblioteki/pliki_uzytkownikow/"+this.login+".bin";
        
        RandomAccessFile plik = new RandomAccessFile(sciezka,"rw");
        System.out.println("Wprowadź nazwę książki w formacie tytuł;autor;rok_wydania");
        Scanner daneW = new Scanner(System.in);
        String ksiazka = daneW.nextLine();
        
        //sprawdź czy już nie wypoprzyczona
        boolean mozna = true;
        int licznik=0;
        while (plik.getFilePointer() != plik.length()) {
            String str = plik.readUTF();
            if (str.equals(ksiazka)) {
                System.out.println("Ta książka została już przez ciebie wyporzyczona");
                mozna = false;
                break;
            }
            licznik++;
        }
        
        if (licznik>=this.maxWypozyczonych) {
            mozna=false;
            System.out.println("Nie można wypożyczyć więcej niż+" +this.maxWypozyczonych+" książki. Książka nie została wypożyczona.");
            plik.close();
            return;
        }
        //sprawdź dostępność i wypożycz
        if (mozna) {
           Ksiazka k1 = new Ksiazka();
            if (k1.SprawdzDostepnosc(ksiazka)) {
                //zapisz do pliku
                k1.WypozyczonoZwrocono(ksiazka,-1);
                plik.writeUTF(ksiazka);
                plik.close();
                System.out.println("Wypożyczono");
                Menu_zalogowanych();
            } else {
                System.out.println("Przykro nam, podana książka ("+ksiazka+") została już wyporzyczona lub nie znajduje się w naszej bazie danych");
                plik.close();
                Menu_zalogowanych();
            } 
        } 
        plik.close();
    }
    
    //zwróć kasiążkę
    public void Zwroc() throws FileNotFoundException, IOException {
        File sprawdz = new File("C:/pliki_biblioteki/pliki_uzytkownikow/"+this.login+".bin");
        String ksiazka="";
        try {
        if (sprawdz.exists()) {
            RandomAccessFile plik = new RandomAccessFile("C:/pliki_biblioteki/pliki_uzytkownikow/"+this.login+".bin","rw");
            RandomAccessFile temp = new RandomAccessFile("C:/pliki_biblioteki/pliki_uzytkownikow/"+this.login+".temp","rw");
            System.out.println("Wprowadź nazwę książki w formacie tytuł;autor;rok_wydania");
            Scanner daneW = new Scanner(System.in);
            ksiazka = daneW.nextLine();
            
            plik.seek(0);
            boolean flaga = false;
            while (plik.getFilePointer() != plik.length()) {
                String str = plik.readUTF();
                if (str.equals(ksiazka)) {
                    flaga = true;
                }
            }
            if (flaga ==false) {
                System.out.println("Nie wypożyczałeś takiej książki");
                plik.close();
                temp.close();
                return;
            }
            plik.seek(0);
            while (plik.getFilePointer() != plik.length()) {
                String str = plik.readUTF();
                if (str.equals(ksiazka)) {
                    //nic nie rób
                } else {
                    temp.writeUTF(str);
                }
            }
            //usun start
            plik.close();
            temp.close();
            
            
            File stary = new File("C:/pliki_biblioteki/pliki_uzytkownikow/"+this.login+".bin");
            if (stary.delete()) {
                //System.out.println("log: usunięto");
            } else {
                //System.out.println("log: nie usunięto");
                System.out.println("Przepraszamy, zwrot książki nie powiódł się.");
                return;
            }
            //rename
            File nameold = new File("C:/pliki_biblioteki/pliki_uzytkownikow/"+this.login+".temp");
            File namenew = new File("C:/pliki_biblioteki/pliki_uzytkownikow/"+this.login+".bin");
            if (nameold.renameTo(namenew)) {
               //System.out.println("log: rename success");
            } else {
                //System.out.println("log: rename failed");
            }
            
            
        } else {
            System.out.println("nie masz wyporzyczonych książek");
        }
        
        } catch (Exception e) {
            System.out.println("Przepraszamy, zwrot książki nie powiódł się.");
            Menu_zalogowanych();
            return;
        }
        ks.DodajKsiazke(ksiazka,1);
        System.out.println("Zwrócono książkę.");
        Menu_zalogowanych();
    }
    
    //menus
    
    private void Menu_zalogowanych() throws FileNotFoundException, IOException {
        System.out.println("Menu:\n1) Szukaj po autorze \n2) Szukaj po Tytule \n3) Wyświetl wszystkie (alfabetycznie) \n4) Wypożycz \n5) Zwróć książkę \n6) Wyloguj");
        
        int wybor = Wybor(6);
        switch (wybor){
            case 1:
                ks.SzukajAutor();
                break;
            case 2:
                ks.SzukajTytul();
                break;
            case 3:
                ks.WyswietlWszystkie();
                break;
            case 4:
                Wypozycz();
                break;
            case 5:
                Zwroc();
                break;
            case 6:
                this.login=null;
                this.haslo=null;
                this.uprawnienia="guest";
                Menu1();
                return;
            default:
                System.out.println("Nie podano właściwej wartości");
        }
        Menu_zalogowanych();
    }
    
    private void Menu_administratora() throws FileNotFoundException, IOException {
        System.out.println("Menu:\n1) Szukaj po autorze \n2) Szukaj po Tytule \n3) Wyświetl wszystkie \n4) Dodaj książkę do księgozbioru \n5) Importuj książki do księgozbioru z pliku txt format: tytuł;autor;rok_wydania;dostępność    każda książka w osobnej lini \n6) Wyloguj");
        
        int wybor = Wybor(6);
        switch (wybor){
            case 1:
                ks.SzukajAutor();
                break;
            case 2:
                ks.SzukajTytul();
                break;
            case 3:
                ks.WyswietlWszystkie();
                break;
            case 4:
                Scanner daneW = new Scanner(System.in);
                System.out.println("Podaj tytuł");
                String tytul = daneW.nextLine();
                while (tytul.contains(";")) {
                    System.out.println("Tytuł nie powinien zawierać średników. Spróbuj jeszcze raz");
                    tytul=daneW.nextLine();
                }
                System.out.println("Podaj autora");
                String autor = daneW.nextLine();
                while (autor.contains(";")) {
                    System.out.println("Autor nie powinien zawierać średników. Spróbuj jeszcze raz");
                    autor=daneW.nextLine();
                }
                System.out.println("Podaj rok wydania");
                int rok = Wybor(Integer.MAX_VALUE);
                String ksiazka=tytul+";"+autor+";"+rok;
                System.out.println("Podaj ile książek dodajesz:");
                int ilosc = Wybor(Integer.MAX_VALUE);
                ks.DodajKsiazke(ksiazka,ilosc);
                System.out.println("Dodano");
                System.out.println();
                break;
            case 5:
                ks.ImportujTxt();
                break;
            case 6:
                this.login=null;
                this.haslo=null;
                this.uprawnienia="guest";
                Menu1();
                return;
            default:
                System.out.println("Nie podano właściwej wartości");
        }
        Menu_administratora();
    }
    
    
    
    
    
}
