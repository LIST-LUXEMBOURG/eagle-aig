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
package lu.list.itis.dkd.assess.opennlp.syllibification;

/**
 * @author Alain Pfeiffer [alain.pfeiffer@list.lu]
 * @since 1.0
 * @version 1.0
 */
public class ConsonantBlend {
	private String label;
	private int position;
	
	private static String[] CONSONANTBLENDS =
	{
		"bl", "br", "ch", "cl", "cr", "dr", "ff", "fl", "fr", "gh", "gl", 
		"gn", "kn", "ll", "mb", "nd", "ng", "nk", "ph", "pl", "pr", "qu",
		"sh", "sl", "sm", "sn", "sp", "ss", "st", "shr", "spl", "spr", "squ", 
		"str", "th", "thr", "tt", "wr", "zz"
	};
	
	ConsonantBlend(String label, int position)
	{
		this.label = label;
		this.position = position;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public static String[] getConsonantblends() {
		return CONSONANTBLENDS;
	}
	
	

}
