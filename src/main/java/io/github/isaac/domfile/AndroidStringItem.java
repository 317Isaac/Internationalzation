package io.github.isaac.domfile;

import com.intellij.util.xml.DomElement;

public interface AndroidStringItem extends DomElement {
    String getValue();
    void setValue(String value);
}