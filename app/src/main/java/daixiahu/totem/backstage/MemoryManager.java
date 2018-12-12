package daixiahu.totem.backstage;

import java.util.HashMap;
import java.util.List;

public class MemoryManager {

    public HashMap<bufferTag, sbufesc> hashMap;
    public List<sbufesc> freeList;

}

class PageHeaderData {

    /* Header of a block */

    int startFreespace; //Offset of the beginning of freespace
    int endFreespace; //Offset of the end of freespace
    int tupleNum; //Number of tuples
    boolean[] flag = new boolean[1024]; //bitmap

}

class Header {

    /* Header of a record(tuple) */

    int recordLen;
    boolean[] flag = new boolean[10]; //bitmap

}

class bufferTag {

    /* Specifying where a block is */

    public int dbOid; //ID of database
    public int tableOid; //

}

class sbufesc {

    /* A descriptor of a buffer */

    bufferTag tag; //block place
    Boolean flag; //dirty bit

}

