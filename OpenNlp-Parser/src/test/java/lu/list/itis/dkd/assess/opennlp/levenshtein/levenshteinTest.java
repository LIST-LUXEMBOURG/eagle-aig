/**
 * Copyright (c) 2016-2017  Luxembourg Institute of Science and Technology (LIST).
 * 
 * This software is licensed under the Apache License, Version 2.0 (the "License") ; you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at : http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 * for more information about the software, please contact info@list.lu
 */
package lu.list.itis.dkd.assess.opennlp.levenshtein;

import org.junit.Assert;
import org.junit.Test;

import lu.list.itis.dkd.assess.opennlp.Text;
import lu.list.itis.dkd.assess.opennlp.util.Type.Language;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class levenshteinTest {
    
    //TODO Test this
    
    @Test
    public void englishTest() {
        String string = "A computer is a general-purpose device that can be programmed to carry out a set of arithmetic or logical operations automatically. Since a sequence of operations can be readily changed, the computer can solve more than one kind of problem! Conventionally, a computer consists of at least one processing element, typically a central processing unit (CPU), and some form of memory. The processing element carries out arithmetic and logic operations, and a sequencing and control unit can change the order of operations in response to stored information. Peripheral devices allow information to be retrieved from an external source, and the result of operations saved and retrieved.";    
        Text text = new Text(string, Language.EN);

        //Number of sentences
        Assert.assertEquals(5, text.getSentences().size());
            
        TagComparison comparison = new TagComparison(text.getSentences());
        SentenceStatistics statistics = comparison.getStatistics();
        
        //Sentence statistic 1 (1 - 2)
        LevenshteinTagDistance sentenceStatistic1 = statistics.getSentenceStatistics().get(0);
        Assert.assertEquals(1, sentenceStatistic1.getSentence1().getSentenceNumber());
        Assert.assertEquals(2, sentenceStatistic1.getSentence2().getSentenceNumber());
        Assert.assertEquals(2, sentenceStatistic1.getDifference());
        Assert.assertEquals(9, sentenceStatistic1.getCommonTags().size());
        Assert.assertEquals(7, sentenceStatistic1.getDistance());
        Assert.assertEquals(0.6666666666666667, sentenceStatistic1.getNormalizedDistance(), 0);
  
        //Sentence statistic 2 (1 - 3)
        LevenshteinTagDistance sentenceStatistic2 = statistics.getSentenceStatistics().get(1);
        Assert.assertEquals(1, sentenceStatistic2.getSentence1().getSentenceNumber());
        Assert.assertEquals(3, sentenceStatistic2.getSentence2().getSentenceNumber());
        Assert.assertEquals(0, sentenceStatistic2.getDifference());
        Assert.assertEquals(9, sentenceStatistic2.getCommonTags().size());
        Assert.assertEquals(9, sentenceStatistic2.getDistance());
        Assert.assertEquals(0.5714285714285714, sentenceStatistic2.getNormalizedDistance(), 0);
  
        //Sentence statistic 3 (1 - 4)
        LevenshteinTagDistance sentenceStatistic3 = statistics.getSentenceStatistics().get(2);
        Assert.assertEquals(1, sentenceStatistic3.getSentence1().getSentenceNumber());
        Assert.assertEquals(4, sentenceStatistic3.getSentence2().getSentenceNumber());
        Assert.assertEquals(5, sentenceStatistic3.getDifference());
        Assert.assertEquals(11, sentenceStatistic3.getCommonTags().size());
        Assert.assertEquals(8, sentenceStatistic3.getDistance());
        Assert.assertEquals(0.6923076923076923, sentenceStatistic3.getNormalizedDistance(), 0);
  
        //Sentence statistic 4 (1 - 5)
        LevenshteinTagDistance sentenceStatistic4 = statistics.getSentenceStatistics().get(3);
        Assert.assertEquals(1, sentenceStatistic4.getSentence1().getSentenceNumber());
        Assert.assertEquals(5, sentenceStatistic4.getSentence2().getSentenceNumber());
        Assert.assertEquals(2, sentenceStatistic4.getDifference());
        Assert.assertEquals(9, sentenceStatistic4.getCommonTags().size());
        Assert.assertEquals(7, sentenceStatistic4.getDistance());
        Assert.assertEquals(0.6666666666666667, sentenceStatistic4.getNormalizedDistance(), 0);
  
        //Sentence statistic 5 (2 - 3)
        LevenshteinTagDistance sentenceStatistic5 = statistics.getSentenceStatistics().get(4);
        Assert.assertEquals(2, sentenceStatistic5.getSentence1().getSentenceNumber());
        Assert.assertEquals(3, sentenceStatistic5.getSentence2().getSentenceNumber());
        Assert.assertEquals(2, sentenceStatistic5.getDifference());
        Assert.assertEquals(7, sentenceStatistic5.getCommonTags().size());
        Assert.assertEquals(6, sentenceStatistic5.getDistance());
        Assert.assertEquals(0.7142857142857143, sentenceStatistic5.getNormalizedDistance(), 0);
  
        //Sentence statistic 6 (2 - 4)
        LevenshteinTagDistance sentenceStatistic6 = statistics.getSentenceStatistics().get(5);
        Assert.assertEquals(2, sentenceStatistic6.getSentence1().getSentenceNumber());
        Assert.assertEquals(4, sentenceStatistic6.getSentence2().getSentenceNumber());
        Assert.assertEquals(7, sentenceStatistic6.getDifference());
        Assert.assertEquals(7, sentenceStatistic6.getCommonTags().size());
        Assert.assertEquals(10, sentenceStatistic6.getDistance());
        Assert.assertEquals(0.6153846153846154, sentenceStatistic6.getNormalizedDistance(), 0);
  
        //Sentence statistic 7 (2 - 5)
        LevenshteinTagDistance sentenceStatistic7 = statistics.getSentenceStatistics().get(6);
        Assert.assertEquals(2, sentenceStatistic7.getSentence1().getSentenceNumber());
        Assert.assertEquals(5, sentenceStatistic7.getSentence2().getSentenceNumber());
        Assert.assertEquals(0, sentenceStatistic7.getDifference());
        Assert.assertEquals(6, sentenceStatistic7.getCommonTags().size());
        Assert.assertEquals(9, sentenceStatistic7.getDistance());
        Assert.assertEquals(0.5263157894736843, sentenceStatistic7.getNormalizedDistance(), 0);
  
        //Sentence statistic 8 (3 - 4)
        LevenshteinTagDistance sentenceStatistic8 = statistics.getSentenceStatistics().get(7);
        Assert.assertEquals(3, sentenceStatistic8.getSentence1().getSentenceNumber());
        Assert.assertEquals(4, sentenceStatistic8.getSentence2().getSentenceNumber());
        Assert.assertEquals(5, sentenceStatistic8.getDifference());
        Assert.assertEquals(6, sentenceStatistic8.getCommonTags().size());
        Assert.assertEquals(10, sentenceStatistic8.getDistance());
        Assert.assertEquals(0.6153846153846154, sentenceStatistic8.getNormalizedDistance(), 0);
  
        //Sentence statistic 9 (3 - 5)
        LevenshteinTagDistance sentenceStatistic9 = statistics.getSentenceStatistics().get(8);
        Assert.assertEquals(3, sentenceStatistic9.getSentence1().getSentenceNumber());
        Assert.assertEquals(5, sentenceStatistic9.getSentence2().getSentenceNumber());
        Assert.assertEquals(2, sentenceStatistic9.getDifference());
        Assert.assertEquals(6, sentenceStatistic9.getCommonTags().size());
        Assert.assertEquals(10, sentenceStatistic9.getDistance());
        Assert.assertEquals(0.5238095238095238, sentenceStatistic9.getNormalizedDistance(), 0);
  
        //Sentence statistic 10 (4 - 5)
        LevenshteinTagDistance sentenceStatistic10 = statistics.getSentenceStatistics().get(9);
        Assert.assertEquals(4, sentenceStatistic10.getSentence1().getSentenceNumber());
        Assert.assertEquals(5, sentenceStatistic10.getSentence2().getSentenceNumber());
        Assert.assertEquals(7, sentenceStatistic10.getDifference());
        Assert.assertEquals(8, sentenceStatistic10.getCommonTags().size());
        Assert.assertEquals(9, sentenceStatistic10.getDistance());
        Assert.assertEquals(0.6538461538461539, sentenceStatistic10.getNormalizedDistance(), 0);
     
        comparison.getStatistics().print();
    }
}
