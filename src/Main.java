
import java.io.FileNotFoundException;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) throws FileNotFoundException
    {
        //this is for Bayes
       //TestBayes testBayes = new TestBayes();
      //testBayes.WriteTxt();

        PrepereID3Data id=new PrepereID3Data();
        id.initializeData();
        Id3Data id3Data =new Id3Data();
        id3Data.Initializer(id.getMailCounter(),id.TrainingDataNumber,id.path);

//        Id3Impl id3 = new Id3Impl();
//        id3.Initializer();

    }
}
