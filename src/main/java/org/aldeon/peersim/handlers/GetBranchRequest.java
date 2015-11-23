package org.aldeon.peersim.handlers;

import org.aldeon.model.Tree;

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
    public Response handler(Tree tree) {
        return tree.contains(id)
                ? new BranchFoundResponse(tree.findById(id))
                : new BranchNotFoundResponse();
    }
}
