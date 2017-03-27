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
(function(global) {
	"use strict";
	global.lu = global.lu || {};
	global.lu.list = global.lu.list || {};
	global.lu.list.aig = global.lu.list.aig || {};
	global.lu.list.aig.demo = global.lu.list.aig.demo || {};
	global.lu.list.aig.demo.Tools = new function Tools() {
		if (Tools.prototype._singletonInstance) {
			return Tools.prototype._singletonInstance;
		}
		Tools.prototype._singletonInstance = this;

		function exists(em) {
			return (typeof em !== "undefined");
		}

		function addCustomEvt(p_evtStr, p_cb) {
			if (objHasProperty(global, 'addEventListener')) {
				// FF2, FF3
				global.addEventListener(p_evtStr, p_cb, false);
			} else if (objHasProperty(document, 'addEventListener')) {
				document.addEventListener(p_evtStr, p_cb, false);
			} else if (objHasProperty(global, 'attachEvent')) {
				// IE6, IE8
				global.attachEvent('on' + p_evtStr, p_cb);
			} else if (global && typeof global['on' + p_evtStr] === 'function') {
				var old = global['on' + p_evtStr];
				global['on' + p_evtStr] = function() {
					old();
					p_cb();
				};
			} else {
				global['on' + p_evtStr] = p_cb;
			}
		}

		function objHasProperty(p_obj, p_prop) {
			return (p_obj && p_prop && p_prop in p_obj) ? true : false;
		}

		// API
		return {
			exists : exists,
			addCustomEvt : addCustomEvt,
			objHasProperty : objHasProperty,
		};
	};
})(window);