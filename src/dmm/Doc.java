package dmm;
import java.util.ArrayList;
import java.util.HashSet;


public class Doc {
	public int id;
	public Topic topic;
	public ArrayList<Word> words;
	public String str="";

	public int[] wordCount;
	public HashSet<Integer> wordCountSet;

	public Doc()
    {
		topic = null;
		words = new ArrayList<Word>();
    }
	public void AddWord(Word w)
    {
        words.add(w);
    }
	public void AddWord(String w)
    {
        words.add(new Word(w));
    }

	public void AssignTopic(Topic t)
	{
		this.topic = t;
		for(int key:wordCountSet)
		{
			t.wordCount[key]+=this.wordCount[key];
			
		}
		
		t.customers++;    
	}

	public  void UnassignTopic()
	{
		for(int key:wordCountSet)
		{
			topic.wordCount[key]-=this.wordCount[key];
		}
	
		topic.customers--;
	
		this.topic = null;
	}	

}
class Word   {
        public Topic topic;
        
        public String text;
        public int index;
    

        public Word(String text)
        {
            topic = null;
            this.text = text;
            index = -1;
            
        }  
        

       
}

   


