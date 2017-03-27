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
package lu.list.itis.dkd.assess.opennlp.density;

import java.util.Formatter;
import java.util.Locale;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class Item {
    private String token;
    private String tag;
    private String lemma;
    private boolean isword;
    private boolean isprop;
    private int rulenumber;
    
    public Item()   // default constructor
    {
      this.token = "";
      this.tag = "";
      this.isword = false;
      this.isprop = false;
      this.rulenumber = 0;
    }
    
    public Item(String token, String tag, String lemma, boolean isword, boolean isprop, int rulenumber)
    {
      this.token = token;
      this.tag = tag;
      this.lemma = lemma;
      this.isword = isword;
      this.isprop = isprop;
      this.rulenumber = rulenumber;
    }
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTag() {
        return tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public boolean isWord() {
        return isword;
    }
    
    public boolean isProp() {
        return isprop;
    }
    
    public void setIsword(boolean isWord) {
        this.isword = isWord;
    }
    
    public void setIsprop(boolean isProp) {
        this.isprop = isProp;
    }
    
    public int getRulenumber() {
        return rulenumber;
    }
    
    public void setRulenumber(int ruleNumber) {
        this.rulenumber = ruleNumber;
    }
    
    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String ToString() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format(
          " {2:#000} {3,-4} {1} {0} {4}",    // most amusing format string you've seen lately
          (this.isprop ? 'P' : ' '),
          (this.isword ? 'W' : ' '),
          this.rulenumber,
          this.tag,
          this.token
      );
        
      String r = formatter.toString();
      if (this.tag.equals(".")) {
          formatter.close();
          return r + "\r\n";    // skip extra line after end of sentence marker
      }

      formatter.close();
      return r;
    }
}
