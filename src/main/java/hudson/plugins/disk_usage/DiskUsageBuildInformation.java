/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hudson.plugins.disk_usage;

import hudson.util.VersionNumber;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import jenkins.model.Jenkins;

/**
 *
 * @author Lucie Votypkova
 */
public class DiskUsageBuildInformation implements Serializable, Comparable{
    
    private static final DateFormat LEGACY_ID_FORMATTER = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    private String id;
    private long timestamp;
    private int number;
    private boolean isLocked;
    
    private Long size;
    
    public DiskUsageBuildInformation(String id, long timestamp, int number, Long size){
        this.id = id;
        this.timestamp = timestamp;
        this.number = number;
        this.size = size;
        this.isLocked = false;
    }
    
    public DiskUsageBuildInformation(String id, long timestamp, int number, Long size, boolean isLocked){
        this.id = id;
        this.timestamp = timestamp;
        this.number = number;
        this.size = size;
        this.isLocked = isLocked;
    }
    
    public boolean isLocked(){
        return isLocked;
    }

    public void lock(){
        isLocked = true;
    }
    
    public void unLock(){
        isLocked = false;
    }
    
    public void setLockState(boolean locked){
        isLocked = locked;
    }
    
    private Object readResolve() {
        if (timestamp == 0) {
            try {
                timestamp = LEGACY_ID_FORMATTER.parse(id).getTime();
            } catch (ParseException x) {
                // never mind
            }
        }
        return this;
    }
    
    public String getId(){
        if(Jenkins.getVersion().isNewerThan(new VersionNumber("1.597"))){
            return String.valueOf(number);
        }
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }
    
    public int getNumber(){
        return number;
    }
    
    public Long getSize(){
        if(size==null)
            return 0L;
        return size;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof DiskUsageBuildInformation){
            DiskUsageBuildInformation information = (DiskUsageBuildInformation) o;
            return information.getId().equals(id);
        }
        return false;
    }
    
    public int compareTo(Object o){
        if(o instanceof DiskUsageBuildInformation){
            return id.compareTo(((DiskUsageBuildInformation)o).getId());
        }
        throw new IllegalArgumentException("Can not compare with different type");
    }
    
    public void setSize(Long size){
        this.size = size;
    }
    
    public String toString(){
        return "Id " + id + " number " + number + " size " + size;
    }
}
