package com.example.test1;
public class nanamibean {
    public Live_room live_room;
}
class Live_room {

    private int roomStatus;
    private int liveStatus;
    private String url;
    private String title;
    private String cover;
    private int online;
    private long roomid;
    private int roundStatus;
    private int broadcast_type;
    public void setRoomStatus(int roomStatus) {
        this.roomStatus = roomStatus;
    }
    public int getRoomStatus() {
        return roomStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }
    public int getLiveStatus() {
        return liveStatus;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getCover() {
        return cover;
    }

    public void setOnline(int online) {
        this.online = online;
    }
    public int getOnline() {
        return online;
    }

    public void setRoomid(long roomid) {
        this.roomid = roomid;
    }
    public long getRoomid() {
        return roomid;
    }

    public void setRoundStatus(int roundStatus) {
        this.roundStatus = roundStatus;
    }
    public int getRoundStatus() {
        return roundStatus;
    }

    public void setBroadcast_type(int broadcast_type) {
        this.broadcast_type = broadcast_type;
    }
    public int getBroadcast_type() {
        return broadcast_type;
    }

}