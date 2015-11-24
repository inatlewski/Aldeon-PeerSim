package org.aldeon.peersim.handlers;

import org.aldeon.model.Forest;

public class GetBranchRequest extends Request {

    private final long id;

    /**
     * @param id - id of desired branch
     */
    public GetBranchRequest(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GetBranchMessage branch id = " + id;
    }

    @Override
    public Response handler(Forest forest) {
        return forest.contains(id)
                ? new BranchFoundResponse(forest.parent(id), forest.view(id))
                : new BranchNotFoundResponse();
    }
}
