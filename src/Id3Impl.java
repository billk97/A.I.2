import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Id3Impl {
    public HashMap<Integer, Double[]> wordHash;
    public HashSet<Integer> tempSet;
    public String path= "src\\pu_corpora_public\\pu3\\part";
    private double spamCounter=0.0;
    private double hamCounter=0.0;
    double MailCounter=0.0;
    double[][]  IgTable ;

    public void Initializer () throws FileNotFoundException {
        fillWordHash();
        fillNonFoundWords();
        laplace();
        addIG();
        InputToIgTable();
        //printHash();
        SortIgTable();
        PrintIgTable();
        System.out.println("entropy = "+ TotalEntropy());

    }//endInitializer
    /**wordHash=[hamfound,Spamfound,hamNotFound,SpamNotFound,IG]**/
    private void fillWordHash() throws FileNotFoundException {
        wordHash=new HashMap<Integer, Double[]>();
        /**loops all the directories until  8**/
        for (int i = 1; i <= 8; i++) {
            String localPath = path + Integer.toString(i);
            File dir = new File(localPath);
            /**lops all the files**/
            for (File file : dir.listFiles()) {
                MailCounter++;
                tempSet = new HashSet<Integer>();
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    if (!word.equals("Subject:")) {
                        tempSet.add(Integer.parseInt(word));
                    }//end if
                }//end while tempset filled
                if(!checkSpam(file)){
                    hamCounter++;
                    for(int k: tempSet){
                        if(wordHash.containsKey(k)){
                            wordHash.get(k)[0]+=1;
                        }else{
                            wordHash.put(k,new Double[]{1.0,0.0,0.0,0.0,0.0});
                        }//word found in spam mail
                    }//end for
                }else{
                    spamCounter++;
                    for(int k: tempSet){
                        if(wordHash.containsKey(k)){
                            wordHash.get(k)[1]+=1;
                        }else{
                            wordHash.put(k,new Double[]{0.0,1.0,0.0,0.0,0.0});
                        }//word found in ham mail
                    }
                }//end else
                scanner.close();
            }//end for
        }//end for
    }//end fillWordHash
    /**fills the table with the number of **/
    private void fillNonFoundWords(){
        for(int k: wordHash.keySet()){
            wordHash.get(k)[2]=hamCounter-wordHash.get(k)[0];
            wordHash.get(k)[3]=spamCounter-wordHash.get(k)[1];
        }
    }

    private boolean checkSpam(File dir) throws FileNotFoundException {
        if (dir.getName().contains("spmsg")) {
            return true;
        }
        return false;
    }

    private void printHash(){
        for(int k: wordHash.keySet()){
            System.out.println("word "+k+ " possibilities are = "+wordHash.get(k)[0]+" "+wordHash.get(k)[1]+" "+wordHash.get(k)[2]+" "+wordHash.get(k)[3]+" IG = "+wordHash.get(k)[4]);
        }
        int counter=0;
        for(int word: wordHash.keySet()){
            if(!Double.isNaN(wordHash.get(word)[4])){
                counter++;
            }
        }
        System.out.println("no Nan counter = "+counter);
        System.out.println("email counter ="+MailCounter);

    }
    public double log2(double n)
    {
        return (Math.log(n) / Math.log(2));
    }

    private double getSpamProp(){
        return spamCounter/MailCounter;
    }

    private double getHamProp(){
        return 1-getSpamProp();
    }

    private double TotalEntropy(){
        //System.out.println("SpamProbability: "+getSpamProp()+" HamProbability: "+getHamProp() );
        //System.out.println("logSpamProbability: "+log2(getSpamProp())+" logHamProbability: "+log2(getHamProp()) );

        return -(getSpamProp()*log2(getSpamProp())+getHamProp()*log2(getHamProp()));
    }

    private double Entropy(int word,int i){

        double p1=wordHash.get(word)[0]/(wordHash.get(word)[0]+wordHash.get(word)[1]);
        double p2=wordHash.get(word)[1]/(wordHash.get(word)[0]+wordHash.get(word)[1]);
        double p3=wordHash.get(word)[2]/(wordHash.get(word)[2]+wordHash.get(word)[3]);
        double p4=wordHash.get(word)[3]/(wordHash.get(word)[2]+wordHash.get(word)[3]);
        if(i==0){
            return -(p3*log2(p3)+p4*log2(p4));
        }else{
            return -(p1*log2(p1)+p2*log2(p2));
        }
    }

    private double IG(int word){
        double PwordFound=(wordHash.get(word)[0]+wordHash.get(word)[1])/MailCounter;
        double PwordNonFound=(wordHash.get(word)[2]+wordHash.get(word)[3])/MailCounter;
        return TotalEntropy()-(PwordFound*Entropy(word,1)+PwordNonFound*Entropy(word,0));
    }

    private void addIG(){
        for(int k: wordHash.keySet()){
            wordHash.get(k)[4]=IG(k);
        }
    }//end addIG

    private void laplace(){
        for(int k:wordHash.keySet()){
            wordHash.get(k)[0]++;
            wordHash.get(k)[1]++;
        }
        spamCounter+=2;
        hamCounter+=2;
        MailCounter+=4;
    }//end laplace
    private void InputToIgTable()
    {
        IgTable = new double[wordHash.size()][2];
        System.out.println("lentgh" +IgTable.length);
        int i=0;
        for(int k :wordHash.keySet())
        {
           IgTable[i][0]=(double)k;
           IgTable[i][1]=wordHash.get(k)[4];
           i++;
        }

    }
    private void SortIgTable()
    {
        //bubbleSort(IgTable);
       Sort2Table sort = new Sort2Table();
       sort.bubbleSort(IgTable);

    }

    private void PrintIgTable()
    {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 2; j++)
                System.out.print(IgTable[i][j] + " ");
            System.out.println();
        }
    }


}//endImpl
