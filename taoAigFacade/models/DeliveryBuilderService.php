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
namespace itis\taoAigFacade\models;

/**
 *
 * @author Olivier Pedretti
 *        
 */
class DeliveryBuilderService extends \taoSimpleDelivery_models_classes_SimpleDeliveryService {
	public function __construct() {
		parent::__construct ();
	}
	/**
	 * Generate a delivery by compiling a test and linked latter one to the specified group
	 *
	 * @param \core_kernel_classes_Resource $testRsc        	
	 * @param \core_kernel_classes_Resource $groupRsc        	
	 * @throws \InvalidArgumentException if arguments are not of the type '\core_kernel_classes_Resource'
	 * @return \core_kernel_classes_Resource The delivery generated
	 */
	public function deliverTest($testRsc, $groupRsc, $deliveryLabel = null) {
		if (! self::areResources ( array (
				$testRsc,
				$groupRsc 
		) )) {
			throw new \InvalidArgumentException ( 'not resources' );
		}
		$deliveryClass = new \core_kernel_classes_Class ( CLASS_COMPILEDDELIVERY );
		$report = $this->create ( $deliveryClass, $testRsc, ($deliveryLabel) ? $deliveryLabel : 'AigDelivery' );
		$deliveryRsc = $this->parseReport ( $report );
		
		$this->setGroup ( $deliveryRsc, $groupRsc );
		
		return $deliveryRsc;
	}
	/**
	 *
	 * @param \core_kernel_classes_Resource $deliveryRsc        	
	 * @return bool True on success, False otherwise
	 */
	public function deleteDelivery($deliveryRsc) {
		$s = \taoDelivery_models_classes_DeliveryAssemblyService::singleton ();
		$deleted = $s->deleteInstance ( $deliveryRsc );
		return $deleted;
	}
	/**
	 *
	 * @param \core_kernel_classes_Resource $deliveryRsc        	
	 * @param \core_kernel_classes_Resource $groupRsc        	
	 * @throws \common_exception_Error
	 */
	protected function setGroup(\core_kernel_classes_Resource $deliveryRsc, \core_kernel_classes_Resource $groupRsc) {
		$propertyRsc = new \core_kernel_classes_Property ( 'http://www.tao.lu/Ontologies/TAOGroup.rdf#Deliveries' );
		$success = $groupRsc->setPropertyValue ( $propertyRsc, $deliveryRsc );
		if (! $success) {
			throw new \common_exception_Error ( 'failed to set delivery to group' );
		}
	}
	/**
	 *
	 * @param \common_report_Report $report        	
	 * @throws \common_exception_Error
	 * @return \core_kernel_classes_Resource
	 */
	protected function parseReport(\common_report_Report $report) {
		if ($report->containsError ()) {
			throw new \common_exception_Error ( $report->getMessage () );
		}
		return $report->getData ();
	}
	/**
	 *
	 * @param \common_report_Report[] $rs
	 *        	List of resources
	 * @return bool True on success, False otherwise
	 */
	protected function areResources($rs) {
		foreach ( $rs as $rsc ) {
			if (! ($rsc instanceof \core_kernel_classes_Resource)) {
				return false;
			}
		}
		return true;
	}
}
