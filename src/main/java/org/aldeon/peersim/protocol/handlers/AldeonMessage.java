package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.models.DbStub;
import peersim.core.Protocol;

import java.util.ArrayList;

/**
 * Created by mb on 22.06.15.
 */
public abstract class AldeonMessage {
    /**
     * @param dbStub - access to local post storage
     * @return response objects; null if no response
     */
    public abstract ArrayList<AldeonMessage> handle(DbStub dbStub, Protocol protocol);
}
