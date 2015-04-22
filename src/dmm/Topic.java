package dmm;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Topic {
	
	public double customers;
	public double oldcustomers;
	public double[] wordCount;//每维对应一个词，值为该词在这个topic下的次数
	public double[] oldWordCount;

	public double topicProb;
	public int id;
	public int oldID;
	public double count=1.0;
	
	
	public double weights;
	public Topic()
	{
		count =1.0;
		id=-1;
		customers=0;
		wordCount = new double[Global.vocabSize];
	}
	public void WriteTopWords(List<String> vocabularyIndex, BufferedWriter sw)
    {
         double beta = Global.beta;
         double Vbeta = Global.beta * Global.vocabSize;
         double sum =0;
         for(double count:wordCount)
         {
        	 sum+=count;
         }
     
         try {
			sw.write(" Customers:"+ customers+" P:"+customers/Global.numDocuments+"\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         List<Pair> wordsProbsList = new ArrayList<Pair>(); 
		 for (int w = 0; w < Global.vocabSize; w++){
			double p = Math.log(wordCount[w] + beta) - Math.log(sum + Vbeta);
			Pair pair = new Pair(vocabularyIndex.get(w), p, false);			
			wordsProbsList.add(pair);
		}//end foreach word
			
			//print topic				
	
		Collections.sort(wordsProbsList);
		
		for (int i = 0; i < Global.twords; i++){
			try {
				sw.write("\t" + wordsProbsList.get(i).first + " " + Math.exp( (Double) wordsProbsList.get(i).second) + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
     }
	public double logCrp(double currentCustomers)
	{
		double logcrp = 0;
		if(this.customers!=0)
		{
			 logcrp= Math.log(this.customers)-Math.log(currentCustomers-1 + Global.gamma);
		}
	
		return logcrp;
	}
	
	public double logCrpWithKnowledge(double currentCustomers, HashSet<Topic> oldTopics)
	{
		double logcrp = 0;
		
		if(oldTopics.contains(this) )
		{
			 logcrp= Math.log(this.customers+Global.alpha)-Math.log(currentCustomers-1+ oldTopics.size()*Global.alpha + Global.gamma);
		}
		
		else if(this.customers!=0)
		{
			logcrp =  Math.log(this.customers)-Math.log(currentCustomers-1+ oldTopics.size()*Global.alpha + Global.gamma);
		}
		
	
		return logcrp;
	}
	// to be done
	public double logCrpWithTGKnowledge(double nowCustomers, HashSet<Topic> oldTopics,HashMap<Topic,HashSet<Topic>> oldTopicRelations)
	{
		
		
		double logcrp = 0;
		
		if(oldTopics.contains(this) )
		{
			double this_customers =this.customers;
			if(oldTopicRelations.get(this)!=null)
			{
				
				for(Topic tempT:oldTopicRelations.get(this))
				{
					this_customers+=tempT.customers*Global.mu;
				}
			}
			logcrp= Math.log(this_customers+Global.alpha)-Math.log(nowCustomers-1+ oldTopics.size()*Global.alpha + Global.gamma);
			
		}
		
		else if(this.customers!=0)
		{
			logcrp =  Math.log(this.customers)-Math.log(nowCustomers-1+ oldTopics.size()*Global.alpha + Global.gamma);
		}
		
	
		return logcrp;
	}
	
}

   
  
