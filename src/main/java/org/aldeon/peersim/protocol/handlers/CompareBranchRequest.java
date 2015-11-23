package org.aldeon.peersim.protocol.handlers;

import org.aldeon.peersim.protocol.model.Branch;
import org.aldeon.peersim.protocol.model.Tree;

import java.util.List;


public class CompareBranchRequest extends Request {

    private long branch;
    private long hash;
    private boolean allowSuggest;

    /**
     * @param branch - branch root identifier
     * @param hash - value of branch hash function of the other node
     * @param force - allowSuggest, if set to true, the suggest option is disabled
     */
    public CompareBranchRequest(long branch, long hash, boolean force) {
        this.branch = branch;
        this.hash = hash;
        this.allowSuggest = force;
    }

    @Override
    public String toString() {
        return "CompareBranchMessage branch=" + branch + ", hash=" + hash + ", allowSuggest=" + allowSuggest;
    }

    @Override
    public Response handler(Tree tree) {

        // check if branch is known
        if (! tree.contains(branch)) return new BranchNotFoundResponse();

        long diff = tree.findById(branch).hash() ^ hash;

        // if so, check if hashes differ
        if (diff == Branch.ZERO) return new BranchInSyncResponse();

        // so hashes differ - try the suggest
        if (allowSuggest) {
            List<Branch> suggests = tree.findByHash(diff);
            if (suggests.size() > 0) {
                Branch pick = suggests.get(0);
                return new SuggestResponse(pick.identifier(), pick.parent(), branch);
            }
        }

        return ChildrenResponse.fromBranch(tree.findById(branch));

    }
}
