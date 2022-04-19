package biblioteka;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.EOFException;
//import java.io.File;


public class Ksiazka {
    
    public String tytul;
    public String autor;
    public int rok_wydania;
    public int dostepnosc;
    
    public Ksiazka() {
        
    }
    
    
    public Ksiazka(String tytul, String autor, int rok_wydania, int dostepnosc) {
        this.tytul=tytul;
        this.autor=autor;
        this.rok_wydania=rok_wydania;
        this.dostepnosc=dostepnosc;
    }
    
    public void DodajKsiazke(String ksiazka, int dostepnosc) throws FileNotFoundException, IOException {
        //sprawdź czy istnieje; tak->zwieksz dostępność; nie->dodaj
        //String ksiazka = tytul+";"+autor+";"+rok_wydania/*+";"+dostepnosc*/;
        boolean czyDostepna = SprawdzIstnienie(ksiazka);
        if (czyDostepna==true) {
            //zwiększ ilość
            RandomAccessFile plik =null;
            try {
                plik=new RandomAccessFile("C:/pliki_biblioteki/ksiazki.bin","rw");
                plik.seek(0);
                while (plik.getFilePointer() != plik.length()) {
                    String str = plik.readUTF();
                    if (str.equals(ksiazka)) {
                        int ile = plik.readInt();
                        plik.seek(plik.getFilePointer()-4);
                        plik.writeInt(ile+dostepnosc);
                        break;
                    }
                    plik.seek(plik.getFilePointer()+4);
                }
            } catch (Exception e) {
                System.out.println("Niestety napotkano błąd. Opis błędu: "+e.getMessage());
            } finally {
                plik.close();
            }
        } else {
            RandomAccessFile plik =null;
            try {
                plik=new RandomAccessFile("C:/pliki_biblioteki/ksiazki.bin","rw");
                plik.seek(plik.length());
                plik.writeUTF(ksiazka);
                plik.writeInt(dostepnosc);
            } catch (Exception e) {
                System.out.println("Niestety napotkano błąd. Opis błędu: "+e.getMessage());
            } finally {
                plik.close();
            }
        }
    }
    
    public boolean SprawdzIstnienie(String ksiazka) throws FileNotFoundException, IOException {
        RandomAccessFile plik=null;
        try {
            plik = new RandomAccessFile("C:/pliki_biblioteki/ksiazki.bin","rw");
            plik.seek(0);
            while (plik.getFilePointer() != plik.length()) {
                String str = plik.readUTF();
                if (str.equals(ksiazka)) {
                    plik.close();
                    
                    return true;
                }
                plik.seek(plik.getFilePointer()+4);
            }
            plik.close();
        } catch (EOFException e) {
            plik.close();
            return false;
        } catch (Exception e) {
            System.out.println("Niestety napotkano błąd. Opis błędu: "+e.getMessage());
        } finally {
            plik.close();
        }
        
        return false;
    }
    
    public boolean SprawdzDostepnosc(String ksiazka) throws FileNotFoundException, IOException {
        
        RandomAccessFile plik = new RandomAccessFile("C:/pliki_biblioteki/ksiazki.bin","rw");
        plik.seek(0);
        while (plik.getFilePointer() != plik.length()) {
            String str = plik.readUTF();
            if (str.equals(ksiazka)) {
                if (plik.readInt()>0) {
                    plik.close();
                    return true;
                }
                plik.close();
                return false;
            }
            plik.seek(plik.getFilePointer()+4);
        }
        plik.close();
        return false;
    }
    
    //wyświetlenie książek danego autora -ok?
    //to to raczej nie w tej klasie
    public void SzukajAutor() throws FileNotFoundException, IOException {
        Scanner daneW = new Scanner(System.in);
        System.out.println("Podaj autora:");
        String autor = daneW.nextLine();
        
        RandomAccessFile plik =null;
        try {
            plik=new RandomAccessFile("C:/pliki_biblioteki/ksiazki.bin","rw");
            //odczytuj plik aż znajdzie string contain autor
            plik.seek(0);
            String str;
            autor = autor.toLowerCase();
        
            //sprawdź ile maksymalnie może być książek
            int ile=0;
            while (plik.getFilePointer() != plik.length()) {
                str = plik.readUTF();
                String tab[] = str.split(";");
                String samAutor = tab[1];
                if ((samAutor.toLowerCase().contains(autor))==true) {
                    plik.seek(plik.getFilePointer()+4);
                    ile++;
                } else {
                    plik.seek(plik.getFilePointer()+4);
                }
            }
        
            plik.seek(0);
            String[] sort = new String[ile];
            if (ile==0) {
                System.out.println("Nie znaleziono książek tego autora.");
                plik.close();
                return;
            }
            int ii =0;
        
            System.out.println("Książki tego autora:");
            while (plik.getFilePointer() != plik.length()) {
                str = plik.readUTF();
                String tab[] = str.split(";");
                String samAutor = tab[1];
                if ((samAutor.toLowerCase().contains(autor))==true) {
                    sort[ii]=str+" dostępne: "+plik.readInt();
                    ii++;
                } else {
                    plik.seek(plik.getFilePointer()+4);
                }
            }
        
            //sortowanie
            String temp;
            for (int i=0;i<ile;i++) {
                for (int j=i+1;j<ile;j++) {
                    if (sort[i].compareTo(sort[j])>0) {
                        temp=sort[i];
                        sort[i]=sort[j];
                        sort[j]=temp;
                    }
                }
            }
            for (int i=0;i<ile;i++) {
                System.out.println(sort[i]);
            }
            System.out.println();
        }catch (Exception e) {
            System.out.println("Niestety napotkano błąd. Opis błędu: "+e.getMessage());
        } finally {
            plik.close();
        }
    }
    
    public void SzukajTytul() throws IOException {
        Scanner daneW = new Scanner(System.in);
        System.out.println("Podaj tytuł:");
        String tytul = daneW.nextLine();
        
        RandomAccessFile plik =null;
        try {
            plik=new RandomAccessFile("C:/pliki_biblioteki/ksiazki.bin","rw");
            //odczytuj plik aż znajdzie string contain autor
            plik.seek(0);
            String str;
            tytul = tytul.toLowerCase();
        
            //sprawdź ile maksymalnie może być książek
            int ile=0;
            while (plik.getFilePointer() != plik.length()) {
                str = plik.readUTF();
                String tab[] = str.split(";");
                String samTytul = tab[0];
                if ((samTytul.toLowerCase().contains(tytul))==true) {
                    ile++;
                    plik.seek(plik.getFilePointer()+4);
                } else {
                    plik.seek(plik.getFilePointer()+4);
                }
            }
        
            plik.seek(0);
            String[] sort = new String[ile];
            if (ile==0) {
                System.out.println("Nie znaleziono książek o podanym tytule.");
                plik.close();
                return;
            }
            int ii =0;
        
            System.out.println("Książki o tym tytule:");
            while (plik.getFilePointer() != plik.length()) {
                str = plik.readUTF();
                String tab[] = str.split(";");
                String samTytul = tab[0];
                if ((samTytul.toLowerCase().contains(tytul))==true) {
                    sort[ii]=str+" dostępne: "+plik.readInt();
                    ii++;
                } else {
                    plik.seek(plik.getFilePointer()+4);
                }
            }
        
            //sortowanie
            String temp;
            for (int i=0;i<ile;i++) {
                for (int j=i+1;j<ile;j++) {
                    if (sort[i].compareTo(sort[j])>0) {
                        temp=sort[i];
                        sort[i]=sort[j];
                        sort[j]=temp;
                    }
                }
            }
            for (int i=0;i<ile;i++) {
                System.out.println(sort[i]);
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Niestety napotkano błąd. Opis błędu: "+e.getMessage());
        } finally {
            plik.close();
        }
    }
    
    public void ImportujTxt() throws IOException {
        BufferedReader odczyt = null;
        Scanner daneW = new Scanner(System.in);
        System.out.println("Import z pliku\nPodaj scieżkę do pliku tekstowego: ");
        String sciezka = daneW.nextLine();
        
        try {
            odczyt = new BufferedReader(new FileReader(sciezka));
            String line;
            while ((line=odczyt.readLine())!=null) {
                String lineCheck=line;
                if  ((lineCheck.length() - lineCheck.replace(";", "").length())==3) {
                   String[] tab = line.split(";");
                    tytul = tab[0];
                    autor = tab[1];
                    rok_wydania = Integer.parseInt(tab[2]);
                    dostepnosc = Integer.parseInt(tab[3]);
                    DodajKsiazke(tytul+";"+autor+";"+rok_wydania,dostepnosc); 
                    
                } else {
                    System.out.println("Pominięto pozycję podczas importu. Powód: zły format.");
                }
                
                
            }
        } catch (Exception e) {
            System.out.println("Niestety napotkano błąd\nOpis błędu: "+e.getMessage());
        } finally {
            if (odczyt!=null) {
                odczyt.close();
            }
        }
        System.out.println("Zakończono import.");
        System.out.println();
    }
    
    
    
    public void WyswietlWszystkie() throws FileNotFoundException, IOException {
        System.out.println("Oto książki w naszej bibliotece:");
        RandomAccessFile odczyt =null;
        int ile=0;
        try {
            odczyt = new RandomAccessFile("C:/pliki_biblioteki/ksiazki.bin","rw");
            odczyt.seek(0);
            
        
            
            while (odczyt.getFilePointer() != odczyt.length()) {
                odczyt.readUTF();
                odczyt.readInt();
                ile++;
            }
            
            
            if (ile<=0) {
                System.out.println("Księgozbiór jest na razie pusty");
                odczyt.close();
                return;
            } 
        
            String tab[] = new String[ile];
            odczyt.seek(0);
            for (int i=0;i<ile;i++) {
                tab[i]=odczyt.readUTF() +"  dostępnych: "+ odczyt.readInt();
            }
            
            //Sortowanie
            String temp;
            for (int i=0;i<ile;i++) {
                for (int j=i+1;j<ile;j++) {
                    if (tab[i].compareTo(tab[j])>0) {
                        temp=tab[i];
                        tab[i]=tab[j];
                        tab[j]=temp;
                    }
                    
                }
            }
            for (int i=0;i<ile;i++) {
                System.out.println(tab[i]);
            }
        } catch (Exception e) {
            System.out.println("Niestety napotkano błąd. Opis błędu: "+e.getMessage());
        } finally {
            odczyt.close();
        }
    } 
    
    public void WypozyczonoZwrocono(String ksiazka, int plusminus) throws FileNotFoundException, IOException {
        RandomAccessFile plik =null;
        try {
            //zmniejsz o 1
            plik = new RandomAccessFile("C:/pliki_biblioteki/ksiazki.bin","rw");
            plik.seek(0);
            while (plik.getFilePointer() != plik.length()) {
                String str = plik.readUTF();
                if (str.equals(ksiazka)) {
                    int ile = plik.readInt();
                    plik.seek(plik.getFilePointer()-4);
                    plik.writeInt(ile+plusminus);
                    break;
                }
                plik.seek(plik.getFilePointer()+4);
            }
            
        } catch (Exception e) {
            System.out.println("Niestety napotkano błąd. Opis błędu: "+e.getMessage());
        } finally {
            plik.close();
        }
    }
    
    
    
    
    
}
