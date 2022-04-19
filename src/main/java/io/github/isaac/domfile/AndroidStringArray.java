package io.github.isaac.domfile;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.SubTag;

import java.util.List;

public interface AndroidStringArray extends DomElement {
    @Attribute("name")
    GenericAttributeValue<String> getName();

    @SubTag("item")
    List<AndroidStringItem> getStringItem();
}
