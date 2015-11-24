package org.aldeon.peersim.handlers;

import org.aldeon.model.Branch;
import org.aldeon.model.Forest;
import peersim.config.Configuration;

import java.util.Set;


public class CompareBranchRequest extends Request {

    private long branch;
    private long hash;
    private boolean allowSuggest;

    private static final boolean globalAllow = Configuration.getBoolean("protocol.aldeon.suggests");

    /**
     * @param branch - branch root identifier
     * @param hash - value of branch hash function of the other node
     * @param allow - if set to true, the suggest is enabled
     */
    public CompareBranchRequest(long branch, long hash, boolean allow) {
        this.branch = branch;
        this.hash = hash;
        this.allowSuggest = allow && globalAllow;
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
