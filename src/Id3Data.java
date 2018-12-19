import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Id3Data {
    public HashMap<Integer, Integer[]> MailHash;
    public HashSet<Integer> tempSet;


    /**
     * this function inputs all the data from the txt
     * into a HashMap
     **/
    public void inputToHashMap(String path, int posost) throws FileNotFoundException {
        MailHash = new HashMap<Integer, Integer[]>();
        /**loops all the directories until  8**/
        for (int i = 1; i <= posost; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            /**lops all the files**/
            for (File file : dir.listFiles()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();//inputs every word in a local variable
                    if (!word.equals("Subject:")) {//checks if its not Subject otherwise sketchy things may happen
                        int lexi =Integer.parseInt(word);
                        if(MailHash.containsKey(lexi))
                        {
                            MailHash.get(lexi)[0]++;
                        }else{
                        MailHash.put(lexi,new Integer[]{1});
                        }
                    }//end if
                }//end while
                scanner.close();
            }//end for2
        }//end for1
    }//end input To HashMap
}
