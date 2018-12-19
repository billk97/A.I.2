import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Id3Data {
    public HashMap<Integer, Integer[]> MailHash;
    public HashSet<Integer> tempSet;
    private  int [] words ;
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
                tempSet = new HashSet<Integer>();
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();//inputs every word in a local variable
                    if (!word.equals("Subject:")) {//checks if its not Subject otherwise sketchy things may happen
                        int lexi =Integer.parseInt(word);
                        tempSet.add(lexi);
                    }//end if
                }//end while
                scanner.close();
                UpdateMailHash();
            }//end for2
        }//end for1
        Iterator<Integer> it = MailHash.keySet().iterator();
        while (it.hasNext())
        {
            int onoma=it.next();
            if(MailHash.get(onoma)[0]<3||MailHash.get(onoma)[0]>2000)
            {
                it.remove();
            }
        }//end while
        System.out.println("Mailhash: "+ MailHash.size());
        HashWordToTable();
    }//end input To HashMap
    /**this function Updates the MailHash HashMap
     * and increases the counter when an word is found in a email **/
    private void UpdateMailHash()
    {
        /**loops all the tempSet which contains
         * unique word **/
        for (int k : tempSet) {
            /**checks if the (word)=k that is contained in the mail
             * exists in the known words or dictionary
             * and ads an counter which means that the word is found
             * at least 1 time**/
            if (MailHash.containsKey(k)) {
                MailHash.get(k)[0] += 1;
            } else {
                /**if (word)=k does not exists it create on
                 * which contains 1 table 1 column for the number of spam
                 * 1 for the number of ham**/
                MailHash.put(k, new Integer[]{1});
            }
        }//end for
    }//end UpdateMailHash
    public void HashWordToTable()
    {
        int count=0;
        words=new int[MailHash.size()+1];
        for(int lexi :MailHash.keySet())
        {
            words[count]=lexi;
            System.out.println("count: "+count+" : "+ words[count]);
            count++;
        }
        System.out.println("Mailhash: "+ MailHash.size());
    }//end HashWordToTable

}
