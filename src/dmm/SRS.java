package dmm;
/* 
 * Copyright (C) 2014 Vasilis Vryniotis <bbriniotis at datumbox.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */



/**
 * Simple Random Sampling class.
 * 
 * @author Vasilis Vryniotis <bbriniotis at datumbox.com>
 */
public class SRS {
    
    /**
     * Samples an observation based on a probability Table.
     * 
     * @param probabilityTable  The probability table
     * @return  The id that was selected based on sampling
     */



        public static int sample(double[] probabilityTable) {
                int topic;
           
         		for (int k = 1; k < probabilityTable.length; k++){
         			probabilityTable[k] += probabilityTable[k - 1];
         		}
         		
         		// scaled sample because of unnormalized p[]
         		double u = MyRandom.nextDouble() * probabilityTable[probabilityTable.length - 1];
         		
         		for (topic = 0; topic < probabilityTable.length; topic++){
         			if (probabilityTable[topic] > u) //sample topic w.r.t distribution p
         				break;
         		}
         		
            
            
            return topic;
        }
        public static int sampleLog(double[] logProbabilityTable) {
            int topic;
            double max = -Double.MAX_VALUE;
            for(int k=0;k<logProbabilityTable.length;++k) {
                if(logProbabilityTable[k]>max) {
                    max=logProbabilityTable[k];
                }
            }
            
     		for (int k = 0; k < logProbabilityTable.length; k++){
     			logProbabilityTable[k] = Math.exp(logProbabilityTable[k]-max);
     			if(k>0)
     			{
     				logProbabilityTable[k]+=logProbabilityTable[k-1];
     			}
     		}
     		
     		// scaled sample because of unnormalized p[]
     		double u = Math.random() * logProbabilityTable[logProbabilityTable.length - 1];
     		
     		for (topic = 0; topic < logProbabilityTable.length; topic++){
     			if (logProbabilityTable[topic] > u) //sample topic w.r.t distribution p
     				break;
     		}
     		
        
        
        return topic;
    }

}
