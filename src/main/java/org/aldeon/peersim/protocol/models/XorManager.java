package org.aldeon.peersim.protocol.models;

import org.aldeon.peersim.protocol.models.*;

import java.util.*;

public class XorManager {

    // P - parent, Q - xor
    private Map<Id, Pair<Id, Id>> data;


    public XorManager() {
        data = new HashMap<>();
    }

    public void putId(Id id, Id parent) throws UnknownParentException, IdentifierAlreadyPresentException {
        if(data.get(parent) == null && !parent.isEmpty()) {
            throw new UnknownParentException();
        } else {
            if(data.get(id) == null && !id.isEmpty()) {
                data.put(id, new Pair<>(parent, id));
                try {
                    update(parent, id);
                } catch (UnknownParentException e) {
                    throw new InconsistentDatabaseStateException("Database contains conflicting information", e);
                }
            } else {
                throw new IdentifierAlreadyPresentException();
            }
        }
    }

    public void delId(Id id) throws UnknownIdentifierException {
        Pair<Id, Id> pair = data.get(id);
        if(pair == null) {
            throw new UnknownIdentifierException();
        } else {
            recDelChildren(id);
            data.remove(id);
            try {
                update(pair.getP(), pair.getQ());
            } catch (UnknownParentException e) {
                throw new InconsistentDatabaseStateException("Database contains conflicting information", e);
            }
        }
    }

    public Id getXor(Id id) throws UnknownIdentifierException {
        Pair<Id, Id> pair = data.get(id);
        if(pair == null) {
            throw new UnknownIdentifierException();
        } else {
            return pair.getQ();
        }
    }

    public Set<Id> getIds(Id xor) {

        Set<Id> result = new HashSet<>();

        for(Map.Entry<Id, Pair<Id, Id>> entry: data.entrySet()) {
            if(entry.getValue().getQ().equals(xor)) {
                result.add(entry.getKey());
            }
        }

        return result;
    }

    public Set<Id> getChildren(Id parent) {
        Set<Id> result = new HashSet<>();

        for(Map.Entry<Id, Pair<Id, Id>> entry: data.entrySet()) {
//            System.out.println("looking for " + parent + "; current " + entry.getValue().getP());
            if(entry.getValue().getP().equals(parent)) {
                result.add(entry.getKey());
            }
        }

        return result;
    }

    public boolean contains(Id id) {
        return data.get(id) != null;
    }

    private void update(Id id, Id xor) throws UnknownParentException {
        while(!id.isEmpty()) {
            Pair<Id, Id> p = data.get(id);
            if(p == null) {
                throw new UnknownParentException();
            } else {
                p.setQ(p.getQ().xor(xor));
                id = p.getP();
            }
        }
    }

    private void recDelChildren(Id id) {
        Iterator<Map.Entry<Id, Pair<Id, Id>>> it = data.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Id, Pair<Id, Id>> entry = it.next();
            if(id.equals(entry.getValue().getP())){
                recDelChildren(entry.getKey());
                it.remove();
            }
        }
    }
}
