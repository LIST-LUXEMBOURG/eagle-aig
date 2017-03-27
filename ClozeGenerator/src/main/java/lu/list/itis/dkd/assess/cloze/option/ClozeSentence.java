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
package lu.list.itis.dkd.assess.cloze.option;

import lu.list.itis.dkd.assess.opennlp.Sentence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class ClozeSentence {
    private Sentence sentence;
    private String content;
    private List<Key> keys = new ArrayList<>();
    
    /**
     * Creates a cloze sentence object by using the sentence object String as it is.
     * @param sentence: sentence object
     */
    public ClozeSentence(Sentence sentence) {
        this.sentence = sentence;
        this.content = sentence.getContent();
    }
    
    /**
     * Creates a cloze sentence object. Overrides the original sentence String by the cloze sentence String.
     * @param sentence: sentence object
     * @param clozeSentence: cloze sentence String
     * @throws IOException
     */
    public ClozeSentence(Sentence sentence, String clozeSentence) {
        this.sentence = sentence;
        this.content = clozeSentence;
    }
    
    /**
     * Creates a cloze sentence object. Overrides the original sentence String by the cloze sentence String. 
     * In addition, a list of keys is added to the cloze sentence object.
     * @param sentence: sentence object
     * @param clozeSentence: cloze sentence String
     * @param keys: List of key objects
     * @throws IOException
     */
    public ClozeSentence(Sentence sentence, String clozeSentence, List<Key> keys) {
        this.sentence = sentence;
        this.content = clozeSentence;
        this.keys = keys;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public String getContent() {
        return content.trim();
    }

    public List<Key> getKeys() {
        return keys;
    }
    
    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }
}

