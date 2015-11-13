package org.aldeon.peersim.protocol.models;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Imitates the real database. Thread safe, not thread efficient.
 */
public class DbStub {

    public static enum PutStatus {
        SUCCESS,
        NO_PARENT,
        MSG_EXISTS
    }

    protected static enum DelStatus {
        SUCCESS,
        ID_UNKNOWN
    }

    private int numMessages;

    protected Map<Id, Post> posts;
    protected XorManager mgr;

    public DbStub(XorManager mgr) {
        posts = new ConcurrentHashMap<>();
        this.mgr = mgr;
        numMessages = 0;
    }

    protected PutStatus put(Post post) {
        try {
            mgr.putId(post.getId(), post.getParent());
        } catch (UnknownParentException e) {
            return PutStatus.NO_PARENT;
        } catch (IdentifierAlreadyPresentException e) {
            return PutStatus.MSG_EXISTS;
        }
        posts.put(post.getId(), post);
        numMessages++;
        return PutStatus.SUCCESS;
    }

    protected DelStatus del(Id id) {
        try {
            mgr.delId(id);
            posts.remove(id);
        } catch (UnknownIdentifierException e) {
            return DelStatus.ID_UNKNOWN;
        }
        numMessages--;
        return DelStatus.SUCCESS;
    }

    public int getNumMessages() {
        return numMessages;
    }

    public Post getMessageById(Id msgId) {
        return posts.get(msgId);
    }

    public PutStatus insertMessage(Post post) {
        return put(post);
    }

    public void deleteMessage(Id msgId) {
        DelStatus s = del(msgId);
    }

    public Id getMessageXorById(Id msgId) {
        if (msgId.equals(Id.getEmpty())) {
            //get children of root
            Id rootXor = null;
            Map<Id, Id> m = getIdsAndXorsByParentId(msgId);
            for (Map.Entry<Id, Id> entry : m.entrySet()) {
                Id msgXor = entry.getValue();
                if (rootXor == null) {
                    rootXor = msgXor;
                } else {
                    rootXor = rootXor.xor(msgXor);
                }
            }
            return rootXor;
        }
        try {
            Id id = mgr.getXor(msgId);
            return id;
        } catch (UnknownIdentifierException e) {
            return null;
        }
    }

    public Set<Id> getMessageIdsByXor(Id msgXor) {
        return mgr.getIds(msgXor);
    }

    public Set<Post> getMessagesByParentId(Id parentId) {
        Set<Post> result = new HashSet<>();

        for(Id child: mgr.getChildren(parentId)) {
            Post post = posts.get(child);
            if(post != null) {
                result.add(post);
            }
        }

        return result;
    }

    public Set<Id> getMessageIdsByParentId(Id parentId) {
        return mgr.getChildren(parentId);
    }

    public Map<Id, Id> getIdsAndXorsByParentId(Id parentId) {
        Map<Id, Id> result = new HashMap<>();

        for(Id child: mgr.getChildren(parentId)) {
            try {
                result.put(child, mgr.getXor(child));
            } catch (UnknownIdentifierException e) {
                // State changed between queries
            }
        }

        return result;
    }

    public boolean checkAncestry(Id descendant, Id ancestor) {
        Id current = descendant;
        while(current != null && !current.isEmpty()) {
            Post post = posts.get(current);
            if(post == null) {
                break;
            } else {
                current = post.getParent();
                if(current.equals(ancestor)) {
                    return true;
                }
            }
        }
        return false;
    }

}
