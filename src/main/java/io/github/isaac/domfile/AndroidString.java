package io.github.isaac.domfile;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

public interface AndroidString extends DomElement {

    @Attribute("name")
    GenericAttributeValue<String> getName();

    @Attribute("name")
    void setName(String name);

    String getValue();

    void setValue(String value);
}
