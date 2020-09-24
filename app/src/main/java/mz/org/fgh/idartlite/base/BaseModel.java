package mz.org.fgh.idartlite.base;

import java.io.Serializable;

public abstract class BaseModel implements Serializable {

    protected int listPosition;

    public static final String SYNC_SATUS_READY = "R";
    public static final String SYNC_SATUS_SENT = "S";
    public static final String SYNC_SATUS_UPDATED = "U";

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public int getListPosition() {
        return listPosition;
    }

    public boolean isSyncStatusReady(String syncStatus){
        return syncStatus.equals(SYNC_SATUS_READY);
    }

    public boolean isSyncStatusSent(String syncStatus){
        return syncStatus.equals(SYNC_SATUS_SENT);
    }

    public boolean isSyncStatusUpdated(String syncStatus){
        return syncStatus.equals(SYNC_SATUS_UPDATED);
    }

}
