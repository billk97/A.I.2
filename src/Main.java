
import java.io.FileNotFoundException;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) throws FileNotFoundException
    {
      // TestBayes testBayes = new TestBayes();
      // testBayes.WriteTxt();
        //Id3Data id3Data =new Id3Data();
       //id3Data.inputToHashMap("src\\pu_corpora_public\\pu3\\part",8);
        Id3Impl id3= new Id3Impl();
        id3.Initializer();
    }


}
