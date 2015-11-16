package org.aldeon.peersim.protocol.controls;

public class BaseTreeInitializer {
    protected String PAR_TOTAL_NUM_MSGS = "totalNumMessages";
    protected String PAR_PROT = "protocol";
    protected String PAR_RANDOM_SEED = "random.seed";
    protected String PAR_PERCENT_OF_MISSING_MESSAGES = "percentOfMissingMessages";

    protected int totalNumMessages;
    protected int percentOfMissingMessages;
    protected int pid;
    protected long seed;

}
