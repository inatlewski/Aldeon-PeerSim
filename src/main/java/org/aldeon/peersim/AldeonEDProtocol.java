package org.aldeon.peersim;

import org.aldeon.peersim.utils.LazyInitEDProtocol;

public class AldeonEDProtocol extends LazyInitEDProtocol<AldeonProtocolHandler> {

    public AldeonEDProtocol(String name) {
        super(AldeonProtocolHandler.class);
    }
}
