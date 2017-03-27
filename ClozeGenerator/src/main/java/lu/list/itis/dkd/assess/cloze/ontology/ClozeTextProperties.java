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
package lu.list.itis.dkd.assess.cloze.ontology;

import com.hp.hpl.jena.ontology.OntProperty;

import lu.list.itis.dkd.assess.opennlp.util.OntologyHelper;

public class ClozeTextProperties {

    private OntProperty content;
//    private OntProperty firstSentence;
    private OntProperty hasClozeSentence;
    
    public ClozeTextProperties(OntologyHelper onto, String namespace) {
        content = onto.createProperty("content", namespace);
        hasClozeSentence = onto.createProperty("clozeSentence", namespace);
//        firstSentence = onto.createProperty("firstSentenceSkipped", namespace);
    }

    public OntProperty getContent() {
        return content;
    }
    
    public OntProperty getHasClozeSentence(){
        return hasClozeSentence;
    }
    
//    public OntProperty getFirstSentence(){
//        return firstSentence;
//    }
    
}
