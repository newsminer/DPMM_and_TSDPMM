package dmm;
import java.util.ArrayList;
import java.util.HashMap;



public class Global
{
    
    public static double gamma =1.0;
    public static double alpha =1.0;
    public static double beta =1.0;
    public static int twords = 20;
 
    public static int vocabSize = 1;
    public static double numDocuments=0;
    
    public static double mu=1.0;
    
   
    public static double w = 1;//打折权重

 
    public static ArrayList<String> vocabularyIndex; //
    public static HashMap<String, Integer> vocabulary; //
 


    //----------------------------------------------
    public static int globalWordSize;
    public static HashMap<Integer, Integer> wordIndexDictionary;//新，旧
    public static HashMap<Integer, Integer> wordIndexDictionary_r;
    public static ArrayList<String> newVocabularyIndex; //
    public static HashMap<String, Integer> newVocabulary; //
	
    

    
    public void setVocabularyIndex(ArrayList<String> list)
    {
    	vocabularyIndex = list;
    }
    public ArrayList<String> getVocabularyIndex()
    {
    	return vocabularyIndex;
    }
    
    public void setVocabulary(HashMap<String, Integer> map)
    {
    	vocabulary = map;
    }
    public HashMap<String, Integer> getVocabulary()
    {
    	return vocabulary;
    }
//    public  void setCustomers(double numCustomers)
//    {
//    	this.customers=numCustomers;
//    }
//    public static double getCustomers()
//    {
//    	return customers;
//    }
}
