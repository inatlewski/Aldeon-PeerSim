package org.aldeon.peersim.handlers;

import org.aldeon.model.Branch;
import org.aldeon.model.Forest;
import java.util.Set;


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
    public Response handler(Forest forest) {

        // check if branch is known
        if (! forest.contains(branch)) return new BranchNotFoundResponse();

        long diff = forest.hash(branch) ^ hash;

        // if so, check if hashes differ
        if (diff == Branch.ZERO) return new BranchInSyncResponse();

        // so hashes differ - try the suggest
        if (allowSuggest) {
            Set<Long> suggests = forest.withHash(diff);
            if (suggests.size() > 0) {
                Long pick = suggests.iterator().next();
                return new SuggestResponse(pick, forest.parent(pick), branch);
            }
        }

        return ChildrenResponse.fromBranch(forest.view(branch));

    }
}
