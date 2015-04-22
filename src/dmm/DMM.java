package dmm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class DMM {
	 public List<Topic> topics;
     public double numDocuments; 
   
     public List<Doc> documents; 
     public List<String> vocabularyIndex;
     public HashMap<String, Integer> vocabulary; 
     public HashMap<String, Integer> oldVocabulary; 
//     public List<String> oldVocabularyIndex; 
     boolean noChangeMade;
     public DMM()
     {
       
         documents = new ArrayList<Doc>();
         vocabulary = new HashMap<String, Integer>();
         vocabularyIndex = new ArrayList<String>();
         topics = new ArrayList<Topic>();
     }
     public void WriteInfor(BufferedWriter sw)
     {
         /*1.numTopic
          * 2.numDocuments
           * 3.vocabularyIndex£¬separated by ``####''
           * 4.entityVocabularyIndex
           * 5.Dictionary<string, int> vocabulary£¬ string####int;;;;
           * 6.Dictionary<string, int> entityVocabulary£¬ string####int;;;;
           * */
         try {
			sw.write(topics.size()+"\r\n");
			sw.write(numDocuments+"\r\n");
			StringBuilder sb = new StringBuilder();
			int voSize = vocabularyIndex.size();
			for (int i = 0; i < voSize; ++i)
			{
				sb.append(vocabularyIndex.get(i) + "#");
			}
			sw.write(sb.toString()+"\r\n");

			sb = new StringBuilder();
			for (Entry<String, Integer> item: vocabulary.entrySet())
			{
				sb.append(item.getKey() + "#" + item.getValue() + ";;;;");
			}
			sw.write(sb.toString()+"\r\n");
         } catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}

     }
     public  void writeNode(Topic node, BufferedWriter sw)
     {
         /*
          * 2.ID
          * 3.customers
          * 4.wordCount
          * 5.entityCount
          * 6.ncrp
          * 7.weights
          * 8.children number
          * 
          * */
        
         try {
		//	sw.write(node.ID+"\r\n");
		//
         sw.write(node.customers+"\r\n");//

         StringBuilder sb = new StringBuilder();
         int size = 0;
         size = node.wordCount.length;
         for (int i = 0; i < size; ++i)
         {
             sb.append(node.wordCount[i] + "####");
         }
         sw.write(sb.toString()+"\r\n");   
         } catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
     }
     
     public void writeInfor()
     {
        
         try {
		    BufferedWriter bw_voc = new BufferedWriter(new FileWriter("vocab.txt"));
			StringBuilder sb = new StringBuilder();
		
			for (int i=0;i<vocabularyIndex.size();i++)
			{
				sb.append(i + " " + vocabularyIndex.get(i) + "\r\n");
			}
			bw_voc.write(sb.toString());
			bw_voc.close();
         } catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
     }
     
     public  void writeNode()
     {
    	 try {
    		 BufferedWriter bw_topic = new BufferedWriter(new FileWriter("topics.txt"));  //1.topic number 2.Topic: id customers 3.
    		 StringBuilder sb = new StringBuilder();
    		 sb.append("topicsNumber: "+topics.size()+"\r\n");
    		 for (Topic topic:topics)
    		 { 
    			 sb.append("Topic: "+topic.id + " "+topic.customers+"\r\n");
    			 for (int i = 0; i < topic.wordCount.length; ++i)
    			 {
    				 sb.append(topic.wordCount[i] + " ");
    			 }
    			 sb.append("\r\n");
    			   
    		 }
    		 bw_topic.write(sb.toString()); 
    		 bw_topic.close();
 			
       } catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
     }
     


    

     public int AddWord(String word)
     {
         if (!vocabulary.containsKey(word))
         {
        	 int id = vocabulary.size();
             vocabulary.put(word,id); 
             vocabularyIndex.add(word);
         }
         return vocabulary.get(word);
     }

     public void AddDocument(Doc doc)
     {
         for (Word w: doc.words)
         {
             w.index = AddWord(w.text);
         }
         documents.add(doc);
     }
     
     public  void initTopics()
     {
    	 double currentDocuments =0;
         for (Doc doc:documents)
         {
        	 System.out.println("doc"+doc.id);
        	 currentDocuments+=1;
        	 doc.wordCount = new int[Global.vocabSize];
        	 doc.wordCountSet = new HashSet<Integer>();
        	 for(Word w:doc.words)
        	 {
        		 doc.wordCount[w.index]++;
        		 doc.wordCountSet.add(w.index);
        	 }
        	 if(topics.size()==0)
        	 {
        		 Topic t = new Topic();
        		 topics.add(t);
        		 doc.AssignTopic(t);
        	 }
        	 else
        	 {
        		 int selected = chooseTopic(doc,topics,currentDocuments);
        		 if(selected==topics.size())
        		 {
        			 Topic t = new Topic();
            		 topics.add(t);
        		 }
        		 doc.AssignTopic(topics.get(selected));
        	 }
//        	 doc.wordCount = new int[Global.vocabSize];
//        	 for(Word w:doc.words)
//        	 {
//        		 doc.wordCount[w.index]++;
//        	 }
//        	 Topic t = new Topic();
//    		 topics.add(t);
//    		 doc.AssignTopic(t);
         }
     }
     

     public double posteriorLogPdf(Doc doc, int topic) {
    	 
         double logPdf= 0;
         double logC = 0;
         double logC_i = 0;
         
         if(topic==topics.size())
         {
        	  logC= logC_new(doc,topic);
              logC_i =logC_i_new(doc,topic);
         }
         else
         {
        	 logC= logC(doc,topic);
             logC_i =logC_i(doc,topic);
         }
       
         logPdf= logC-logC_i;
         return logPdf;
     }
   
    
    
	private double logC_new(Doc doc, int topic) {
		// TODO Auto-generated method stub
		double sumLogGamma=0;
		double sum=0;
		double num =logGamma(Global.beta);
		for(int key:doc.wordCountSet)
		{
			sumLogGamma+=logGamma(Global.beta+doc.wordCount[key]);
			sum+=doc.wordCount[key];
		}
		sumLogGamma+=(num*(Global.vocabSize-doc.wordCountSet.size()));
		sum+=(Global.beta*Global.vocabSize);
//		for(int i=0;i<Global.vocabSize;i++)
//		{
//			sumLogGamma+=logGamma(Global.beta+doc.wordCount[i]);
//			sum+=Global.beta+doc.wordCount[i];
//		}
   	 
		double logGammaSum = logGamma(sum);
		double logC=sumLogGamma - logGammaSum;
		return logC;
	}
	
	 private double logC_i_new(Doc doc, int topic) {
			// TODO Auto-generated method stub
		 double sumLogGamma=Global.vocabSize*logGamma(Global.beta); 	 
    	 double logGammaSum = logGamma(Global.vocabSize*Global.beta);
    	 double logC_i=sumLogGamma - logGammaSum;
 		 return logC_i;
		}
	
	private double logC(Doc doc, int topic) {
		// TODO Auto-generated method stub
    	 double sumLogGamma=0;
    	 double sum=0;
    	 
    		
    	 for(int i=0;i<Global.vocabSize;i++)
    	 {
    		 sumLogGamma+=logGamma(Global.beta+topics.get(topic).wordCount[i]+doc.wordCount[i]);
    		 sum+=Global.beta+topics.get(topic).wordCount[i]+doc.wordCount[i];
    	 }
    	 
    	 double logGammaSum = logGamma(sum);
    	 double logC=sumLogGamma - logGammaSum;
 		 return logC;
	
	}
     
     private double logC_i(Doc doc, int topic) {
 		// TODO Auto-generated method stub
    	 double sumLogGamma=0;
    	 double sum=0;
    	 for(int i=0;i<Global.vocabSize;i++)
    	 {
    		 sumLogGamma+=logGamma(Global.beta+topics.get(topic).wordCount[i]);
    		 sum+=Global.beta+topics.get(topic).wordCount[i];
    	 }
    	 
    	 double logGammaSum = logGamma(sum);
    	 double logC_i=sumLogGamma - logGammaSum;
 		 return logC_i;
 	}
     
	/**
      * It estimates a numeric approximation of LogGamma function.
      * Modified code from http://introcs.cs.princeton.edu/java/91float/Gamma.java.html
      * 
      * @param x     The input x
      * @return      The value of LogGamma(x)
      */
     private double logGamma(double x) {
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
                         + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
                         +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
        return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
     }
     public int chooseTopic(Doc doc,List<Topic> all,double currentDocuments)
     {
    	 double max = Double.MIN_VALUE;
    	
    	 double[] logP = new double[all.size()+1];
         for (int i = 0; i < logP.length-1; i++)
         {
             logP[i] = all.get(i).logCrp(currentDocuments) + posteriorLogPdf(doc,i) ; 
         }
      
         logP[logP.length-1] =Math.log(Global.gamma)-Math.log(currentDocuments-1 + Global.gamma) +  posteriorLogPdf(doc,logP.length-1);
         
         int selected = SRS.sampleLog(logP); 
         
         logP =null;  
         return selected;
         
         
     }
 

     protected  void sampleTopic(Doc doc)
     {
    	 Topic preTopic = doc.topic;
         doc.UnassignTopic();   
         boolean justRemove =false;
    	 if(preTopic.customers==0)
         {
    		 topics.remove(preTopic);
    		 justRemove=true;
         }
        
         int selected = chooseTopic(doc,topics, numDocuments);
         //Add Xi back to the sampled Cluster
         if(selected==topics.size()) { //if new cluster
        	 if(!justRemove)
        	 {
        		 noChangeMade=false;
        	 }
             Topic newTopic= new Topic();
             topics.add(newTopic);
            
         }
         else {
             if(noChangeMade && preTopic!=topics.get(selected)) {
                 noChangeMade=false;
             }
         }
         doc.AssignTopic(topics.get(selected));
     }

  

     protected void GibbsSampling()
     {
         for(int i=0;i<documents.size();i++)
         {
             //Console.WriteLine("Documents:\t{0}/{1}", i, documents.Count);
             sampleTopic(documents.get(i));
         }
     }

     public void StartProcessing(String dir, int iterations)
     {
         Global.wordIndexDictionary = new HashMap<Integer,Integer>();
         Global.vocabSize = vocabulary.size();
         
         
        // Global.Veta = Global.vocabSize * Global.eta;
         if (dir.equals("nothing"))
         {
        	 topics.clear();
             initTopics();
            
             noChangeMade=false;
             int iteration=0;
             
             while(iteration<iterations && noChangeMade==false) {
           
                 System.out.println("Iteration  "+ iteration);           
                 noChangeMade=true;                 

                 GibbsSampling();
                
                 iteration++;
                 
             }
         
         }
        
     }



  

  
     public  void WriteTopicToFile(String file)
     {

    	 BufferedWriter bw;
		try {
			bw = new BufferedWriter(
						new OutputStreamWriter(	new FileOutputStream(file),"UTF-8"));
			 bw.write("Topic number:"+topics.size()+"\r\n");
	         bw.write("Wordsize:"+ Global.vocabSize+"\r\n");
	         for(Topic t:topics)
	         {
	        	 t.WriteTopWords(vocabularyIndex, bw);
	         }
	         bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }   
    

     public void ReadFromDirectory(String directory)
     {
//         File dir = new File(directory);
         //Document doc;
         Doc doc;
         BufferedReader br;
         String  line="";
        
//         for (File file:dir.listFiles())
//         {
             try {
				br = new BufferedReader(new FileReader(directory));	
				int id =0;
				while (null!=(line = br.readLine()))
				{
					//System.out.println("line:"+line);
					// String[] two = regex.Split(line,);
					String[] words = line.split("\\s+");
//					for(int i=0;i<words.length;i++)
//					{
//					System.out.print(words[i]+"\t");
//					}
//					System.out.print("\r\n");
					//if (words.length >= 15)
					{
						doc = new Doc();
						doc.id =id;
						doc.str = line;
						for (int n=0;n<words.length;n++)
						{
							if (words[n].equals("")) continue;                         
							doc.AddWord(words[n]);                              
						}
						AddDocument(doc);
					}
					id++;
				}
				br.close();
				numDocuments= documents.size();
				Global.numDocuments = documents.size();
             } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }

     public double CalcPerplexity()
     {
         double sumlog = 0;
         double sumwords = 0;
         double[] topicTotalWords = new double [topics.size()];
         for (int k = 0; k < topics.size(); k++) 
         {
        	 double temp = 0;
        	 for(int v =0;v<Global.vocabSize;v++)
        	 {	
        		 
        		 temp+=topics.get(k).wordCount[v];
        		 
        	 }
             topicTotalWords[k]=temp;   
         }
              
         for (int i = 0; i < documents.size(); i++)
         {
             double logDoc = 0;        
             for (int w = 0; w <documents.get(i).words.size(); w++)
             {
            	 int w_id = documents.get(i).words.get(w).index;
                 double pw = 0;
                 pw = (documents.get(i).topic.wordCount[w_id]  + Global.beta) / (topicTotalWords[documents.get(i).topic.id]+ Global.vocabSize *Global.beta);   
                 
                 logDoc += Math.log(pw);
             }
             
             sumlog += logDoc;
             sumwords += documents.get(i).words.size();
        

         }
         double p = Math.exp(-(sumlog) / (sumwords));
         System.out.println("Perp:  " +p);
         return p;
     }
     
     
     public static void main(String[] args)
     {
         DMM corpus = new DMM();
         
         corpus.ReadFromDirectory("./2-MAR-1987");
         System.out.println("Reading finished");
         long startTime = System.currentTimeMillis();
        
         corpus.StartProcessing("nothing", 100);
         long stopTime = System.currentTimeMillis();
         long elapsedTime = stopTime - startTime;
         System.out.println("Completed in "+String.valueOf(elapsedTime/1000.0)+" sec");
    
       //  corpus.GlobalVocabNow();//
         corpus.WriteTopicToFile("./twords.txt");
         //now we should find out whether the algorithm work
         //print out the topic hierarchy
        
         System.out.println("numDocuments"+corpus.documents.size()); 
         try {
			FileWriter fw = new FileWriter("cluster_result.txt");
			int id=0;
			for (Topic t:corpus.topics)
			{
				t.id =id;
				fw.write("Topic "+t.id+"\r\n");
				
				id++;
				for(Doc doc:corpus.documents)
				{
					if (doc.topic.id==t.id)
					{
						fw.write(doc.id+": "+doc.str+"\r\n");
					}
				}
			}
	
			fw.write("-----------------------------------\r\n");
			fw.close();
			corpus.writeInfor();		
			corpus.writeNode();
			double perp = corpus.CalcPerplexity();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       // corpus.WriteTopicToFile_now(@"..\..\HLDA\topic-zhili.txt-now", perp);
      //   corpus.WriteTopicToFile2(@"..\..\HLDA\1000topic-chile.txt",perp);
     }
    
}



       

   

      

       

//        public virtual void adjustNode(HldaTopic node)
//        {
//            node.customers = node.customers / Global.customer_weight + node.oldCustomers; // * Global.customer_weight;//
//            //    node.ncrp += node.oldNcrp;// * Global.w
//            //   node.weights += node.oldWeights;//* Global.w
//            double[] tempWord = new double[Global.globalWordSize];
//           
//            for (int i = 0; i < oldVocabularyIndex.Count(); ++i)
//            {
//                if (node.oldWordCount != null)
//                {
//                    tempWord[i] = node.oldWordCount[i] * Global.w;
//                }
//                if (node.wordCount != null)
//                {
//                    if (Global.wordIndexDictionary_r.ContainsKey(i))
//                    {
//                        tempWord[i] += node.wordCount[Global.wordIndexDictionary_r[i]];
//                    }
//                }
//            }
//           
//            int temp = 0;
//            if (node.wordCount != null)
//            {
//                for (int i = 0; i < vocabularyIndex.Count(); ++i)
//                {
//                    if (!Global.wordIndexDictionary.ContainsKey(i))
//                    {
//                        //Console.WriteLine("Yes {0} \t\t{1}", oldVocabularyIndex.Count + i, Global.globalWordSize);
//                        tempWord[oldVocabularyIndex.Count + temp] = node.wordCount[i];
//                        ++temp;
//                    }
//                }
//            }
//            node.wordCount = tempWord;              
//
//        }
//
//        public void GlobalVocabNow()
//        {
//            Global.vocabularyIndexNow = new List<string>();
//            Global.vocabularyNow = new Dictionary<string, int>();//
//      
//            int size = vocabularyIndex.Count;
//            for (int i = 0; i < size; ++i)
//            {
//                Global.vocabularyIndexNow.Add(vocabularyIndex[i]);
//            }
//            foreach (var item in vocabulary)
//            {
//                Global.vocabularyNow.Add(item.Key, item.Value);
//            }
//        }


       