package io.github.isaac.domfile;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;
import com.intellij.util.xml.SubTagList;

import java.util.List;

public interface Resources extends DomElement {
    @SubTag("string")
    List<AndroidString> getAndroidStrings();

    @SubTagList("string")
    AndroidString addAndroidString();


    @SubTag("string-array")
    List<AndroidStringArray> getAndroidStringArrays();

    @SubTagList("string-array")
    AndroidStringArray addAndroidStringArray();
}
