package daixiahu.totem.backstage;

class TableInfo{
    String tableName;//name of table
    int tableOid;//table id
    int tupleNum;//num of tuple
    String[] tupleName = new String[10];//
    int[] tupleType = new int[10];//Type of tuple,0 is string,1 is int,default is 0
}

class DbInfo{
    String dbName;//name of db
    int dbOid;//id
    int tableNum;//num of table
    TableInfo[] table = new TableInfo[10];//max table num is 10
}

public class DatabaseManager {

}
