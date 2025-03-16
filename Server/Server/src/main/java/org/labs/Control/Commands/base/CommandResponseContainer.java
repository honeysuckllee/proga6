package org.labs.Control.Commands.base;

import java.io.Serializable;

public final class CommandResponseContainer implements Serializable {

    private static final long serialVersionUID = -70710302340434549L;
    private final String result;

    public CommandResponseContainer(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
