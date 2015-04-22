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

public class TGDMM {
	 public ArrayList<Topic> topics;
//	 public Topic[]  too = new Topic[10];
     public double numDocuments; 
   
     public ArrayList<Doc> documents; 
     public ArrayList<String> vocabularyIndex;
     public HashMap<String, Integer> vocabulary; 
     //---------------------------------------------
     public HashMap<String, Integer> oldVocabulary; 
     public HashSet<String> oldVocabularyIndex; 
     public HashSet<Topic> oldTopics;
     public HashMap<Integer, HashSet<Integer>> idRelations;
     public HashMap<Topic,HashSet<Topic>> oldTopicRelations;
    
     public double oldTopicNumber;
    // boolean noChangeMade;
     public TGDMM()
     {
    	 idRelations = new HashMap<Integer, HashSet<Integer>> ();
    	 oldTopicRelations = new HashMap<Topic,HashSet<Topic>>();
    	 oldVocabulary =new HashMap<String, Integer>();
    	 oldVocabularyIndex = new HashSet<String>();
       
         documents = new ArrayList<Doc>();
         vocabulary = new HashMap<String, Integer>();
         vocabularyIndex = new ArrayList<String>();
         topics = new ArrayList<Topic>();
     }
     public void writeInfor()
     {
        
         try {
		    BufferedWriter bw_voc = new BufferedWriter(new FileWriter("new_vacab.txt"));
			StringBuilder sb = new StringBuilder();
		
			for (Entry<String, Integer> item: vocabulary.entrySet())
			{
				sb.append(item.getValue() + "\t" + item.getKey() + "\r\n");
			}
			bw_voc.write(sb.toString());//第五行
			bw_voc.close();
         } catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
         

     }
     public  void writeNode()
     {
    	 try {
    		 BufferedWriter bw_topic = new BufferedWriter(new FileWriter("new_topics.txt"));  //1.topic number 2.Topic: id customers 3.
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
     
    

    //---------------------------------------------------------------------------------------------
//    public void adjustNode(Topic node)
//    {
//       // node.customers = node.customers / Global.customer_weight + node.oldCustomers; // * Global.customer_weight;//
//       
//        double[] tempWord = new double[Global.globalWordSize];
//       
//        for (int i = 0; i < oldVocabularyIndex.size(); ++i)
//        {
//            if (node.oldWordCount != null)
//            {
//                tempWord[i] = node.oldWordCount[i] * Global.w;
//            }
//            if (node.wordCount != null)
//            {
//                if (Global.wordIndexDictionary_r.containsKey(i))
//                {
//                    tempWord[i] += node.wordCount[Global.wordIndexDictionary_r.get(i)];
//                }
//            }
//        }
//        //把旧的词典根据新的词典扩充
//        int temp = 0;
//        if (node.wordCount != null)
//        {
//            for (int i = 0; i < vocabularyIndex.size(); ++i)
//            {
//                if (!Global.wordIndexDictionary.containsKey(i))
//                {
//                    //Console.WriteLine("Yes {0} \t\t{1}", oldVocabularyIndex.Count + i, Global.globalWordSize);
//                    tempWord[oldVocabularyIndex.size() + temp] = node.wordCount[i];
//                    ++temp;
//                }
//            }
//        }
//        node.wordCount = tempWord;   //扩展后的词典存在wordCount中            
//    }
    
//    public  void adjust()
//    {
//     
//          
//            if (documents != null)
//            {
//                numDocuments += documents.size();//
//            }
//      
//
//           
//
//            int size = 0;
//            size = oldVocabularyIndex.size();
//            int tempInt = 0;
//            tempInt = vocabularyIndex.size();
//            for (int i = 0; i < tempInt; ++i)
//            {
//                if (!Global.wordIndexDictionary.containsKey(i))
//                {
//                    size++;
//                }
//            }
//            Global.globalWordSize = size;//
//           
//    
//            for (int i = 0; i < vocabularyIndex.size(); ++i)
//            {
//                if (!Global.wordIndexDictionary.containsKey(i))
//                {
//                    oldVocabularyIndex.add(vocabularyIndex.get(i));
//                    oldVocabulary.put(vocabularyIndex.get(i), oldVocabulary.size());
//
//                }
//            }
//            vocabularyIndex = oldVocabularyIndex;//
//            vocabulary = oldVocabulary;
//
//
//        }

    

    


    
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
     public  void LoadTrnModel()
     {
    	 oldTopics = new HashSet<Topic>();
    	 oldVocabularyIndex = new HashSet<String>();
         oldVocabulary = new HashMap<String,Integer>();
         // if (root == null) root = new HldaTopic();
         try{
         BufferedReader br = new BufferedReader(new FileReader("./vocab.txt"));
          //oldVocab
         String line="";
         while(null!=(line=br.readLine()))
         {
        	 String[] words =line.split("\\s+");
        	 oldVocabularyIndex.add(words[1]);
        	 oldVocabulary.put(words[1], Integer.parseInt(words[0]));
        	 
         }
         br.close();
         
         Global.wordIndexDictionary = new HashMap<Integer, Integer>();//新，旧
         for (int i = 0; i < vocabularyIndex.size(); ++i)
         {
             if (oldVocabulary.containsKey(vocabularyIndex.get(i)))
             {
                 Global.wordIndexDictionary.put(i, oldVocabulary.get(vocabularyIndex.get(i)));
             }
         }
//         Global.wordIndexDictionary_r = new HashMap<Integer, Integer>();
//
//         for ( Entry<Integer,Integer> entry: Global.wordIndexDictionary.entrySet())
//         {
//             Global.wordIndexDictionary_r.put(entry.getValue(), entry.getKey());//
//         }
         
         //oldtopics         
         BufferedReader br_topics = new BufferedReader(new FileReader("./topics.txt"));
   
        String line_topic=br_topics.readLine();
        oldTopicNumber = Double.parseDouble(line_topic.split("\\s+")[1]);
        while(null!=(line_topic=br_topics.readLine()))
        {
        	 //read the realtions
        	BufferedReader br_relation = new BufferedReader(new FileReader("./topicRelations.txt"));
            String line_relation = br_relation.readLine();
            while(null !=line_relation)
            {
            	String[] topic_ids=line_relation.split("\\s+");
               	int id = Integer.parseInt(topic_ids[0]);
               	HashSet<Integer> tempSet = new HashSet<Integer> ();
                for(int i=1;i<topic_ids.length;i++)
               	{
               		tempSet.add(Integer.parseInt(topic_ids[i]));
               	}
                idRelations.put(id, tempSet);
               
                line_relation=br_relation.readLine();
            }
            br_relation.close();
        	   //oldtopics
        	Topic oldTopic = new Topic();
        	oldTopic.oldID = Integer.parseInt(line_topic.split("\\s+")[1]);
        	
        	oldTopic.oldcustomers = Double.parseDouble(line_topic.split("\\s+")[2]);
        	oldTopic.oldWordCount = new double[oldVocabularyIndex.size()];
        	
        	line_topic = br_topics.readLine();
        	String[] word_count =line_topic.split("\\s+");
       	    //oldWordCount
        	for(int i=0;i<word_count.length;i++)
        	{
        		oldTopic.oldWordCount[i] = Double.parseDouble(word_count[i]);
        	}
        	oldTopics.add(oldTopic);
        	topics.add(oldTopic);
        }
        br_topics.close();
        //oldTopicRelations
        for(Topic t:oldTopics)
        {
        	if(idRelations!=null)
        	{
        		t.count = 1+idRelations.size()*Global.mu;
        	
        		if(idRelations.containsKey(t.oldID))
        		{
        			HashSet<Topic> tempSet =new HashSet<Topic>();
        			for(Topic oldTopic:oldTopics)
        			{
        				if(idRelations.get(t.oldID).contains(oldTopic.oldID))
        				{
        					tempSet.add(oldTopic);
        				}
        			}
        			oldTopicRelations.put(t,tempSet);
        		}
        	}
        	
        }
       
     
         }catch(Exception e)
         {
        	 e.printStackTrace();
         }
         
     }

     


//        
//     public  void initTopics()
//     {
//    	 double currentDocuments =0;
//         for (Doc doc:documents)
//         {
//        	 System.out.println("doc"+doc.id);
//        	 currentDocuments+=1;
//        	 doc.wordCount = new int[Global.vocabSize];
//        	 for(Word w:doc.words)
//        	 {
//        		 doc.wordCount[w.index]++;
//        	 }
//        	 if(topics.size()==0)
//        	 {
//        		 Topic t = new Topic();
//        		 topics.add(t);
//        		 doc.AssignTopic(t);
//        	 }
//        	 else
//        	 {
//        		 int selected = chooseTopic(doc,topics,currentDocuments);
//        		 if(selected==topics.size())
//        		 {
//        			 Topic t = new Topic();
//            		 topics.add(t);
//        		 }
//        		 doc.AssignTopic(topics.get(selected));
//        	 }
////        	 doc.wordCount = new int[Global.vocabSize];
////        	 for(Word w:doc.words)
////        	 {
////        		 doc.wordCount[w.index]++;
////        	 }
////        	 Topic t = new Topic();
////    		 topics.add(t);
////    		 doc.AssignTopic(t);
//         }
//     }
     public  void initTopicsWithKnowledge()
     {
    	 double currentDocuments =0;
    	 int vocabSize  = Global.vocabSize;
         for (Doc doc:documents)
         {
        	 System.out.println("doc"+doc.id);
        	 currentDocuments+=1;
        	 doc.wordCount = new int[vocabSize];
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
        		 
        		 int selected = chooseTopicWithTGKnowledge(doc,topics,oldTopics,oldTopicRelations,currentDocuments);
        		 if(selected==topics.size())
        		 {
        			 Topic t = new Topic();
            		 topics.add(t);
        		 }
        		 doc.AssignTopic(topics.get(selected));
        	 }
         }
     }
     

//     public double posteriorLogPdf(Doc doc, int topic) {
//    	 
//         double logPdf= 0;
//         double logC = 0;
//         double logC_i = 0;
//         
//         if(topic==topics.size())
//         {
//        	  logC= logC_new(doc,topic);
//              logC_i =logC_i_new(doc,topic);
//         }
//         else
//         {
//        	 logC= logC(doc,topic);
//             logC_i =logC_i(doc,topic);
//         }
//       
//         logPdf= logC-logC_i;
//         return logPdf;
//     }
     
    public double posteriorLogPdfWithKnowledge(Doc doc, int topic) {
    	 
         double logPdf= 0;
         // new topic
         if(topic==topics.size())
         {
        	 logPdf= logC_new(doc,topic);
         }
         else
         {
        	 logPdf = logCWithKnowledge(doc,topic);
         }
   
         return logPdf;
     }
   
    
    
	private double logC_new(Doc doc, int topic) {
		// TODO Auto-generated method stub
		double beta = Global.beta;
		int vocabSize = Global.vocabSize;
		double sumLogGamma=0;
		double sumLogGamma_i=vocabSize*logGamma(beta);;
		double sum=0;
		
		double num =logGamma(Global.beta);
		for(int key:doc.wordCountSet)
		{
			sumLogGamma+=logGamma(Global.beta+doc.wordCount[key]);
			sum+=doc.wordCount[key];
		}
		sumLogGamma+=(num*(Global.vocabSize-doc.wordCountSet.size()));
		sum+=(Global.beta*Global.vocabSize);
		
//		for(int i=0;i<vocabSize;i++)
//		{
//			sumLogGamma+=logGamma(beta+doc.wordCount[i]);
//			sum+=beta+doc.wordCount[i];
//		}
		return sumLogGamma - logGamma(sum)-(sumLogGamma_i - logGamma(vocabSize*beta));
//		double logGammaSum = logGamma(sum);
//		double logGammaSum_i = logGamma(vocabSize*beta);
//		double logC=sumLogGamma - logGamma(sum);
//		double logC_i=sumLogGamma_i - logGamma(vocabSize*beta);
		
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
	
	private double logCWithKnowledge(Doc doc, int topic) {
		// TODO Auto-generated method stub
    	 double sumLogGamma=0;
    	 double sumLogGamma_i = 0;
    	 double sum=0;
    	 double sum_i=0;
    	 Topic tempTopic = topics.get(topic);
    	 boolean flag=  oldTopics.contains(tempTopic);
    	 double beta = Global.beta;    	 
    	 for(int i=0;i<Global.vocabSize;i++)
    	 {
    		 if(flag && oldVocabularyIndex.contains(vocabularyIndex.get(i)))
     		 {
     			 double tempNewWordCount = tempTopic.wordCount[i]+tempTopic.oldWordCount[Global.wordIndexDictionary.get(i)];
     			 sumLogGamma+=logGamma(beta+tempNewWordCount+doc.wordCount[i]);
     			 sumLogGamma_i+=logGamma(beta+tempNewWordCount);
				 sum+=beta+tempNewWordCount+doc.wordCount[i];
				 sum_i+=beta+tempNewWordCount;
				 
     		 }
    		 else
    		 {    			
    			 sumLogGamma+=logGamma(beta+tempTopic.wordCount[i]+doc.wordCount[i]);
    			 sumLogGamma_i+=logGamma(beta+tempTopic.wordCount[i]);
				 sum+=beta+tempTopic.wordCount[i]+doc.wordCount[i];
				 sum_i+=beta+tempTopic.wordCount[i];
    		 }
    	 }
    	 return sumLogGamma - logGamma(sum)-(sumLogGamma_i - logGamma(sum_i));
//    	 double logGammaSum = logGamma(sum);
//		 double logGammaSum_i = logGamma(sum_i);
//    	 double logC=sumLogGamma - logGamma(sum);
//		 double logC_i = sumLogGamma_i - logGamma(sum_i);
	}
     
//     private double logC_i(Doc doc, int topic) {
// 		// TODO Auto-generated method stub
//    	 double sumLogGamma=0;
//    	 double sum=0;
//    	 for(int i=0;i<Global.vocabSize;i++)
//    	 {
//    		 sumLogGamma+=logGamma(Global.beta+topics.get(topic).wordCount[i]);
//    		 sum+=Global.beta+topics.get(topic).wordCount[i];
//    	 }
//    	 
//    	 double logGammaSum = logGamma(sum);
//    	 double logC_i=sumLogGamma - logGammaSum;
// 		 return logC_i;
// 	}
     
//     private double logC_iWithKnowledge(Doc doc, int topic) {
//  		// TODO Auto-generated method stub
//     	 double sumLogGamma=0;
//     	 double sum=0;
//     	 Topic tempTopic = topics.get(topic);
//     	 boolean flag = oldTopics.contains(tempTopic);
//     	 double beta = Global.beta;
//     	 for(int i=0;i<Global.vocabSize;i++)
//     	 {
//     		 if(flag && oldVocabularyIndex.contains(vocabularyIndex.get(i)))
//     		 {
//     			 double tempNewWordCount = tempTopic.wordCount[i]+tempTopic.oldWordCount[Global.wordIndexDictionary.get(i)];
//     			 sumLogGamma+=logGamma(beta+tempNewWordCount);
//     			 sum+=beta+tempNewWordCount;
//     		 }
//     		 else
//     		 {
//     			sumLogGamma+=logGamma(beta+tempTopic.wordCount[i]);
//     			sum+=beta+tempTopic.wordCount[i];
//     		 }
//     	 }
//     	 
//     	 double logGammaSum = logGamma(sum);
//     	 double logC_i=sumLogGamma - logGammaSum;
//  		 return logC_i;
//  	}
      
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
//     public int chooseTopic(Doc doc,List<Topic> all,double currentDocuments)
//     {
//    	 
//    	
//    	 double[] logP = new double[all.size()+1];
//         for (int i = 0; i < logP.length-1; i++)
//         {
//             logP[i] = all.get(i).logCrp(currentDocuments) + posteriorLogPdf(doc,i) ; //weights如何得到？还要remove空的topic
//         }
//      
//         logP[logP.length-1] =Math.log(Global.gamma)-Math.log(currentDocuments-1 + Global.gamma) +  posteriorLogPdf(doc,logP.length-1);
//         
//         int selected = SRS.sampleLog(logP); 
//         
//         logP =null;  //释放内存
//         return selected;
//         
//         
//     }
     
     public int chooseTopicWithTGKnowledge(Doc doc,List<Topic> all,HashSet<Topic> oldTopics, HashMap<Topic,HashSet<Topic>> oldTopicRelations, double currentDocuments)
     {
    	 //要注意topic的wordcount  以及  crp(考虑新的和已有的)
    	double nowCustomers = 0.0;
 		for(Topic t:oldTopics)
 		{
 			nowCustomers += t.customers*t.count;
 		}
    	
    	 double[] logP = new double[all.size()+1];
         for (int i = 0; i < logP.length-1; i++)
         {
             logP[i] = all.get(i).logCrpWithTGKnowledge(nowCustomers,oldTopics,oldTopicRelations) + posteriorLogPdfWithKnowledge(doc,i) ; //weights如何得到？还要remove空的topic
         }
         
         logP[logP.length-1] =Math.log(Global.gamma)-Math.log(nowCustomers-1+ oldTopics.size()*Global.alpha + Global.gamma) +  posteriorLogPdfWithKnowledge(doc,logP.length-1);
  
         int selected = SRS.sampleLog(logP); 
         
         logP =null;  //释放内存
         return selected;
         
         
     }
 
//
//     protected  void sampleTopic(Doc doc)
//     {
//    	 Topic preTopic = doc.topic;
//         doc.UnassignTopic();   
//         boolean justRemove =false;
//    	 if(preTopic.customers==0)
//         {
//    		 if(!oldTopics.contains(preTopic))
//    		 {
//    			 topics.remove(preTopic);
//    			 justRemove=true;
//    		 }
//         }
//        
//         int selected = chooseTopic(doc,topics, numDocuments);
//         //Add Xi back to the sampled Cluster
//         if(selected==topics.size()) { //if new cluster
////        	 if(!justRemove)
////        	 {
////        		 noChangeMade=false;
////        	 }
//             Topic newTopic= new Topic();
//             topics.add(newTopic);
//            
//         }
////         else {
////             if(noChangeMade && preTopic!=topics.get(selected)) {
////                 noChangeMade=false;
////             }
// //        }
//         doc.AssignTopic(topics.get(selected));
//     }
     
     protected  void sampleTopicWithKnowledge(Doc doc)
     {
    	 Topic preTopic = doc.topic;
         doc.UnassignTopic();   
         boolean justRemove =false;
         if(preTopic.customers==0)
         {
    		 if(!oldTopics.contains(preTopic))
    		 {
    			 topics.remove(preTopic);
    			 justRemove=true;
    		 }
         }
        
         int selected = chooseTopicWithTGKnowledge(doc,topics,oldTopics, oldTopicRelations,numDocuments);
         //Add Xi back to the sampled Cluster
         if(selected==topics.size()) { //if new cluster
//        	 if(!justRemove)
//        	 {
//        		 noChangeMade=false;
//        	 }
             Topic newTopic= new Topic();
             topics.add(newTopic);
            
         }
//         else {
//             if(noChangeMade && preTopic!=topics.get(selected)) {
//                 noChangeMade=false;
//             }
//         }
         doc.AssignTopic(topics.get(selected));
     }

  

//     protected void GibbsSampling()
//     {
//         for(int i=0;i<documents.size();i++)
//         {
//             //Console.WriteLine("Documents:\t{0}/{1}", i, documents.Count);
//             sampleTopic(documents.get(i));
//         }
//     }
     
     protected void GibbsSamplingWithKnowledge()
     {
         for(int i=0;i<documents.size();i++)
         {
             //Console.WriteLine("Documents:\t{0}/{1}", i, documents.Count);
             sampleTopicWithKnowledge(documents.get(i));
         }
     }

     public void StartProcessing(String dir, int iterations)
     {
         Global.wordIndexDictionary = new HashMap<Integer,Integer>();
         
         
         
        // Global.Veta = Global.vocabSize * Global.eta;
         if (dir.equals("nothing"))
         {
//        	 topics.clear();
//             initTopics();
//            
////             noChangeMade=false;
//             int iteration=0;
//             
//             while(iteration<iterations) { // && noChangeMade==false
//           
//                 System.out.println("Iteration  "+ iteration);           
//   //              noChangeMade=true;                 
//
//                 GibbsSampling();
//                
//                 iteration++;                 
//             }
         
         }
         else
         {
             //这里要用到以前的信息了，比较难写

             LoadTrnModel();//将训练好的模型加载进来
             
           
            
             initTopicsWithKnowledge();
        //     noChangeMade=false;
             int iteration=0;
             
             while(iteration<iterations ) { //&& noChangeMade==false
           
                 System.out.println("Iteration  "+ iteration);           
              //   noChangeMade=true;                 

                 GibbsSamplingWithKnowledge();
                
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
					String[] words = line.split("\\s+|#");
//					for(int i=0;i<words.length;i++)
//					{
//					System.out.print(words[i]+"\t");
//					}
//					System.out.print("\r\n");
				//	if (words.length >= 15)
					{
						doc = new Doc();
						doc.id =id;
						doc.str = line;
						for (int n=1;n<words.length;n++)
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
				Global.vocabSize = vocabulary.size();
             } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
     public double CalcPerplexity()
     {
         double sumlog = 0;
         double sumwords = 0;
         double[][] topicNewWordCount= new double[topics.size()][Global.vocabSize];
         double[] topicTotalWord = new double[topics.size()] ;
         for(int i=0;i<topics.size();i++)
         {
        	 topicTotalWord[i]=0;
//        	 if(oldTopics.contains(topics.get(i)))
//        	 {
//        		
//        		 for(int v=0;v<vocabulary.size();v++)
//        		 {
//        			 if(Global.wordIndexDictionary.containsKey(v))
//        			 {
//        				 topicNewWordCount[i][v]=topics.get(i).wordCount[v]+topics.get(i).oldWordCount[Global.wordIndexDictionary.get(v)]; 
//        				 topicTotalWord[i]+= topicNewWordCount[i][v];
//        			 }
//        			 else
//        			 {
//        				 topicNewWordCount[i][v]=topics.get(i).wordCount[v];
//        				 topicTotalWord[i]+= topicNewWordCount[i][v];
//        			 }
//        		 }
//        	 }
//        	 else
        	 {
        		 for(int v=0;v<vocabulary.size();v++)
        		 {
     				 topicNewWordCount[i][v]=topics.get(i).wordCount[v];
    				 topicTotalWord[i]+= topicNewWordCount[i][v];
    			 }
        	 }
        		
        		 
         }
         for (int i = 0; i < documents.size(); i++)
         {
             double logDoc = 0;        
             for (int w = 0; w <documents.get(i).words.size(); w++)
             {
            	 int w_id = documents.get(i).words.get(w).index;
                 double pw=               
               	 (topicNewWordCount[documents.get(i).topic.id][w_id]  + Global.beta) / (topicTotalWord[documents.get(i).topic.id]+ Global.vocabSize *Global.beta);
//                	 if(pw==0)System.out.println("pw==0");
                 
                 
                 logDoc += Math.log(pw);
        
             }
          
             sumlog += logDoc;
             sumwords += documents.get(i).words.size();
         }
        
         double p = Math.exp(-(sumlog) / (sumwords));
         System.out.println("Perp:  " +p);
         return p;
     }
     
//     public double CalcPerplexity()
//     {
//         double sumlog = 0;
//         double sumwords = 0;
//              
//         for (int i = 0; i < documents.size(); i++)
//         {
//             double logDoc = 0;        
//             for (int w = 0; w <documents.get(i).words.size(); w++)
//             {
//            	 int w_id = documents.get(i).words.get(w).index;
//                 double pw = 0;
//                 for (int k = 0; k < topics.size(); k++) 
//                 {
//                	 double temp = 0;
//                	 for(int v =0;v<Global.vocabSize;v++)
//                	 {	
//                		 
//                		 temp+=topics.get(k).wordCount[v];
//                		 
//                	 }
//                      pw += (topics.get(k).wordCount[w_id]  + Global.beta) / (temp+ Global.vocabSize *Global.beta);   //要用之前的wordliklihood啊。。。
//                 }
//                 logDoc += Math.log(pw);
//             }
//             
//             sumlog += logDoc;
//             sumwords += documents.get(i).words.size();
//        
//
//         }
//         double p = Math.exp(-(sumlog) / (sumwords));
//         System.out.println("Perp:  " +p);
//         return p;
//     }
     public static void main(String[] args)
     {
         TGDMM corpus = new TGDMM();
         
         corpus.ReadFromDirectory("./12-may-93");
         System.out.println("Reading finished");
         long startTime = System.currentTimeMillis();
        
         corpus.StartProcessing("load", 100);  //如果需要loadmodel，修改nothing
         long stopTime = System.currentTimeMillis();
         long elapsedTime = stopTime - startTime;
         System.out.println("Completed in "+String.valueOf(elapsedTime/1000.0)+" sec");
    
       //
         corpus.WriteTopicToFile("./new_twords.txt"); //输出topic的top words
        
         System.out.println("numDocuments"+corpus.documents.size()); 
         try {
			FileWriter fw = new FileWriter("new_cluster_result.txt");
//			int id =0;
//			for (Topic t:corpus.topics)
//			{
//				t.id =id;
//				fw.write("Topic "+t.id+"\r\n");
//				
//				id++;
//				for(Doc doc:corpus.documents)
//				{
//					if (doc.topic.id==t.id)
//					{
//						fw.write(doc.id+": "+doc.str+"\r\n");
//					}
//				}
//			}
			for (int i=0;i<corpus.topics.size();i++)
			{
				corpus.topics.get(i).id =i;
		         if(corpus.topics.get(i).customers!=0)
		         {
		        	 fw.write("Topic: "+i+"\r\n");
		        	 if(corpus.oldTopics.contains(corpus.topics.get(i)))
		        	 {
		        		 fw.write("Topic is an old Topic"+corpus.oldTopics.size()+"\r\n");
		        	 }
		        			
		        		 
		        			 
		        	 for(Doc doc:corpus.documents)
						{
							if (doc.topic.id==i)
							{
								fw.write(doc.id+": "+doc.str+"\r\n");
							}
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
      //   corpus.WriteTopicToFile2(@"..\..\HLDA\1000topic-chile.txt",perp);//这个是必须的，因为Global.globalWordSize
     }
     
     
    

//public double CalcPerplexity()
//{
//    double sumlog = 0;
//    double sumwords = 0;
//    HashMap<Integer,Integer> newID2ID = new HashMap();
//   
//    int j=0;
//    for(int i=0;i<topics.size();i++)
//    {
//   	 if(topics.get(i).customers!=0)
//   	 {
//   		 newID2ID.put(j, i);
//   		 j++;
//   	 }
//   	 
//    }
//    double[] topicPro = new double[newID2ID.size()];
//    for(int i=0;i<newID2ID.size();i++)
//    {
//   	 topicPro[i]=(topics.get(newID2ID.get(i)).customers)/(numDocuments);
//    }
//    
//    for (int i = 0; i < documents.size(); i++)
//    {
//        double logDoc = 0;        
//        for (int w = 0; w <documents.get(i).words.size(); w++)
//        {
//       	 int w_id = documents.get(i).words.get(w).index;
//            double pw = 0;
//            for (int k = 0; k < newID2ID.size(); k++) 
//            {
//           	 double temp = 0;
//           	 for(int v =0;v<Global.vocabSize;v++)
//           	 {	
//           		 
//           		 temp+=topics.get(newID2ID.get(k)).wordCount[v];
//           		 
//           	 }
//                 pw += ((topics.get(newID2ID.get(k)).wordCount[w_id]  + Global.beta) / (temp+ Global.vocabSize *Global.beta))
//                    *topicPro[k];   //要用之前的wordliklihood啊。。。
//            }
//            logDoc += Math.log(pw);
//        }
//        
//        sumlog += logDoc;
//        sumwords += documents.get(i).words.size();
//   
//
//    }
//    double p = Math.exp(-(sumlog) / (sumwords));
//    System.out.println("Perp:  " +p);
//    return p;
//}

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
//            //把旧的词典根据新的词典扩充
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
//            node.wordCount = tempWord;   //扩展后的词典存在wordCount中            
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

       