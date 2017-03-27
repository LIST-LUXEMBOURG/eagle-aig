<?php
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
/**
 *
 * @author Olivier Pedretti
 */
$todefine = array (
		// SIREN configuration
		//'TAO_AIG_FACADE_SIREN_HOST' => 'eagle.list.lu',
		'TAO_AIG_FACADE_SIREN_HOST' => '127.0.0.1',
		'TAO_AIG_FACADE_SIREN_PORT' => '8080',
		
		'TAO_AIG_FACADE_REST_REQUEST_FILENAME' => 'request',
		'TAO_AIG_FACADE_REST_REQUEST_IDENTIFIER' => 'request',
		'TAO_AIG_FACADE_INSTANCE_ROLE_AIGFACADEMANAGER' => 'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#AigFacadeManagerRole',
		
		// extracted from model
		'TAO_AIG_FACADE_MODEL_ITEMCLASS' => 'http://www.tao.lu/Ontologies/TAOItem.rdf#Item',
		'TAO_AIG_FACADE_MODEL_TESTREPORTCLASS' => 'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#TestReportClass',
		'TAO_AIG_FACADE_MODEL_PROPERTY_CREATIONDATE' => 'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#CreationDate',
		'TAO_AIG_FACADE_MODEL_PROPERTY_SOURCETEMPLATE' => 'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#SourceTemplate',
		'TAO_AIG_FACADE_MODEL_PROPERTY_RELATEDCONSTRUCT' => 'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#RelatedConstruct',
		'TAO_AIG_FACADE_MODEL_PROPERTY_ITEMTITLE' => 'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#ItemTitle',
		'TAO_AIG_FACADE_MODEL_PROPERTY_CATEGORY' => 'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#ItemCategory',
		'TAO_AIG_FACADE_MODEL_PROPERTY_CORRECTRESPONSE' => 'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#ItemCorrectResponse',
		'TAO_AIG_FACADE_MODEL_PROPERTY_CREATOR' => 'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#Creator',
		'TAO_AIG_FACADE_MODEL_PROPERTY_SESSION' => 'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#Session',
		
		
		// Item Feature Analysis
		'TAO_AIG_FACADE_ITEMFEATUREANALYSIS_EXTENSTION_NAME' => 'itemFeatureAnalysis', 
		
		// Curl timeout value in seconds
		'TAO_AIG_FACADE_CURL_TIMEOUT' => '600',		
);

?>
