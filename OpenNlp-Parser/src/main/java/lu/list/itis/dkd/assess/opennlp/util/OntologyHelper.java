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
package lu.list.itis.dkd.assess.opennlp.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0.0
 */
public class OntologyHelper {
	
	private String rootNS;
	private OntModel ontModel;

	public OntologyHelper(String rootNS) {
		this.rootNS = rootNS;
	}
	
//	public void init() {
//		//Load file
//		InputStream in = FileManager.get().open(owlPath);
//		
//		//Create Model
//		ontModel = ModelFactory.createOntologyModel();
//		ontModel.read(in, "");
//	}
	
	public void init(String name) {
	    Model model = ModelFactory.createDefaultModel();
	    
        //Set reasoner & spec
//      Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
	    OntModelSpec spec = OntModelSpec.OWL_DL_MEM;
//      spec.setReasoner(reasoner);
        
        //Create Model
        ontModel = ModelFactory.createOntologyModel(spec, model);
    }
	
	public void addProperty (Individual individual, OntProperty property, String content) {
		individual.addProperty(property, content);
	}
	
	public void addProperty (Individual individual, OntProperty property, String content, String language) {
		individual.addProperty(property, ontModel.createLiteral(content, language));
	}
	
	public OntClass connect2class(String name) {
		String uri = rootNS + name;		
		OntClass ontClass = ontModel.getOntClass(uri);
		
		return ontClass;
	}
	
	public OntClass createClass(String name) {
		String uri = rootNS + name;		
		OntClass ontClass = ontModel.createClass(uri);
		
		return ontClass;
	}
	
	public Individual connect2Individual(String name) {
		String uri = rootNS + name;
		
		if (name.equals("TEXT")){
		    System.out.println(uri);
        }
		
		Individual indidividual = ontModel.getIndividual(uri);
		return indidividual;
	}	
		
	public Individual createIndividual(String name, OntClass ontclass) {
		String uri = rootNS + name;
		
		if (name.equals("TEXT")){
            System.out.println(uri);
        }		
		
		Individual individual = ontModel.createIndividual(uri, ontclass);
		return individual;
	}
	
	public OntProperty connect2Property (String name) {
		String uri = rootNS + name;
		OntProperty ontProperty = ontModel.getOntProperty(uri);
		return ontProperty;
	}
	
	public OntProperty connect2Property (String name, String NS) {
		String uri = NS + name;
		OntProperty ontProperty = ontModel.getOntProperty(uri);
		return ontProperty;
	}
		
	public OntProperty createProperty (String name) {
		String uri = rootNS + name;
		OntProperty ontProperty = ontModel.createOntProperty(uri);
		return ontProperty;
	}
	
	public OntProperty createProperty (String name, String NS) {
		String uri = NS + name;
		OntProperty ontProperty = ontModel.createOntProperty(uri);
		return ontProperty;
	}
	
	public ObjectProperty createObjectProperty (String name) {
		String uri = rootNS + name;
		ObjectProperty objectProperty = ontModel.getObjectProperty(uri);
		return objectProperty;
	}
		
	public void save (String path) throws FileNotFoundException {
		FileOutputStream fout = new FileOutputStream(path);
		ontModel.write(fout);
	}		
	
	public void save (OutputStream outputStream) {
		ontModel.write(outputStream);
	}		
	
	public void createObecjtProperty (Individual domain, Individual range, String propertyName) {
		ObjectProperty op = ontModel.getObjectProperty(rootNS + propertyName);
		ontModel.add(domain, op, range);
	}
	
	public void createObecjtProperty (Individual domain, Individual range, OntProperty property) {
		ontModel.add(domain, property, range);
	}
	
	public void createObecjtProperty (Individual domain, Individual range, ObjectProperty property) {
		ontModel.add(domain, property, range);
	}
}
