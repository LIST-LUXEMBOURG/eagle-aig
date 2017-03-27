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
return [ 
		'name' => 'taoAigFacade',
		'label' => 'Automatic Item Generation Facade',
		'description' => 'Automatic Item Generation Facade extension',
		'license' => 'Apache-2.0',
		'version' => '1.1.0',
		'author' => 'Luxembourg Institute of Science and Technology (LIST)',
		'requires' => [ 
				'tao' => '>=2.10.0',
				'taoGroups' => '*',
				'taoItems' => '2.8.1',
				'taoQtiItem' => '*',
				'taoQtiTest' => '*',
				'taoTestTaker' => '*',
				'taoOutcomeUi' => '*',
				'taoDelivery' => '*',
				'taoSimpleDelivery' => '*' 
		],
		'models' => [ 
				'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf' 
		],
		'install' => [ 
				'rdf' => [ 
						dirname ( __FILE__ ) . '/models/ontology/TAOAIGFacade.rdf' 
				],
				'php' => [ 
						dirname ( __FILE__ ) . '/install/script/addToAutoloader.php' 
				] 
		],
		'uninstall' => [ 
				'php' => [ 
						dirname ( __FILE__ ) . '/install/script/removeFromAutoloader.php' 
				] 
		],
		'managementRole' => 'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#AigFacadeManagerRole',
		'acl' => [ 
				[ 
						'grant',
						'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#AigFacadeManagerRole',
						array (
								'ext' => 'taoAigFacade' 
						) 
				]/*,
				[
						'grant',
						'http://www.tao.lu/Ontologies/TAOAIGFacade.rdf#AigFacadeManagerRole',
						[
								'ext' => 'taoTestTaker' 
						]
				] */
		],
		'routes' => [ 
				'/taoAigFacade' => 'itis\\taoAigFacade\\controller' 
		],
		'constants' => [
				
				// extension name
				"EXT_NAME" => 'taoAigFacade',
				
				// default module name
				'DEFAULT_MODULE_NAME' => 'RestFacade',
				
				// default action name
				'DEFAULT_ACTION_NAME' => 'index',
				
				// BASE WWW the web resources path
				'BASE_WWW' => ROOT_URL . 'taoAigFacade/views/',
				
				// BASE URL (usually the domain root)
				'BASE_URL' => ROOT_URL . 'taoAigFacade',
				
				// actions directory
				"DIR_ACTIONS" => dirname ( __FILE__ ) . DIRECTORY_SEPARATOR . "controller" . DIRECTORY_SEPARATOR,
				
				// models directory
				"DIR_MODELS" => dirname ( __FILE__ ) . DIRECTORY_SEPARATOR . "models" . DIRECTORY_SEPARATOR,
				
				// views directory
				"DIR_VIEWS" => dirname ( __FILE__ ) . DIRECTORY_SEPARATOR . "views" . DIRECTORY_SEPARATOR,
				
				// helpers directory
				"DIR_HELPERS" => dirname ( __FILE__ ) . DIRECTORY_SEPARATOR . "helpers" . DIRECTORY_SEPARATOR,
				
				// BASE PATH: the root path in the file system (usually the document root)
				'BASE_PATH' => dirname ( __FILE__ ) . DIRECTORY_SEPARATOR,
				
				'DOCS_PATH' => dirname ( __FILE__ ) . DIRECTORY_SEPARATOR . "views" . DIRECTORY_SEPARATOR . "data" . DIRECTORY_SEPARATOR,
				'DOCS_URL' => ROOT_URL . "taoAigFacade/views/data/",
				
				// TAO extension Paths
				'TAOBASE_WWW' => ROOT_URL . 'tao/views/',
				'TAOVIEW_PATH' => dirname ( dirname ( __FILE__ ) ) . DIRECTORY_SEPARATOR . 'tao' . DIRECTORY_SEPARATOR . 'views' . DIRECTORY_SEPARATOR,
				'TAO_TPL_PATH' => dirname ( dirname ( __FILE__ ) ) . DIRECTORY_SEPARATOR . 'tao' . DIRECTORY_SEPARATOR . 'views' . DIRECTORY_SEPARATOR . 'templates' . DIRECTORY_SEPARATOR 
		],
		'extra' => [ 
				'structures' => dirname ( __FILE__ ) . DIRECTORY_SEPARATOR . 'controller' . DIRECTORY_SEPARATOR . 'structures.xml' 
		] 
];
