package io.github.isaac.domfile;

import com.intellij.util.xml.DomFileDescription;

public class AndroidStringDomFileDescription extends DomFileDescription<Resources> {

    public AndroidStringDomFileDescription() {
        super(Resources.class, "resources","");
    }
}
