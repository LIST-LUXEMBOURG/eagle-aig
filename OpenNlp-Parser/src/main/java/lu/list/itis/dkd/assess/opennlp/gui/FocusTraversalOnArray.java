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
package lu.list.itis.dkd.assess.opennlp.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;


/**
 * @author scheglov_ke
 * @author Younes Djaghloul [younes.djaghloul@list.lu]
 * @since 1.0
 * @version 4.0.1
 */
public class FocusTraversalOnArray extends FocusTraversalPolicy {
    private final Component m_Components[];

    /**
     * Simple constructor
     * @param components
     */
    public FocusTraversalOnArray(Component components[]) {
        m_Components = components;
    }


    private int indexCycle(int index, int delta) {
        int size = m_Components.length;
        int next = (index + delta + size) % size;
        return next;
    }
    
    private Component cycle(Component currentComponent, int delta) {
        int index = -1;
        loop : for (int i = 0; i < m_Components.length; i++) {
            Component component = m_Components[i];
            for (Component c = currentComponent; c != null; c = c.getParent()) {
                if (component == c) {
                    index = i;
                    break loop;
                }
            }
        }
        
        // try to find enabled component in "delta" direction
        int initialIndex = index;
        while (true) {
            int newIndex = indexCycle(index, delta);
            if (newIndex == initialIndex) {
                break;
            }
            index = newIndex;
            //
            Component component = m_Components[newIndex];
            if (component.isEnabled() && component.isVisible() && component.isFocusable()) {
                return component;
            }
        }

        return currentComponent;
    }


    public Component getComponentAfter(Container container, Component component) {
        return cycle(component, 1);
    }
    
    public Component getComponentBefore(Container container, Component component) {
        return cycle(component, -1);
    }
    
    public Component getFirstComponent(Container container) {
        return m_Components[0];
    }
    
    public Component getLastComponent(Container container) {
        return m_Components[m_Components.length - 1];
    }
    
    public Component getDefaultComponent(Container container) {
        return getFirstComponent(container);
    }
}
