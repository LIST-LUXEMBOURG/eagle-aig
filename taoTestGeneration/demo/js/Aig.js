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
	global.lu.list.aig.demo.Aig = new function Aig() {
		if (Aig.prototype._singletonInstance) {
			return Aig.prototype._singletonInstance;
		}
		Aig.prototype._singletonInstance = this;

		// properties
		var SERVER_BASE_URL = '';
		var inputTextArea = null;
		var exampleButtons = [];
		var exampleArea = null;

		// methods
		function init() {
			// get elements
			inputTextArea = global.document.getElementById('textInput');
			exampleArea = global.document.getElementById('exampleArea');
			var goToTaoButton = global.document.getElementById('goToTao');
			var useExampleButton = global.document
					.getElementById('useExampleButton');
			var identifieds = global.document.querySelectorAll('[id]');
			for (var i = 0; i < identifieds.length; i++) {
				var identified = identifieds[i];
				var matchResult = identified.id.match(/^example(\d+)Button$/);
				if (matchResult == null) {
					continue;
				}
				// get area
				var areaId = 'example' + matchResult[1];
				var area = global.document.getElementById(areaId);
				if (area == null) {
					throw new Error('An area is missing: ' + areaId);
				}
				exampleButtons.push({
					button : identified,
					area : area,
					index : matchResult[1]
				});
			}

			// init elements
			for (var i = 0; i < exampleButtons.length; i++) {
				var exampleButton = exampleButtons[i];
				exampleButton.button.disabled = false;
			}

			// init events
			useExampleButton.onclick = onClickUseExampleButton
			goToTaoButton.onclick = onClickGoToTaoButton;
			for (var i = 0; i < exampleButtons.length; i++) {
				var exampleButton = exampleButtons[i];
				exampleButton.button.onclick = onClickExampleButton;
			}
		}
		function onClickExampleButton(evt) {
			var buttonClicked = evt.target;
			exampleArea.style.display = 'block';
			exampleButtons
					.forEach(function(exampleButton) {
						var button = exampleButton.button;
						button.disabled = (button == buttonClicked);
						exampleButton.area.style.display = (button == buttonClicked) ? 'block'
								: 'none';
					});
		}
		function onClickUseExampleButton() {
			for (var i = 0; i < exampleButtons.length; i++) {
				var exampleButton = exampleButtons[i];
				var button = exampleButton.button;
				if (button.disabled) {
					var area = exampleButton.area;
					inputTextArea.value = area.innerHTML;
					break;
				}
			}
		}
		function onClickGoToTaoButton() {
			try {
				goToTao();
			} catch (e) {
				console.log(e);
				alert(e);
			}

		}
		function goToTao() {
			var text = inputTextArea.value.trim();
			if (!text) {
				throw new Error('empty input');
				return;
			}
			var sessionId = 'demo';
			var locale = 'en';
			var group = 'AIG';

			var request = '/tao/Main/index?structure=tests&ext=taoTests&section=manage_tests&sessionId='
					+ sessionId
					+ '&dataUri=http://knowledge.list.lu/resource/content/demo01&resourceUrl=https://knowledge.list.lu/document/custom/demo01.html&resourceType=OER&locale='
					+ locale
					+ '&group='
					+ group
					+ '&content='
					+ encodeURIComponent(text);

			global.location.href = request;
		}
		// define starting point
		global.lu.list.aig.demo.Tools.addCustomEvt('load', function() {
			try {
				init();
			} catch (e) {
				console.log(e);
				alert('Error: initialization failed');
			}
		});

		// API
		return {

		};
	};
})(window);
